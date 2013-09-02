package exercise.refactoring;

class Player {
    int winLength;
    GameBoardMark playerMark;
    private static int PLAYER_WIN_LENGTH = 5;
    private static int COMPUTER_WIN_LENGTH = 6;

    private Player(int winLength, GameBoardMark playerMark) {
        this.winLength = winLength;
        this.playerMark = playerMark;
    }

    public static Player xPlayer() {
        return new Player(PLAYER_WIN_LENGTH, GameBoardMark.X_MARK_FOR_PLAYER);
    }

    public static Player computerPlayer() {
        return new Player(COMPUTER_WIN_LENGTH, GameBoardMark.ZERO_MARK_FOR_COMPUTER);
    }
}
