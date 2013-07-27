package exercise.refactoring;

public enum GameBoardMark {
    EMPTY(0), ZERO_MARK_FOR_COMPUTER(2),
    X_MARK_FOR_PLAYER(1);
    public final int index;

    GameBoardMark(int index) {
        this.index = index;
    }
}
