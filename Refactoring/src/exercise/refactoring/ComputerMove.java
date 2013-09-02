package exercise.refactoring;

public class ComputerMove {
    public static final int eightyNine = 89;
    private final LegacyGame legacyGame;

    public ComputerMove(LegacyGame legacyGame) {
        this.legacyGame = legacyGame;
    }

    public GamePosition makeComputerMove(RawPlayerMove rawPlayerMove) {
        GamePosition position = new GamePosition();

        if (legacyGame.isFirstMove()) {
            return rawPlayerMove.firstComputerMove();
        }

        position.setPosition(legacyGame.closeGapInSeries());
        if ((legacyGame.moveNumberIs(2))) {
            return position;
        }

        if (legacyGame.moveNumberIsOver(3)) {
            position = legacyGame.blockSeriesOfFourOrMoreInCheckMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER);
            if (position.isNotNone()) {
                return position;
            }

            position = legacyGame.blockSeriesOfFourOrMoreInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER);
            if (position.isNotNone()) {
                return position;
            }
        }

        position.setPosition(legacyGame.responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((legacyGame.moveNumberIsOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(legacyGame.tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((legacyGame.moveNumberIsOver(5) && position.isNotNone())) {
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(GameBoardMark.X_MARK_FOR_PLAYER);

        position.setPosition(legacyGame.tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((legacyGame.moveNumberIsOver(4) && position.isNotNone())) {
            return position;
        }

        position.setPosition(legacyGame.responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((legacyGame.moveNumberIsOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(legacyGame.tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((legacyGame.moveNumberIsOver(5) && position.isNotNone())) {
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(GameBoardMark.ZERO_MARK_FOR_COMPUTER);

        position.setPosition(legacyGame.tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((legacyGame.moveNumberIsOver(4) && position.isNotNone())) {
            return position;
        }

        legacyGame.gameBoard.setBoardTwoToDuplicateOfMainBoard();

        position.setPosition(legacyGame.checkSeries(GameBoardMark.ZERO_MARK_FOR_COMPUTER, 0));
        if ((legacyGame.moveNumberIsOver(3) && position.isNotNone())) {
            return position;
        }

        if (legacyGame.moveNumberIsOver(3)) {
            position.setPosition(legacyGame.checkSeries(GameBoardMark.X_MARK_FOR_PLAYER, 0));
            if (position.isNotNone()) {
                return position;
            }
        }

        position.setPosition(legacyGame.check2o3c(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((legacyGame.moveNumberIsOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(legacyGame.check2o3c(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((legacyGame.moveNumberIsOver(20) && position.isNotNone())) {
            return position;
        }

        if (legacyGame.moveNumberIsOver(3)) {
            position.setPosition(legacyGame.checkCross(GameBoardMark.X_MARK_FOR_PLAYER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (legacyGame.moveNumberIsOver(3)) {
            position.setPosition(legacyGame.checkCross(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (legacyGame.moveNumberIsOver(2)) {
            position.setPosition(legacyGame.checkBox(GameBoardMark.X_MARK_FOR_PLAYER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (legacyGame.moveNumberIsOver(2)) {
            position.setPosition(legacyGame.closeGapInSeries());
            if (position.isNotNone()) {
                return position;
            }
        }

        position = createTwoAxesOrCreateOneAndBlockAnother(legacyGame);
        if (position.isNotNone()) {
            return position;
        }

        position = legacyGame.responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER);
        if (position.isNotNone()) {
            return position;
        }

        position = legacyGame.responseTo3Or4InaRowOpportunityOnMainBoardInCleanMode(GameBoardMark.X_MARK_FOR_PLAYER);
        if (position.isNotNone()) {
            return position;
        }

        position.setPosition(legacyGame.findSpot());
        return position;
    }

    private GamePosition createTwoAxesOrCreateOneAndBlockAnother(LegacyGame legacyGame) {
        legacyGame.seto4cc(GameBoardMark.X_MARK_FOR_PLAYER);

        for (int position = GameBoard.oneMoreThanSquaresPerSide; position < eightyNine; position++)
            if (positionIsOccupiedOnStagingBoardAndDesirable(legacyGame.gameBoard, legacyGame.stagingBoard, position)) {
                return new GamePosition(position);
            }
        return GamePosition.nonePosition();
    }

    private boolean positionIsOccupiedOnStagingBoardAndDesirable(GameBoard gameBoard, int[] stagingBoard, int position) {
        return stagingBoard[position] == LegacyGame.OCCUPIED && gameBoard.positionIsDesirableForCreateTwoAxesOrCreateOneAndBlockAnother(position);
    }
}
