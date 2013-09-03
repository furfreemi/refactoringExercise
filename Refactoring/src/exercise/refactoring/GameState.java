package exercise.refactoring;

public enum GameState {
    NoWinner(0), ComputerWon(3), PlayerWon(2);
    private final int state;

    GameState(int state) {
        this.state = state;
    }
}
