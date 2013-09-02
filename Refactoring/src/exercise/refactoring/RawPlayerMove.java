package exercise.refactoring;

public class RawPlayerMove {
    final int x;
    final int y;

    public RawPlayerMove(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private GamePosition computerMoveIfFirstComputerMoveAndPlayerMoveOverFive() {
        return new GamePosition(y * GameBoard.SQUARES_PER_SIDE + x - GameBoard.oneMoreThanSquaresPerSide);
    }

    private GamePosition computerMoveIfFirstComputerMoveAndPlayerMoveUnderFive() {
        return new GamePosition(y * GameBoard.SQUARES_PER_SIDE + x + GameBoard.oneMoreThanSquaresPerSide);
    }

    public GamePosition firstComputerMove() {
        if (y > 5) {
            return computerMoveIfFirstComputerMoveAndPlayerMoveOverFive();
        } else {
            return computerMoveIfFirstComputerMoveAndPlayerMoveUnderFive();
        }
    }
}
