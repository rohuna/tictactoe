import java.util.ArrayList;
import java.util.Random;

class Player {
    private Board board;
    private Move bestMove;
    private char computerSymbol, humanSymbol;

    Player(Board board, char computerSymbol) {
        this.board = board;
        this.computerSymbol = computerSymbol;
        if (computerSymbol == 'X') humanSymbol = 'O';
        else humanSymbol = 'X';
    }

    Move getBestMove() {
        minimax(true, 0);
        return bestMove;
    }

    /**
     * Returns the score of the current game board state.
     *
     * Additionally, if depth = 0, sets Player.bestMove to the optimal move.
     * If there are multiple optimal moves, it randomly selects one.
     *
     * A score of -100 = human player wins, and a score of +100 = AI wins
     */
    private int minimax(boolean maximizing, int depth) {
        if (board.isTie()) return 0;
        // prioritize lesser depth moves
        if (board.checkWin(this.computerSymbol)) return 100 - depth;
        if (board.checkWin(this.humanSymbol)) return -100 + depth;

        int best = 0;
        // we want O to win => choose the highest score
        if (maximizing) best = -100;
        // we want X to win => chose the lowest score
        if (!maximizing) best = 100;

        ArrayList<Move> possibleMove = board.getPossibleMoves();
        ArrayList<Move> bestMoves = new ArrayList<>();
        for (Move move : possibleMove) {
            board.playMove(move);
            int moveScore = minimax(!maximizing, depth + 1);

            if (maximizing) {
                if (best < moveScore) {
                    bestMoves.clear();
                    best = moveScore;
                    bestMoves.add(move);
                } else if (best == moveScore) {
                    bestMoves.add(move);
                }
            } else {
                if (best > moveScore) {
                    bestMoves.clear();
                    best = moveScore;
                    bestMoves.add(move);
                } else if (best == moveScore) {
                    bestMoves.add(move);
                }
            }

            board.undoMove(move);
        }

        if (depth == 0) {
            // Set this.bestMove with the most optimal move.
            this.bestMove = bestMoves.get(new Random().nextInt(bestMoves.size()));
        }

        return best;
    }
}
