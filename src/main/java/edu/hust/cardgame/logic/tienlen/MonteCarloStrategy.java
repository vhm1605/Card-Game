package edu.hust.cardgame.logic.tienlen;

import edu.hust.cardgame.ai.AIStrategy;
import edu.hust.cardgame.core.CardCollection;
import edu.hust.cardgame.core.CardComboType;
import edu.hust.cardgame.core.Player;
import edu.hust.cardgame.core.SheddingGame;
import edu.hust.cardgame.core.StandardCard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Chooses the move with the best average result across randomized, bounded rollouts.
 * The strategy evaluates cloned games, so searching never mutates the live match.
 */
public final class MonteCarloStrategy
        implements AIStrategy<StandardCard, SheddingGame<StandardCard>> {
    private static final int DEFAULT_ROLLOUT_DEPTH = 40;

    private final int simulationsPerMove;
    private final int rolloutDepth;
    private final Random random;

    public MonteCarloStrategy(int simulationsPerMove) {
        this(simulationsPerMove, DEFAULT_ROLLOUT_DEPTH);
    }

    public MonteCarloStrategy(int simulationsPerMove, int rolloutDepth) {
        this(simulationsPerMove, rolloutDepth, new Random());
    }

    MonteCarloStrategy(int simulationsPerMove, int rolloutDepth, Random random) {
        if (simulationsPerMove < 1) {
            throw new IllegalArgumentException("simulationsPerMove must be positive");
        }
        if (rolloutDepth < 1) {
            throw new IllegalArgumentException("rolloutDepth must be positive");
        }
        this.simulationsPerMove = simulationsPerMove;
        this.rolloutDepth = rolloutDepth;
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public CardCollection<StandardCard> decideMove(
            SheddingGame<StandardCard> game,
            Player<StandardCard> ai
    ) {
        if (!(game instanceof TienLen liveGame)) {
            throw new IllegalArgumentException("MonteCarloStrategy requires a Tien Len game");
        }

        int aiIndex = liveGame.getPlayers().indexOf(ai);
        if (aiIndex < 0) {
            throw new IllegalArgumentException("The AI player does not belong to this game");
        }

        TienLen searchRoot = liveGame.clone();
        List<CardCollection<StandardCard>> legalMoves = generateLegalMoves(searchRoot);
        if (legalMoves.isEmpty()) {
            return new CardCollection<>();
        }

        CardCollection<StandardCard> bestMove = legalMoves.get(0);
        double bestScore = Double.NEGATIVE_INFINITY;

        for (CardCollection<StandardCard> move : legalMoves) {
            double score = 0;
            for (int simulation = 0; simulation < simulationsPerMove; simulation++) {
                TienLen simulatedGame = liveGame.clone();
                applyMove(simulatedGame, move);
                score += rollout(simulatedGame, aiIndex);
            }
            double averageScore = score / simulationsPerMove;
            if (averageScore > bestScore) {
                bestScore = averageScore;
                bestMove = move;
            }
        }

        return bestMove.clone();
    }

    private double rollout(TienLen game, int aiIndex) {
        for (int turn = 0; turn < rolloutDepth && !game.isGameOver(); turn++) {
            List<CardCollection<StandardCard>> moves = generateLegalMoves(game);
            CardCollection<StandardCard> move = moves.isEmpty()
                    ? new CardCollection<>()
                    : moves.get(random.nextInt(moves.size()));
            applyMove(game, move);
        }
        return evaluate(game, aiIndex);
    }

    private double evaluate(TienLen game, int aiIndex) {
        int playerNumber = aiIndex + 1;
        int finishIndex = game.playerRanking.indexOf(playerNumber);
        if (finishIndex >= 0) {
            return 1.0 - ((double) finishIndex / Math.max(1, game.getPlayers().size() - 1));
        }

        int aiCards = game.getPlayers().get(aiIndex).getHandSize();
        int largestHand = game.getPlayers().stream()
                .mapToInt(Player::getHandSize)
                .max()
                .orElse(aiCards);
        return 1.0 - ((double) aiCards / Math.max(1, largestHand));
    }

    private void applyMove(TienLen game, CardCollection<StandardCard> move) {
        if (move.isEmpty()) {
            game.passTurn();
            return;
        }

        game.getSelectedCards().empty();
        game.getSelectedCards().addAll(move);
        game.playGame();
        game.getSelectedCards().empty();
    }

    private List<CardCollection<StandardCard>> generateLegalMoves(TienLen game) {
        List<CardCollection<StandardCard>> moves = new ArrayList<>();
        CardCollection<StandardCard> hand = game.getCurrentPlayer().cloneHand();

        for (int size : candidateSizes(game, hand.getSize())) {
            collectCombinations(game, hand, size, 0, new ArrayList<>(), moves);
        }
        if (!game.getLastPlayedCards().isEmpty()) {
            moves.add(new CardCollection<>());
        }

        game.getSelectedCards().empty();
        return moves;
    }

    private Set<Integer> candidateSizes(TienLen game, int handSize) {
        Set<Integer> sizes = new LinkedHashSet<>();
        CardCollection<StandardCard> lastPlay = game.getLastPlayedCards();
        if (lastPlay.isEmpty()) {
            for (int size = 1; size <= handSize; size++) {
                sizes.add(size);
            }
            return sizes;
        }

        int lastSize = lastPlay.getSize();
        sizes.add(lastSize);
        CardComboType lastType = game.determineComboType(lastPlay);
        boolean highestCardIsTwo = game.order.getFaceOrder(lastPlay.getCardAt(lastSize - 1)) == 15;

        if (game instanceof TienLenMienBac) {
            if (lastType == CardComboType.SINGLE && highestCardIsTwo) {
                sizes.add(4);
            }
            return sizes;
        }

        if (lastType == CardComboType.CONSECUTIVE_PAIRS) {
            for (int size = lastSize + 2; size <= handSize; size += 2) {
                sizes.add(size);
            }
            if (lastSize == 6) {
                sizes.add(4);
            }
        } else if (lastType == CardComboType.FOUR_OF_A_KIND) {
            for (int size = 8; size <= handSize; size += 2) {
                sizes.add(size);
            }
        } else if (highestCardIsTwo && lastType == CardComboType.SINGLE) {
            sizes.add(4);
            for (int size = 6; size <= handSize; size += 2) {
                sizes.add(size);
            }
        } else if (highestCardIsTwo && lastType == CardComboType.PAIR) {
            sizes.add(4);
            for (int size = 8; size <= handSize; size += 2) {
                sizes.add(size);
            }
        }

        sizes.removeIf(size -> size > handSize);
        return sizes;
    }

    private void collectCombinations(
            TienLen game,
            CardCollection<StandardCard> hand,
            int targetSize,
            int start,
            List<Integer> selectedIndexes,
            List<CardCollection<StandardCard>> moves
    ) {
        if (selectedIndexes.size() == targetSize) {
            CardCollection<StandardCard> candidate = new CardCollection<>();
            selectedIndexes.forEach(index -> candidate.addCard(hand.getCardAt(index)));

            int originalFlag = game.flag;
            game.getSelectedCards().empty();
            game.getSelectedCards().addAll(candidate);
            if (game.isValidPlay()) {
                moves.add(candidate);
            }
            game.flag = originalFlag;
            return;
        }

        int cardsNeeded = targetSize - selectedIndexes.size();
        for (int index = start; index <= hand.getSize() - cardsNeeded; index++) {
            selectedIndexes.add(index);
            collectCombinations(game, hand, targetSize, index + 1, selectedIndexes, moves);
            selectedIndexes.remove(selectedIndexes.size() - 1);
        }
    }
}
