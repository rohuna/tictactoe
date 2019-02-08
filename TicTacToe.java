import java.awt.*;
import java.util.ArrayList;

class UserAction {
    static final String ACTION_NEWGAME = "ACTION_NEWGAME";
    static final String ACTION_PLAYMOVE = "ACTION_PLAYMOVE";

    private String actionType;
    private Move move;

    UserAction(String actionType) {
        this.actionType = actionType;
    }

    UserAction(Move move) {
        this.actionType = ACTION_PLAYMOVE;
        this.move = move;
    }

    String getActionType() {
        return actionType;
    }

    Move getMove() {
        return move;
    }
}

class Move {
    private int x, y;

    Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }
}

class GameWindow {
    GameWindow() {
        StdDraw.setCanvasSize(700, 700);
        StdDraw.enableDoubleBuffering();
    }

    void render(Board board) {
        StdDraw.setPenColor(new Color(0, 0, 0));
        StdDraw.filledRectangle(0.3666, 0.6, 0.01, 0.36666);
        StdDraw.filledRectangle(0.6222, 0.6, 0.01, 0.36666);
        StdDraw.filledRectangle(0.5, 0.7, 0.36666, 0.01);
        StdDraw.filledRectangle(0.5, 0.4666, 0.3666, 0.01);
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 60));
        double x, y = 0.84;
        for (int i = 0; i < 3; i++) {
            x = 0.25;
            for (int j = 0; j < 3; j++) {
                StdDraw.text(x, y, Character.toString(board.get(i, j)));
                x += 0.2433333;
            }
            y -= 0.2433333;
        }

        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 16));
        StdDraw.text(0.9, 0.1, "New Game");

        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 60));
        if (board.isTie()) {
            StdDraw.text(0.5, 0.1, "Tie!");
        } else if (board.checkWin('X')) {
            StdDraw.text(0.5, 0.1, "Player X Wins!");
        } else if (board.checkWin('O')) {
            StdDraw.text(0.1, 0.1, "Player O Wins!");
        }

        StdDraw.show();
        StdDraw.pause(5);
    }

    UserAction getUserAction() {
        if (!StdDraw.isMousePressed()) {
            return null;
        }

        double xLoc = StdDraw.mouseX();
        double yLoc = StdDraw.mouseY();

        if (xLoc > 0.8 && yLoc < 0.2) {
            return new UserAction(UserAction.ACTION_NEWGAME);
        }

        if (xLoc > 0.13333 && xLoc < 1.0 - 0.13333 && yLoc > 0.23333 && yLoc < 0.96666) {
            int row = 2 - (int) ((yLoc - 0.23333) / 0.243333);
            int col = (int) ((xLoc - 0.13333) / 0.243333);

            if (row == 3) row = 2;
            if (col == 3) col = 2;

            return new UserAction(new Move(row, col));
        }

        return null;
    }
}

class Board {
    private char[][] board = new char[3][3];
    private char turn = 'X';

    Board() {
        this.resetGame();
    }

    char get(int i, int j) {
        return this.board[i][j];
    }

    void resetGame() {
        turn = 'X';
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    void playMove(Move move) {
        board[move.getX()][move.getY()] = turn;
        this.flipTurn();
    }

    void undoMove(Move move) {
        board[move.getX()][move.getY()] = ' ';
        this.flipTurn();
    }

    void flipTurn() {
        if (turn == 'X') turn = 'O';
        else turn = 'X';
    }

    boolean isValidMove(Move move) {
        return board[move.getX()][move.getY()] == ' ' && !checkWin('X') && !checkWin('O');
    }

    boolean isTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    boolean checkWin(char player) {
        return (board[0][0] == player && board[0][1] == player && board[0][2] == player) ||
                (board[1][0] == player && board[1][1] == player && board[1][2] == player) ||
                (board[2][0] == player && board[2][1] == player && board[2][2] == player) ||
                (board[0][0] == player && board[1][0] == player && board[2][0] == player) ||
                (board[0][1] == player && board[1][1] == player && board[2][1] == player) ||
                (board[0][2] == player && board[1][2] == player && board[2][2] == player) ||
                (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Move candidate = new Move(i, j);
                if (isValidMove(candidate)) moves.add(candidate);
            }
        }
        return moves;
    }
}

public class TicTacToe {
    public static void main(String[] args) throws InterruptedException {
        boolean playerGoesFirst = false;

        GameWindow window = new GameWindow();
        Board board = new Board();
        Player player = new Player(board, playerGoesFirst ? 'O' : 'X');

        if (!playerGoesFirst) {
            Move move = player.getBestMove();
            board.playMove(move);
        }

        while (true) {
            StdDraw.clear();
            window.render(board);

            Thread.sleep(50);

            UserAction action = window.getUserAction();
            if (action == null) continue;

            if (action.getActionType().equals(UserAction.ACTION_NEWGAME)) {
                board.resetGame();

                if (!playerGoesFirst) {
                    Move move = player.getBestMove();
                    board.playMove(move);
                }
            } else if (action.getActionType().equals(UserAction.ACTION_PLAYMOVE)) {
                Move move = action.getMove();
                if (board.isValidMove(move)) {
                    board.playMove(action.getMove());

                    Move computerMove = player.getBestMove();
                    board.playMove(computerMove);
                }
            }
        }
    }
}
