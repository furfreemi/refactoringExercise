package exercise.refactoring;

public class GameBoard {
    private final int numberOfBoards = 2;
    private int board[][];
    public static final int SQUARES_PER_SIDE = 10;
    public static int mainBoardIndex = 0;

    public GameBoard() {
        this.board = new int[numberOfBoards][LegacyGame.TOTAL_SQUARES_PER_BOARD];
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
}