package exercise.refactoring;

public class ComputerMove {
    private static final int lastPositionOnSecondToLastLine = 89;
    private final LegacyGame legacyGame;
    private final MoveSequence moveNumber;
    private final GamePosition nextComputerMove;
    private final GameBoard gameBoard;

    public ComputerMove(LegacyGame legacyGame, RawPlayerMove rawPlayerMove, GameBoard gameBoard) {
        this.legacyGame = legacyGame;
        this.gameBoard = gameBoard;
        this.moveNumber = legacyGame.moveNumber;
        this.nextComputerMove = makeComputerMove(rawPlayerMove);
    }

    private GamePosition makeComputerMove(RawPlayerMove rawPlayerMove) {
        GamePosition position = new GamePosition();

        if (moveNumber.isFirstMove()) {
            return rawPlayerMove.firstComputerMove();
        }

        position = closeGapInSeries(gameBoard);
        if ((moveNumber.isMove(2))) {
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

        position = responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER, legacyGame);
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

        position = responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame);
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

        gameBoard.setBoardTwoToDuplicateOfMainBoard();

        position = legacyGame.checkSeries(GameBoardMark.ZERO_MARK_FOR_COMPUTER, 0);
        if ((moveNumber.isOver(3) && position.isNotNone())) {
            return position;
        }

        if (moveNumber.isOver(3)) {
            position = legacyGame.checkSeries(GameBoardMark.X_MARK_FOR_PLAYER, 0);
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
            position = closeGapInSeries(gameBoard);
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

        position.setPosition(findSpot(gameBoard));
        return position;
    }

    private GamePosition createTwoAxesOrCreateOneAndBlockAnother(LegacyGame legacyGame) {
        legacyGame.seto4cc(GameBoardMark.X_MARK_FOR_PLAYER);

        for (int position = GameBoard.oneMoreThanSquaresPerSide; position < lastPositionOnSecondToLastLine; position++)
            if (positionIsOccupiedOnStagingBoardAndDesirable(gameBoard, gameBoard.stagingBoard, position)) {
                return new GamePosition(position);
            }
        return GamePosition.nonePosition();
    }

    private boolean positionIsOccupiedOnStagingBoardAndDesirable(GameBoard gameBoard, int[] stagingBoard, int position) {
        return stagingBoard[position] == LegacyGame.occupiedflag.index && gameBoard.positionIsDesirableForCreateTwoAxesOrCreateOneAndBlockAnother(position);
    }

