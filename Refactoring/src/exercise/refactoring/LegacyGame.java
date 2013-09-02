package exercise.refactoring;

public class LegacyGame {
    public static final int oneLessThanCountInRow = GameBoard.SQUARES_PER_SIDE - 1;
    public final GameBoard gameBoard = new GameBoard();
    final MarksForChecking marksForChecking = new MarksForChecking();
    final MarksByAxis marksByAxis = new MarksByAxis();
    public int gameState = 0;
    public MoveSequence moveNumber = new MoveSequence();
    public int lastMove = NONE;


    static final int NONE = 100;
    static final int OCCUPIED = 1;


    public GamePosition checkForWinOpportunity(GameBoardMark playerMark, int boardNumber) {
        GamePosition winner = blockSeriesOfFourOrMore(switchPlayers(playerMark), boardNumber, Mode.CHECK);
        if (winner.isNotNone()) {
            return winner;
        }

        GamePosition winnerCheck2 = responseTo3Or4InaRowOpportunity(switchPlayers(playerMark), boardNumber, Mode.CHECK);
        if (winnerCheck2.isNotNone()) {
            return winnerCheck2;
        }

        return GamePosition.nonePosition();
    }

    private boolean isNotNone(int response) {
        return response != NONE;
    }

    public GameBoardMark winner() {
        if (checkForComputerWin()) return GameBoardMark.ZERO_MARK_FOR_COMPUTER;
        if (checkForPlayerWin()) return GameBoardMark.X_MARK_FOR_PLAYER;
        return GameBoardMark.EMPTY;
    }

