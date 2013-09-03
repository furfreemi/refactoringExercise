package exercise.refactoring;

public class PlayLogic {
    static void changeMarksToFindDiagonalWin(int fasterIndex, int slowerIndex, int winSize, GameBoard gameBoard, MarksForChecking marksForChecking) {
        for (int k = 0; k < winSize; k++) {
            incrementWinCountForDirection(indexOnBoardForDiagonalLeft(fasterIndex, slowerIndex, winSize, k), Directions.DIAGONAL_LEFT, gameBoard, marksForChecking);
            incrementWinCountForDirection(indexOnBoardForDiagonalRight(fasterIndex, slowerIndex, k), Directions.DIAGONAL_RIGHT, gameBoard, marksForChecking);
        }
    }

    static void incrementWinCountForDirection(int indexOnBoard, Directions direction, GameBoard gameBoard, MarksForChecking marksForChecking) {
        if (indexOnBoard < GameBoard.oneMoreThanLastPositionOnBoard && indexOnBoard >= 0) { // i.e. position.isValid()
            marksForChecking.incrementValueFor(direction, gameBoard.gameMarkAtMainBoardPosition(indexOnBoard));
        }
    }

    private static int indexOnBoardForDiagonalRight(int fasterIndex, int slowerIndex, int k) {
        return slowerIndex * GameBoard.SQUARES_PER_SIDE + fasterIndex + k * (GameBoard.SQUARES_PER_SIDE + 1);
    }

    private static int indexOnBoardForDiagonalLeft(int fasterIndex, int slowerIndex, int winSize, int k) {
        return slowerIndex * GameBoard.SQUARES_PER_SIDE + fasterIndex - k * GameBoard.oneLessThanCountInRow + (winSize - 1) * GameBoard.SQUARES_PER_SIDE;
    }
}
