package exercise.refactoring;

public class MoveSequence {
    private int moveNumber;
    private final int defaultMoveNumber = -1;

    public void MoveSequence(){
        this.moveNumber = defaultMoveNumber;
    }

    public void increment() {
        moveNumber += 1;
    }

    public void setToGameStart() {
        moveNumber = 0;
    }

    public boolean isFirstMove() {
        return moveNumber == 1;
    }

    public boolean isMove(int comparisonMoveNumber){
        return moveNumber == comparisonMoveNumber;
    }

    public boolean isOver(int moveNumber) {
        return this.moveNumber > moveNumber;
    }

    public boolean isDefault() {
        return moveNumber == defaultMoveNumber;
    }
}
