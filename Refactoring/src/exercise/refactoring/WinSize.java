package exercise.refactoring;

public enum WinSize {
    COMPUTER(6), PLAYER(5);
    final int length;

    WinSize(int length) {
        this.length = length;
    }
}
