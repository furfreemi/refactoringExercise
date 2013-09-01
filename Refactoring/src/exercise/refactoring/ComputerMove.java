package exercise.refactoring;

public class ComputerMove {
    public static final int oneMoreThanSquaresPerSide = GameBoard.SQUARES_PER_SIDE + 1;
    private final LegacyGame legacyGame;

    public ComputerMove(LegacyGame legacyGame) {
        this.legacyGame = legacyGame;
    }

    public GamePosition makeComputerMove(int x, int y, boolean reporting) {
        GamePosition position = new GamePosition();

        if (legacyGame.isFirstMove()) {
            return makeArbitraryFirstComputerMoveBasedOnPlayerY(x, y);
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

        copyBoardZeroToBoardTwo();

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

        position.setPosition(createTwoAxesOrCreateOneAndBlockAnother(legacyGame));
        if (position.isNotNone()) {
            return position;
        }

        position.setPosition(legacyGame.responseTo3Or4InaRowOpportunity(GameBoardMark.ZERO_MARK_FOR_COMPUTER, GameBoard.mainBoardIndex, Mode.CLEAN));
        if (position.isNotNone()) {
            return position;
        }

        position.setPosition(legacyGame.blockSeriesOfFourOrMore(GameBoardMark.X_MARK_FOR_PLAYER, GameBoard.mainBoardIndex, Mode.CLEAN));
        if (position.isNotNone()) {
            return position;
        }

        position.setPosition(legacyGame.findSpot());
        return position;
    }

    private GamePosition makeArbitraryFirstComputerMoveBasedOnPlayerY(int x, int y) {
        if (y > 5) {
            return new GamePosition(y * GameBoard.SQUARES_PER_SIDE + x - oneMoreThanSquaresPerSide);
        } else {
            return new GamePosition(y * GameBoard.SQUARES_PER_SIDE + x + oneMoreThanSquaresPerSide);
        }
    }


    private void copyBoardZeroToBoardTwo() {
        for (int i = 0; i < LegacyGame.TOTAL_SQUARES_PER_BOARD; i++)
            legacyGame.gameBoard.setValueAt(2, i, legacyGame.gameBoard.getValueAt(0, i));
    }

    private int createTwoAxesOrCreateOneAndBlockAnother(LegacyGame legacyGame) {
        int i;
        legacyGame.seto4cc(GameBoardMark.X_MARK_FOR_PLAYER);
        for (i = oneMoreThanSquaresPerSide; i < 89; i++)
            if (foo(legacyGame, i)) {
                return i;
            }
        return LegacyGame.NONE;
    }

    private boolean foo(LegacyGame legacyGame, int i) {
        return legacyGame.stagingBoard[i] == LegacyGame.OCCUPIED && (isPlayer(legacyGame.gameBoard, i - oneMoreThanSquaresPerSide) || isPlayer(legacyGame.gameBoard, i - GameBoard.SQUARES_PER_SIDE) || isPlayer(legacyGame.gameBoard, i - oneMoreThanSquaresPerSide) || isPlayer(legacyGame.gameBoard, i - 1) || isPlayer(legacyGame.gameBoard, i + 1) || isPlayer(legacyGame.gameBoard, i + oneMoreThanSquaresPerSide) || isPlayer(legacyGame.gameBoard, i + GameBoard.SQUARES_PER_SIDE) || isPlayer(legacyGame.gameBoard, i + oneMoreThanSquaresPerSide));
    }

    private boolean isPlayer(GameBoard gameBoard, int position) {
        return gameBoard.playerXOccupiesMainBoardPosition(position);
    }
}
