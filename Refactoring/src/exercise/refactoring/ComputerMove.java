package exercise.refactoring;

public class ComputerMove {
    public static final int lastPositionOnSecondToLastLine = 89;
    private final LegacyGame legacyGame;
    private final MoveSequence moveNumber;

    public ComputerMove(LegacyGame legacyGame) {
        this.legacyGame = legacyGame;
        this.moveNumber = legacyGame.moveNumber;
    }

    public GamePosition makeComputerMove(RawPlayerMove rawPlayerMove) {
        GamePosition position = new GamePosition();

        if (moveNumber.isFirstMove()) {
            return rawPlayerMove.firstComputerMove();
        }

        position.setPosition(closeGapInSeries(legacyGame));
        if ((moveNumber.isMove(2))) {
            return position;
        }

        if (moveNumber.isOver(3)) {
            position = blockSeriesOfFourOrMoreInCheckMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER, legacyGame);
            if (position.isNotNone()) {
                return position;
            }

            position = blockSeriesOfFourOrMoreInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame);
            if (position.isNotNone()) {
                return position;
            }
        }

        position.setPosition(responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER, legacyGame));
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.ZERO_MARK_FOR_COMPUTER, legacyGame));
        if ((moveNumber.isOver(5) && position.isNotNone())) {
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(GameBoardMark.X_MARK_FOR_PLAYER);

        position.setPosition(tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.ZERO_MARK_FOR_COMPUTER, legacyGame));
        if ((moveNumber.isOver(4) && position.isNotNone())) {
            return position;
        }

        position.setPosition(responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame));
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame));
        if ((moveNumber.isOver(5) && position.isNotNone())) {
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(GameBoardMark.ZERO_MARK_FOR_COMPUTER);

        position.setPosition(tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame));
        if ((moveNumber.isOver(4) && position.isNotNone())) {
            return position;
        }

        legacyGame.gameBoard.setBoardTwoToDuplicateOfMainBoard();

        position.setPosition(legacyGame.checkSeries(GameBoardMark.ZERO_MARK_FOR_COMPUTER, 0));
        if ((moveNumber.isOver(3) && position.isNotNone())) {
            return position;
        }

        if (moveNumber.isOver(3)) {
            position.setPosition(legacyGame.checkSeries(GameBoardMark.X_MARK_FOR_PLAYER, 0));
            if (position.isNotNone()) {
                return position;
            }
        }

        position.setPosition(check2o3c(GameBoardMark.ZERO_MARK_FOR_COMPUTER, legacyGame));
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(check2o3c(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame));
        if ((moveNumber.isOver(20) && position.isNotNone())) {
            return position;
        }

        if (moveNumber.isOver(3)) {
            position.setPosition(checkCross(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(3)) {
            position.setPosition(checkCross(GameBoardMark.ZERO_MARK_FOR_COMPUTER, legacyGame));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(2)) {
            position.setPosition(checkBox(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(2)) {
            position.setPosition(closeGapInSeries(legacyGame));
            if (position.isNotNone()) {
                return position;
            }
        }

        position = createTwoAxesOrCreateOneAndBlockAnother(legacyGame);
        if (position.isNotNone()) {
            return position;
        }

        position = responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER, legacyGame);
        if (position.isNotNone()) {
            return position;
        }

        position = responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame);
        if (position.isNotNone()) {
            return position;
        }

        position.setPosition(findSpot(legacyGame.gameBoard));
        return position;
    }

    private GamePosition createTwoAxesOrCreateOneAndBlockAnother(LegacyGame legacyGame) {
        legacyGame.seto4cc(GameBoardMark.X_MARK_FOR_PLAYER);

        for (int position = GameBoard.oneMoreThanSquaresPerSide; position < lastPositionOnSecondToLastLine; position++)
            if (positionIsOccupiedOnStagingBoardAndDesirable(legacyGame.gameBoard, legacyGame.stagingBoard, position)) {
                return new GamePosition(position);
            }
        return GamePosition.nonePosition();
    }

    private boolean positionIsOccupiedOnStagingBoardAndDesirable(GameBoard gameBoard, int[] stagingBoard, int position) {
        return stagingBoard[position] == LegacyGame.OCCUPIED && gameBoard.positionIsDesirableForCreateTwoAxesOrCreateOneAndBlockAnother(position);
    }

    public int closeGapInSeries(LegacyGame legacyGame) {
        int upToSeven, upToNine, position, otherPosition;

        for (upToSeven = 1; upToSeven < 7; upToSeven++) {
            for (upToNine = 1; upToNine < LegacyGame.oneLessThanCountInRow; upToNine++) {
                position = upToSeven + GameBoard.SQUARES_PER_SIDE * upToNine;
                otherPosition = upToNine + upToSeven * GameBoard.SQUARES_PER_SIDE;

                if (legacyGame.gameBoard.hasOccupiedUnoccupiedOccupiedPatternStartingAt(position)) {
                    return (position + 1);
                }

                if (legacyGame.gameBoard.hasOccupiedUnoccupiedOccupiedDiagonalPatternStartingAt(otherPosition)) {
                    return (otherPosition + GameBoard.SQUARES_PER_SIDE);
                }
            }
        }
        return LegacyGame.NONE;
    }

    public GamePosition blockSeriesOfFourOrMoreInCheckMode(GameBoardMark playerMark, LegacyGame legacyGame) {
        int zero = 0;

        int upToSquaresPerSide, upToFive, upToSix;
        int position = 0;
        int position2 = 0;

        for (upToSix = 0; upToSix < 6; upToSix++) {
            for (upToSquaresPerSide = 0; upToSquaresPerSide < GameBoard.SQUARES_PER_SIDE; upToSquaresPerSide++) {
                legacyGame.resetAllMarksAlongAxesForFirstHalfOfBoard();

                position = legacyGame.checkFor5AlongHorizAxis(playerMark, zero, upToSquaresPerSide, upToSix, position);

                if (legacyGame.marksByAxisByPlayerForChecking[0] == 4 && legacyGame.marksByAxisByPlayerForChecking[1] == 1) {
                    return new GamePosition(position);
                }

                position = legacyGame.checkFor5AlongVertAxis(playerMark, zero, upToSquaresPerSide, upToSix, position);

                if (legacyGame.marksByAxisByPlayerForChecking[2] == 4 && legacyGame.marksByAxisByPlayerForChecking[3] == 1) {
                    return new GamePosition(position);
                }
            }

            for (upToSquaresPerSide = 0; upToSquaresPerSide < 6; upToSquaresPerSide++) {
                legacyGame.resetAllMarksAlongAxesForFirstHalfOfBoard();

                for (upToFive = 0; upToFive < 5; upToFive++) {
                    position = legacyGame.checkFor5AlongDiagDownRightAxis(playerMark, zero, upToSquaresPerSide, upToFive, upToSix, position);
                    position2 = legacyGame.checkFor5AlongDiagUpRightAxis(playerMark, zero, upToSquaresPerSide, upToFive, upToSix, position2);
                }

                if (legacyGame.marksByAxisByPlayerForChecking[0] == 4 && legacyGame.marksByAxisByPlayerForChecking[1] == 1) {
                    return new GamePosition(position);
                }

                if (legacyGame.marksByAxisByPlayerForChecking[2] == 4 && legacyGame.marksByAxisByPlayerForChecking[3] == 1) {
                    return new GamePosition(position2);
                }
            }
        }
        return GamePosition.nonePosition();
    }

    public int responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark playerMark, LegacyGame legacyGame) {
        int j, k, l;
        int place = 0;
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 4; k++) {
            tempRowForChecks[k] = 0;
        }

        for (l = 0; l < 5; l++) {
            for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
                legacyGame.clearMarksByAxisArray();

                if (legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(j * GameBoard.SQUARES_PER_SIDE + l) && legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(j * GameBoard.SQUARES_PER_SIDE + l + 5)) {

                    place = legacyGame.checkForHoriz4InRow(playerMark, 0, j, l);
                    if (legacyGame.anyHoriz4MatchToMark(Mode.CHECK, place)) return place;
                }

                if (legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j) && legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 50)) {

                    place = legacyGame.checkForVert4InRow(playerMark, 0, j, l);
                    if (legacyGame.anyVert4MatchToMark(Mode.CHECK, place)) return place;
                }
            }

            for (j = 0; j < 5; j++) {
                legacyGame.clearMarksByAxisArray();

                if (legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j) && legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 55)) {

                    place = legacyGame.checkForDiagDown4InRow(playerMark, 0, j, l);
                    if (legacyGame.anyDiagDown4MatchToMark(Mode.CHECK, place)) return place;
                }

                if (legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 50) && legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 5)) {
                    place = legacyGame.checkForDiagUp4InRow(playerMark, 0, j, l);
                    if (legacyGame.anyDiagUp4MatchToMark(Mode.CHECK, place)) return place;
                }
            }
        }

        if (Mode.CHECK.equals(Mode.COUNT)) {
            return tempRowForChecks[0] + tempRowForChecks[1] + tempRowForChecks[2] + tempRowForChecks[3];
        }

        return (LegacyGame.NONE);
    }

    public int tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark playerMark, LegacyGame legacyGame) {
        legacyGame.copyBoardToCheck(0);

        for (int k = 0; k < LegacyGame.TOTAL_SQUARES_PER_BOARD; k++) {
            if (legacyGame.gameBoard.hasEmptyValueAt(1, k)) {
                legacyGame.gameBoard.setValueAt(1, k, playerMark);

                if (legacyGame.countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, Mode.CLEAN.rawMode) > 1) return k;

                legacyGame.gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (LegacyGame.NONE);
    }

    public int tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark playerMark, LegacyGame legacyGame) {
        int k;
        int gameBoardLevelToCheck = GameBoard.mainBoardIndex;

        for (k = 0; k < LegacyGame.TOTAL_SQUARES_PER_BOARD; k++)
            legacyGame.gameBoard.setValueAt(1, k, legacyGame.gameBoard.getValueAt(gameBoardLevelToCheck, k));
        for (k = 0; k < LegacyGame.TOTAL_SQUARES_PER_BOARD; k++) {
            if (legacyGame.gameBoard.hasEmptyValueAt(1, k)) {
                legacyGame.gameBoard.setValueAt(1, k, playerMark);
                if (legacyGame.responseTo3Or4InaRowOpportunity(playerMark, 1, Mode.CHECK) != LegacyGame.NONE && legacyGame.countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, Mode.SAFE.rawMode) > 0) {
                    return k;
                }
                legacyGame.gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (LegacyGame.NONE);
    }

    public int check2o3c(GameBoardMark playerMark, LegacyGame legacyGame) {
        int k;

        for (k = 0; k < LegacyGame.TOTAL_SQUARES_PER_BOARD; k++) {
            if (!legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(k)) continue;

            legacyGame.gameBoard.setValueAt(GameBoard.mainBoardIndex, k, playerMark);

            if (legacyGame.responseTo3Or4InaRowOpportunity(playerMark, GameBoard.mainBoardIndex, Mode.COUNT) > 1) {
                return k;
            }

            legacyGame.gameBoard.setValueAt(GameBoard.mainBoardIndex, k, GameBoardMark.EMPTY);
        }
        return LegacyGame.NONE;
    }

    public int checkCross(GameBoardMark playerMark, LegacyGame legacyGame) {
        int k, l, x;

        for (k = 1; k < 7; k++) {
            for (l = 1; l < 7; l++) {
                x = k + 10 * l;
                if (legacyGame.gameBoard.mainBoard()[x] == playerMark.index && legacyGame.gameBoard.mainBoard()[x + 2] == playerMark.index && legacyGame.gameBoard.mainBoard()[x + 20] == playerMark.index && legacyGame.gameBoard.mainBoard()[x + 22] == playerMark.index && legacyGame.gameBoard.mainBoard()[x + 11] == 0) return (x + 11);
            }
        }
        return LegacyGame.NONE;
    }

    public int checkBox(GameBoardMark playerMark, LegacyGame legacyGame) {
        for (int k = 1; k < 8; k++) {
            for (int l = 1; l < 8; l++) {
                int cnt = 0;
                int pos = -1;
                for (int a = 0; a < 2; a++) {
                    for (int b = 0; b < 2; b++) {
                        int x = k + a + 10 * (l + b);
                        int c = legacyGame.gameBoard.mainBoard()[x];
                        if (c == playerMark.index) cnt++;
                        else if (c == 0) pos = x;
                    }
                }
                if (cnt == 3 && pos != -1) return pos;
            }
        }
        return LegacyGame.NONE;
    }

    public GamePosition responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark playerMark, LegacyGame legacyGame) {
        int alsoUpToFive, upToFour, upToFive;
        int place = 0;

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (upToFour = 0; upToFour < 4; upToFour++) {
            tempRowForChecks[upToFour] = 0;
        }

        for (upToFive = 0; upToFive < 5; upToFive++) {
            for (alsoUpToFive = 0; alsoUpToFive < GameBoard.SQUARES_PER_SIDE; alsoUpToFive++) {
                legacyGame.clearMarksByAxisArray();

                if (legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(alsoUpToFive * GameBoard.SQUARES_PER_SIDE + upToFive) && legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(alsoUpToFive * GameBoard.SQUARES_PER_SIDE + upToFive + 5)) {

                    place = legacyGame.checkForHoriz4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (legacyGame.anyHoriz4MatchToMark(Mode.CLEAN, place)) return new GamePosition(place);
                }

                if (legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive) && legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 50)) {

                    place = legacyGame.checkForVert4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (legacyGame.anyVert4MatchToMark(Mode.CLEAN, place)) return new GamePosition(place);
                }
            }

            for (alsoUpToFive = 0; alsoUpToFive < 5; alsoUpToFive++) {
                legacyGame.clearMarksByAxisArray();

                if (legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive) && legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 55)) {

                    place = legacyGame.checkForDiagDown4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (legacyGame.anyDiagDown4MatchToMark(Mode.CLEAN, place)) return new GamePosition(place);
                }

                if (legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 50) && legacyGame.gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 5)) {
                    place = legacyGame.checkForDiagUp4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (legacyGame.anyDiagUp4MatchToMark(Mode.CLEAN, place)) return new GamePosition(place);
                }
            }
        }

        return GamePosition.nonePosition();
    }

    public int findSpot(GameBoard gameBoard) {
        int position;
        int i;
        i = GameBoardMark.EMPTY.index;
        do {
            position = (int) (Math.random() * LegacyGame.TOTAL_SQUARES_PER_BOARD);
            if (gameBoard.mainBoard()[position] != GameBoardMark.EMPTY.index) continue;
            if ((position > 0 && gameBoard.mainBoard()[position - 1] != GameBoardMark.EMPTY.index) || (position > GameBoard.SQUARES_PER_SIDE && (gameBoard.mainBoard()[position - 11] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position - GameBoard.SQUARES_PER_SIDE] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position - LegacyGame.oneLessThanCountInRow] != GameBoardMark.EMPTY.index)) || (position < 99 && gameBoard.mainBoard()[position + 1] != GameBoardMark.EMPTY.index) || (position < 88 && (gameBoard.mainBoard()[position + LegacyGame.oneLessThanCountInRow] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position + GameBoard.SQUARES_PER_SIDE] != GameBoardMark.EMPTY.index || gameBoard.mainBoard()[position + 11] != GameBoardMark.EMPTY.index))) i = LegacyGame.OCCUPIED;
        } while (i == GameBoardMark.EMPTY.index);
        return position;
    }
}
