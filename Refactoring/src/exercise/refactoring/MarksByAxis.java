package exercise.refactoring;

public class MarksByAxis {
    int[] marks;

    public MarksByAxis() {
        this.marks = new int[8];
    }

    public void setPositionsToZero(int... positions) {
        for (int position : positions) {
            marks[position] = 0;
        }
    }
}