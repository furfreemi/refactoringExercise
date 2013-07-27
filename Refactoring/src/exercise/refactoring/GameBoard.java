package exercise.refactoring;

public class GameBoard {
    public int gameBoard[][];

    public GameBoard() {
        this.gameBoard = new int[3][LegacyGame.TOTAL_SQUARES_PER_BOARD];
    }

    int[] gameBoardZero() {
        return gameBoard[0];
    }

    int[] gameBoardTwo() {
        return gameBoard[2];
    }

    public void markMove(int position, int playerMark) {
        gameBoardZero()[position] = playerMark;
	}
}