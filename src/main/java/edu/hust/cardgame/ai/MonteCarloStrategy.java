package main.java.edu.hust.cardgame.ai;

import java.util.*;
import main.java.edu.hust.cardgame.core.SheddingGame;
import main.java.edu.hust.cardgame.logic.tienlen.TienLen;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.model.PlayerState;
import main.java.edu.hust.cardgame.model.StandardCard;

public class MonteCarloStrategy<C extends StandardCard, G extends SheddingGame<C>> implements AIStrategy<C, G> {
    private final int simulationsPerMove;
    private final int mctsIterations;
    private final Random rng = new Random();

    public MonteCarloStrategy(int simulationsPerMove) {
        this(simulationsPerMove, 10);
    }

    public MonteCarloStrategy(int simulationsPerMove, int mctsIterations) {
        this.simulationsPerMove = simulationsPerMove;
        this.mctsIterations = mctsIterations;
    }

    @Override
    public CardCollection<C> decideMove(G game, Player<C> ai) {
        List<CardCollection<C>> legalMoves = generateLegalMoves(game, ai);
        if (legalMoves.isEmpty()) return new CardCollection<>();

        Map<CardCollection<C>, Integer> moveScores = new HashMap<>();
        for (CardCollection<C> move : legalMoves) moveScores.put(move, 0);

        List<Player<C>> players = getPlayersSafe(game);
        int aiIndex = players.indexOf(ai);
        List<Integer> opponentHandSizes = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (i != aiIndex) opponentHandSizes.add(players.get(i).getHandSize());
        }
        CardCollection<C> aiHandOrig = game.getHandOf(ai).clone();
        CardCollection<C> playedCardsOrig = getPlayedCardsSafe(game).clone();

        for (int sim = 0; sim < simulationsPerMove; sim++) {
            // Clone the game for this simulation
            G clonedGame = cloneGame(game);
            List<Player<C>> clonedPlayers = getPlayersSafe(clonedGame);
            Player<C> clonedAI = clonedPlayers.get(aiIndex);

            // Randomize opponents' hands in the cloned game
            randomizeOpponentsHands(clonedGame, aiIndex, playedCardsOrig, aiHandOrig, opponentHandSizes);

            // Run full MCTS on the cloned game
            CardCollection<C> recommendedMove = mctsSearch(clonedGame, clonedAI, mctsIterations);

            // Increment score for the recommended move
            for (CardCollection<C> m : legalMoves) {
                if (m.equals(recommendedMove)) {
                    moveScores.put(m, moveScores.get(m) + 1);
                    break;
                }
            }
        }

