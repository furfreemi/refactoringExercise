package exercise.refactoring;

public class ComputerMove {
    public static final int oneMoreThanSquaresPerSide = LegacyGame.SQUARES_PER_SIDE + 1;
    private final LegacyGame legacyGame;

    public ComputerMove(LegacyGame legacyGame) {
        this.legacyGame = legacyGame;
    }

    public int makeComputerMove(int x, int y, boolean reporting) {
        int position = 0;

        if (legacyGame.isFirstMove()) {
            return makeArbitraryFirstComputerMoveBasedOnPlayerY(x, y);
        }

        position = legacyGame.closeGapInSeries();
        if ((legacyGame.moveNumberIs(2) && (position != LegacyGame.NONE))) {
            return position;
        }

        if (legacyGame.moveNumberIsOver(3)) {
            position = legacyGame.blockSeriesOfFourOrMore(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index, 0, LegacyGame.CHECK_MODE);
            if (position != LegacyGame.NONE) {
                return position;
            }

            position = legacyGame.blockSeriesOfFourOrMore(GameBoardMark.X_MARK_FOR_PLAYER.index, 0, LegacyGame.CHECK_MODE);
            if (position != LegacyGame.NONE) {
                return position;
            }
        }

        position = legacyGame.responseTo3Or4InaRowOpportunity(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index, 0, LegacyGame.CHECK_MODE);
        if ((legacyGame.getMoveNumber() > 2) && (position != LegacyGame.NONE)) {
            return position;
        }

        position = legacyGame.tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index, 0);
        if ((legacyGame.getMoveNumber() > 5) && (position != LegacyGame.NONE)) {
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(GameBoardMark.X_MARK_FOR_PLAYER.index);

        position = legacyGame.tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index, 0);
        if ((legacyGame.getMoveNumber() > 4) && (position != LegacyGame.NONE)) {
            return position;
        }

        position = legacyGame.responseTo3Or4InaRowOpportunity(GameBoardMark.X_MARK_FOR_PLAYER.index, 0, LegacyGame.CHECK_MODE);
        if ((legacyGame.getMoveNumber() > 2) && (position != LegacyGame.NONE)) {
            return position;
        }

        position = legacyGame.tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.X_MARK_FOR_PLAYER.index, 0);
        if ((legacyGame.getMoveNumber() > 5) && (position != LegacyGame.NONE)) {
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index);

        position = legacyGame.tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.X_MARK_FOR_PLAYER.index, 0);
        if ((legacyGame.getMoveNumber() > 4) && (position != LegacyGame.NONE)) {
            return position;
        }

        copyBoardZeroToBoardTwo();

        position = legacyGame.checkSeries(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index, 0);
        if ((legacyGame.getMoveNumber() > 3) && (position != LegacyGame.NONE)) {
            return position;
        }

        if (legacyGame.getMoveNumber() > 3 && (position = legacyGame.checkSeries(GameBoardMark.X_MARK_FOR_PLAYER.index, 0)) != LegacyGame.NONE) {
            return position;
        }

        position = legacyGame.check2o3c(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index, 0);
        if ((legacyGame.getMoveNumber() > 2) && (position != LegacyGame.NONE)) {
            return position;
        }

        position = legacyGame.check2o3c(GameBoardMark.X_MARK_FOR_PLAYER.index, 0);
        if ((legacyGame.getMoveNumber() > 20) && (position != LegacyGame.NONE)) {
            return position;
        }

        if (legacyGame.getMoveNumber() > 3 && (position = legacyGame.checkCross(GameBoardMark.X_MARK_FOR_PLAYER.index)) != LegacyGame.NONE) {
            return position;
        }

        if (legacyGame.getMoveNumber() > 3 && (position = legacyGame.checkCross(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index)) != LegacyGame.NONE) {
            return position;
        }

        if (legacyGame.getMoveNumber() > 2 && (position = legacyGame.checkBox(GameBoardMark.X_MARK_FOR_PLAYER.index)) != LegacyGame.NONE) {
            return position;
        }

        if (legacyGame.getMoveNumber() > 2 && (position = legacyGame.closeGapInSeries()) != LegacyGame.NONE) {
            return position;
        }

        position = createTwoAxesOrCreateOneAndBlockAnother(legacyGame);
        if (position != LegacyGame.NONE) {
            return position;
        }

        if ((position = legacyGame.responseTo3Or4InaRowOpportunity(GameBoardMark.ZERO_MARK_FOR_COMPUTER.index, 0, LegacyGame.CLEAN_MODE)) != LegacyGame.NONE) {
            return position;
        }

        if ((position = legacyGame.blockSeriesOfFourOrMore(GameBoardMark.X_MARK_FOR_PLAYER.index, 0, LegacyGame.CLEAN_MODE)) != LegacyGame.NONE) {
            if (reporting) {
            }
            return position;
        }

        position = legacyGame.findSpot();
        return position;
    }

    private int makeArbitraryFirstComputerMoveBasedOnPlayerY(int x, int y) {
        if (y > 5) {
            return (y * LegacyGame.SQUARES_PER_SIDE + x - oneMoreThanSquaresPerSide);
        } else return (y * LegacyGame.SQUARES_PER_SIDE + x + oneMoreThanSquaresPerSide);
    }


    private void copyBoardZeroToBoardTwo() {
        for (int i = 0; i < LegacyGame.TOTAL_SQUARES_PER_BOARD; i++)
            legacyGame.gameBoard.setValueAt(2, i, legacyGame.gameBoard.getValueAt(0, i));
    }

    private int createTwoAxesOrCreateOneAndBlockAnother(LegacyGame legacyGame) {
        int i;
        legacyGame.seto4cc(GameBoardMark.X_MARK_FOR_PLAYER.index);
        for (i = oneMoreThanSquaresPerSide; i < 89; i++)
            if (legacyGame.stagingBoard[i] == LegacyGame.OCCUPIED && (isPlayer(legacyGame.gameBoard, i - oneMoreThanSquaresPerSide) || isPlayer(legacyGame.gameBoard, i - LegacyGame.SQUARES_PER_SIDE) || isPlayer(legacyGame.gameBoard, i - 9) || isPlayer(legacyGame.gameBoard, i - 1) || isPlayer(legacyGame.gameBoard, i + 1) || isPlayer(legacyGame.gameBoard, i + 9) || isPlayer(legacyGame.gameBoard, i + LegacyGame.SQUARES_PER_SIDE) || isPlayer(legacyGame.gameBoard, i + oneMoreThanSquaresPerSide))) {

                return i;
            }
        return LegacyGame.NONE;
    }

    private boolean isPlayer(GameBoard gameBoard, int position) {
        return gameBoard.playerXOccupiesMainBoardPosition(position);
    }
}