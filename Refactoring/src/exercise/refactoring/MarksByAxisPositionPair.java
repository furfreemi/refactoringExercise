package exercise.refactoring;

public class MarksByAxisPositionPair {
    public final int position;
    public final int desiredValue;

    public MarksByAxisPositionPair(int position, int desiredValue) {
        this.position = position;
        this.desiredValue = desiredValue;
    }

    public MarksByAxisPositionPair(GameBoardMark playerMark, int desiredValue) {
        this.position = playerMark.index;
        this.desiredValue = desiredValue;
    }
}
