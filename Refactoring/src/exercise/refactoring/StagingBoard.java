package exercise.refactoring;

public class StagingBoard {
  static GameBoardMark[] stagingBoard;

    public StagingBoard() {
        this.stagingBoard = new GameBoardMark[GameBoard.TOTAL_SQUARES_PER_BOARD];
    }

    public static boolean isEmptyAtPosition(int position) {
        return stagingBoard[position] == GameBoardMark.EMPTY;
    }

    public static void setValueAt(int position, int newValue) {
        stagingBoard[position] = GameBoardMark.valueOf(newValue);
    }

    public static int getValueAt(int position) {
        return stagingBoard[position].index;
    }

    public boolean isOccupiedAtPosition(int position) {
        return stagingBoard[position] == GameBoardMark.OCCUPIED;
    }

    public static void setValueAtPositionToEmpty(int positionToEmpty) {
        stagingBoard[positionToEmpty] = GameBoardMark.EMPTY;
    }

    public static void setValueAtPositionToOccupied(int position) {
        stagingBoard[position] = GameBoardMark.OCCUPIED;
    }
}