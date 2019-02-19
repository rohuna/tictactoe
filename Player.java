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
        //return 0 if tie
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
        
        //for every possible move, adjust best and bestMoves accordingly
        for (Move move : possibleMove) {
            
            //play the move
            board.playMove(move);
            
            //recursively call minimax where mximizing is switched and depth increasesd
            int moveScore = minimax(!maximizing, depth + 1);
            
            //determine best based on moveScore depending on maximizing or minimizing
            if (maximizing) {
                
                /*if the best is less than the current move score, set best as the
                current move, and reset best moves with the current move*/
                if (best < moveScore) {
                    bestMoves.clear();
                    best = moveScore;
                    bestMoves.add(move);
                } 
                //if best is the same as the current move score, add it to bestMoves
                else if (best == moveScore) {
                    bestMoves.add(move);
                }
            } else {
                
                /*if the best is greater than the current move score, set best as the
                current move, and reset best moves with the current move*/
                if (best > moveScore) {
                    bestMoves.clear();
                    best = moveScore;
                    bestMoves.add(move);
                } 
                //if best is the same as the current move score, add it to bestMoves
                else if (best == moveScore) {
                    bestMoves.add(move);
                }
            }
            
            //undo the move to avoid changing the board
            board.undoMove(move);
        }
        
        //base case: depth is 0 and the best move is achieved
        if (depth == 0) {
            // Set this.bestMove with the most optimal move.
            this.bestMove = bestMoves.get(new Random().nextInt(bestMoves.size()));
        }

        return best;
    }
}
