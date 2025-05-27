package main.java.edu.hust.cardgame.ai;

import java.util.*;
import main.java.edu.hust.cardgame.core.SheddingGame;
import main.java.edu.hust.cardgame.logic.tienlen.TienLen;
import main.java.edu.hust.cardgame.logic.tienlen.TienLenMienNam;
import main.java.edu.hust.cardgame.core.CardCollection;
import main.java.edu.hust.cardgame.core.Player;
import main.java.edu.hust.cardgame.core.StandardCard;

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
            G clonedGame = cloneGame(game);
            List<Player<C>> clonedPlayers = getPlayersSafe(clonedGame);
            Player<C> clonedAI = clonedPlayers.get(aiIndex);

            randomizeOpponentsHands(clonedGame, aiIndex, playedCardsOrig, aiHandOrig, opponentHandSizes);

            CardCollection<C> recommendedMove = mctsSearch(clonedGame, clonedAI, mctsIterations);

            for (CardCollection<C> m : legalMoves) {
                if (m.equals(recommendedMove)) {
                    moveScores.put(m, moveScores.get(m) + 1);
                    break;
                }
            }
        }

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

    private class MCTSNode {
        G state;
        Player<C> ai;
        CardCollection<C> move;
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

    private CardCollection<C> mctsSearch(G game, Player<C> ai, int iterations) {
        MCTSNode root = new MCTSNode(game, ai, null, null, ai);
        for (int i = 0; i < iterations; i++) {
            MCTSNode node = root;
            while (!node.isTerminal && node.untriedMoves.isEmpty() && !node.children.isEmpty()) {
                node = selectChild(node);
            }
            if (!node.isTerminal && !node.untriedMoves.isEmpty()) {
                CardCollection<C> move = node.untriedMoves.remove(rng.nextInt(node.untriedMoves.size()));
                G nextState = cloneGame(node.state);
                List<Player<C>> nextStatePlayers = getPlayersSafe(nextState);
                int currentPlayerIndex = nextStatePlayers.indexOf(node.currentPlayer);
                if (currentPlayerIndex == -1) currentPlayerIndex = 0;
                Player<C> nextPlayer = nextStatePlayers.get(currentPlayerIndex);
                applyMove(nextState, nextPlayer, move);
                Player<C> nextTurnPlayer = getNextPlayer(nextState, nextPlayer);
                MCTSNode child = new MCTSNode(nextState, ai, move, node, nextTurnPlayer);
                node.children.add(child);
                node = child;
            }
            double result = rolloutMCTS(node.state, ai, node.currentPlayer);
            while (node != null) {
                node.visits++;
                node.wins += result;
                node = node.parent;
            }
        }
        MCTSNode bestChild = null;
        int mostVisits = -1;
        for (MCTSNode child : root.children) {
            if (child.visits > mostVisits) {
                mostVisits = child.visits;
                bestChild = child;
            }
        }
        return bestChild != null ? bestChild.move : new CardCollection<>();
    }

    private MCTSNode selectChild(MCTSNode node) {
        double logParentVisits = Math.log(node.visits + 1);
        MCTSNode best = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        for (MCTSNode child : node.children) {
            double uctValue = (child.wins / (child.visits + 1e-6)) + 2 * Math.sqrt( logParentVisits / (child.visits + 1e-6));
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
        return ((TienLen) game).isGameOver();
    }

    private Player<C> getNextPlayer(G game, Player<C> current) {
        List<Player<C>> players = getPlayersSafe(game);
        int currentIdx = players.indexOf(current);
        if (currentIdx == -1) currentIdx = 0;
        ((TienLen) game).moveToNextPlayer();
        int nextIdx = ((TienLen) game).getCurrentPlayerIndex();
        if (nextIdx == -1) nextIdx = (currentIdx + 1) % players.size();
        return players.get(nextIdx);
    }

    private double rolloutMCTS(G game, Player<C> ai, Player<C> currentPlayer) {
        List<Player<C>> players = getPlayersSafe(game);
        int currentIdx = players.indexOf(currentPlayer);
        if (currentIdx == -1) currentIdx = 0;
        while (true) {
            for (int i = 0; i < players.size(); i++) {
                Player<C> p = players.get(currentIdx);
                if (p.getHandSize() == 0) return (p == ai) ? 1.0 : 0.0;
                List<CardCollection<C>> moves = generateLegalMoves(game, p);
                if (!moves.isEmpty()) {
                    CardCollection<C> move = moves.get(rng.nextInt(moves.size()));
                    applyMove(game, p, move);
                } else {
                    ((TienLen) game).passTurn();
                }
                currentIdx = ((TienLen) game).getCurrentPlayerIndex();
                if (currentIdx == -1) currentIdx = (currentIdx + 1) % players.size();
            }
        }
    }

    private List<CardCollection<C>> generateLegalMoves(G game, Player<C> player) {
        List<CardCollection<C>> out = new ArrayList<>();

        TienLen tienlen = (TienLen) game;
        boolean initial = tienlen.getFlag() == 1 && tienlen.getLastPlayedCards().isEmpty();

        CardCollection<C> hand = game.getHandOf(player).clone();
        int lastSize = game.getLastPlayedCards().getSize();
        int minSize = lastSize > 0 ? lastSize : 1;
        if (initial && game instanceof TienLenMienNam) {
            ArrayList<Integer> path = new ArrayList<>();
            path.add(0);
            for (int sz = minSize; sz <= hand.getSize(); sz++) {
                backtrack(game, hand, sz, 1, path, out);
            }
        } else {
            for (int sz = minSize; sz <= hand.getSize(); sz++) {
                backtrack(game, hand, sz, 0, new ArrayList<>(), out);
            }
        }
        if (lastSize > 0) {
            out.add(new CardCollection<>());
        }
        Collections.shuffle(out, rng);

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

    private void applyMove(G game, Player<C> player, CardCollection<C> move) {
        if (move.isEmpty()) {
            ((TienLen) game).passTurn();
        } else {
            CardCollection<C> sel = game.getSelectedCards();
            sel.empty();
            for (int i = 0; i < move.getSize(); i++) sel.addCard(move.getCardAt(i));
            if (game.isValidPlay()) player.useCards(sel);
        }
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
