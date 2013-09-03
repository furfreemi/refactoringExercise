package exercise.refactoring;

public class Game {

    public final GameBoard gameBoard = new GameBoard();
    final MarksForChecking marksForChecking = new MarksForChecking();

    public GameState gameState = GameState.NoWinner;
    public MoveSequence moveNumber = new MoveSequence();
    public GamePosition mostRecentComputerMove = null;

    public boolean hasNoWinner() {
        return gameState == GameState.NoWinner;
    }

    public boolean wonByPlayer() {
        return gameState == GameState.PlayerWon;
    }

    public boolean wonByComputer() {
        return gameState == GameState.ComputerWon;
    }

    public boolean hasAWinner() {
        return gameState != GameState.NoWinner;
    }

    public void resetMainGameBoard(int boardLevel) {
        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++)
            gameBoard.setValueAt(boardLevel, k, GameBoardMark.EMPTY);
    }

    public void respondToMouseUp(GamePosition playerMove, RawPlayerMove rawPlayerMove) {
        moveNumber.increment();
        gameBoard.markMove(playerMove, GameBoardMark.X_MARK_FOR_PLAYER.index);
        if (winner() == GameBoardMark.EMPTY) {
            mostRecentComputerMove = new ComputerMove(moveNumber, rawPlayerMove, gameBoard).getNextComputerMove();
            System.out.println("Location of most recent computer move: " + mostRecentComputerMove.getRaw());
            gameBoard.markMove(mostRecentComputerMove, GameBoardMark.ZERO_MARK_FOR_COMPUTER.index);
            gameState = GameState.NoWinner;
        }
        if (winner() == GameBoardMark.ZERO_MARK_FOR_COMPUTER) {
            gameState = GameState.ComputerWon;
        }

        if (winner() == GameBoardMark.X_MARK_FOR_PLAYER) {
            gameState = GameState.PlayerWon;
        }
    }

    public void run() {
        resetMainGameBoard(0);
        moveNumber.setToGameStart();
        gameState = GameState.NoWinner;
    }

    public GameBoardMark winner() {
        if (checkForComputerWin()) return GameBoardMark.ZERO_MARK_FOR_COMPUTER;
        if (checkForPlayerWin()) return GameBoardMark.X_MARK_FOR_PLAYER;
        return GameBoardMark.EMPTY;
    }

    public boolean checkForPlayerWin() {
        return checkForWin(Player.xPlayer());
    }

    public boolean checkForComputerWin() {
        return checkForWin(Player.computerPlayer());
    }

    public boolean checkForWin(Player p) {
        int fasterIndex;
        int slowerIndex;
        for (slowerIndex = 0; slowerIndex < (GameBoard.SQUARES_PER_SIDE - p.winLength.length + 1); slowerIndex++) {

            for (fasterIndex = 0; fasterIndex < GameBoard.SQUARES_PER_SIDE; fasterIndex++) {
                marksForChecking.resetAllValuesToZero();
                changeMarksToFindHorizontalOrVerticalWin(fasterIndex, slowerIndex, p.winLength.length);
                if (p.hasWon(marksForChecking)) {
                    return true;
                }

                if (fasterIndex < (GameBoard.SQUARES_PER_SIDE - p.winLength.length + 1)) {
                    marksForChecking.resetAllValuesToZero();
                    PlayLogic.changeMarksToFindDiagonalWin(fasterIndex, slowerIndex, p.winLength.length, gameBoard, marksForChecking);
                    if (p.hasWon(marksForChecking)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void changeMarksToFindHorizontalOrVerticalWin(int fasterIndex, int slowerIndex, int winSize) {
        for (int k = 0; k < winSize; k++) {
            PlayLogic.incrementWinCountForDirection((fasterIndex * GameBoard.SQUARES_PER_SIDE + slowerIndex + k), Directions.HORIZONTAL, gameBoard, marksForChecking);
            PlayLogic.incrementWinCountForDirection((slowerIndex * GameBoard.SQUARES_PER_SIDE + fasterIndex + k * GameBoard.SQUARES_PER_SIDE), Directions.VERTICAL, gameBoard, marksForChecking);
        }


    }
}