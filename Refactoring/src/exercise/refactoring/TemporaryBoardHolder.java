package exercise.refactoring;

public class TemporaryBoardHolder {//   should not be public
    static int[][] perhapsaTemporaryBoardHolder;

    public TemporaryBoardHolder() {
        this.perhapsaTemporaryBoardHolder = new int[GameBoard.MAX_DEPTH_FOR_TEMP_BOARD][GameBoard.TOTAL_SQUARES_PER_BOARD];
    }

    public static void setValueAt(int depth, int squareOnBoard, int newValue) {
        perhapsaTemporaryBoardHolder[depth][squareOnBoard] = newValue;
    }

    public static int getValueAt(int depth, int squareOnBoard) {
        return perhapsaTemporaryBoardHolder[depth][squareOnBoard];
    }
}