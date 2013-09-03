package exercise.refactoring;

public class MarksByAxis {
    private int[] marks;

    public MarksByAxis() {
        this.marks = new int[8];
    }

    public void setPositionsToZero(int... positions) {
        for (int position : positions) {
            marks[position] = 0;
        }
    }

    public void incrementValueAtPosition(int position) {
        marks[position] += 1;
    }

    public int incrementValueAtPositionAndReturnValue(int position) {
        marks[position] += 1;
        return marks[position];
    }

    public void incrementValueAtPosition(GameBoardMark gameBoardMark) {
        incrementValueAtPosition(gameBoardMark.index);
    }

    public int getValueAtPosition(int position) {
        return marks[position];
    }

    public boolean valueAtPositionPairsMatch(MarksByAxisPositionPair... positionPairs) {
        for(MarksByAxisPositionPair positionPair : positionPairs){
            if (getValueAtPosition(positionPair.position) != positionPair.desiredValue){
                return false;
            }
        }
        return true;
    }
}