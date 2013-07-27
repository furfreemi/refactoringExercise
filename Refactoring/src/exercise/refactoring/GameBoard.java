package exercise.refactoring;

public class GameBoard {
    public int gameBoard[][];

    public GameBoard() {
        this.gameBoard = new int[3][LegacyGame.TOTAL_SQUARES_PER_BOARD];
    }

    int[] gameBoardZero() {
        return gameBoard[0];
    }
}