package exercise.refactoring;

public class RawPlayerMove {
    final int x;
    final int y;

    public RawPlayerMove(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GamePosition computerMoveIfFirstComputerMoveAndPlayerMoveOverFive() {
        return new GamePosition(y * GameBoard.SQUARES_PER_SIDE + x - GameBoard.oneMoreThanSquaresPerSide);
    }

    public GamePosition computerMoveIfFirstComputerMoveAndPlayerMoveUnderFive() {
        return new GamePosition(y * GameBoard.SQUARES_PER_SIDE + x + GameBoard.oneMoreThanSquaresPerSide);
    }
}
