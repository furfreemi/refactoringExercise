package exercise.refactoring;

public class TemporaryChecksTable {
    static int[] tempTableForChecks;

    public TemporaryChecksTable() {
        this.tempTableForChecks = new int[GameBoard.TOTAL_SQUARES_PER_BOARD];
    }
}