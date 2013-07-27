package exercise.refactoring;

public enum Directions {
    HORIZONTAL(0), VERTICAL(2), DIAGONAL_RIGHT(1), DIAGONAL_LEFT(3);
    final int indexInMarksForChecking;

    Directions(int indexInMarksForChecking) {
        this.indexInMarksForChecking = indexInMarksForChecking;
    }

}