    private GamePosition closeGapInSeries(GameBoard gameBoard) {
        int upToSeven, upToNine, position, otherPosition;

        for (upToSeven = 1; upToSeven < 7; upToSeven++) {
            for (upToNine = 1; upToNine < GameBoard.oneLessThanCountInRow; upToNine++) {
                position = upToSeven + GameBoard.SQUARES_PER_SIDE * upToNine;
                otherPosition = upToNine + upToSeven * GameBoard.SQUARES_PER_SIDE;

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
                gameBoard.resetAllMarksAlongAxesForFirstHalfOfBoard(legacyGame);

                position = legacyGame.checkFor5AlongHorizAxis(playerMark, zero, upToSquaresPerSide, upToSix, position);

                if (legacyGame.marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 4), new MarksByAxisPositionPair(1, 1))) {
                    return position;
                }

                position = legacyGame.checkFor5AlongVertAxis(playerMark, zero, upToSquaresPerSide, upToSix, position);

                if (legacyGame.marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 4), new MarksByAxisPositionPair(3, 1))) {
                    return position;
                }
            }

            for (upToSquaresPerSide = 0; upToSquaresPerSide < 6; upToSquaresPerSide++) {
                gameBoard.resetAllMarksAlongAxesForFirstHalfOfBoard(legacyGame);

                for (upToFive = 0; upToFive < 5; upToFive++) {
                    position = legacyGame.checkFor5AlongDiagDownRightAxis(playerMark, zero, upToSquaresPerSide, upToFive, upToSix, position);
                    position2 = legacyGame.checkFor5AlongDiagUpRightAxis(playerMark, zero, upToSquaresPerSide, upToFive, upToSix, position2);
                }

                if (legacyGame.marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(0, 4), new MarksByAxisPositionPair(1, 1))) {
                    return position;
                }

                if (legacyGame.marksByAxis.valueAtPositionPairsMatch(new MarksByAxisPositionPair(2, 4), new MarksByAxisPositionPair(3, 1))) {
                    return position2;
                }
            }
        }
        return GamePosition.nonePosition();
    }

    private GamePosition responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark playerMark, LegacyGame legacyGame) {
        int j, k, l;
        GamePosition place = new GamePosition();
        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (k = 0; k < 4; k++) {
            tempRowForChecks[k] = 0;
        }

        for (l = 0; l < 5; l++) {
            for (j = 0; j < GameBoard.SQUARES_PER_SIDE; j++) {
                legacyGame.clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(j * GameBoard.SQUARES_PER_SIDE + l) && gameBoard.hasEmptyValueOnMainBoardAt(j * GameBoard.SQUARES_PER_SIDE + l + 5)) {

                    place = legacyGame.checkForHoriz4InRow(playerMark, 0, j, l);
                    if (legacyGame.anyHoriz4MatchToMark(Mode.CHECK, place)) return place;
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j) && gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 50)) {

                    place = legacyGame.checkForVert4InRow(playerMark, 0, j, l);
                    if (legacyGame.anyVert4MatchToMark(Mode.CHECK, place)) return place;
                }
            }

            for (j = 0; j < 5; j++) {
                legacyGame.clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j) && gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 55)) {

                    place = legacyGame.checkForDiagDown4InRow(playerMark, 0, j, l);
                    if (legacyGame.anyDiagDown4MatchToMark(Mode.CHECK, place)) return place;
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 50) && gameBoard.hasEmptyValueOnMainBoardAt(l * GameBoard.SQUARES_PER_SIDE + j + 5)) {
                    place = legacyGame.checkForDiagUp4InRow(playerMark, 0, j, l);
                    if (legacyGame.anyDiagUp4MatchToMark(Mode.CHECK, place)) return place;
                }
            }
        }

        if (Mode.CHECK.equals(Mode.COUNT)) {
            return new GamePosition(tempRowForChecks[0] + tempRowForChecks[1] + tempRowForChecks[2] + tempRowForChecks[3]);
        }

        return GamePosition.nonePosition();
    }

    private int tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark playerMark, LegacyGame legacyGame) {
        legacyGame.copyBoardToCheck(0);

        for (int k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            if (gameBoard.hasEmptyValueAt(1, k)) {
                gameBoard.setValueAt(1, k, playerMark);

                if (legacyGame.countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, Mode.CLEAN.rawMode) > 1) return k;

                gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (GameBoard.oneMoreThanLastPositionOnBoard);
    }

    private int tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark playerMark, LegacyGame legacyGame) {
        int k;
        int gameBoardLevelToCheck = GameBoard.mainBoardIndex;

        for (k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++)
            gameBoard.setValueAt(1, k, gameBoard.getValueAt(gameBoardLevelToCheck, k));
        for (k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            if (gameBoard.hasEmptyValueAt(1, k)) {
                gameBoard.setValueAt(1, k, playerMark);
                if (legacyGame.responseTo3Or4InaRowOpportunity(playerMark, 1, Mode.CHECK).isNotNone() && legacyGame.countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1, Mode.SAFE.rawMode) > 0) {
                    return k;
                }
                gameBoard.setPositionToEmpty(1, k);
            }
        }
        return (GameBoard.oneMoreThanLastPositionOnBoard);
    }

    private int check2o3c(GameBoardMark playerMark, LegacyGame legacyGame) {
        int k;

        for (k = 0; k < GameBoard.TOTAL_SQUARES_PER_BOARD; k++) {
            if (!gameBoard.hasEmptyValueOnMainBoardAt(k)) continue;

            gameBoard.setValueAt(GameBoard.mainBoardIndex, k, playerMark);

            if (legacyGame.responseTo3Or4InaRowOpportunity(playerMark, GameBoard.mainBoardIndex, Mode.COUNT).isOverOne()) {
                return k;
            }

            gameBoard.setValueAt(GameBoard.mainBoardIndex, k, GameBoardMark.EMPTY);
        }
        return GameBoard.oneMoreThanLastPositionOnBoard;
    }

    private int checkCross(GameBoardMark playerMark, LegacyGame legacyGame) {
        int k, l, x;

        for (k = 1; k < 7; k++) {
            for (l = 1; l < 7; l++) {
                x = k + 10 * l;
                if (gameBoard.mainBoard()[x] == playerMark.index && gameBoard.mainBoard()[x + 2] == playerMark.index && gameBoard.mainBoard()[x + 20] == playerMark.index && gameBoard.mainBoard()[x + 22] == playerMark.index && gameBoard.mainBoard()[x + 11] == 0) return (x + 11);
            }
        }
        return GameBoard.oneMoreThanLastPositionOnBoard;
    }

    private int checkBox(GameBoardMark playerMark, LegacyGame legacyGame) {
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

    private GamePosition responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark playerMark, LegacyGame legacyGame) {
        int alsoUpToFive, upToFour, upToFive;
        GamePosition place = new GamePosition();

        int tempRowForChecks[] = new int[GameBoard.SQUARES_PER_SIDE];
        for (upToFour = 0; upToFour < 4; upToFour++) {
            tempRowForChecks[upToFour] = 0;
        }

        for (upToFive = 0; upToFive < 5; upToFive++) {
            for (alsoUpToFive = 0; alsoUpToFive < GameBoard.SQUARES_PER_SIDE; alsoUpToFive++) {
                legacyGame.clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(alsoUpToFive * GameBoard.SQUARES_PER_SIDE + upToFive) && gameBoard.hasEmptyValueOnMainBoardAt(alsoUpToFive * GameBoard.SQUARES_PER_SIDE + upToFive + 5)) {

                    place = legacyGame.checkForHoriz4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (legacyGame.anyHoriz4MatchToMark(Mode.CLEAN, place)) return place;
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 50)) {

                    place = legacyGame.checkForVert4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (legacyGame.anyVert4MatchToMark(Mode.CLEAN, place)) return place;
                }
            }

            for (alsoUpToFive = 0; alsoUpToFive < 5; alsoUpToFive++) {
                legacyGame.clearMarksByAxisArray();

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 55)) {

                    place = legacyGame.checkForDiagDown4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (legacyGame.anyDiagDown4MatchToMark(Mode.CLEAN, place)) return place;
                }

                if (gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 50) && gameBoard.hasEmptyValueOnMainBoardAt(upToFive * GameBoard.SQUARES_PER_SIDE + alsoUpToFive + 5)) {
                    place = legacyGame.checkForDiagUp4InRow(playerMark, 0, alsoUpToFive, upToFive);
                    if (legacyGame.anyDiagUp4MatchToMark(Mode.CLEAN, place)) return place;
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
                i = LegacyGame.occupiedflag.index;
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
}
