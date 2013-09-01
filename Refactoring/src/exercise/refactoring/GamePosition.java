package exercise.refactoring;

public class GamePosition {
    private int position;

    public GamePosition() {
        this.position = 0;
    }

    public GamePosition(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isNotNone() {
        return position != LegacyGame.NONE;
    }

    public int getRaw() {
        return position;
    }

    public static GamePosition nonePosition() {
        return new GamePosition(LegacyGame.NONE);
    }
}
