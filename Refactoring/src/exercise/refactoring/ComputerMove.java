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

            position = legacyGame.computerMove.blockSeriesOfFourOrMoreInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER, legacyGame);
            if (position.isNotNone()) {
                return position;
            }
        }

        position.setPosition(legacyGame.responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(legacyGame.tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((moveNumber.isOver(5) && position.isNotNone())) {
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(GameBoardMark.X_MARK_FOR_PLAYER);

        position.setPosition(legacyGame.tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((moveNumber.isOver(4) && position.isNotNone())) {
            return position;
        }

        position.setPosition(legacyGame.responseTo3Or4InaRowOpportunityOnMainBoardInCheckMode(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(legacyGame.tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((moveNumber.isOver(5) && position.isNotNone())) {
            return position;
        }

        legacyGame.setFlagsForLaterProcessing(GameBoardMark.ZERO_MARK_FOR_COMPUTER);

        position.setPosition(legacyGame.tryToMake3WithGap_FromVert4IntersectingWithHoriz4(GameBoardMark.X_MARK_FOR_PLAYER));
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

        position.setPosition(legacyGame.check2o3c(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
        if ((moveNumber.isOver(2) && position.isNotNone())) {
            return position;
        }

        position.setPosition(legacyGame.check2o3c(GameBoardMark.X_MARK_FOR_PLAYER));
        if ((moveNumber.isOver(20) && position.isNotNone())) {
            return position;
        }

        if (moveNumber.isOver(3)) {
            position.setPosition(legacyGame.checkCross(GameBoardMark.X_MARK_FOR_PLAYER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(3)) {
            position.setPosition(legacyGame.checkCross(GameBoardMark.ZERO_MARK_FOR_COMPUTER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(2)) {
            position.setPosition(legacyGame.checkBox(GameBoardMark.X_MARK_FOR_PLAYER));
            if (position.isNotNone()) {
                return position;
            }
        }

        if (moveNumber.isOver(2)) {
            position.setPosition(legacyGame.computerMove.closeGapInSeries(legacyGame));
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
}
