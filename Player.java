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
        
        //if the board is a tie return 0
        if (board.isTie()) return 0;

        /*if either computer or human win, return the respective winning
        score except reduce the depth from either 100 or -100 (add for human,
        subtract for computer) to ensure that the winning move with the least
        depth is chosen */
        if (board.checkWin(this.computerSymbol)) return 100 - depth;
        if (board.checkWin(this.humanSymbol)) return -100 + depth;

        int best = 0;

        /*set best to either -100 or 100 depending on maximizing.*/
        if (maximizing) best = -100;
        if (!maximizing) best = 100;

        //all possible moves from curent board state
        ArrayList<Move> possibleMoves = board.getPossibleMoves();

        //the moves where the current player wins
        ArrayList<Move> winningMoves = new ArrayList<>();

        //loop throught every move in possibleMoves
        for (Move move : possibleMoves) {

            //play the move
            board.playMove(move);

            /*set moveScore by recursing to the next play (the next player
            is either maximizing or minimizing and the depth increases by 1)*/
            int moveScore = minimax(!maximizing, depth + 1);

            /*in the case of maximizing, add to winningMoves if the moveScore
            is better than the current best*/
            if (maximizing) {

                /*in the case that the current best is less than the moveScore
                (that means the moveScore is better), reset the winningMoves
                list (because now there is a better winning move), set the best
                to the moveScore, and add the new winning move to the winningMoves
                list*/
                if (best < moveScore) {
                    winningMoves.clear();
                    best = moveScore;
                    winningMoves.add(move);
                 } 
                 /*in the case that the moveScore is the same as the current best
                 score, then another optimal move is found so it must be added to the
                 winning moves*/
                 else if (best == moveScore) {
                    winningMoves.add(move);
                }
            } 
            
            /*repeating the same logic as with maximizing but the comparisn
            between best and moveScore is switched */
            else {
                if (best > moveScore) {
                    winningMoves.clear();
                    best = moveScore;
                    winningMoves.add(move);
                } else if (best == moveScore) {
                    winningMoves.add(move);
                }
            }

            //undo the move to prevent disrupting the actual board
            board.undoMove(move);
        }

        //Base Case: randomly assign the bestMove out of the winning moves
        if (depth == 0) {
            // Set this.bestMove with the most optimal move.
            this.bestMove = winningMoves.get(new Random().nextInt(winningMoves.size()));
        }

        //only needed for compilation
        return best;
    }
}
