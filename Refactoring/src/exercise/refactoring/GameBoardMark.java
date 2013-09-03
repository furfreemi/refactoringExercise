package exercise.refactoring;

public enum GameBoardMark {
    EMPTY(0), ZERO_MARK_FOR_COMPUTER(2),
    X_MARK_FOR_PLAYER(1), OCCUPIED(1);
    public final int index;

    private GameBoardMark(int index) {
        this.index = index;
    }

    public static GameBoardMark valueOf(int index) {
        for (GameBoardMark mark : GameBoardMark.values()) {
            if (mark.index == index) {
                return mark;
            }
        }
        return null;
    }
}
