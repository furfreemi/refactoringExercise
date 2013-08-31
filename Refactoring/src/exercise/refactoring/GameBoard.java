package exercise.refactoring;

public class GameBoard {
    public static final int numberOfBoards = 2;
    public int board[][];

    public GameBoard() {
        this.board = new int[numberOfBoards][LegacyGame.TOTAL_SQUARES_PER_BOARD];
    }

    int[] mainBoard() {
        return board[0];
    }

    int[] auxilliaryBoard() {
        return board[2];
    }

    public void markMove(int position, int playerMark) {
        mainBoard()[position] = playerMark;
	}
}