        // Select the move with the highest score
        CardCollection<C> best = legalMoves.get(0);
        int bestScore = -1;
        for (CardCollection<C> move : legalMoves) {
            int score = moveScores.get(move);
            if (score > bestScore) {
                bestScore = score;
                best = move;
            }
        }
        return best.clone();
    }

    // --- MCTS with save/restore ---
    private CardCollection<C> mctsSearchWithSaveRestore(G game, Player<C> ai, int iterations, int aiIndex, List<Integer> opponentHandSizes, CardCollection<C> aiHandOrig, CardCollection<C> playedCardsOrig, CardCollection<C> lastPlayedOrig, List<List<C>> allHandsOrig, List<PlayerState> allStatesOrig) {
        List<CardCollection<C>> legal = generateLegalMoves(game, ai);
        if (legal.isEmpty()) return new CardCollection<>();
        Map<CardCollection<C>, Integer> moveScores = new HashMap<>();
        List<Player<C>> players = getPlayersSafe(game);
        for (CardCollection<C> move : legal) moveScores.put(move, 0);
        for (CardCollection<C> move : legal) {
            int score = 0;
            for (int i = 0; i < iterations; i++) {
                // Restore all hands and states
                for (int j = 0; j < players.size(); j++) {
                    players.get(j).clearHand();
                    for (C card : allHandsOrig.get(j)) players.get(j).receiveCard(card);
                    players.get(j).setState(allStatesOrig.get(j));
                }
                // Restore last played and played cards
                CardCollection<C> playedCards = getPlayedCardsSafe(game);
                playedCards.empty();
                playedCards.addAll(playedCardsOrig);
                CardCollection<C> lastPlayed = game.getLastPlayedCards();
                lastPlayed.empty();
                lastPlayed.addAll(lastPlayedOrig);

                // Apply move
                CardCollection<C> sel = game.getSelectedCards();
                sel.empty();
                for (int k = 0; k < move.getSize(); k++) sel.addCard(move.getCardAt(k));
                if (game.isValidPlay()) players.get(aiIndex).useCards(sel);

                if (rollout(game, players.get(aiIndex))) score++;
            }
            moveScores.put(move, score);
        }
        CardCollection<C> best = legal.get(0);
        int bestScore = -1;
        for (CardCollection<C> move : legal) {
            int score = moveScores.get(move);
            if (score > bestScore) {
                bestScore = score;
                best = move;
            }
        }
        return best.clone();
    }

    // --- MCTS Tree Node ---
    private class MCTSNode {
        G state;
        Player<C> ai;
        CardCollection<C> move; // move that led to this node (null for root)
        MCTSNode parent;
        List<MCTSNode> children = new ArrayList<>();
        int visits = 0;
        double wins = 0;
        boolean isTerminal = false;
        List<CardCollection<C>> untriedMoves;
        Player<C> currentPlayer;

        MCTSNode(G state, Player<C> ai, CardCollection<C> move, MCTSNode parent, Player<C> currentPlayer) {
            this.state = state;
            this.ai = ai;
            this.move = move;
            this.parent = parent;
            this.currentPlayer = currentPlayer;
            this.untriedMoves = generateLegalMoves(state, currentPlayer);
            this.isTerminal = isTerminalState(state);
        }
    }

    private CardCollection<C> mctsSearch(G rootState, Player<C> ai, int iterations) {
        List<Player<C>> players = getPlayersSafe(rootState);
        int aiIndex = players.indexOf(ai);
        if (aiIndex == -1) {
            // If AI player not found, use the first player
            aiIndex = 0;
        }
        Player<C> rootAI = players.get(aiIndex);
        MCTSNode root = new MCTSNode(cloneGame(rootState), rootAI, null, null, rootAI);
        for (int i = 0; i < iterations; i++) {
            // Selection
            MCTSNode node = root;
            while (!node.untriedMoves.isEmpty() && !node.children.isEmpty()) {
                node = selectChild(node);
            }
            // Expansion
            if (!node.untriedMoves.isEmpty() && !node.isTerminal) {
                CardCollection<C> move = node.untriedMoves.remove(rng.nextInt(node.untriedMoves.size()));
                G nextState = cloneGame(node.state);
                List<Player<C>> nextStatePlayers = getPlayersSafe(nextState);
                int currentPlayerIndex = nextStatePlayers.indexOf(node.currentPlayer);
                if (currentPlayerIndex == -1) {
                    currentPlayerIndex = 0;
                }
                Player<C> nextPlayer = nextStatePlayers.get(currentPlayerIndex);
                applyMove(nextState, nextPlayer, move);
                Player<C> nextTurnPlayer = getNextPlayer(nextState, nextPlayer);
                MCTSNode child = new MCTSNode(nextState, ai, move, node, nextTurnPlayer);
                node.children.add(child);
                node = child;
            }
            // Simulation (rollout)
            double result = rolloutMCTS(node.state, ai, node.currentPlayer);
            // Backpropagation
            while (node != null) {
                node.visits++;
                node.wins += result;
                node = node.parent;
            }
        }
        // Pick the child of root with the highest visit count
        MCTSNode bestChild = null;
        int bestVisits = -1;
        for (MCTSNode child : root.children) {
            if (child.visits > bestVisits) {
                bestVisits = child.visits;
                bestChild = child;
            }
        }
        return (bestChild != null && bestChild.move != null) ? bestChild.move.clone() : generateLegalMoves(rootState, ai).get(0).clone();
    }

    private MCTSNode selectChild(MCTSNode node) {
        // UCB1 selection
        double logParentVisits = Math.log(node.visits + 1);
        MCTSNode best = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        for (MCTSNode child : node.children) {
            double uctValue = (child.wins / (child.visits + 1e-6)) + Math.sqrt(2 * logParentVisits / (child.visits + 1e-6));
            if (uctValue > bestValue) {
                bestValue = uctValue;
                best = child;
            }
        }
        return best;
    }

    private boolean isTerminalState(G game) {
        List<Player<C>> players = getPlayersSafe(game);
        for (Player<C> p : players) {
            if (p.getHandSize() == 0) return true;
        }
        return false;
    }

    private Player<C> getNextPlayer(G game, Player<C> current) {
        List<Player<C>> players = getPlayersSafe(game);
        int currentIdx = players.indexOf(current);
        if (currentIdx == -1) {
            // If current player not found, start from the first player
            currentIdx = 0;
        }
        ((TienLen) game).moveToNextPlayer();
        int nextIdx = ((TienLen) game).getCurrentPlayerIndex();
        if (nextIdx == -1) {
            // If next player index is invalid, cycle to the next player
            nextIdx = (currentIdx + 1) % players.size();
        }
        return players.get(nextIdx);
    }

    private double rolloutMCTS(G game, Player<C> ai, Player<C> currentPlayer) {
        List<Player<C>> players = getPlayersSafe(game);
        int currentIdx = players.indexOf(currentPlayer);
        if (currentIdx == -1) {
            currentIdx = 0;
        }
        
        while (true) {
            for (int i = 0; i < players.size(); i++) {
                Player<C> p = players.get(currentIdx);
                if (p.getHandSize() == 0) return (p == ai) ? 1.0 : 0.0;
                List<CardCollection<C>> moves = generateLegalMoves(game, p);
                if (!moves.isEmpty()) {
                    CardCollection<C> move = moves.get(rng.nextInt(moves.size()));
                    applyMove(game, p, move);
                } else {
                    // Simulate passing
                    ((TienLen) game).passTurn();
                }
                currentIdx = ((TienLen) game).getCurrentPlayerIndex();
                if (currentIdx == -1) {
                    currentIdx = (currentIdx + 1) % players.size();
                }
            }
        }
    }

    // --- Rollout simulation for save/restore MCTS ---
    private boolean rollout(G game, Player<C> ai) {
        List<Player<C>> players = getPlayersSafe(game);
        int numPlayers = players.size();
        // Use currentPlayerIndex from TienLen if available
        int currentPlayerIdx = 0;
        if (game instanceof main.java.edu.hust.cardgame.logic.tienlen.TienLen tienLenGame) {
            try {
                java.lang.reflect.Field idxField = tienLenGame.getClass().getDeclaredField("currentPlayerIndex");
                idxField.setAccessible(true);
                currentPlayerIdx = idxField.getInt(tienLenGame);
            } catch (Exception e) {
                // fallback to 0
                currentPlayerIdx = 0;
            }
        }
        boolean[] hasPassed = new boolean[numPlayers];
        while (true) {
            // Check for winner
            for (Player<C> p : players) {
                if (p.getHandSize() == 0) return p == ai;
            }
            // If all but one have passed, reset round
            int notPassed = 0, lastNotPassed = -1;
            for (int i = 0; i < numPlayers; i++) {
                if (!hasPassed[i] && players.get(i).getHandSize() > 0) {
                    notPassed++;
                    lastNotPassed = i;
                }
            }
            if (notPassed == 1) {
                // Reset round: clear last played, everyone can play again
                CardCollection<C> lp = game.getLastPlayedCards();
                lp.empty();
                Arrays.fill(hasPassed, false);
                currentPlayerIdx = lastNotPassed;
            }
            Player<C> current = players.get(currentPlayerIdx);
            if (current.getHandSize() == 0) {
                // Skip finished players
                currentPlayerIdx = (currentPlayerIdx + 1) % numPlayers;
                continue;
            }
            if (hasPassed[currentPlayerIdx]) {
                // Skip passed players
                currentPlayerIdx = (currentPlayerIdx + 1) % numPlayers;
                continue;
            }
            List<CardCollection<C>> moves = generateLegalMoves(game, current);
            if (!moves.isEmpty()) {
                CardCollection<C> move = moves.get(rng.nextInt(moves.size()));
                applyMove(game, current, move);
                hasPassed[currentPlayerIdx] = false;
            } else {
                hasPassed[currentPlayerIdx] = true;
            }
            currentPlayerIdx = (currentPlayerIdx + 1) % numPlayers;
        }
    }

    // --- Helper methods ---

    private List<CardCollection<C>> generateLegalMoves(G game, Player<C> ai) {
        List<CardCollection<C>> out = new ArrayList<>();
        CardCollection<C> hand = game.getHandOf(ai).clone();
        int lastSize = game.getLastPlayedCards().getSize();
        int minSize = lastSize > 0 ? lastSize : 1;
        for (int sz = minSize; sz <= hand.getSize(); sz++) {
            backtrack(game, hand, sz, 0, new ArrayList<>(), out);
        }
        return out;
    }

    private void backtrack(G game, CardCollection<C> hand, int targetSize, int start, List<Integer> path, List<CardCollection<C>> out) {
        if (path.size() == targetSize) {
            CardCollection<C> sel = game.getSelectedCards();
            sel.empty();
            for (int idx : path) sel.addCard(hand.getCardAt(idx));
            if (game.isValidPlay()) out.add(sel.clone());
            return;
        }
        int need = targetSize - path.size();
        for (int i = start; i <= hand.getSize() - need; i++) {
            path.add(i);
            backtrack(game, hand, targetSize, i + 1, path, out);
            path.remove(path.size() - 1);
        }
    }

    private G cloneGame(G game) {
        try {
            return (G) game.getClass().getMethod("clone").invoke(game);
        } catch (Exception e) {
            throw new RuntimeException("Game clone failed", e);
        }
    }

    private void applyMove(G game, Player<C> ai, CardCollection<C> move) {
        CardCollection<C> sel = game.getSelectedCards();
        sel.empty();
        for (int i = 0; i < move.getSize(); i++) sel.addCard(move.getCardAt(i));
        if (game.isValidPlay()) ai.useCards(sel);
    }

    private void randomizeOpponentsHands(G game, int aiIndex, CardCollection<C> playedCards, CardCollection<C> aiHand, List<Integer> opponentHandSizes) {
        List<Player<C>> players = getPlayersSafe(game);
        Set<C> allCards = new HashSet<>();
        for (Player<C> p : players) allCards.addAll(p.getHand().getAllCards());
        allCards.addAll(playedCards.getAllCards());
        Set<C> available = new HashSet<>(allCards);
        available.removeAll(playedCards.getAllCards());
        available.removeAll(aiHand.getAllCards());
        List<C> availableList = new ArrayList<>(available);
        Collections.shuffle(availableList, rng);
        int idx = 0;
        for (int i = 0, opp = 0; i < players.size(); i++) {
            if (i == aiIndex) continue;
            Player<C> p = players.get(i);
            p.clearHand();
            int handSize = opponentHandSizes.get(opp++);
            for (int j = 0; j < handSize && idx < availableList.size(); j++) {
                p.receiveCard(availableList.get(idx++));
            }
        }
    }

    // --- Utility methods for safe access ---
    @SuppressWarnings("unchecked")
    private CardCollection<C> getPlayedCardsSafe(G game) {
        try {
            return (CardCollection<C>) game.getClass().getMethod("getPlayedCards").invoke(game);
        } catch (Exception e) {
            return new CardCollection<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Player<C>> getPlayersSafe(G game) {
        try {
            return (List<Player<C>>) game.getClass().getMethod("getPlayers").invoke(game);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