    public GamePosition checkSeries(GameBoardMark playerMark, int depth) {
        int[] auxilliaryBoard = new int[GameBoard.TOTAL_SQUARES_PER_BOARD];
        GamePosition winningPosition;
        if (depth == GameBoard.MAX_DEPTH_FOR_TEMP_BOARD) return GamePosition.nonePosition();

        setc4c(playerMark);

        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            if (GameBoard.stagingBoard[k] == GameBoardMark.EMPTY.index || auxilliaryBoard[k] != GameBoardMark.EMPTY.index) continue;
            copyStagingBoardIntoOddGroupOfBoardsAtDepth(depth);

            auxilliaryBoard[k] = playerMark.index;

            winningPosition = checkForWinOpportunity(switchPlayers(playerMark), 2);
            if (winningPosition.isNone()) return GamePosition.nonePosition();

            auxilliaryBoard[winningPosition.getRaw()] = switchPlayers(playerMark).index;
            if (blockSeriesOfFourOrMore(playerMark, 2, Mode.CHECK).isNotNone()) {
                return new GamePosition(k);
            }

            if (blockSeriesOfFourOrMore(switchPlayers(playerMark), 2, Mode.CHECK).isNotNone()) {
                auxilliaryBoard[k] = GameBoardMark.EMPTY.index;
                auxilliaryBoard[winningPosition.getRaw()] = GameBoardMark.EMPTY.index;
                copyIntoStagingBoardFromOddBoardGroupAtDepth(depth);
                continue;
            }

            if (checkSeries(playerMark, depth + 1) != GamePosition.nonePosition()) {
                return new GamePosition(k);
            }

            auxilliaryBoard[k] = GameBoardMark.EMPTY.index;
            auxilliaryBoard[winningPosition.getRaw()] = GameBoardMark.EMPTY.index;
            copyIntoStagingBoardFromOddBoardGroupAtDepth(depth);
        }
        return GamePosition.nonePosition();
    }

    public GameBoardMark switchPlayers(GameBoardMark playerMark) {
        return GameBoardMark.valueOf(3 - playerMark.index);
    }

    public int countNumberOfAxesAlongWhichSeriesOfFourOccur(GameBoardMark playerMark, int x, int type) {
        int j, k, l;
        int zbir = 0;
        int flag, flag2;

        flag = GameBoardMark.EMPTY.index;
        for (j = 0; j < 10; j++) {
            for (l = 0; l < 6; l++) {
                marksByAxis.setPositionsToZero(0, 1, 2);
                for (k = 0; k < 5; k++){
                    marksByAxis.incrementValueAtPosition(gameBoard.getValueAt(x, j * 10 + l + k).index);
                }
                if (marksByAxis.marks[playerMark.index] == 4 && marksByAxis.marks[GameBoardMark.EMPTY.index] == 1) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, j * 10 + l + k) && GameBoard.tempTableForChecks[j * 10 + l + k] == LegacyGame.OCCUPIED) {
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
                marksByAxis.setPositionsToZero(0, 1, 2);
                for (k = 0; k < 5; k++)
                    marksByAxis.incrementValueAtPosition(gameBoard.getValueAt(x, l * 10 + j + k * 10));
                if (marksByAxis.marks[playerMark.index] == 4 && marksByAxis.marks[GameBoardMark.EMPTY.index] == 1) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++)
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j + k * 10) && GameBoard.tempTableForChecks[l * 10 + j + k * 10] == LegacyGame.OCCUPIED) flag2 = LegacyGame.OCCUPIED;
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
                    marksByAxis.setPositionsToZero(k);
                for (k = 0; k < 5; k++)
                    /* diag\ */
                    marksByAxis.incrementValueAtPosition(gameBoard.getValueAt(x, l * 10 + j + k * 11));
                if (marksByAxis.marks[playerMark.index] == 4 && marksByAxis.marks[GameBoardMark.EMPTY.index] == 1) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j + k * 11) && GameBoard.tempTableForChecks[l * 10 + j + k * 11] == LegacyGame.OCCUPIED) {
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
                for (k = 0; k < 3; k++){
                    marksByAxis.setPositionsToZero(k);
                }
                for (k = 0; k < 5; k++){
                    marksByAxis.incrementValueAtPosition(gameBoard.getValueAt(x, l * 10 + j - k * oneLessThanCountInRow + 40));
                }
                if (marksByAxis.marks[playerMark.index] == 4 && marksByAxis.marks[GameBoardMark.EMPTY.index] == 1) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY.index;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j - k * oneLessThanCountInRow + 40) && GameBoard.tempTableForChecks[l * 10 + j - k * oneLessThanCountInRow + 40] == LegacyGame.OCCUPIED) {
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

    public void setc4c(GameBoardMark playerMark) {
        int j, k, l;
        int position, x = 2; // TODO make this Position object

        for (j = 0; j < GameBoard.TOTAL_SQUARES_PER_BOARD; j++) {
            GameBoard.stagingBoard[j] = GameBoardMark.EMPTY.index;
        }
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxis.setPositionsToZero(0, 1);
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * GameBoard.SQUARES_PER_SIDE + k + l).index;
                    if (position == playerMark.index) marksByAxis.incrementValueAtPosition(GameBoardMark.ZERO_MARK_FOR_COMPUTER);
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxis.getValueAtPosition(1)] = j * GameBoard.SQUARES_PER_SIDE + k + l;
                        marksByAxis.incrementValueAtPosition(1);
                    }
                }
                if (marksByAxis.marks[0] == 3 && marksByAxis.marks[1] == 2) for (l = 0; l < 2; l++)
                    GameBoard.stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxis.marks[0] = 0;
                marksByAxis.marks[1] = 0;
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE).index;
                    if (position == playerMark.index) marksByAxis.incrementValueAtPosition(0);
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxis.marks[1]] = k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE;
                        marksByAxis.marks[1]++;
                    }
                }
                if (marksByAxis.marks[0] == 3 && marksByAxis.marks[1] == 2) for (l = 0; l < 2; l++)
                    GameBoard.stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }

        for (j = 0; j < 6; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxis.marks[0] = 0;
                marksByAxis.marks[1] = 0;
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * GameBoard.SQUARES_PER_SIDE + k + l * 11).index;
                    if (position == playerMark.index) marksByAxis.incrementValueAtPosition(0);
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxis.marks[1]] = j * GameBoard.SQUARES_PER_SIDE + k + l * 11;
                        marksByAxis.marks[1]++;
                    }
                }
                if (marksByAxis.marks[0] == 3 && marksByAxis.marks[1] == 2) for (l = 0; l < 2; l++)
                    GameBoard.stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }

        for (j = 0; j < 6; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxis.setPositionsToZero(0, 1);
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * GameBoard.SQUARES_PER_SIDE + k - l * oneLessThanCountInRow + 40).index;
                    if (position == playerMark.index) marksByAxis.incrementValueAtPosition(0);
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxis.getValueAtPosition(1)] = j * GameBoard.SQUARES_PER_SIDE + k - l * oneLessThanCountInRow + 40;
                        marksByAxis.incrementValueAtPosition(1);
                    }
                }
                if (marksByAxis.marks[0] == 3 && marksByAxis.marks[1] == 2) for (l = 0; l < 2; l++)
                    GameBoard.stagingBoard[tempRowForChecks[l]] = OCCUPIED;
            }
        }
    }

    public void seto4cc(GameBoardMark playerMark) {
        int j, k, l;
        int position;

        for (j = 0; j < GameBoard.TOTAL_SQUARES_PER_BOARD; j++) {
            GameBoard.stagingBoard[j] = GameBoardMark.EMPTY.index;
        }
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 5; k++) {
                position = j * GameBoard.SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAtPositionOnBoardTwoAndPositionWithDiff(position, 5)) {
                    marksByAxis.setPositionsToZero(0, 1);
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.boardTwoIndex, j * GameBoard.SQUARES_PER_SIDE + k + l).index;
                        if (position == playerMark.index) marksByAxis.incrementValueAtPosition(0);
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxis.getValueAtPosition(1)] = j * GameBoard.SQUARES_PER_SIDE + k + l;
                            marksByAxis.incrementValueAtPosition(1);
                        }
                    }
                    if (marksByAxis.marks[0] == 2 && marksByAxis.marks[1] == 2) for (l = 0; l < 2; l++)
                        GameBoard.stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 5; k++) {
                position = k * GameBoard.SQUARES_PER_SIDE + j;
                if (gameBoard.hasEmptyValueAtPositionOnBoardTwoAndPositionWithDiff(position, 50)) {
                    marksByAxis.setPositionsToZero(0, 1);
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.boardTwoIndex, k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE).index;
                        if (position == playerMark.index) marksByAxis.incrementValueAtPosition(0);
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxis.marks[1]] = k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE;
                            marksByAxis.marks[1]++;
                        }
                    }
                    if (marksByAxis.marks[0] == 2 && marksByAxis.marks[1] == 2) for (l = 0; l < 2; l++)
                        GameBoard.stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }

        for (j = 0; j < 5; j++) {
            for (k = 0; k < 5; k++) {
                position = j * GameBoard.SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAt(GameBoard.boardTwoIndex, position) && gameBoard.hasEmptyValueAt(GameBoard.boardTwoIndex, position + 55)) {
                    marksByAxis.marks[0] = 0;
                    marksByAxis.marks[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.boardTwoIndex, j * GameBoard.SQUARES_PER_SIDE + k + l * 11).index;
                        if (position == playerMark.index) marksByAxis.incrementValueAtPosition(0);
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxis.marks[1]] = j * GameBoard.SQUARES_PER_SIDE + k + l * 11;
                            marksByAxis.marks[1]++;
                        }
                    }
                    if (marksByAxis.marks[0] == 2 && marksByAxis.marks[1] == 2) for (l = 0; l < 2; l++)
                        GameBoard.stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }

        for (j = 0; j < 5; j++) {
            for (k = 0; k < 5; k++) {
                position = j * GameBoard.SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAt(GameBoard.boardTwoIndex, position + 50) && gameBoard.hasEmptyValueAt(GameBoard.boardTwoIndex, position + 5)) {
                    marksByAxis.marks[0] = 0;
                    marksByAxis.marks[1] = 0;
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.boardTwoIndex, j * GameBoard.SQUARES_PER_SIDE + k - l * oneLessThanCountInRow + 50).index;
                        if (position == playerMark.index) marksByAxis.incrementValueAtPosition(0);
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxis.marks[1]] = j * GameBoard.SQUARES_PER_SIDE + k - l * oneLessThanCountInRow + 50;
                            marksByAxis.marks[1]++;
                        }
                    }
                    if (marksByAxis.marks[0] == 2 && marksByAxis.marks[1] == 2) for (l = 0; l < 2; l++)
                        GameBoard.stagingBoard[tempRowForChecks[l]] = OCCUPIED;
                }
            }
        }
    }

    public GamePosition blockSeriesOfFourOrMore(GameBoardMark playerMark, int x, Mode type) {
        int j, k, l;
        GamePosition position = new GamePosition();
        GamePosition position2 = new GamePosition();

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (l = 0; l < 6; l++) {
            for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
                gameBoard.resetAllMarksAlongAxesForFirstHalfOfBoard(this);

                position = checkFor5AlongHorizAxis(playerMark, x, j, l, position);

                if (marksByAxis.marks[0] == 3 && marksByAxis.marks[1] == 2) {
                    if (type.equals(Mode.SETFLAGS)) {
                        GameBoard.tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        GameBoard.tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type.equals(Mode.CLEAN)) return new GamePosition(tempRowForChecks[0]);
                }

                if (marksByAxis.marks[0] == 4 && marksByAxis.marks[1] == 1 && type.equals(Mode.CHECK)) return position;

                position = checkFor5AlongVertAxis(playerMark, x, j, l, position);

                if (marksByAxis.marks[2] == 3 && marksByAxis.marks[3] == 2) {
                    if (type.equals(Mode.SETFLAGS)) {
                        GameBoard.tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        GameBoard.tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type.equals(Mode.CLEAN)) return new GamePosition(tempRowForChecks[0]);
                }
                if (marksByAxis.marks[2] == 4 && marksByAxis.marks[3] == 1 && type.equals(Mode.CHECK)) return position;
            }

            for (j = 0; j < 6; j++) {
                gameBoard.resetAllMarksAlongAxesForFirstHalfOfBoard(this);

                for (k = 0; k < 5; k++) {
                    position = checkFor5AlongDiagDownRightAxis(playerMark, x, j, k, l, position);
                    position2 = checkFor5AlongDiagUpRightAxis(playerMark, x, j, k, l, position2);
                }

                if (marksByAxis.marks[0] == 3 && marksByAxis.marks[1] == 2) {
                    if (type.equals(Mode.SETFLAGS)) {
                        GameBoard.tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        GameBoard.tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type.equals(Mode.CLEAN)) return new GamePosition(tempRowForChecks[0]);
                }
                if (marksByAxis.marks[0] == 4 && marksByAxis.marks[1] == 1 && type.equals(Mode.CHECK)) return position;

                if (marksByAxis.marks[2] == 3 && marksByAxis.marks[3] == 2) {
                    if (type.equals(Mode.SETFLAGS)) {
                        GameBoard.tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                        GameBoard.tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                    }
                    if (type.equals(Mode.CLEAN)) return new GamePosition(tempRowForChecks[0]);
                }
                if (marksByAxis.marks[2] == 4 && marksByAxis.marks[3] == 1 && type.equals(Mode.CHECK)) return position2;
            }
        }
        return GamePosition.nonePosition();
    }

    public GamePosition checkFor5AlongDiagUpRightAxis(GameBoardMark playerMark, int x, int j, int k, int l, GamePosition position2) {
        if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 40, playerMark)) marksByAxis.marks[2]++;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 40)) {
            position2 = new GamePosition(l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 40);
            tempRowForChecks[marksByAxis.marks[3]] = position2.getRaw();
            marksByAxis.marks[3]++;
        }
        return position2;
    }

    public GamePosition checkFor5AlongDiagDownRightAxis(GameBoardMark playerMark, int x, int j, int k, int l, GamePosition position) {
        if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j + k * 11, playerMark)) marksByAxis.incrementValueAtPosition(0);

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j + k * 11)) {
            position = new GamePosition(l * GameBoard.SQUARES_PER_SIDE + j + k * 11);
            tempRowForChecks[marksByAxis.marks[1]] = position.getRaw();
            marksByAxis.marks[1]++;
        }
        return position;
    }

    public GamePosition checkFor5AlongVertAxis(GameBoardMark playerMark, int x, int j, int l, GamePosition position) {
        int k;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE, playerMark)) marksByAxis.marks[2]++;
            else if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE)) {
                position = new GamePosition(GameBoard.SQUARES_PER_SIDE * l + j + k * GameBoard.SQUARES_PER_SIDE);
                tempRowForChecks[marksByAxis.marks[3]] = position.getRaw();
                marksByAxis.marks[3]++;
            } else break;
        }
        return position;
    }

    public GamePosition checkFor5AlongHorizAxis(GameBoardMark playerMark, int x, int j, int l, GamePosition position) {
        int k;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(x, j * GameBoard.SQUARES_PER_SIDE + l + k, playerMark)) marksByAxis.incrementValueAtPosition(0);

            else if (gameBoard.hasEmptyValueAt(x, j * GameBoard.SQUARES_PER_SIDE + l + k)) {
                position = new GamePosition(GameBoard.SQUARES_PER_SIDE * j + l + k);
                tempRowForChecks[marksByAxis.marks[1]] = position.getRaw();
                marksByAxis.marks[1]++;

            } else break;
        }
        return position;
    }

    public GamePosition responseTo3Or4InaRowOpportunity(GameBoardMark playerMark, int boardLevel, Mode type) {
        int j, k, l;
        GamePosition place = new GamePosition();

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
            return new GamePosition(tempRowForChecks[0] + tempRowForChecks[1] + tempRowForChecks[2] + tempRowForChecks[3]);
        }

        return GamePosition.nonePosition();
    }

    public GamePosition checkForHoriz4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) { /* horiz */
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, j * GameBoard.SQUARES_PER_SIDE + l + k, playerMark)) marksByAxis.incrementValueAtPosition(0);
            else if (gameBoard.hasEmptyValueAt(boardLevel, j * GameBoard.SQUARES_PER_SIDE + l + k)) {
                place = GameBoard.SQUARES_PER_SIDE * j + l + k;
                marksByAxis.marks[1]++;
            } else break;
        }
        return new GamePosition(place);
    }

    public GamePosition checkForVert4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        int place = NONE;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE, playerMark)) marksByAxis.marks[2]++;
            else if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE)) {
                place = GameBoard.SQUARES_PER_SIDE * l + j + k * GameBoard.SQUARES_PER_SIDE;
                marksByAxis.marks[3]++;
            } else break;

        }
        return new GamePosition(place);
    }

    public GamePosition checkForDiagDown4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        GamePosition place = GamePosition.nonePosition();
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * 11, playerMark)) marksByAxis.incrementValueAtPosition(0);
            if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * 11)) {
                place = new GamePosition(l * GameBoard.SQUARES_PER_SIDE + j + k * 11);
                marksByAxis.marks[1]++;
            }
        }
        return place;
    }

    public GamePosition checkForDiagUp4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        GamePosition place = GamePosition.nonePosition();
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 50, playerMark)) marksByAxis.marks[2]++;
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 50, GameBoardMark.EMPTY)) {
                place = new GamePosition(l * GameBoard.SQUARES_PER_SIDE + j - k * oneLessThanCountInRow + 50);
                marksByAxis.marks[3]++;
            }
        }
        return place;
    }

    public boolean anyDiagUp4MatchToMark(Mode type, GamePosition place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxis.marks[2] == 3 && marksByAxis.marks[3] == 1) {
            tempRowForChecks[3] = 1;
            if (type.equals(Mode.CHECK)) {
                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) GameBoard.tempTableForChecks[place.getRaw()] = OCCUPIED;
        }
        if (type.equals(Mode.CLEAN) && marksByAxis.marks[2] == 2 && marksByAxis.marks[3] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyDiagDown4MatchToMark(Mode type, GamePosition place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxis.marks[0] == 3 && marksByAxis.marks[1] == 1) {
            tempRowForChecks[2] = 1;
            if (type.equals(Mode.CHECK)) {
                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) GameBoard.tempTableForChecks[place.getRaw()] = OCCUPIED;
        }
        if (type.equals(Mode.CLEAN) && marksByAxis.marks[0] == 2 && marksByAxis.marks[1] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyVert4MatchToMark(Mode type, GamePosition place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxis.marks[2] == 3 && marksByAxis.marks[3] == 1) {
            tempRowForChecks[1] = 1;
            if (type.equals(Mode.CHECK)) {

                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) GameBoard.tempTableForChecks[place.getRaw()] = OCCUPIED;
        }
        if (type.equals(Mode.CLEAN) && marksByAxis.marks[2] == 2 && marksByAxis.marks[3] == 2) {
            match = true;
        }
        return match;
    }

    public boolean anyHoriz4MatchToMark(Mode type, GamePosition gamePosition) {
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxis.marks[0] == 3 && marksByAxis.marks[1] == 1) {
            tempRowForChecks[0] = 1;
            if (type.equals(Mode.CHECK)) {
                return true;
            }
            if (type.equals(Mode.SETFLAGS)) GameBoard.tempTableForChecks[gamePosition.getRaw()] = OCCUPIED;
        }

        if (type.equals(Mode.CLEAN) && marksByAxis.marks[0] == 2 && marksByAxis.marks[1] == 2) {
            return true;
        }
        return false;
    }


    public void setFlagsForLaterProcessing(GameBoardMark playerMark) {
        int k;

        for (k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++)
            GameBoard.tempTableForChecks[k] = GameBoardMark.EMPTY.index;
        blockSeriesOfFourOrMore(playerMark, 0, Mode.SETFLAGS);
        responseTo3Or4InaRowOpportunity(playerMark, 0, Mode.SETFLAGS);
    }

    public void copyIntoStagingBoardFromOddBoardGroupAtDepth(int depth) {
        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++)
            GameBoard.stagingBoard[k] = GameBoard.perhapsaTemporaryBoardHolder[depth][k];
    }

    public void copyStagingBoardIntoOddGroupOfBoardsAtDepth(int depth) {
        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            GameBoard.perhapsaTemporaryBoardHolder[depth][k] = GameBoard.stagingBoard[k];
        }
    }

    void copyBoardToCheck(int indexForBoardToCheck) {
        for (int j = 0; j < GameBoard.TOTAL_SQUARES_PER_BOARD; j++) {
            gameBoard.setValueAt(1, j, gameBoard.getValueAt(indexForBoardToCheck, j));
        }
    }

    void clearMarksByAxisArray() {
        int k;
        for (k = 0; k < 4; k++)
            marksByAxis.marks[k] = GameBoardMark.EMPTY.index;
    }

    public void resetMainGameBoard(int boardLevel) {
        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++)
            gameBoard.setValueAt(boardLevel, k, GameBoardMark.EMPTY);
    }

    public void respondToMouseUp(int playerMove, RawPlayerMove rawPlayerMove) {
        moveNumber.increment();
        gameBoard.markMove(playerMove, GameBoardMark.X_MARK_FOR_PLAYER.index);
        if (winner() == GameBoardMark.EMPTY) {
            lastMove = new ComputerMove(this, rawPlayerMove, gameBoard).getNextComputerMove().getRaw();
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
