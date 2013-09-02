package exercise.refactoring;

class Player {
    WinSize winLength;
    GameBoardMark playerMark;

    private Player(WinSize winLength, GameBoardMark playerMark) {
        this.winLength = winLength;
        this.playerMark = playerMark;
    }

    public static Player xPlayer() {
        return new Player(WinSize.PLAYER, GameBoardMark.X_MARK_FOR_PLAYER);
    }

    public static Player computerPlayer() {
        return new Player(WinSize.COMPUTER, GameBoardMark.ZERO_MARK_FOR_COMPUTER);
    }

    public boolean sumForDirectionIsLargerThanWinLength(Directions direction, MarksForChecking marksForChecking) {
        return marksForChecking.countForDirectionAndPlayerMark(direction, playerMark) >= winLength.length;
    }
}
