package exercise.refactoring;

public enum Mode {
    CLEAN(3), CHECK(0), SETFLAGS(1), COUNT(2), SAFE(1);
    final int rawMode;

    Mode(int rawMode) {
        this.rawMode = rawMode;
    }
}
