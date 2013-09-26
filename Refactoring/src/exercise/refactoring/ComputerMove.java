package exercise.refactoring;

public class ComputerMove {
    private static final int lastPositionOnSecondToLastLine = 89;
    private final MoveSequence moveNumber;
    private final GamePosition nextComputerMove;
    private final GameBoard gameBoard;
    final MarksByAxis marksByAxis = new MarksByAxis();

    public ComputerMove(MoveSequence moveNumber, RawPlayerMove rawPlayerMove, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.moveNumber = moveNumber;
        this.nextComputerMove = makeComputerMove(rawPlayerMove);
    }

    private GamePosition makeComputerMove(RawPlayerMove rawPlayerMove) {
        GamePosition position = new GamePosition();

        if (moveNumber.isFirstMove()) {
            return rawPlayerMove.firstComputerMove();
        }

        position = closeGapInSeries(gameBoard);
        if (moveNumber.isMove(2) && position.isNotNone()) {
            return position;
        }

        if (moveNumber.isOver(3)) {
            position = blockSeriesOfFourOrMoreInCheckMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER);
            if (position.isNotNone()) {
                return position;
            }

            position = blockSeriesOfFourOrMoreInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER);
            if (position.isNotNone()) {
                return position;
            }
        }

        position = responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER);
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((moveNumber.isOver(5) && position.isNotNone())) {
            return position;
        }

        setFlagsForLaterProcessing(GameBoardMark.X_MARK_FOR_PLAYER);

        position.setPosition(tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((moveNumber.isOver(4) && position.isNotNone())) {
            return position;
        }

        position = responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER);
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((moveNumber.isOver(5) && position.isNotNone())) {
            return position;
        }

        setFlagsForLaterProcessing(GameBoardMark.ZERO_MARK_FOR_COMPUTER);

        position.setPosition(tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((moveNumber.isOver(4) && position.isNotNone())) {
            return position;
        }

        gameBoard.setBoardTwoToDuplicateOfMainBoard();

        position = checkSeries(GameBoardMark.ZERO_MARK_FOR_COMPUTER, 0);
        if ((moveNumber.isOver(3) && position.isNotNone())) {
            return position;
        }

        if (moveNumber.isOver(3)) {
            position = checkSeries(GameBoardMark.X_MARK_FOR_PLAYER, 0);
            if (position.isNotNone()) {
                return position;
            }
        }

        position.setPosition(check2o3c(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(check2o3c(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((moveNumber.isOver(20) && position.isNotNone())) {
            return position;
        }

        if (moveNumber.isOver(3)) {
            position.setPosition(checkCross(GameBoardMark.X_MARK_FOR_PLAYER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(3)) {
            position.setPosition(checkCross(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(2)) {
            position.setPosition(checkBox(GameBoardMark.X_MARK_FOR_PLAYER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(2)) {
            position = closeGapInSeries(gameBoard);
            if (position.isNotNone()) {
                return position;
            }
        }

        position = createTwoAxesOrCreateOneAndBlockAnother();
        if (position.isNotNone()) {
            return position;
        }

        position = responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER);
        if (position.isNotNone()) {
            return position;
        }

        position = responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark.X_MARK_FOR_PLAYER);
        if (position.isNotNone()) {
            return position;
        }

        position.setPosition(findSpot(gameBoard));
        return position;
    }

    private GamePosition createTwoAxesOrCreateOneAndBlockAnother() {
        seto4cc(GameBoardMark.X_MARK_FOR_PLAYER);

        for (int position = GameBoard.oneMoreThanSquaresPerSide; position < lastPositionOnSecondToLastLine; position++)
            if (positionIsOccupiedOnStagingBoardAndDesirable(gameBoard, new StagingBoard(), position)) {
                return new GamePosition(position);
            }
        return GamePosition.nonePosition();
    }

    private boolean positionIsOccupiedOnStagingBoardAndDesirable(GameBoard gameBoard, StagingBoard stagingBoard, int position) {
        return stagingBoard.isOccupiedAtPosition(position) && gameBoard.positionIsDesirableForCreateTwoAxesOrCreateOneAndBlockAnother(position);
    }

    private GamePosition closeGapInSeries(GameBoard gameBoard) {
        for (int upToSeven = 1; upToSeven < 7; upToSeven++) {
            for (int upToNine = 1; upToNine < GameBoard.oneLessThanCountInRow; upToNine++) {
                int position = upToSeven + GameBoard.SQUARES_PER_SIDE * upToNine;
                int otherPosition = upToNine + upToSeven * GameBoard.SQUARES_PER_SIDE;

                if (gameBoard.hasOccupiedUnoccupiedOccupiedPatternStartingAt(position)) {
                    return new GamePosition(position + 1);
                }

                if (gameBoard.hasOccupiedUnoccupiedOccupiedDiagonalPatternStartingAt(otherPosition)) {
                    return new GamePosition(otherPosition + GameBoard.SQUARES_PER_SIDE);
                }
            }
        }
        return GamePosition.nonePosition();
    }

    private GamePosition blockSeriesOfFourOrMoreInCheckMode(GameBoardMark playerMark) {
        int zero = 0;

        int upToSquaresPerSide, upToFive, upToSix;
        GamePosition position = new GamePosition();
        GamePosition position2 = new GamePosition();

        for (upToSix = 0; upToSix < 6; upToSix++) {
            for (upToSquaresPerSide = 0; upToSquaresPerSide < GameBoard.SQUARES_PER_SIDE; upToSquaresPerSide++) {
                gameBoard.resetAllMarksAlongAxesForFirstHalfOfBoard(marksByAxis);

                position = checkFor5AlongHorizAxis(playerMark, zero, upToSquaresPerSide, upToSix, position);

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 4), new MarksByAxisPositionPair(1, 1))) {
                    return position;
                }

                position = checkFor5AlongVertAxis(playerMark, zero, upToSquaresPerSide, upToSix, position);

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 4), new MarksByAxisPositionPair(3, 1))) {
                    return position;
                }
            }

            for (upToSquaresPerSide = 0; upToSquaresPerSide < 6; upToSquaresPerSide++) {
                gameBoard.resetAllMarksAlongAxesForFirstHalfOfBoard(marksByAxis);

                for (upToFive = 0; upToFive < 5; upToFive++) {
                    position = checkFor5AlongDiagDownRightAxis(playerMark, zero, upToSquaresPerSide, upToFive, upToSix, position);
                    position2 = checkFor5AlongDiagUpRightAxis(playerMark, zero, upToSquaresPerSide, upToFive, upToSix, position2);
                }

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 4), new MarksByAxisPositionPair(1, 1))) {
                    return position;
                }

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 4), new MarksByAxisPositionPair(3, 1))) {
                    return position2;
                }
            }
        }
        return GamePosition.nonePosition();
    }

    private GamePosition responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark playerMark) {
        int j, k, l;
        GamePosition place = new GamePosition();
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
            return new GamePosition(tempRowForChecks[0] + tempRowForChecks[1] + tempRowForChecks[2] + tempRowForChecks[3]);
        }

        return GamePosition.nonePosition();
    }

    private int tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark playerMark) {
        copyBoardToCheck(0);

        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            if (gameBoard.hasEmptyValueAt(1, k)) {
                gameBoard.setValueAt(1, k, playerMark);

                if (countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, Mode.CLEAN.rawMode) > 1) return k;

                gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (GameBoard.oneMoreThanLastPositionOnBoard);
    }

    private int tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark playerMark) {
        int k;
        int gameBoardLevelToCheck = GameBoard.indexOfMainBoard;

        for (k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++)
            gameBoard.setValueAt(1, k, gameBoard.getValueAt(gameBoardLevelToCheck, k));
        for (k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            if (gameBoard.hasEmptyValueAt(1, k)) {
                gameBoard.setValueAt(1, k, playerMark);
                if (responseTo3Or4InaRowOpportunity(playerMark, 1, Mode.CHECK).isNotNone() && countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, Mode.SAFE.rawMode) > 0) {
                    return k;
                }
                gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (GameBoard.oneMoreThanLastPositionOnBoard);
    }

    private int check2o3c(GameBoardMark playerMark) {
        int k;

        for (k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            if (!gameBoard.hasEmptyValueOnMainBoardAt(k)) continue;

            gameBoard.setValueAt(GameBoard.indexOfMainBoard, k, playerMark);

            if (responseTo3Or4InaRowOpportunity(playerMark, GameBoard.indexOfMainBoard, Mode.COUNT).isOverOne()) {
                return k;
            }

            gameBoard.setValueAt(GameBoard.indexOfMainBoard, k, GameBoardMark.EMPTY);
        }
        return GameBoard.oneMoreThanLastPositionOnBoard;
    }

    private int checkCross(GameBoardMark playerMark) {
        int k, l, x;

        for (k = 1; k < 7; k++) {
            for (l = 1; l < 7; l++) {
                x = k + 10 * l;
                if (gameBoard.mainBoard()[x] == playerMark.index && gameBoard.mainBoard()[x + 2] == playerMark.index && gameBoard.mainBoard()[x + 20] == playerMark.index && gameBoard.mainBoard()[x + 22] == playerMark.index && gameBoard.mainBoard()[x + 11] == 0) return (x + 11);
            }
        }
        return GameBoard.oneMoreThanLastPositionOnBoard;
    }

    private int checkBox(GameBoardMark playerMark) {
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
        return GameBoard.oneMoreThanLastPositionOnBoard;
    }

    private GamePosition responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark playerMark) {
        int alsoUpToFive, upToFour, upToFive;
        GamePosition place = new GamePosition();

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (upToFour = 0; upToFour < 4; upToFour++) {
            tempRowForChecks[upToFour] = 0;
        }

        for (upToFive = 0; upToFive < 5; upToFive++) {
            for (alsoUpToFive = 0; alsoUpToFive < GameBoard.SQUARES_PER_SIDE; alsoUpToFive++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(alsoUpToFive * GameBoard.SQUARES_PER_SIDE + upToFive) && gameBoard.hasEmptyValueOnMainBoardAt(alsoUpToFive * GameBoard.SQUARES_PER_SIDE + upToFive + 5)) {

                    place = checkForHoriz4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (anyHoriz4MatchToMark(Mode.CLEAN, place)) return place;
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 50)) {

                    place = checkForVert4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (anyVert4MatchToMark(Mode.CLEAN, place)) return place;
                }
            }

            for (alsoUpToFive = 0; alsoUpToFive < 5; alsoUpToFive++) {
                clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 55)) {

                    place = checkForDiagDown4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (anyDiagDown4MatchToMark(Mode.CLEAN, place)) return place;
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 50) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 5)) {
                    place = checkForDiagUp4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (anyDiagUp4MatchToMark(Mode.CLEAN, place)) return place;
                }
            }
        }

        return GamePosition.nonePosition();
    }

    private int findSpot(GameBoard gameBoard) {
        int position;
        int i;
        i = GameBoardMark.EMPTY.index;
        do {
            position = (int) (Math.random() * GameBoard.TOTAL_SQUARES_PER_BOARD);
            if (gameBoard.mainBoard()[position] != GameBoardMark.EMPTY.index) continue;
            if (spotFinderCondition(gameBoard, position)) {
                i = GameBoardMark.OCCUPIED.index;
            }
        } while (i == GameBoardMark.EMPTY.index);
        return position;
    }

    private boolean spotFinderCondition(GameBoard gameBoard, int position) {
        return (position > 0 && gameBoard.mainBoard()[position - 1] != GameBoardMark.EMPTY.index) || (position > GameBoard.SQUARES_PER_SIDE && (gameBoard.mainBoard()[position - 11] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position - GameBoard.SQUARES_PER_SIDE] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position - GameBoard.oneLessThanCountInRow] != GameBoardMark.EMPTY.index)) || (position < 99 && gameBoard.mainBoard()[position + 1] != GameBoardMark.EMPTY.index) || (position < 88 && (gameBoard.mainBoard()[position + GameBoard.oneLessThanCountInRow] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position + GameBoard.SQUARES_PER_SIDE] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position + 11] != GameBoardMark.EMPTY.index));
    }

    public GamePosition getNextComputerMove() {
        return nextComputerMove;
    }

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
        return response != GameBoard.oneMoreThanLastPositionOnBoard;
    }


    public GamePosition checkSeries(GameBoardMark playerMark, int depth) {
        int[] auxilliaryBoard = new int[GameBoard.TOTAL_SQUARES_PER_BOARD];
        GamePosition winningPosition;
        if (depth == GameBoard.MAX_DEPTH_FOR_TEMP_BOARD) return GamePosition.nonePosition();

        setc4c(playerMark);

        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            if (StagingBoard.isEmptyAtPosition(k) || auxilliaryBoard[k] != GameBoardMark.EMPTY.index) continue;
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
        GameBoardMark flag, flag2;

        flag = GameBoardMark.EMPTY;
        for (j = 0; j < 10; j++) {
            for (l = 0; l < 6; l++) {
                marksByAxis.setPositionsToZero(0, 1, 2);
                for (k = 0; k < 5; k++) {
                    marksByAxis.incrementValueAtPositionAndReturnValue(gameBoard.getValueAt(x, j * 10 + l + k).index);
                }
                if (getDOublt(playerMark)) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, j * 10 + l + k) && TemporaryChecksTable.tempTableForChecks[j * 10 + l + k] == GameBoardMark.OCCUPIED.index) {
                                flag2 = GameBoardMark.OCCUPIED;
                            }
                        }
                        if (flag2 == GameBoardMark.EMPTY) {
                            zbir++;
                            flag = GameBoardMark.OCCUPIED;
                            break;
                        }
                    } else {
                        zbir++;
                        flag = GameBoardMark.OCCUPIED;
                        break;
                    }
                }
            }
            if (flag == GameBoardMark.OCCUPIED) break;
        }
        flag = GameBoardMark.EMPTY;

        for (j = 0; j < 10; j++) {
            for (l = 0; l < 6; l++) {
                marksByAxis.setPositionsToZero(0, 1, 2);
                for (k = 0; k < 5; k++)
                    marksByAxis.incrementValueAtPosition(gameBoard.getValueAt(x, l * 10 + j + k * 10));
                if (getDOublt(playerMark)) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY;
                        for (k = 0; k < 5; k++)
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j + k * 10) && TemporaryChecksTable.tempTableForChecks[l * 10 + j + k * 10] == GameBoardMark.OCCUPIED.index) flag2 = GameBoardMark.OCCUPIED;
                        if (flag2 == GameBoardMark.EMPTY) {
                            zbir++;
                            flag = GameBoardMark.OCCUPIED;
                            break;
                        }
                    } else {
                        zbir++;
                        flag = GameBoardMark.OCCUPIED;
                        break;
                    }
                }
            }
            if (flag == GameBoardMark.OCCUPIED) break;
        }
        flag = GameBoardMark.EMPTY;
        for (l = 0; l < 6; l++) {
            for (j = 0; j < 6; j++) {
                for (k = 0; k < 3; k++)
                    /* za diag */
                    marksByAxis.setPositionsToZero(k);
                for (k = 0; k < 5; k++)
                    /* diag\ */
                    marksByAxis.incrementValueAtPosition(gameBoard.getValueAt(x, l * 10 + j + k * 11));
                if (getDOublt(playerMark)) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j + k * 11) && TemporaryChecksTable.tempTableForChecks[l * 10 + j + k * 11] == GameBoardMark.OCCUPIED.index) {
                                flag2 = GameBoardMark.OCCUPIED;
                            }
                        }
                        if (flag2 == GameBoardMark.EMPTY) {
                            zbir++;
                            flag = GameBoardMark.OCCUPIED;
                            break;
                        }
                    } else {
                        zbir++;
                        flag = GameBoardMark.OCCUPIED;
                        break;
                    }
                }
            }
            if (flag == GameBoardMark.OCCUPIED) break;
        }
        flag = GameBoardMark.EMPTY;
        for (l = 0; l < 6; l++) {
            for (j = 0; j < 6; j++) {
                for (k = 0; k < 3; k++) {
                    marksByAxis.setPositionsToZero(k);
                }
                for (k = 0; k < 5; k++) {
                    marksByAxis.incrementValueAtPosition(gameBoard.getValueAt(x, l * 10 + j - k * GameBoard.oneLessThanCountInRow + 40));
                }
                if (getDOublt(playerMark)) {
                    if (type == Mode.SAFE.rawMode) {
                        flag2 = GameBoardMark.EMPTY;
                        for (k = 0; k < 5; k++) {
                            if (gameBoard.hasEmptyValueAt(x, l * 10 + j - k * GameBoard.oneLessThanCountInRow + 40) && TemporaryChecksTable.tempTableForChecks[l * 10 + j - k * GameBoard.oneLessThanCountInRow + 40] == GameBoardMark.OCCUPIED.index) {
                                flag2 = GameBoardMark.OCCUPIED;
                            }
                        }
                        if (flag2 == GameBoardMark.EMPTY) {
                            zbir++;
                            flag = GameBoardMark.OCCUPIED;
                            break;
                        }
                    } else {
                        zbir++;
                        flag = GameBoardMark.OCCUPIED;
                        break;
                    }
                }
            }
            if (flag == GameBoardMark.OCCUPIED) break;
        }
        return zbir;
    }

    private boolean getDOublt(GameBoardMark playerMark) {
        return marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(playerMark, 4), new MarksByAxisPositionPair(GameBoardMark.EMPTY, 1));
    }

    public void setc4c(GameBoardMark playerMark) {
        int j, k, l;
        int position, x = 2; // TODO make this Position object

        for (j = 0; j < GameBoard.TOTAL_SQUARES_PER_BOARD; j++) {
            StagingBoard.setValueAtPositionToEmpty(j);
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
                        marksByAxis.incrementValueAtPositionAndReturnValue(1);
                    }
                }
                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 3), new MarksByAxisPositionPair(1, 2))) for (l = 0; l < 2; l++)
                    StagingBoard.setValueAtPositionToOccupied(tempRowForChecks[l]);
            }
        }

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxis.setPositionsToZero(0, 1);
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE).index;
                    if (position == playerMark.index) marksByAxis.incrementValueAtPositionAndReturnValue(0);
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxis.getValueAtPosition(1)] = k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE;
                        marksByAxis.incrementValueAtPositionAndReturnValue(1);
                    }
                }
                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 3), new MarksByAxisPositionPair(1, 2))) for (l = 0; l < 2; l++)
                    StagingBoard.setValueAtPositionToOccupied(tempRowForChecks[l]);
            }
        }

        for (j = 0; j < 6; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxis.setPositionsToZero(0, 1);
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * GameBoard.SQUARES_PER_SIDE + k + l * 11).index;
                    if (position == playerMark.index) marksByAxis.incrementValueAtPositionAndReturnValue(0);
                    if (position == GameBoardMark.EMPTY.index) {
                        tempRowForChecks[marksByAxis.getValueAtPosition(1)] = j * GameBoard.SQUARES_PER_SIDE + k + l * 11;
                        marksByAxis.incrementValueAtPositionAndReturnValue(1);
                    }
                }
                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 3), new MarksByAxisPositionPair(1, 2))) for (l = 0; l < 2; l++)
                    StagingBoard.setValueAtPositionToOccupied(tempRowForChecks[l]);
            }
        }

        for (j = 0; j < 6; j++) {
            for (k = 0; k < 6; k++) {
                marksByAxis.setPositionsToZero(0, 1);
                for (l = 0; l < 5; l++) {
                    position = gameBoard.getValueAt(x, j * GameBoard.SQUARES_PER_SIDE + k - l * GameBoard.oneLessThanCountInRow + 40).index;
                    if (position == playerMark.index) marksByAxis.incrementValueAtPositionAndReturnValue(0);
                    if (position == GameBoardMark.EMPTY.index) {
                        setValueInTempRowForChecksToCalculatedValue(j, k, l, tempRowForChecks);
                        marksByAxis.incrementValueAtPositionAndReturnValue(1);
                    }
                }
                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 3), new MarksByAxisPositionPair(1, 2))) for (l = 0; l < 2; l++){
                    StagingBoard.setValueAtPositionToOccupied(tempRowForChecks[l]);
                }
            }
        }
    }

    private void setValueInTempRowForChecksToCalculatedValue(int j, int k, int l, int[] tempRowForChecks) {
        tempRowForChecks[marksByAxis.getValueAtPosition(1)] = j * GameBoard.SQUARES_PER_SIDE + k - l * GameBoard.oneLessThanCountInRow + 40;
    }

    public void seto4cc(GameBoardMark playerMark) {
        int j, k, l;
        int position;

        for (j = 0; j < GameBoard.TOTAL_SQUARES_PER_BOARD; j++) {
            StagingBoard.setValueAtPositionToEmpty(j);
        }
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 5; k++) {
                position = j * GameBoard.SQUARES_PER_SIDE + k;
                if (gameBoard.hasEmptyValueAtPositionOnBoardTwoAndPositionWithDiff(position, 5)) {
                    marksByAxis.setPositionsToZero(0, 1);
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.indexOfBoardTwo, j * GameBoard.SQUARES_PER_SIDE + k + l).index;
                        if (position == playerMark.index) marksByAxis.incrementValueAtPositionAndReturnValue(0);
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxis.getValueAtPosition(1)] = j * GameBoard.SQUARES_PER_SIDE + k + l;
                            marksByAxis.incrementValueAtPositionAndReturnValue(1);
                        }
                    }
                    if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 2), new MarksByAxisPositionPair(1, 2))) for (l = 0; l < 2; l++)
                        StagingBoard.setValueAtPositionToOccupied(tempRowForChecks[l]);
                }
            }
        }

        for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
            for (k = 0; k < 5; k++) {
                position = k * GameBoard.SQUARES_PER_SIDE + j;
                if (gameBoard.hasEmptyValueAtPositionOnBoardTwoAndPositionWithDiff(position, 50)) {
                    marksByAxis.setPositionsToZero(0, 1);
                    for (l = 1; l < 5; l++) {
                        position = gameBoard.getValueAt(GameBoard.indexOfBoardTwo, k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE).index;
                        if (position == playerMark.index) marksByAxis.incrementValueAtPositionAndReturnValue(0);
                        if (position == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxis.getValueAtPosition(1)] = k * GameBoard.SQUARES_PER_SIDE + j + l * GameBoard.SQUARES_PER_SIDE;
                            marksByAxis.incrementValueAtPositionAndReturnValue(1);
                        }
                    }
                    if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 2), new MarksByAxisPositionPair(1, 2))) for (l = 0; l < 2; l++)
                        StagingBoard.setValueAtPositionToOccupied(tempRowForChecks[l]);
                }
            }
        }

        for (int a1 = 0; a1 < 5; a1++) {
            for (int a2 = 0; a2 < 5; a2++) {
                int positionA = a1 * GameBoard.SQUARES_PER_SIDE + a2;
                if (gameBoard.hasEmptyValueAt(GameBoard.indexOfBoardTwo, positionA) && gameBoard.hasEmptyValueAt(GameBoard.indexOfBoardTwo, positionA + 55)) {
                    marksByAxis.setPositionsToZero(0, 1);
                    for (int a3 = 1; a3 < 5; a3++) {
                        positionA = gameBoard.getValueAt(GameBoard.indexOfBoardTwo, a1 * GameBoard.SQUARES_PER_SIDE + a2 + a3 * 11).index;
                        if (positionA == playerMark.index) marksByAxis.incrementValueAtPositionAndReturnValue(0);
                        if (positionA == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxis.getValueAtPosition(1)] = a1 * GameBoard.SQUARES_PER_SIDE + a2 + a3 * 11;
                            marksByAxis.incrementValueAtPositionAndReturnValue(1);
                        }
                    }
                    if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 2), new MarksByAxisPositionPair(1, 2))) for (int a4 = 0; a4 < 2; a4++)
                        StagingBoard.setValueAtPositionToOccupied(tempRowForChecks[a4]);
                }
            }
        }

        for (int b1 = 0; b1 < 5; b1++) {
            for (int b2 = 0; b2 < 5; b2++) {
                int positionB = b1 * GameBoard.SQUARES_PER_SIDE + b2;
                if (gameBoard.hasEmptyValueAt(GameBoard.indexOfBoardTwo, positionB + 50) && gameBoard.hasEmptyValueAt(GameBoard.indexOfBoardTwo, positionB + 50 + 5)) {
                    marksByAxis.setPositionsToZero(0, 1);
                    for (int b3 = 1; b3 < 5; b3++) {
                        positionB = gameBoard.getValueAt(GameBoard.indexOfBoardTwo, b1 * GameBoard.SQUARES_PER_SIDE + b2 - b3 * GameBoard.oneLessThanCountInRow + 50).index;
                        if (positionB == playerMark.index) marksByAxis.incrementValueAtPositionAndReturnValue(0);
                        if (positionB == GameBoardMark.EMPTY.index) {
                            tempRowForChecks[marksByAxis.getValueAtPosition(1)] = b1 * GameBoard.SQUARES_PER_SIDE + b2 - b3 * GameBoard.oneLessThanCountInRow + 50;
                            marksByAxis.incrementValueAtPositionAndReturnValue(1);
                        }
                    }
                    if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 2), new MarksByAxisPositionPair(1, 2))) for (int b4 = 0; b4 < 2; b4++)
                        StagingBoard.setValueAtPositionToOccupied(tempRowForChecks[b4]);
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
                gameBoard.resetAllMarksAlongAxesForFirstHalfOfBoard(marksByAxis);

                position = checkFor5AlongHorizAxis(playerMark, x, j, l, position);

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 3), new MarksByAxisPositionPair(1, 2))) {
                    if (type.equals(Mode.SETFLAGS)) {
                        TemporaryChecksTable.tempTableForChecks[tempRowForChecks[0]] = GameBoardMark.OCCUPIED.index;
                        TemporaryChecksTable.tempTableForChecks[tempRowForChecks[1]] = GameBoardMark.OCCUPIED.index;
                    }
                    if (type.equals(Mode.CLEAN)) return new GamePosition(tempRowForChecks[0]);
                }

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 4), new MarksByAxisPositionPair(1, 1)) && type.equals(Mode.CHECK)) return position;

                position = checkFor5AlongVertAxis(playerMark, x, j, l, position);

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 3), new MarksByAxisPositionPair(3, 2))) {
                    if (type.equals(Mode.SETFLAGS)) {
                        TemporaryChecksTable.tempTableForChecks[tempRowForChecks[0]] = GameBoardMark.OCCUPIED.index;
                        TemporaryChecksTable.tempTableForChecks[tempRowForChecks[1]] = GameBoardMark.OCCUPIED.index;
                    }
                    if (type.equals(Mode.CLEAN)) return new GamePosition(tempRowForChecks[0]);
                }
                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 4), new MarksByAxisPositionPair(3, 1)) && type.equals(Mode.CHECK)) return position;
            }

            for (j = 0; j < 6; j++) {
                gameBoard.resetAllMarksAlongAxesForFirstHalfOfBoard(marksByAxis);

                for (k = 0; k < 5; k++) {
                    position = checkFor5AlongDiagDownRightAxis(playerMark, x, j, k, l, position);
                    position2 = checkFor5AlongDiagUpRightAxis(playerMark, x, j, k, l, position2);
                }

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 3), new MarksByAxisPositionPair(1, 2))) {
                    if (type.equals(Mode.SETFLAGS)) {
                        TemporaryChecksTable.tempTableForChecks[tempRowForChecks[0]] = GameBoardMark.OCCUPIED.index;
                        TemporaryChecksTable.tempTableForChecks[tempRowForChecks[1]] = GameBoardMark.OCCUPIED.index;
                    }
                    if (type.equals(Mode.CLEAN)) return new GamePosition(tempRowForChecks[0]);
                }
                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 4), new MarksByAxisPositionPair(1, 1)) && type.equals(Mode.CHECK)) return position;

                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 3), new MarksByAxisPositionPair(3, 2))) {
                    if (type.equals(Mode.SETFLAGS)) {
                        TemporaryChecksTable.tempTableForChecks[tempRowForChecks[0]] = GameBoardMark.OCCUPIED.index;
                        TemporaryChecksTable.tempTableForChecks[tempRowForChecks[1]] = GameBoardMark.OCCUPIED.index;
                    }
                    if (type.equals(Mode.CLEAN)) return new GamePosition(tempRowForChecks[0]);
                }
                if (marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 4), new MarksByAxisPositionPair(3, 1)) && type.equals(Mode.CHECK)) return position2;
            }
        }
        return GamePosition.nonePosition();
    }

    public GamePosition checkFor5AlongDiagUpRightAxis(GameBoardMark playerMark, int x, int j, int k, int l, GamePosition position2) {
        if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j - k * GameBoard.oneLessThanCountInRow + 40, playerMark)) marksByAxis.incrementValueAtPositionAndReturnValue(2);

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j - k * GameBoard.oneLessThanCountInRow + 40)) {
            position2 = new GamePosition(l * GameBoard.SQUARES_PER_SIDE + j - k * GameBoard.oneLessThanCountInRow + 40);
            tempRowForChecks[marksByAxis.getValueAtPosition(3)] = position2.getRaw();
            marksByAxis.incrementValueAtPosition(3);
        }
        return position2;
    }

    public GamePosition checkFor5AlongDiagDownRightAxis(GameBoardMark playerMark, int x, int j, int k, int l, GamePosition position) {
        if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j + k * 11, playerMark)) marksByAxis.incrementValueAtPositionAndReturnValue(0);

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j + k * 11)) {
            position = new GamePosition(l * GameBoard.SQUARES_PER_SIDE + j + k * 11);
            tempRowForChecks[marksByAxis.getValueAtPosition(1)] = position.getRaw();
            marksByAxis.incrementValueAtPositionAndReturnValue(1);
        }
        return position;
    }

    public GamePosition checkFor5AlongVertAxis(GameBoardMark playerMark, int x, int j, int l, GamePosition position) {
        int k;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(x, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE, playerMark)) marksByAxis.incrementValueAtPositionAndReturnValue(2);
            else if (gameBoard.hasEmptyValueAt(x, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE)) {
                position = new GamePosition(GameBoard.SQUARES_PER_SIDE * l + j + k * GameBoard.SQUARES_PER_SIDE);
                tempRowForChecks[marksByAxis.getValueAtPosition(3)] = position.getRaw();
                marksByAxis.incrementValueAtPosition(3);
            } else break;
        }
        return position;
    }

    public GamePosition checkFor5AlongHorizAxis(GameBoardMark playerMark, int x, int j, int l, GamePosition position) {
        int k;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(x, j * GameBoard.SQUARES_PER_SIDE + l + k, playerMark)) marksByAxis.incrementValueAtPositionAndReturnValue(0);

            else if (gameBoard.hasEmptyValueAt(x, j * GameBoard.SQUARES_PER_SIDE + l + k)) {
                position = new GamePosition(GameBoard.SQUARES_PER_SIDE * j + l + k);
                tempRowForChecks[marksByAxis.getValueAtPosition(1)] = position.getRaw();
                marksByAxis.incrementValueAtPositionAndReturnValue(1);

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
        int place = GameBoard.oneMoreThanLastPositionOnBoard;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, j * GameBoard.SQUARES_PER_SIDE + l + k, playerMark)) marksByAxis.incrementValueAtPositionAndReturnValue(0);
            else if (gameBoard.hasEmptyValueAt(boardLevel, j * GameBoard.SQUARES_PER_SIDE + l + k)) {
                place = GameBoard.SQUARES_PER_SIDE * j + l + k;
                marksByAxis.incrementValueAtPositionAndReturnValue(1);
            } else break;
        }
        return new GamePosition(place);
    }

    public GamePosition checkForVert4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        int place = GameBoard.oneMoreThanLastPositionOnBoard;
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE, playerMark)) marksByAxis.incrementValueAtPositionAndReturnValue(2);
            else if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * GameBoard.SQUARES_PER_SIDE)) {
                place = GameBoard.SQUARES_PER_SIDE * l + j + k * GameBoard.SQUARES_PER_SIDE;
                marksByAxis.incrementValueAtPosition(3);
            } else break;

        }
        return new GamePosition(place);
    }

    public GamePosition checkForDiagDown4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        GamePosition place = GamePosition.nonePosition();
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * 11, playerMark)) marksByAxis.incrementValueAtPositionAndReturnValue(0);
            if (gameBoard.hasEmptyValueAt(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j + k * 11)) {
                place = new GamePosition(l * GameBoard.SQUARES_PER_SIDE + j + k * 11);
                marksByAxis.incrementValueAtPositionAndReturnValue(1);
            }
        }
        return place;
    }

    public GamePosition checkForDiagUp4InRow(GameBoardMark playerMark, int boardLevel, int j, int l) {
        GamePosition place = GamePosition.nonePosition();
        int k;
        for (k = 1; k < 5; k++) {
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j - k * GameBoard.oneLessThanCountInRow + 50, playerMark)) marksByAxis.incrementValueAtPosition(2);
            if (gameBoard.valueAtPositionMatches(boardLevel, l * GameBoard.SQUARES_PER_SIDE + j - k * GameBoard.oneLessThanCountInRow + 50, GameBoardMark.EMPTY)) {
                place = new GamePosition(l * GameBoard.SQUARES_PER_SIDE + j - k * GameBoard.oneLessThanCountInRow + 50);
                marksByAxis.incrementValueAtPosition(3);
            }
        }
        return place;
    }

    public boolean anyDiagUp4MatchToMark(Mode type, GamePosition place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 3), new MarksByAxisPositionPair(3, 1))) {
            tempRowForChecks[3] = 1;
            if (type.equals(Mode.CHECK)) {
                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) TemporaryChecksTable.tempTableForChecks[place.getRaw()] = GameBoardMark.OCCUPIED.index;
        }
        if (type.equals(Mode.CLEAN) && marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 2), new MarksByAxisPositionPair(3, 2))) {
            match = true;
        }
        return match;
    }

    public boolean anyDiagDown4MatchToMark(Mode type, GamePosition place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 3), new MarksByAxisPositionPair(1, 1))) {
            tempRowForChecks[2] = 1;
            if (type.equals(Mode.CHECK)) {
                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) TemporaryChecksTable.tempTableForChecks[place.getRaw()] = GameBoardMark.OCCUPIED.index;
        }
        if (type.equals(Mode.CLEAN) && marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 2), new MarksByAxisPositionPair(1, 2))) {
            match = true;
        }
        return match;
    }

    public boolean anyVert4MatchToMark(Mode type, GamePosition place) {
        boolean match = false;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 3), new MarksByAxisPositionPair(3, 1))) {
            tempRowForChecks[1] = 1;
            if (type.equals(Mode.CHECK)) {

                match = true;
            }
            if (type.equals(Mode.SETFLAGS)) TemporaryChecksTable.tempTableForChecks[place.getRaw()] = GameBoardMark.OCCUPIED.index;
        }
        if (type.equals(Mode.CLEAN) && marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 2), new MarksByAxisPositionPair(3, 2))) {
            match = true;
        }
        return match;
    }

    public boolean anyHoriz4MatchToMark(Mode type, GamePosition gamePosition) {
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        if (!type.equals(Mode.CLEAN) && marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 3), new MarksByAxisPositionPair(1, 1))) {
            tempRowForChecks[0] = 1;
            if (type.equals(Mode.CHECK)) {
                return true;
            }
            if (type.equals(Mode.SETFLAGS)) TemporaryChecksTable.tempTableForChecks[gamePosition.getRaw()] = GameBoardMark.OCCUPIED.index;
        }

        if (type.equals(Mode.CLEAN) && marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 2), new MarksByAxisPositionPair(1, 2))) {
            return true;
        }
        return false;
    }


    public void setFlagsForLaterProcessing(GameBoardMark playerMark) {
        int k;

        for (k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++)
            TemporaryChecksTable.tempTableForChecks[k] = GameBoardMark.EMPTY.index;
        blockSeriesOfFourOrMore(playerMark, 0, Mode.SETFLAGS);
        responseTo3Or4InaRowOpportunity(playerMark, 0, Mode.SETFLAGS);
    }

    public void copyIntoStagingBoardFromOddBoardGroupAtDepth(int depth) {
        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++)
            StagingBoard.setValueAt(k, TemporaryBoardHolder.getValueAt(depth, k));
    }

    public void copyStagingBoardIntoOddGroupOfBoardsAtDepth(int depth) {
        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            TemporaryBoardHolder.setValueAt(depth, k, StagingBoard.getValueAt(k));
        }
    }

    void copyBoardToCheck(int indexForBoardToCheck) {
        for (int j = 0; j < GameBoard.TOTAL_SQUARES_PER_BOARD; j++) {
            gameBoard.setValueAt(1, j, gameBoard.getValueAt(indexForBoardToCheck, j));
        }
    }

    void clearMarksByAxisArray() {
        for (int k = 0; k < 4; k++) {
            marksByAxis.setPositionsToZero(k);
        }
    }


}
