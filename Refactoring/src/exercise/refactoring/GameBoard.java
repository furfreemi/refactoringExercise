package exercise.refactoring;

public class GameBoard {
    private final int numberOfBoards = 2;
    private int board[][];
    public static final int SQUARES_PER_SIDE = 10;
    public static int mainBoardIndex = 0;
    public static final int oneMoreThanSquaresPerSide = GameBoard.SQUARES_PER_SIDE + 1;
    public static int boardTwoIndex = 2;
    static final int TOTAL_SQUARES_PER_BOARD = 100;

    static final int MAX_DEPTH_FOR_TEMP_BOARD = 7;
    static int[][] perhapsaTemporaryBoardHolder = new int[MAX_DEPTH_FOR_TEMP_BOARD][TOTAL_SQUARES_PER_BOARD];
    static int[] stagingBoard = new int[TOTAL_SQUARES_PER_BOARD];
    static int[] tempTableForChecks = new int[TOTAL_SQUARES_PER_BOARD];

    public GameBoard() {
        this.board = new int[numberOfBoards][TOTAL_SQUARES_PER_BOARD];
    }

    int[] mainBoard() {
        return board[mainBoardIndex];
    }

    public void markMove(int position, int playerMark) {
        mainBoard()[position] = playerMark;
    }

    public GameBoardMark getValueAt(int i, int j) {
        return GameBoardMark.valueOf(board[i][j]);
    }

    public void setValueAt(int boardNumber, int position, GameBoardMark newValue) {
        board[boardNumber][position] = newValue.index;
    }

    public boolean hasEmptyValueAt(int boardNumber, int position) {
        return board[boardNumber][position] == GameBoardMark.EMPTY.index;
    }

    public boolean hasEmptyValueOnMainBoardAt(int position) {
        return mainBoard()[position] == GameBoardMark.EMPTY.index;
    }

    public boolean valueAtPositionMatches(int boardNumber, int position, GameBoardMark playerMark) {
        return board[boardNumber][position] == playerMark.index;
    }

    public boolean valueOnMainBoardAtPositionMatches(int position, int playerMark) {
        return mainBoard()[position] == playerMark;
    }

    public void setPositionToEmpty(int boardNumber, int position) {
        board[boardNumber][position] = GameBoardMark.EMPTY.index;
    }

    public void setBoardTo(int[][] boardTo) {
        this.board = boardTo;
    }

    public boolean playerXOccupiesMainBoardPosition(int position) {
        return mainBoard()[position] == GameBoardMark.X_MARK_FOR_PLAYER.index;
    }

    boolean hasOccupiedUnoccupiedOccupiedPatternStartingAt(int position) {
        return playerXOccupiesMainBoardPosition(position) && playerXOccupiesMainBoardPosition(position + 2) && hasEmptyValueOnMainBoardAt(position + 1);
    }

    public boolean hasOccupiedUnoccupiedOccupiedDiagonalPatternStartingAt(int position) {
        // Not completely sure that this is diagonal
        return playerXOccupiesMainBoardPosition(position) && playerXOccupiesMainBoardPosition(position + 20) && hasEmptyValueOnMainBoardAt(position + SQUARES_PER_SIDE);
    }

    public boolean positionIsDesirableForCreateTwoAxesOrCreateOneAndBlockAnother(int position) {
        return playerXOccupiesMainBoardPosition(position - oneMoreThanSquaresPerSide)
                || playerXOccupiesMainBoardPosition(position - SQUARES_PER_SIDE)
                || playerXOccupiesMainBoardPosition(position - 1)
                || playerXOccupiesMainBoardPosition(position + 1)
                || playerXOccupiesMainBoardPosition(position + oneMoreThanSquaresPerSide)
                || playerXOccupiesMainBoardPosition(position + SQUARES_PER_SIDE);
    }

    public void setBoardTwoToDuplicateOfMainBoard() {
        board[2] = mainBoard().clone();
    }


    boolean hasEmptyValueAtPositionOnBoardTwoAndPositionWithDiff(int position, int diff) {
        return hasEmptyValueAt(boardTwoIndex, position) && hasEmptyValueAt(boardTwoIndex, position + diff);
    }

    public GameBoardMark gameMarkAtMainBoardPosition(int position) {
        return GameBoardMark.valueOf(mainBoard()[position]);
    }

    public void resetAllMarksAlongAxesForFirstHalfOfBoard(LegacyGame legacyGame) {
        for (int k = 0; k < 4; k++)
            legacyGame.marksByAxisByPlayerForChecking[k] = 0;
    }
}