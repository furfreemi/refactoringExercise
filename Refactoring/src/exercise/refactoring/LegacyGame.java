package exercise.refactoring;

public class LegacyGame {

    public static final int TOTAL_SQUARES_PER_BOARD = 100;
    public static final int oneLessThanCountInRow = GameBoard.SQUARES_PER_SIDE - 1;


    public final GameBoard gameBoard = new GameBoard();
    final MarksForChecking marksForChecking = new MarksForChecking();
    final ComputerMove computerMove = new ComputerMove(this);

    public int gameState = 0;
    public MoveSequence moveNumber = new MoveSequence();
    public int lastMove = NONE;

    private static final int MAX_DEPTH = 7;
    static final int NONE = 100;
    static final int OCCUPIED = 1;

    int[] marksByAxisByPlayerForChecking = new int[8];
    private int perhapsaTemporaryBoardHolder[][] = new int[MAX_DEPTH][TOTAL_SQUARES_PER_BOARD];
    int[] stagingBoard = new int[TOTAL_SQUARES_PER_BOARD];
    private int tempTableForChecks[] = new int[TOTAL_SQUARES_PER_BOARD];

    public int findSpot() {
        int position;
        int i;
        i = GameBoardMark.EMPTY.index;
        do {
            position = (int) (Math.random() * TOTAL_SQUARES_PER_BOARD);
            if (gameBoard.mainBoard()[position] != GameBoardMark.EMPTY.index) continue;
            if ((position > 0 && gameBoard.mainBoard()[position - 1] != GameBoardMark.EMPTY.index) || (position > GameBoard.SQUARES_PER_SIDE && (gameBoard.mainBoard()[position - 11] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position - GameBoard.SQUARES_PER_SIDE] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position - oneLessThanCountInRow] != GameBoardMark.EMPTY.index)) || (position < 99 && gameBoard.mainBoard()[position + 1] != GameBoardMark.EMPTY.index) || (position < 88 && (gameBoard.mainBoard()[position + oneLessThanCountInRow] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position + GameBoard.SQUARES_PER_SIDE] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position + 11] != GameBoardMark.EMPTY.index))) i = OCCUPIED;
        } while (i == GameBoardMark.EMPTY.index);
        return position;
    }

    public int checkForWinOpportunity(GameBoardMark playerMark, int boardNumber) {
        if (isNone(blockSeriesOfFourOrMore(switchPlayers(playerMark), boardNumber, Mode.CHECK))) {
            return blockSeriesOfFourOrMore(switchPlayers(playerMark), boardNumber, Mode.CHECK);
        }

        if (isNone(responseTo3Or4InaRowOpportunity(switchPlayers(playerMark), boardNumber, Mode.CHECK))) {
            return responseTo3Or4InaRowOpportunity(switchPlayers(playerMark), boardNumber, Mode.CHECK);
        }

        return NONE;
    }

    private boolean isNone(int response) {
        return response != NONE;
    }

    public GameBoardMark winner() {
        if (checkForComputerWin()) return GameBoardMark.ZERO_MARK_FOR_COMPUTER;
        if (checkForPlayerWin()) return GameBoardMark.X_MARK_FOR_PLAYER;
        return GameBoardMark.EMPTY;
    }

    public int checkSeries(GameBoardMark playerMark, int depth) {
        int[] auxilliaryBoard = new int[LegacyGame.TOTAL_SQUARES_PER_BOARD];
        int winningPosition;
        if (depth == MAX_DEPTH) return NONE;

        setc4c(playerMark);

        for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
            if (stagingBoard[k] == GameBoardMark.EMPTY.index || auxilliaryBoard[k] != GameBoardMark.EMPTY.index) continue;
            copyStagingBoardIntoOddGroupOfBoardsAtDepth(depth);

            auxilliaryBoard[k] = playerMark.index;

            winningPosition = checkForWinOpportunity(switchPlayers(playerMark), 2);
            if (winningPosition == NONE) return NONE;

            auxilliaryBoard[winningPosition] = switchPlayers(playerMark).index;
            if (blockSeriesOfFourOrMore(playerMark, 2, Mode.CHECK) != NONE) {
                return k;
            }

            if (blockSeriesOfFourOrMore(switchPlayers(playerMark), 2, Mode.CHECK) != NONE) {
                auxilliaryBoard[k] = GameBoardMark.EMPTY.index;
                auxilliaryBoard[winningPosition] = GameBoardMark.EMPTY.index;
                copyIntoStagingBoardFromOddBoardGroupAtDepth(depth);
                continue;
            }

            if (checkSeries(playerMark, depth + 1) != NONE) {
                return k;
            }

            auxilliaryBoard[k] = GameBoardMark.EMPTY.index;
            auxilliaryBoard[winningPosition] = GameBoardMark.EMPTY.index;
            copyIntoStagingBoardFromOddBoardGroupAtDepth(depth);
        }
        return NONE;
    }

    public GameBoardMark switchPlayers(GameBoardMark playerMark) {
        return GameBoardMark.valueOf(3 - playerMark.index);
    }

    public int checkBox(GameBoardMark playerMark) {
        for (int k = 1; k < 8; k++) {
            for (int l = 1; l < 8; l++) {
                int cnt = 0;
                int pos = -1;
                for (int a = 0; a < 2; a++) {
                    for (int b = 0; b < 2; b++) {
                        int x = k + a + 10 * (l + b);
                        int c = gameBoard.mainBoard()[x];
                        if (c == playerMark.index) cnt++;
                        else if (c == 0) pos = x;
                    }
                }
                if (cnt == 3 && pos != -1) return pos;
            }
        }
        return LegacyGame.NONE;
    }

    public int checkCross(GameBoardMark playerMark) {
        int k, l, x;

        for (k = 1; k < 7; k++) {
            for (l = 1; l < 7; l++) {
                x = k + 10 * l;
                if (gameBoard.mainBoard()[x] == playerMark.index && gameBoard.mainBoard()[x + 2] == playerMark.index && gameBoard.mainBoard()[x + 20] == playerMark.index && gameBoard.mainBoard()[x + 22] == playerMark.index && gameBoard.mainBoard()[x + 11] == 0) return (x + 11);
            }
        }
        return LegacyGame.NONE;
    }

    public int countNumberOfAxesAlongWhichSeriesOfFourOccur(GameBoardMark playerMark, int x, int type) {
        int j, k, l;
        int zbir = 0;
        int flag, flag2;

        flag = GameBoardMark.EMPTY.index;
        for (j = 0; j < 10; j++) {
            for (l = 0; l < 6; l++) {
                marksByAxisByPlayerForChecking[0] = 0;
                marksByAxisByPlayerForChecking[1] = 0;
                marksByAxisByPlayerForChecking[2] = 0;
                for (k = 0; k < 5; k++)
                    marksByAxisByPlayerForChecking[gameBoard.getValueAt(x, j * 10 + l + k).index]++;
                if (marksByAxisByPlayerForChecking[playerMark.index] == 4 && marksByAxisByPlayerForChecking[GameBoardMark.EMPTY.index] == 1) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, j * 10 + l + k) && tempTableForChecks[j * 10 + l + k] == LegacyGame.OCCUPIED) {
                                flag2 = LegacyGame.OCCUPIED;
                            }
                        }
                        if (flag2 == GameBoardMark.EMPTY.index) {
                            zbir++;
                            flag = LegacyGame.OCCUPIED;
                            break;
                        }
                    } else {
                        zbir++;
                        flag = LegacyGame.OCCUPIED;
                        break;
                    }
                }
            }
            if (flag == LegacyGame.OCCUPIED) break;
        }
        flag = GameBoardMark.EMPTY.index;

        for (j = 0; j < 10; j++) {
            for (l = 0; l < 6; l++) {
                marksByAxisByPlayerForChecking[0] = 0;
                marksByAxisByPlayerForChecking[1] = 0;
                marksByAxisByPlayerForChecking[2] = 0;
                for (k = 0; k < 5; k++)
                    marksByAxisByPlayerForChecking[gameBoard.getValueAt(x, l * 10 + j + k * 10).index]++;
                if (marksByAxisByPlayerForChecking[playerMark.index] == 4 && marksByAxisByPlayerForChecking[GameBoardMark.EMPTY.index] == 1) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++)
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j + k * 10) && tempTableForChecks[l * 10 + j + k * 10] == LegacyGame.OCCUPIED) flag2 = LegacyGame.OCCUPIED;
                        if (flag2 == GameBoardMark.EMPTY.index) {
                            zbir++;
                            flag = LegacyGame.OCCUPIED;
                            break;
                        }
                    } else {
                        zbir++;
                        flag = LegacyGame.OCCUPIED;
                        break;
                    }
                }
            }
            if (flag == LegacyGame.OCCUPIED) break;
        }
        flag = GameBoardMark.EMPTY.index;
        for (l = 0; l < 6; l++) {
            for (j = 0; j < 6; j++) {
                for (k = 0; k < 3; k++)
                    /* za diag */
                    marksByAxisByPlayerForChecking[k] = 0;
                for (k = 0; k < 5; k++)
                    /* diag\ */
                    marksByAxisByPlayerForChecking[gameBoard.getValueAt(x, l * 10 + j + k * 11).index]++;
                if (marksByAxisByPlayerForChecking[playerMark.index] == 4 && marksByAxisByPlayerForChecking[GameBoardMark.EMPTY.index] == 1) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j + k * 11) && tempTableForChecks[l * 10 + j + k * 11] == LegacyGame.OCCUPIED) {
                                flag2 = LegacyGame.OCCUPIED;
                            }
                        }
                        if (flag2 == GameBoardMark.EMPTY.index) {
                            zbir++;
                            flag = LegacyGame.OCCUPIED;
                            break;
                        }
                    } else {
                        zbir++;
                        flag = LegacyGame.OCCUPIED;
                        break;
                    }
                }
            }
            if (flag == LegacyGame.OCCUPIED) break;
        }
        flag = GameBoardMark.EMPTY.index;
        for (l = 0; l < 6; l++) {
            for (j = 0; j < 6; j++) {
                for (k = 0; k < 3; k++)
                    marksByAxisByPlayerForChecking[k] = 0;
                for (k = 0; k < 5; k++)
                    marksByAxisByPlayerForChecking[gameBoard.getValueAt(x, l * 10 + j - k * oneLessThanCountInRow + 40).index]++;
                if (marksByAxisByPlayerForChecking[playerMark.index] == 4 && marksByAxisByPlayerForChecking[GameBoardMark.EMPTY.index] == 1) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j - k * oneLessThanCountInRow + 40) && tempTableForChecks[l * 10 + j - k * oneLessThanCountInRow + 40] == LegacyGame.OCCUPIED) {
                                flag2 = LegacyGame.OCCUPIED;
                            }
                        }
                        if (flag2 == GameBoardMark.EMPTY.index) {
                            zbir++;
                            flag = LegacyGame.OCCUPIED;
                            break;
                        }
                    } else {
                        zbir++;
                        flag = LegacyGame.OCCUPIED;
                        break;
                    }
                }
            }
            if (flag == LegacyGame.OCCUPIED) break;
        }
        return zbir;
    }

    public int tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark playerMark) {
        int k;
        int gameBoardLevelToCheck = GameBoard.mainBoardIndex;

        for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
            gameBoard.setValueAt(1, k, gameBoard.getValueAt(gameBoardLevelToCheck, k));
        for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
            if (gameBoard.hasEmptyValueAt(1, k)) {
                gameBoard.setValueAt(1, k, playerMark);
                if (responseTo3Or4InaRowOpportunity(playerMark, 1, Mode.CHECK) != NONE && countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, Mode.SAFE.rawMode) > 0) {
                    return k;
                }
                gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (NONE);
    }

    public void setc4c(GameBoardMark playerMark) {
        int j, k, l;
        int position, x = 2; // TODO make this Position object

        for (j = 0; j < TOTAL_SQUARES_PER_BOARD; j++) {
            stagingBoard[j] = GameBoardMark.EMPTY.index;
        }
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxisByPlayerForChecking[0] = 0;
                marksByAxisByPlayerForChecking[1] = 0;
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * GameBoard.SQUARES_PER_SIDE + k + l).index;
                    if (position == playerMark.index) marksByAxisByPlayerForChecking[0]++;
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * GameBoard.SQUARES_PER_SIDE + k + l;
                        marksByAxisByPlayerForChecking[1]++;
                    }
                }
                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                    stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxisByPlayerForChecking[0] = 0;
                marksByAxisByPlayerForChecking[1] = 0;
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE).index;
                    if (position == playerMark.index) marksByAxisByPlayerForChecking[0]++;
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE;
                        marksByAxisByPlayerForChecking[1]++;
                    }
                }
                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                    stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }

        for (j = 0; j < 6; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxisByPlayerForChecking[0] = 0;
                marksByAxisByPlayerForChecking[1] = 0;
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * GameBoard.SQUARES_PER_SIDE + k + l * 11).index;
                    if (position == playerMark.index) marksByAxisByPlayerForChecking[0]++;
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * GameBoard.SQUARES_PER_SIDE + k + l * 11;
                        marksByAxisByPlayerForChecking[1]++;
                    }
                }
                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                    stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }

        for (j = 0; j < 6; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxisByPlayerForChecking[0] = 0;
                marksByAxisByPlayerForChecking[1] = 0;
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * GameBoard.SQUARES_PER_SIDE + k - l * oneLessThanCountInRow + 40).index;
                    if (position == playerMark.index) marksByAxisByPlayerForChecking[0]++;
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * GameBoard.SQUARES_PER_SIDE + k - l * oneLessThanCountInRow + 40;
                        marksByAxisByPlayerForChecking[1]++;
                    }
                }
                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                    stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }
    }

    public void seto4cc(GameBoardMark playerMark) {
        int j, k, l;
        int position;

        for (j = 0; j < TOTAL_SQUARES_PER_BOARD; j++) {
            stagingBoard[j] = GameBoardMark.EMPTY.index;
        }
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 5; k++) {
                position = j * GameBoard.SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAtPositionOnBoardTwoAndPositionWithDiff(position, 5)) {
                    marksByAxisByPlayerForChecking[0] = 0;
                    marksByAxisByPlayerForChecking[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.boardTwoIndex, j * GameBoard.SQUARES_PER_SIDE + k + l).index;
                        if (position == playerMark.index) marksByAxisByPlayerForChecking[0]++;
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * GameBoard.SQUARES_PER_SIDE + k + l;
                            marksByAxisByPlayerForChecking[1]++;
                        }
                    }
                    if (marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                        stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 5; k++) {
                position = k * GameBoard.SQUARES_PER_SIDE + j;
                if (gameBoard.hasEmptyValueAtPositionOnBoardTwoAndPositionWithDiff(position, 50)) {
                    marksByAxisByPlayerForChecking[0] = 0;
                    marksByAxisByPlayerForChecking[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.boardTwoIndex, k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE).index;
                        if (position == playerMark.index) marksByAxisByPlayerForChecking[0]++;
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE;
                            marksByAxisByPlayerForChecking[1]++;
                        }
                    }
                    if (marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                        stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }

        for (j = 0; j < 5; j++) {
            for (k = 0; k < 5; k++) {
                position = j * GameBoard.SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAt(GameBoard.boardTwoIndex, position) && gameBoard.hasEmptyValueAt(GameBoard.boardTwoIndex, position + 55)) {
                    marksByAxisByPlayerForChecking[0] = 0;
                    marksByAxisByPlayerForChecking[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.boardTwoIndex, j * GameBoard.SQUARES_PER_SIDE + k + l * 11).index;
                        if (position == playerMark.index) marksByAxisByPlayerForChecking[0]++;
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * GameBoard.SQUARES_PER_SIDE + k + l * 11;
                            marksByAxisByPlayerForChecking[1]++;
                        }
                    }
                    if (marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                        stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }

        for (j = 0; j < 5; j++) {
            for (k = 0; k < 5; k++) {
                position = j * GameBoard.SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAt(GameBoard.boardTwoIndex, position + 50) && gameBoard.hasEmptyValueAt(GameBoard.boardTwoIndex, position + 5)) {
                    marksByAxisByPlayerForChecking[0] = 0;
                    marksByAxisByPlayerForChecking[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.boardTwoIndex, j * GameBoard.SQUARES_PER_SIDE + k - l * oneLessThanCountInRow + 50).index;
                        if (position == playerMark.index) marksByAxisByPlayerForChecking[0]++;
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * GameBoard.SQUARES_PER_SIDE + k - l * oneLessThanCountInRow + 50;
                            marksByAxisByPlayerForChecking[1]++;
                        }
                    }
                    if (marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                        stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }
    }

    public int blockSeriesOfFourOrMore(GameBoardMark playerMark, int x, Mode type) {
        int j, k, l;
        int position = 0, position2 = 0;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (l = 0; l < 6; l++) {
            for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
                resetAllMarksAlongAxesForFirstHalfOfBoard();

                position = checkFor5AlongHorizAxis(playerMark, x, j, l, position);

                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) {
                    if (type.equals(Mode.SETFLAGS)) {
                        tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type.equals(Mode.CLEAN)) return tempRowForChecks[0];
                }

                if (marksByAxisByPlayerForChecking[0] == 4 && marksByAxisByPlayerForChecking[1] == 1 && type.equals(Mode.CHECK)) return position;

                position = checkFor5AlongVertAxis(playerMark, x, j, l, position);

                if (marksByAxisByPlayerForChecking[2] == 3 && marksByAxisByPlayerForChecking[3] == 2) {
                    if (type.equals(Mode.SETFLAGS)) {
                        tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type.equals(Mode.CLEAN)) return tempRowForChecks[0];
                }
                if (marksByAxisByPlayerForChecking[2] == 4 && marksByAxisByPlayerForChecking[3] == 1 && type.equals(Mode.CHECK)) return position;
            }

            for (j = 0; j < 6; j++) {
                resetAllMarksAlongAxesForFirstHalfOfBoard();

                for (k = 0; k < 5; k++) {
                    position = checkFor5AlongDiagDownRightAxis(playerMark, x, j, k, l, position);
                    position2 = checkFor5AlongDiagUpRightAxis(playerMark, x, j, k, l, position2);
                }

                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) {
                    if (type.equals(Mode.SETFLAGS)) {
                        tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type.equals(Mode.CLEAN)) return tempRowForChecks[0];
                }
                if (marksByAxisByPlayerForChecking[0] == 4 && marksByAxisByPlayerForChecking[1] == 1 && type.equals(Mode.CHECK)) return position;

                if (marksByAxisByPlayerForChecking[2] == 3 && marksByAxisByPlayerForChecking[3] == 2) {
                    if (type.equals(Mode.SETFLAGS)) {
                        tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type.equals(Mode.CLEAN)) return tempRowForChecks[0];
                }
                if (marksByAxisByPlayerForChecking[2] == 4 && marksByAxisByPlayerForChecking[3] == 1 && type.equals(Mode.CHECK)) return position2;
            }
        }
        return (NONE);
    }

    public int checkFor5AlongDiagUpRightAxis(GameBoardMark playerMark, int x, int j, int k, int l, int position2) {
        if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 40, playerMark)) marksByAxisByPlayerForChecking[2]++;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 40)) {
            position2 = l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 40;
            tempRowForChecks[marksByAxisByPlayerForChecking[3]] = position2;
            marksByAxisByPlayerForChecking[3]++;
        }
        return position2;
    }

    public int checkFor5AlongDiagDownRightAxis(GameBoardMark playerMark, int x, int j, int k, int l, int position) {
        if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j + k * 11, playerMark)) marksByAxisByPlayerForChecking[0]++;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j + k * 11)) {
            position = l * GameBoard.SQUARES_PER_SIDE + j + k * 11;
            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = position;
            marksByAxisByPlayerForChecking[1]++;
        }
        return position;
    }

    public int checkFor5AlongVertAxis(GameBoardMark playerMark, int x, int j, int l, int position) {
        int k;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE, playerMark)) marksByAxisByPlayerForChecking[2]++;
            else if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE)) {
                position = GameBoard.SQUARES_PER_SIDE * l + j + k * GameBoard.SQUARES_PER_SIDE;
                tempRowForChecks[marksByAxisByPlayerForChecking[3]] = position;
                marksByAxisByPlayerForChecking[3]++;
            } else break;
        }
        return position;
    }

    public int checkFor5AlongHorizAxis(GameBoardMark playerMark, int x, int j, int l, int position) {
        int k;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(x, j * GameBoard.SQUARES_PER_SIDE + l + k, playerMark)) marksByAxisByPlayerForChecking[0]++;

            else if (gameBoard.hasEmptyValueAt(x, j * GameBoard.SQUARES_PER_SIDE + l + k)) {
                position = GameBoard.SQUARES_PER_SIDE * j + l + k;
                tempRowForChecks[marksByAxisByPlayerForChecking[1]] = position;
                marksByAxisByPlayerForChecking[1]++;

            } else break;
        }
        return position;
    }

    public int tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark playerMark) {
        copyBoardToCheck(0);

        for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
            if (gameBoard.hasEmptyValueAt(1, k)) {
                gameBoard.setValueAt(1, k, playerMark);

                if (countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, Mode.CLEAN.rawMode) > 1) return k;

                gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (NONE);
    }

    public int responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark playerMark) {
        int j, k, l;
        int place = 0;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 4; k++) {
            tempRowForChecks[k] = 0;
        }

        for (l = 0; l < 5; l++) {
            for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(j * GameBoard.SQUARES_PER_SIDE + l) && gameBoard.hasEmptyValueOnMainBoardAt(j * GameBoard.SQUARES_PER_SIDE + l + 5)) {

                    place = checkForHoriz4InRow(playerMark, 0, j, l);
                    if (anyHoriz4MatchToMark(Mode.CHECK, place)) return place;
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j) && gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 50)) {

                    place = checkForVert4InRow(playerMark, 0, j, l);
                    if (anyVert4MatchToMark(Mode.CHECK, place)) return place;
                }
            }

            for (j = 0; j < 5; j++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j) && gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 55)) {

                    place = checkForDiagDown4InRow(playerMark, 0, j, l);
                    if (anyDiagDown4MatchToMark(Mode.CHECK, place)) return place;
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 50) && gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 5)) {
                    place = checkForDiagUp4InRow(playerMark, 0, j, l);
                    if (anyDiagUp4MatchToMark(Mode.CHECK, place)) return place;
                }
            }
        }

        if (Mode.CHECK.equals(Mode.COUNT)) {
            return tempRowForChecks[0] + tempRowForChecks[1] + tempRowForChecks[2] + tempRowForChecks[3];
        }

        return (NONE);
    }

    public GamePosition responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark playerMark) {
        int alsoUpToFive, upToFour, upToFive;
        int place = 0;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (upToFour = 0; upToFour < 4; upToFour++) {
            tempRowForChecks[upToFour] = 0;
        }

        for (upToFive = 0; upToFive < 5; upToFive++) {
            for (alsoUpToFive = 0; alsoUpToFive < GameBoard.SQUARES_PER_SIDE; alsoUpToFive++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(alsoUpToFive * GameBoard.SQUARES_PER_SIDE + upToFive) && gameBoard.hasEmptyValueOnMainBoardAt(alsoUpToFive * GameBoard.SQUARES_PER_SIDE + upToFive + 5)) {

                    place = checkForHoriz4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (anyHoriz4MatchToMark(Mode.CLEAN, place)) return new GamePosition(place);
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 50)) {

                    place = checkForVert4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (anyVert4MatchToMark(Mode.CLEAN, place)) return new GamePosition(place);
                }
            }

            for (alsoUpToFive = 0; alsoUpToFive < 5; alsoUpToFive++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 55)) {

                    place = checkForDiagDown4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (anyDiagDown4MatchToMark(Mode.CLEAN, place)) return new GamePosition(place);
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 50) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 5)) {
                    place = checkForDiagUp4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (anyDiagUp4MatchToMark(Mode.CLEAN, place)) return new GamePosition(place);
                }
            }
        }

        return GamePosition.nonePosition();
    }

    public int responseTo3Or4InaRowOpportunity(GameBoardMark playerMark, int boardLevel, Mode type) {
        int j, k, l;
        int place = 0;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 4; k++) {
            tempRowForChecks[k] = 0;
        }

        for (l = 0; l < 5; l++) {
            for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueAt(boardLevel, j * GameBoard.SQUARES_PER_SIDE + l) && gameBoard.hasEmptyValueAt(boardLevel, j * GameBoard.SQUARES_PER_SIDE + l + 5)) {

                    place = checkForHoriz4InRow(playerMark, boardLevel, j, l);
                    if (anyHoriz4MatchToMark(type, place)) return place;
                }

                if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j) && gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + 50)) {

                    place = checkForVert4InRow(playerMark, boardLevel, j, l);
                    if (anyVert4MatchToMark(type, place)) return place;
                }
            }

            for (j = 0; j < 5; j++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j) && gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + 55)) {

                    place = checkForDiagDown4InRow(playerMark, boardLevel, j, l);
                    if (anyDiagDown4MatchToMark(type, place)) return place;
                }

                if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + 50) && gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + 5)) {
                    place = checkForDiagUp4InRow(playerMark, boardLevel, j, l);
                    if (anyDiagUp4MatchToMark(type, place)) return place;
                }
            }
        }

        if (type.equals(Mode.COUNT)) {
            return tempRowForChecks[0] + tempRowForChecks[1] + tempRowForChecks[2] + tempRowForChecks[3];
        }

        return (NONE);
    }

    public int checkForHoriz4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) { /* horiz */
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, j * GameBoard.SQUARES_PER_SIDE + l + k, playerMark)) marksByAxisByPlayerForChecking[0]++;
            else if (gameBoard.hasEmptyValueAt(boardLevel, j * GameBoard.SQUARES_PER_SIDE + l + k)) {
                place = GameBoard.SQUARES_PER_SIDE * j + l + k;
                marksByAxisByPlayerForChecking[1]++;
            } else break;
        }
        return place;
    }

    public int checkForVert4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE, playerMark)) marksByAxisByPlayerForChecking[2]++;
            else if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE)) {
                place = GameBoard.SQUARES_PER_SIDE * l + j + k * GameBoard.SQUARES_PER_SIDE;
                marksByAxisByPlayerForChecking[3]++;
            } else break;

        }
        return place;
    }

    public int checkForDiagDown4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * 11, playerMark)) marksByAxisByPlayerForChecking[0]++;
            if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * 11)) {
                place = l * GameBoard.SQUARES_PER_SIDE + j + k * 11;
                marksByAxisByPlayerForChecking[1]++;
            }
        }
        return place;
    }

    public int checkForDiagUp4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 50, playerMark)) marksByAxisByPlayerForChecking[2]++;
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 50, GameBoardMark.EMPTY)) {
                place = l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 50;
                marksByAxisByPlayerForChecking[3]++;
            }
        }
        return place;
    }

    public boolean anyDiagUp4MatchToMark(Mode type, int place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxisByPlayerForChecking[2] == 3 && marksByAxisByPlayerForChecking[3] == 1) {
            tempRowForChecks[3] = 1;
            if (type.equals(Mode.CHECK)) {
                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) tempTableForChecks[place] = OCCUPIED;
        }
        if (type.equals(Mode.CLEAN) && marksByAxisByPlayerForChecking[2] == 2 && marksByAxisByPlayerForChecking[3] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyDiagDown4MatchToMark(Mode type, int place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 1) {
            tempRowForChecks[2] = 1;
            if (type.equals(Mode.CHECK)) {
                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) tempTableForChecks[place] = OCCUPIED;
        }
        if (type.equals(Mode.CLEAN) && marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyVert4MatchToMark(Mode type, int place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxisByPlayerForChecking[2] == 3 && marksByAxisByPlayerForChecking[3] == 1) {
            tempRowForChecks[1] = 1;
            if (type.equals(Mode.CHECK)) {

                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) tempTableForChecks[place] = OCCUPIED;
        }
        if (type.equals(Mode.CLEAN) && marksByAxisByPlayerForChecking[2] == 2 && marksByAxisByPlayerForChecking[3] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyHoriz4MatchToMark(Mode type, int place) {
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 1) {
            tempRowForChecks[0] = 1;
            if (type.equals(Mode.CHECK)) {
                return true;
            }
            if (type.equals(Mode.SETFLAGS)) tempTableForChecks[place] = OCCUPIED;
        }

        if (type.equals(Mode.CLEAN) && marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) {
            return true;
        }
        return false;
    }

    public int check2o3c(GameBoardMark playerMark) {
        int k;

        for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
            if (!gameBoard.hasEmptyValueOnMainBoardAt(k)) continue;

            gameBoard.setValueAt(GameBoard.mainBoardIndex, k, playerMark);

            if (responseTo3Or4InaRowOpportunity(playerMark, GameBoard.mainBoardIndex, Mode.COUNT) > 1) {
                return k;
            }

            gameBoard.setValueAt(GameBoard.mainBoardIndex, k, GameBoardMark.EMPTY);
        }
        return NONE;
    }


    public void setFlagsForLaterProcessing(GameBoardMark playerMark) {
        int k;

        for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
            tempTableForChecks[k] = GameBoardMark.EMPTY.index;
        blockSeriesOfFourOrMore(playerMark, 0, Mode.SETFLAGS);
        responseTo3Or4InaRowOpportunity(playerMark, 0, Mode.SETFLAGS);
    }

    public void resetAllMarksAlongAxesForFirstHalfOfBoard() {
        for (int k = 0; k < 4; k++)
            marksByAxisByPlayerForChecking[k] = 0;
    }

    public void copyIntoStagingBoardFromOddBoardGroupAtDepth(int depth) {
        for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
            stagingBoard[k] = perhapsaTemporaryBoardHolder[depth][k];
    }

    public void copyStagingBoardIntoOddGroupOfBoardsAtDepth(int depth) {
        for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
            perhapsaTemporaryBoardHolder[depth][k] = stagingBoard[k];
    }

    private void copyBoardToCheck(int indexForBoardToCheck) {
        for (int j = 0; j < TOTAL_SQUARES_PER_BOARD; j++) {
            gameBoard.setValueAt(1, j, gameBoard.getValueAt(indexForBoardToCheck, j));
        }
    }

    private void clearMarksByAxisArray() {
        int k;
        for (k = 0; k < 4; k++)
            marksByAxisByPlayerForChecking[k] = GameBoardMark.EMPTY.index;
    }

    public void resetMainGameBoard(int boardLevel) {
        for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
            gameBoard.setValueAt(boardLevel, k, GameBoardMark.EMPTY);
    }

    public void respondToMouseUp(int playerMove, RawPlayerMove rawPlayerMove) {
        moveNumber.increment();
        gameBoard.markMove(playerMove, GameBoardMark.X_MARK_FOR_PLAYER.index);
        if (winner() == GameBoardMark.EMPTY) {
            lastMove = computerMove.makeComputerMove(rawPlayerMove).getRaw();
            gameBoard.markMove(lastMove, GameBoardMark.ZERO_MARK_FOR_COMPUTER.index);
            gameState = 0;
        }
        if (winner() == GameBoardMark.ZERO_MARK_FOR_COMPUTER) {
            gameState = 3;
        }

        if (winner() == GameBoardMark.X_MARK_FOR_PLAYER) {
            gameState = 2;
        }
    }

    public void run() {
        resetMainGameBoard(0);
        moveNumber.setToGameStart();
        gameState = 0;
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
                    changeMarksToFindDiagonalWin(fasterIndex, slowerIndex, p.winLength.length);
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
            incrementWinCountForDirection((fasterIndex * GameBoard.SQUARES_PER_SIDE + slowerIndex + k), Directions.HORIZONTAL);
            incrementWinCountForDirection((slowerIndex * GameBoard.SQUARES_PER_SIDE + fasterIndex + k * GameBoard.SQUARES_PER_SIDE), Directions.VERTICAL);
        }
    }

    private void changeMarksToFindDiagonalWin(int fasterIndex, int slowerIndex, int winSize) {
        for (int k = 0; k < winSize; k++) {
            incrementWinCountForDirection(indexOnBoardForDiagonalLeft(fasterIndex, slowerIndex, winSize, k), Directions.DIAGONAL_LEFT);
            incrementWinCountForDirection(indexOnBoardForDiagonalRight(fasterIndex, slowerIndex, k), Directions.DIAGONAL_RIGHT);
        }
    }

    private int indexOnBoardForDiagonalRight(int fasterIndex, int slowerIndex, int k) {
        return slowerIndex * GameBoard.SQUARES_PER_SIDE + fasterIndex + k * (GameBoard.SQUARES_PER_SIDE + 1);
    }

    private int indexOnBoardForDiagonalLeft(int fasterIndex, int slowerIndex, int winSize, int k) {
        return slowerIndex * GameBoard.SQUARES_PER_SIDE + fasterIndex - k * oneLessThanCountInRow + (winSize - 1) * GameBoard.SQUARES_PER_SIDE;
    }

    private void incrementWinCountForDirection(int indexOnBoard, Directions direction) {
        if (indexOnBoard < NONE && indexOnBoard >= 0) { // i.e. position.isValid()
            marksForChecking.incrementValueFor(direction, gameBoard.gameMarkAtMainBoardPosition(indexOnBoard));
        }
    }

}
