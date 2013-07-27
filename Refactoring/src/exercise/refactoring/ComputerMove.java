package exercise.refactoring;

public class ComputerMove {
    private final LegacyGame legacyGame;

    public ComputerMove(LegacyGame legacyGame) {
        this.legacyGame = legacyGame;
    }

    public int makeComputerMove(int x, int y, boolean reporting) {
        int position = 0;

        if (legacyGame.getMoveNumber() == 1)
            return makeArbitraryFirstComputerMoveBasedOnPlayerY(x, y);

        position = legacyGame.closeGapInSeries();
        if ((legacyGame.getMoveNumber() == 2) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out.println("closeGapInSeries() found " + position);
            return position;
        }

        if (legacyGame.getMoveNumber() > 3) {
            position = legacyGame.blockSeriesOfFourOrMore(LegacyGame.ZERO_MARK_FOR_COMPUTER, 0,
                    LegacyGame.CHECK_MODE);
            if (position != LegacyGame.NONE) {
                if (reporting)
                    System.out.println("blockSeriesOfFourOrMore() found "
                            + position);
                return position;
            }

            position = legacyGame.blockSeriesOfFourOrMore(LegacyGame.X_MARK_FOR_PLAYER, 0, LegacyGame.CHECK_MODE);
            if (position != LegacyGame.NONE) {
                if (reporting)
                    System.out.println("blockSeriesOfFourOrMore() found "
                            + position);
                return position;
            }
        }

        position = legacyGame.responseTo3Or4InaRowOpportunity(LegacyGame.ZERO_MARK_FOR_COMPUTER, 0,
                LegacyGame.CHECK_MODE);
        if ((legacyGame.getMoveNumber() > 2) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out.println("responseTo3Or4InaRowOpportunity() found "
                        + position);
            return position;
        }

        position = legacyGame.tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(
                LegacyGame.ZERO_MARK_FOR_COMPUTER, 0);
        if ((legacyGame.getMoveNumber() > 5) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out
                        .println("tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes() found "
                                + position);
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(LegacyGame.X_MARK_FOR_PLAYER);

        position = legacyGame.tryToMake3WithGap_FromVert4IntersectingWithHoriz4(
                LegacyGame.ZERO_MARK_FOR_COMPUTER, 0);
        if ((legacyGame.getMoveNumber() > 4) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out
                        .println("tryToMake3WithGap_FromVert4IntersectingWithHoriz4() found "
                                + position);
            return position;
        }

        position = legacyGame.responseTo3Or4InaRowOpportunity(LegacyGame.X_MARK_FOR_PLAYER, 0,
                LegacyGame.CHECK_MODE);
        if ((legacyGame.getMoveNumber() > 2) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out.println("responseTo3Or4InaRowOpportunity() found "
                        + position);
            return position;
        }

        position = legacyGame.tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(
                LegacyGame.X_MARK_FOR_PLAYER, 0);
        if ((legacyGame.getMoveNumber() > 5) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out
                        .println("tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes() found "
                                + position);
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(LegacyGame.ZERO_MARK_FOR_COMPUTER);

        position = legacyGame.tryToMake3WithGap_FromVert4IntersectingWithHoriz4(
                LegacyGame.X_MARK_FOR_PLAYER, 0);
        if ((legacyGame.getMoveNumber() > 4) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out
                        .println("tryToMake3WithGap_FromVert4IntersectingWithHoriz4() found "
                                + position);
            return position;
        }

        copyBoardZeroToBoardTwo();

        position = legacyGame.checkSeries(LegacyGame.ZERO_MARK_FOR_COMPUTER, 0);
        if ((legacyGame.getMoveNumber() > 3) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out.println("checkSeries() found " + position);
            return position;
        }

        if (legacyGame.getMoveNumber() > 3
                && (position = legacyGame.checkSeries(LegacyGame.X_MARK_FOR_PLAYER, 0)) != LegacyGame.NONE) {
            if (reporting)
                System.out.println("checkSeries() found " + position);
            return position;
        }

        position = legacyGame.check2o3c(LegacyGame.ZERO_MARK_FOR_COMPUTER, 0);
        if ((legacyGame.getMoveNumber() > 2) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out.println("check2o3c() found " + position);
            return position;
        }

        position = legacyGame.check2o3c(LegacyGame.X_MARK_FOR_PLAYER, 0);
        if ((legacyGame.getMoveNumber() > 20) && (position != LegacyGame.NONE)) {
            if (reporting)
                System.out.println("check2o3c() found " + position);
            return position;
        }

        if (legacyGame.getMoveNumber() > 3
                && (position = legacyGame.checkCross(LegacyGame.X_MARK_FOR_PLAYER)) != LegacyGame.NONE) {
            if (reporting)
                System.out.println("checkCross() found " + position);
            return position;
        }

        if (legacyGame.getMoveNumber() > 3
                && (position = legacyGame.checkCross(LegacyGame.ZERO_MARK_FOR_COMPUTER)) != LegacyGame.NONE) {
            if (reporting)
                System.out.println("checkCross() found " + position);
            return position;
        }

        if (legacyGame.getMoveNumber() > 2 && (position = legacyGame.checkBox(LegacyGame.X_MARK_FOR_PLAYER)) != LegacyGame.NONE) {
            if (reporting)
                System.out.println("checkBox() found " + position);
            return position;
        }

        if (legacyGame.getMoveNumber() > 2 && (position = legacyGame.closeGapInSeries()) != LegacyGame.NONE) {
            if (reporting)
                System.out.println("closeGapInSeries() found " + position);
            return position;
        }

        position = createTwoAxesOrCreateOneAndBlockAnother(legacyGame);
        if (position != LegacyGame.NONE) {
            if (reporting)
                System.out
                        .println("createTwoAxesOrCreateOneAndBlockAnother() found "
                                + position);
            return position;
        }

        if ((position = legacyGame.responseTo3Or4InaRowOpportunity(LegacyGame.ZERO_MARK_FOR_COMPUTER,
                0, LegacyGame.CLEAN_MODE)) != LegacyGame.NONE) {
            if (reporting)
                System.out.println("responseTo3Or4InaRowOpportunity() found "
                        + position);
            return position;
        }

        if ((position = legacyGame.blockSeriesOfFourOrMore(LegacyGame.X_MARK_FOR_PLAYER, 0,
                LegacyGame.CLEAN_MODE)) != LegacyGame.NONE) {
            if (reporting)
                System.out.println("blockEitherEndOfSeriesOf4OrMore() found "
                        + position);
            return position;
        }

        position = legacyGame.findSpot();
        if (reporting)
            System.out.println("findGoodSpotNearOpponent() found " + position);
        return position;
    }

    private int makeArbitraryFirstComputerMoveBasedOnPlayerY(int x, int y) {
        if (y > 5)
            return (y * LegacyGame.SQUARES_PER_SIDE + x - 11);
        else
            return (y * LegacyGame.SQUARES_PER_SIDE + x + 11);
    }


    private void copyBoardZeroToBoardTwo() {
        for (int i = 0; i < LegacyGame.TOTAL_SQUARES_PER_BOARD; i++)
            legacyGame.gameBoard.gameBoard[2][i] = legacyGame.gameBoard.gameBoard[0][i];
    }

    private int createTwoAxesOrCreateOneAndBlockAnother(LegacyGame legacyGame) {
        int i;
        legacyGame.seto4cc(LegacyGame.X_MARK_FOR_PLAYER);
        for (i = 11; i < 89; i++)
            if (legacyGame.stagingBoard[i] == LegacyGame.OCCUPIED
                    && (legacyGame.gameBoard.gameBoard[0][i - 11] == LegacyGame.X_MARK_FOR_PLAYER
                            || legacyGame.gameBoard.gameBoard[0][i - LegacyGame.SQUARES_PER_SIDE] == LegacyGame.X_MARK_FOR_PLAYER
                            || legacyGame.gameBoard.gameBoard[0][i - 9] == LegacyGame.X_MARK_FOR_PLAYER
                            || legacyGame.gameBoard.gameBoard[0][i - 1] == LegacyGame.X_MARK_FOR_PLAYER
                            || legacyGame.gameBoard.gameBoard[0][i + 1] == LegacyGame.X_MARK_FOR_PLAYER
                            || legacyGame.gameBoard.gameBoard[0][i + 9] == LegacyGame.X_MARK_FOR_PLAYER
                            || legacyGame.gameBoard.gameBoard[0][i + LegacyGame.SQUARES_PER_SIDE] == LegacyGame.X_MARK_FOR_PLAYER || legacyGame.gameBoard.gameBoard[0][i + 11] == LegacyGame.X_MARK_FOR_PLAYER)) {

                return i;
            }
        return LegacyGame.NONE;
    }
}