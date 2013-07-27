package exercise.refactoring;

public enum Directions {
    HORIZONTAL(0), VERTICAL(2), DIAGONAL_RIGHT(1), DIAGONAL_LEFT(3);
    final int indexInMarksForChecking;

    Directions(int indexInMarksForChecking) {
        this.indexInMarksForChecking = indexInMarksForChecking;
    }

    public static Directions valueOf(int indexInMarksForChecking) {
        for (Directions direction : Directions.values()){
            if (direction.indexInMarksForChecking == indexInMarksForChecking){
                return direction;
            }
        }
        return null;
    }
}
