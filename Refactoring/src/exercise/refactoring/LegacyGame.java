package exercise.refactoring;

public class LegacyGame {

    public static final int TOTAL_SQUARES_PER_BOARD = 100;
    public static final int SQUARES_PER_SIDE = 10;

    public static int PLAYER_WIN_LENGTH = 5;
    public static int COMPUTER_WIN_LENGTH = 6;
    public static final int MARK_FOR_PLAYER_1 = 1;
    public static final int MARK_FOR_COMPUTER_2 = 2;
    public static final int empty0 = 0;
    public final GameBoard gameBoard = new GameBoard();
    final MarksForChecking marksForChecking = new MarksForChecking();
    private final ComputerMove computerMove = new ComputerMove(this);

    public int gameState = 0;
    public int moveNumber = -1;
    public int lastMove = NONE;

    private static final String DOUBLE_BLANK_SPACE = "  ";
    private static final String SINGLE_BLANK_SPACE = " ";
    private static final int MAX_DEPTH = 7;
    static final int NONE = 100;
    static final int OCCUPIED = 1;
    private static final int SAFE_MODE = 1;
    static final int CLEAN_MODE = 3;
    private static final int COUNT_MODE = 2;
    private static final int SETFLAGS_MODE = 1;
    static final int CHECK_MODE = 0;

    private int marksByAxisByPlayerForChecking[] = new int[8];
    private int perhapsaTemporaryBoardHolder[][] = new int[MAX_DEPTH][TOTAL_SQUARES_PER_BOARD];
    int[] stagingBoard = new int[TOTAL_SQUARES_PER_BOARD];
    private int tempTableForChecks[] = new int[TOTAL_SQUARES_PER_BOARD];
    private int tempRowForChecks[] = new int[SQUARES_PER_SIDE];

