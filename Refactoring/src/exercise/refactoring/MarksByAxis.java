package exercise.refactoring;

public class MarksByAxis {
//    private int[] marks;
    int[] marks;

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

    public void incrementValueAtPosition(GameBoardMark gameBoardMark) {
        incrementValueAtPosition(gameBoardMark.index);
    }

    public int getValueAtPosition(int position) {
        return marks[position];
    }

    public GameBoardMark getValueAtPosition(GameBoardMark position) {
        return GameBoardMark.valueOf(getValueAtPosition(position.index));
    }
}