    public int findSpot() {
        int position;
        int i;
        i = GameBoardMark.EMPTY.index;
        do {
            position = (int) (Math.random() * TOTAL_SQUARES_PER_BOARD);
            if (gameBoard.mainBoard()[position] != GameBoardMark.EMPTY.index) continue;
            if ((position > 0 && gameBoard.mainBoard()[position - 1] != GameBoardMark.EMPTY.index) || (position > SQUARES_PER_SIDE && (gameBoard.mainBoard()[position - 11] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position - SQUARES_PER_SIDE] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position - 9] != GameBoardMark.EMPTY.index)) || (position < 99 && gameBoard.mainBoard()[position + 1] != GameBoardMark.EMPTY.index) || (position < 88 && (gameBoard.mainBoard()[position + 9] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position + SQUARES_PER_SIDE] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position + 11] != GameBoardMark.EMPTY.index))) i = OCCUPIED;
        } while (i == GameBoardMark.EMPTY.index);
        return position;
    }

    public int checkForWinOpportunity(int playerMark, int boardNumber) {
        if (isNone(blockSeriesOfFourOrMore(switchPlayers(playerMark), boardNumber, CHECK_MODE))) {
            return blockSeriesOfFourOrMore(switchPlayers(playerMark), boardNumber, CHECK_MODE);
        }

        if (isNone(responseTo3Or4InaRowOpportunity(switchPlayers(playerMark), boardNumber, CHECK_MODE))) {
            return responseTo3Or4InaRowOpportunity(switchPlayers(playerMark), boardNumber, CHECK_MODE);
        }

        return NONE;
    }

        private boolean isNone(int response) {
        return response != NONE;
    }

    public int checkToSeeIfEitherSideHasWon() {
        if (checkForComputerWin()) return MARK_FOR_COMPUTER_2;
        if (checkForPlayerWin()) return MARK_FOR_PLAYER_1;
        return (empty0);
    }

    public int checkSeries(int playerMark, int depth) {
        int[] auxilliaryBoard = new int[LegacyGame.TOTAL_SQUARES_PER_BOARD];
        int winningPosition;
        if (depth == MAX_DEPTH) return NONE;

        setc4c(playerMark);

        for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
            if (stagingBoard[k] == GameBoardMark.EMPTY.index || auxilliaryBoard[k] != GameBoardMark.EMPTY.index) continue;
            copyStagingBoardIntoOddGroupOfBoardsAtDepth(depth);

            auxilliaryBoard[k] = playerMark;

            winningPosition = checkForWinOpportunity(switchPlayers(playerMark), 2);
            if (winningPosition == NONE) return NONE;

            auxilliaryBoard[winningPosition] = switchPlayers(playerMark);
            if (blockSeriesOfFourOrMore(playerMark, 2, CHECK_MODE) != NONE) {
                return k;
            }

            if (blockSeriesOfFourOrMore(switchPlayers(playerMark), 2, CHECK_MODE) != NONE) {
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

    public int switchPlayers(int playerMark) {
        return 3 - playerMark;
    }

    public int checkBox(int playerMark) {
        for (int k = 1; k < 8; k++) {
            for (int l = 1; l < 8; l++) {
                int cnt = 0;
                int pos = -1;
                for (int a = 0; a < 2; a++) {
                    for (int b = 0; b < 2; b++) {
                        int x = k + a + 10 * (l + b);
                        int c = gameBoard.mainBoard()[x];
                        if (c == playerMark) cnt++;
                        else if (c == 0) pos = x;
                    }
                }
                if (cnt == 3 && pos != -1) return pos;
            }
        }
        return LegacyGame.NONE;
    }

    public int checkCross(int playerMark) {
        int k, l, x;

        for (k = 1; k < 7; k++) {
            for (l = 1; l < 7; l++) {
                x = k + 10 * l;
                if (gameBoard.mainBoard()[x] == playerMark && gameBoard.mainBoard()[x + 2] == playerMark && gameBoard.mainBoard()[x + 20] == playerMark && gameBoard.mainBoard()[x + 22] == playerMark && gameBoard.mainBoard()[x + 11] == 0) return (x + 11);
            }
        }
        return LegacyGame.NONE;
    }

    public int countNumberOfAxesAlongWhichSeriesOfFourOccur(int playerMark, int x, int type) {
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
                    marksByAxisByPlayerForChecking[gameBoard.getValueAt(x, j * 10 + l + k)]++;
                if (marksByAxisByPlayerForChecking[playerMark] == 4 && marksByAxisByPlayerForChecking[GameBoardMark.EMPTY.index] == 1) {
                    if (type == LegacyGame.SAFE_MODE) {
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
                    marksByAxisByPlayerForChecking[gameBoard.getValueAt(x, l * 10 + j + k * 10)]++;
                if (marksByAxisByPlayerForChecking[playerMark] == 4 && marksByAxisByPlayerForChecking[GameBoardMark.EMPTY.index] == 1) {
                    if (type == LegacyGame.SAFE_MODE) {
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
                    marksByAxisByPlayerForChecking[gameBoard.getValueAt(x, l * 10 + j + k * 11)]++;
                if (marksByAxisByPlayerForChecking[playerMark] == 4 && marksByAxisByPlayerForChecking[GameBoardMark.EMPTY.index] == 1) {
                    if (type == LegacyGame.SAFE_MODE) {
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
                    marksByAxisByPlayerForChecking[gameBoard.getValueAt(x, l * 10 + j - k * 9 + 40)]++;
                if (marksByAxisByPlayerForChecking[playerMark] == 4 && marksByAxisByPlayerForChecking[GameBoardMark.EMPTY.index] == 1) {
                    if (type == LegacyGame.SAFE_MODE) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j - k * 9 + 40) && tempTableForChecks[l * 10 + j - k * 9 + 40] == LegacyGame.OCCUPIED) {
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

    public int tryToMake3WithGap_FromVert4IntersectingWithHoriz4(int playerMark, int gameBoardLevelToCheck) {
        int k;

        for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
            gameBoard.setValueAt(1, k, gameBoard.getValueAt(gameBoardLevelToCheck, k));
        for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
            if (gameBoard.hasEmptyValueAt(1, k)) {
                gameBoard.setValueAt(1, k, playerMark);
                if (responseTo3Or4InaRowOpportunity(playerMark, 1, CHECK_MODE) != NONE && countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, SAFE_MODE) > 0) {
                    return k;
                }
                gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (NONE);
    }

    public void setc4c(int playerMark) {
        int j, k, l;
        int position, x = 2;

        for (j = 0; j < TOTAL_SQUARES_PER_BOARD; j++)
            stagingBoard[j] = GameBoardMark.EMPTY.index;

        for (j = 0; j < SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxisByPlayerForChecking[0] = 0;
                marksByAxisByPlayerForChecking[1] = 0;
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * SQUARES_PER_SIDE + k + l);
                    if (position == playerMark) marksByAxisByPlayerForChecking[0]++;
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * SQUARES_PER_SIDE + k + l;
                        marksByAxisByPlayerForChecking[1]++;
                    }
                }
                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                    stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }

        for (j = 0; j < SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxisByPlayerForChecking[0] = 0;
                marksByAxisByPlayerForChecking[1] = 0;
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, k * SQUARES_PER_SIDE + j + l * SQUARES_PER_SIDE);
                    if (position == playerMark) marksByAxisByPlayerForChecking[0]++;
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = k * SQUARES_PER_SIDE + j + l * SQUARES_PER_SIDE;
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
                    position = gameBoard.getValueAt(x, j * SQUARES_PER_SIDE + k + l * 11);
                    if (position == playerMark) marksByAxisByPlayerForChecking[0]++;
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * SQUARES_PER_SIDE + k + l * 11;
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
                    position = gameBoard.getValueAt(x, j * SQUARES_PER_SIDE + k - l * 9 + 40);
                    if (position == playerMark) marksByAxisByPlayerForChecking[0]++;
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * SQUARES_PER_SIDE + k - l * 9 + 40;
                        marksByAxisByPlayerForChecking[1]++;
                    }
                }
                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                    stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }
    }

    public void seto4cc(int playerMark) {
        int j, k, l;
        int position;
        int x = 2;

        for (j = 0; j < TOTAL_SQUARES_PER_BOARD; j++)
            stagingBoard[j] = GameBoardMark.EMPTY.index;

        for (j = 0; j < SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 5; k++) {
                position = j * SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAt(x, position) && gameBoard.hasEmptyValueAt(x, position + 5)) {
                    marksByAxisByPlayerForChecking[0] = 0;
                    marksByAxisByPlayerForChecking[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(x, j * SQUARES_PER_SIDE + k + l);
                        if (position == playerMark) marksByAxisByPlayerForChecking[0]++;
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * SQUARES_PER_SIDE + k + l;
                            marksByAxisByPlayerForChecking[1]++;
                        }
                    }
                    if (marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                        stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }

        for (j = 0; j < SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 5; k++) {
                position = k * SQUARES_PER_SIDE + j;
                if (gameBoard.hasEmptyValueAt(x, position) && gameBoard.hasEmptyValueAt(x, position + 50)) {
                    marksByAxisByPlayerForChecking[0] = 0;
                    marksByAxisByPlayerForChecking[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(x, k * SQUARES_PER_SIDE + j + l * SQUARES_PER_SIDE);
                        if (position == playerMark) marksByAxisByPlayerForChecking[0]++;
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = k * SQUARES_PER_SIDE + j + l * SQUARES_PER_SIDE;
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
                position = j * SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAt(x, position) && gameBoard.hasEmptyValueAt(x, position + 55)) {
                    marksByAxisByPlayerForChecking[0] = 0;
                    marksByAxisByPlayerForChecking[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(x, j * SQUARES_PER_SIDE + k + l * 11);
                        if (position == playerMark) marksByAxisByPlayerForChecking[0]++;
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * SQUARES_PER_SIDE + k + l * 11;
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
                position = j * SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAt(x, position + 50) && gameBoard.hasEmptyValueAt(x, position + 5)) {
                    marksByAxisByPlayerForChecking[0] = 0;
                    marksByAxisByPlayerForChecking[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(x, j * SQUARES_PER_SIDE + k - l * 9 + 50);
                        if (position == playerMark) marksByAxisByPlayerForChecking[0]++;
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j * SQUARES_PER_SIDE + k - l * 9 + 50;
                            marksByAxisByPlayerForChecking[1]++;
                        }
                    }
                    if (marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) for (l = 0; l < 2; l++)
                        stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }
    }

    public int blockSeriesOfFourOrMore(int playerMark, int x, int type) {
        int j, k, l;
        int position = 0, position2 = 0;

        for (l = 0; l < 6; l++) {
            for (j = 0; j < SQUARES_PER_SIDE; j++) {
                resetAllMarksAlongAxesForFirstHalfOfBoard();

                position = checkFor5AlongHorizAxis(playerMark, x, j, l, position);

                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) {
                    if (type == SETFLAGS_MODE) {
                        tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type == CLEAN_MODE) return tempRowForChecks[0];
                }

                if (marksByAxisByPlayerForChecking[0] == 4 && marksByAxisByPlayerForChecking[1] == 1 && type == CHECK_MODE) return position;

                position = checkFor5AlongVertAxis(playerMark, x, j, l, position);

                if (marksByAxisByPlayerForChecking[2] == 3 && marksByAxisByPlayerForChecking[3] == 2) {
                    if (type == SETFLAGS_MODE) {
                        tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type == CLEAN_MODE) return tempRowForChecks[0];
                }
                if (marksByAxisByPlayerForChecking[2] == 4 && marksByAxisByPlayerForChecking[3] == 1 && type == CHECK_MODE) return position;
            }

            for (j = 0; j < 6; j++) {
                resetAllMarksAlongAxesForFirstHalfOfBoard();

                for (k = 0; k < 5; k++) {
                    position = checkFor5AlongDiagDownRightAxis(playerMark, x, j, k, l, position);
                    position2 = checkFor5AlongDiagUpRightAxis(playerMark, x, j, k, l, position2);
                }

                if (marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 2) {
                    if (type == SETFLAGS_MODE) {
                        tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type == CLEAN_MODE) return tempRowForChecks[0];
                }
                if (marksByAxisByPlayerForChecking[0] == 4 && marksByAxisByPlayerForChecking[1] == 1 && type == CHECK_MODE) return position;

                if (marksByAxisByPlayerForChecking[2] == 3 && marksByAxisByPlayerForChecking[3] == 2) {
                    if (type == SETFLAGS_MODE) {
                        tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type == CLEAN_MODE) return tempRowForChecks[0];
                }
                if (marksByAxisByPlayerForChecking[2] == 4 && marksByAxisByPlayerForChecking[3] == 1 && type == CHECK_MODE) return position2;
            }
        }
        return (NONE);
    }

    public int checkFor5AlongDiagUpRightAxis(int playerMark, int x, int j, int k, int l, int position2) {
        if (gameBoard.valueAtPositionMatches(x, l * SQUARES_PER_SIDE + j - k * 9 + 40, playerMark)) marksByAxisByPlayerForChecking[2]++;
        if (gameBoard.hasEmptyValueAt(x, l * SQUARES_PER_SIDE + j - k * 9 + 40)) {
            position2 = l * SQUARES_PER_SIDE + j - k * 9 + 40;
            tempRowForChecks[marksByAxisByPlayerForChecking[3]] = position2;
            marksByAxisByPlayerForChecking[3]++;
        }
        return position2;
    }

    public int checkFor5AlongDiagDownRightAxis(int playerMark, int x, int j, int k, int l, int position) {
        if (gameBoard.valueAtPositionMatches(x, l * SQUARES_PER_SIDE + j + k * 11, playerMark)) marksByAxisByPlayerForChecking[0]++;
        if (gameBoard.hasEmptyValueAt(x, l * SQUARES_PER_SIDE + j + k * 11)) {
            position = l * SQUARES_PER_SIDE + j + k * 11;
            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = position;
            marksByAxisByPlayerForChecking[1]++;
        }
        return position;
    }

    public int checkFor5AlongVertAxis(int playerMark, int x, int j, int l, int position) {
        int k;
        for (k = 0; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(x, l * SQUARES_PER_SIDE + j + k * SQUARES_PER_SIDE, playerMark)) marksByAxisByPlayerForChecking[2]++;
            else if (gameBoard.hasEmptyValueAt(x, l * SQUARES_PER_SIDE + j + k * SQUARES_PER_SIDE)) {
                position = SQUARES_PER_SIDE * l + j + k * SQUARES_PER_SIDE;
                tempRowForChecks[marksByAxisByPlayerForChecking[3]] = position;
                marksByAxisByPlayerForChecking[3]++;
            } else break;
        }
        return position;
    }

    public int checkFor5AlongHorizAxis(int playerMark, int x, int j, int l, int position) {
        int k;
        for (k = 0; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(x, j * SQUARES_PER_SIDE + l + k, playerMark)) marksByAxisByPlayerForChecking[0]++;

            else if (gameBoard.hasEmptyValueAt(x, j * SQUARES_PER_SIDE + l + k)) {
                position = SQUARES_PER_SIDE * j + l + k;
                tempRowForChecks[marksByAxisByPlayerForChecking[1]] = position;
                marksByAxisByPlayerForChecking[1]++;

            } else break;
        }
        return position;
    }

    public int tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(int playerMark, int indexForBoardToCheck) {
        copyBoardToCheck(indexForBoardToCheck);

        for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
            if (gameBoard.hasEmptyValueAt(1, k)) {
                gameBoard.setValueAt(1, k, playerMark);

                if (countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, CLEAN_MODE) > 1) return k;

                gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (NONE);
    }

    public int responseTo3Or4InaRowOpportunity(int playerMark, int boardLevel, int type) {
        int j, k, l;
        int place = 0;

        for (k = 0; k < 4; k++)
            tempRowForChecks[k] = 0;

        for (l = 0; l < 5; l++) {
            for (j = 0; j < SQUARES_PER_SIDE; j++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueAt(boardLevel, j * SQUARES_PER_SIDE + l) && gameBoard.hasEmptyValueAt(boardLevel, j * SQUARES_PER_SIDE + l + 5)) {

                    place = checkForHoriz4InRow(playerMark, boardLevel, j, l);
                    if (anyHoriz4MatchToMark(type, place)) return place;
                }

                if (gameBoard.hasEmptyValueAt(boardLevel, l * SQUARES_PER_SIDE + j) && gameBoard.hasEmptyValueAt(boardLevel, l * SQUARES_PER_SIDE + j + 50)) {

                    place = checkForVert4InRow(playerMark, boardLevel, j, l);
                    if (anyVert4MatchToMark(type, place)) return place;
                }
            }

            for (j = 0; j < 5; j++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueAt(boardLevel, l * SQUARES_PER_SIDE + j) && gameBoard.hasEmptyValueAt(boardLevel, l * SQUARES_PER_SIDE + j + 55)) {

                    place = checkForDiagDown4InRow(playerMark, boardLevel, j, l);
                    if (anyDiagDown4MatchToMark(type, place)) return place;
                }

                if (gameBoard.hasEmptyValueAt(boardLevel, l * SQUARES_PER_SIDE + j + 50) && gameBoard.hasEmptyValueAt(boardLevel, l * SQUARES_PER_SIDE + j + 5)) {
                    place = checkForDiagUp4InRow(playerMark, boardLevel, j, l);
                    if (anyDiagUp4MatchToMark(type, place)) return place;
                }
            }
        }

        if (type == COUNT_MODE) {
            return tempRowForChecks[0] + tempRowForChecks[1] + tempRowForChecks[2] + tempRowForChecks[3];
        }

        return (NONE);
    }

    public int checkForHoriz4InRow(int playerMark, int boardLevel, int j, int l) { /* horiz */
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, j * SQUARES_PER_SIDE + l + k, playerMark)) marksByAxisByPlayerForChecking[0]++;
            else if (gameBoard.hasEmptyValueAt(boardLevel, j * SQUARES_PER_SIDE + l + k)) {
                place = SQUARES_PER_SIDE * j + l + k;
                marksByAxisByPlayerForChecking[1]++;
            } else break;
        }
        return place;
    }

    public int checkForVert4InRow(int playerMark, int boardLevel, int j, int l) {
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * SQUARES_PER_SIDE + j + k * SQUARES_PER_SIDE, playerMark)) marksByAxisByPlayerForChecking[2]++;
            else if (gameBoard.hasEmptyValueAt(boardLevel, l * SQUARES_PER_SIDE + j + k * SQUARES_PER_SIDE)) {
                place = SQUARES_PER_SIDE * l + j + k * SQUARES_PER_SIDE;
                marksByAxisByPlayerForChecking[3]++;
            } else break;

        }
        return place;
    }

    public int checkForDiagDown4InRow(int playerMark, int boardLevel, int j, int l) {
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * SQUARES_PER_SIDE + j + k * 11, playerMark)) marksByAxisByPlayerForChecking[0]++;
            if (gameBoard.hasEmptyValueAt(boardLevel, l * SQUARES_PER_SIDE + j + k * 11)) {
                place = l * SQUARES_PER_SIDE + j + k * 11;
                marksByAxisByPlayerForChecking[1]++;
            }
        }
        return place;
    }

    public int checkForDiagUp4InRow(int playerMark, int boardLevel, int j, int l) {
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * SQUARES_PER_SIDE + j - k * 9 + 50, playerMark)) marksByAxisByPlayerForChecking[2]++;
            if (gameBoard.valueAtPositionMatches(boardLevel, l * SQUARES_PER_SIDE + j - k * 9 + 50, GameBoardMark.EMPTY.index)) {
                place = l * SQUARES_PER_SIDE + j - k * 9 + 50;
                marksByAxisByPlayerForChecking[3]++;
            }
        }
        return place;
    }

    public boolean anyDiagUp4MatchToMark(int type, int place) {
        boolean match = false;
        if (type != CLEAN_MODE && marksByAxisByPlayerForChecking[2] == 3 && marksByAxisByPlayerForChecking[3] == 1) {
            tempRowForChecks[3] = 1;
            if (type == CHECK_MODE) {
                match = true;
            }
            if (type == SETFLAGS_MODE) tempTableForChecks[place] = OCCUPIED;
        }
        if (type == CLEAN_MODE && marksByAxisByPlayerForChecking[2] == 2 && marksByAxisByPlayerForChecking[3] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyDiagDown4MatchToMark(int type, int place) {
        boolean match = false;

        if (type != CLEAN_MODE && marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 1) {
            tempRowForChecks[2] = 1;
            if (type == CHECK_MODE) {
                match = true;
            }
            if (type == SETFLAGS_MODE) tempTableForChecks[place] = OCCUPIED;
        }
        if (type == CLEAN_MODE && marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyVert4MatchToMark(int type, int place) {
        boolean match = false;
        if (type != CLEAN_MODE && marksByAxisByPlayerForChecking[2] == 3 && marksByAxisByPlayerForChecking[3] == 1) {
            tempRowForChecks[1] = 1;
            if (type == CHECK_MODE) {

                match = true;
            }
            if (type == SETFLAGS_MODE) tempTableForChecks[place] = OCCUPIED;
        }
        if (type == CLEAN_MODE && marksByAxisByPlayerForChecking[2] == 2 && marksByAxisByPlayerForChecking[3] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyHoriz4MatchToMark(int type, int place) {
        if (type != CLEAN_MODE && marksByAxisByPlayerForChecking[0] == 3 && marksByAxisByPlayerForChecking[1] == 1) {
            tempRowForChecks[0] = 1;
            if (type == CHECK_MODE) {
                return true;
            }
            if (type == SETFLAGS_MODE) tempTableForChecks[place] = OCCUPIED;
        }

        if (type == CLEAN_MODE && marksByAxisByPlayerForChecking[0] == 2 && marksByAxisByPlayerForChecking[1] == 2) {
            return true;
        }
        return false;
    }

    public int check2o3c(int playerMark, int x) {
        int k;

        for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
            if (!gameBoard.hasEmptyValueAt(x, k)) continue;

            gameBoard.setValueAt(x, k, playerMark);

            if (responseTo3Or4InaRowOpportunity(playerMark, x, COUNT_MODE) > 1) {
                return k;
            }

            gameBoard.setValueAt(x, k, GameBoardMark.EMPTY.index);
        }
        return NONE;
    }

    public int closeGapInSeries() {
        int k, l, x, y;

        for (k = 1; k < 7; k++) {
            for (l = 1; l < 9; l++) {
                x = k + SQUARES_PER_SIDE * l;
                y = l + k * SQUARES_PER_SIDE;

                if (gameBoard.mainBoard()[x] == GameBoardMark.X_MARK_FOR_PLAYER.index && gameBoard.mainBoard()[x + 2] == GameBoardMark.X_MARK_FOR_PLAYER.index && gameBoard.mainBoard()[x + 1] == 0) return (x + 1);

                if (gameBoard.mainBoard()[y] == GameBoardMark.X_MARK_FOR_PLAYER.index && gameBoard.mainBoard()[y + 20] == GameBoardMark.X_MARK_FOR_PLAYER.index && gameBoard.mainBoard()[y + SQUARES_PER_SIDE] == 0) return (y + SQUARES_PER_SIDE);
            }
        }
        return NONE;
    }

    public void setFlagsForLaterProcessing(int playerMark) {
        int k;

        for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
            tempTableForChecks[k] = GameBoardMark.EMPTY.index;
        blockSeriesOfFourOrMore(playerMark, 0, SETFLAGS_MODE);
        responseTo3Or4InaRowOpportunity(playerMark, 0, SETFLAGS_MODE);
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
            gameBoard.setValueAt(boardLevel, k, 0);
    }

    public void respondToMouseUp(int playerMove, int x, int y) {
        moveNumber++;
        gameBoard.markMove(playerMove, GameBoardMark.X_MARK_FOR_PLAYER.index);
        if (checkToSeeIfEitherSideHasWon() == GameBoardMark.EMPTY.index) {
            lastMove = computerMove.makeComputerMove(x, y, true);
            gameBoard.markMove(lastMove, GameBoardMark.ZERO_MARK_FOR_COMPUTER.index);
            gameState = 0;
        }
        if (checkToSeeIfEitherSideHasWon() == GameBoardMark.ZERO_MARK_FOR_COMPUTER.index) {
            gameState = 3;
        }

        if (checkToSeeIfEitherSideHasWon() == GameBoardMark.X_MARK_FOR_PLAYER.index) {
            gameState = 2;
        }
    }

    public void run() {
        resetMainGameBoard(0);
        moveNumber = 0;
        gameState = 0;
    }

    public boolean checkForPlayerWin() {
        return checkForWin(new Player(PLAYER_WIN_LENGTH, MARK_FOR_PLAYER_1));
    }

    public boolean checkForComputerWin() {
        return checkForWin(new Player(COMPUTER_WIN_LENGTH, MARK_FOR_COMPUTER_2));
    }

    public boolean checkForWin(Player p) {
        int fasterIndex;
        int slowerIndex;
        for (slowerIndex = 0; slowerIndex < (SQUARES_PER_SIDE - p.winLength + 1); slowerIndex++) {

            for (fasterIndex = 0; fasterIndex < SQUARES_PER_SIDE; fasterIndex++) {
                marksForChecking.resetAllValuesToZero();
                changeMarksToFindHorizontalOrVerticalWin(fasterIndex, slowerIndex, p.winLength);
                if (playerWon(p)) {
                    return true;
                }

                if (fasterIndex < (SQUARES_PER_SIDE - p.winLength + 1)) {
                    marksForChecking.resetAllValuesToZero();
                    changeMarksToFindDiagonalWin(fasterIndex, slowerIndex, p.winLength);
                    if (playerWon(p)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void changeMarksToFindHorizontalOrVerticalWin(int fasterIndex, int slowerIndex, int winSize) {
        for (int k = 0; k < winSize; k++) {
            incrementWinCountForDirection((fasterIndex * SQUARES_PER_SIDE + slowerIndex + k), Directions.HORIZONTAL);
            incrementWinCountForDirection((slowerIndex * SQUARES_PER_SIDE + fasterIndex + k * SQUARES_PER_SIDE), Directions.VERTICAL);
        }
    }

    private void changeMarksToFindDiagonalWin(int fasterIndex, int slowerIndex, int winSize) {
        for (int k = 0; k < winSize; k++) {
            incrementWinCountForDirection((slowerIndex * SQUARES_PER_SIDE + fasterIndex - k * 9 + (winSize - 1) * SQUARES_PER_SIDE), Directions.DIAGONAL_LEFT);
            incrementWinCountForDirection((slowerIndex * SQUARES_PER_SIDE + fasterIndex + k * (SQUARES_PER_SIDE + 1)), Directions.DIAGONAL_RIGHT);
        }
    }

    private void incrementWinCountForDirection(int indexOnBoard, Directions direction) {
        if (indexOnBoard < 100 && indexOnBoard >= 0) {
            marksForChecking.incrementValueAt(direction, playerMark(indexOnBoard));
        }
    }

    private GameBoardMark playerMark(int indexOnBoard) {
        return GameBoardMark.valueOf(gameBoard.mainBoard()[indexOnBoard]);
    }

    private boolean playerWon(Player p) {
        for (Directions direction : Directions.values()) {
            if (countLargerThanWinLength(marksForChecking, p, direction)) {
                return true;
            }
        }
        return false;
    }

    protected boolean countLargerThanWinLength(MarksForChecking marksForChecking, Player p, Directions passedInDirection) {
        return marksForChecking.isLargerThan(p.winLength, passedInDirection, GameBoardMark.valueOf(p.playerMark));
    }

    public int getMoveNumber() {
        return moveNumber;
    }
}
