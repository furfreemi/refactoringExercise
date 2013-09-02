package exercise.refactoring;

import java.util.HashMap;

public class MarksForChecking {
    HashMap<Directions, HashMap<GameBoardMark, Integer>> marksForChecking = new HashMap<Directions, HashMap<GameBoardMark, Integer>>();

    public MarksForChecking() {
        resetAllValuesToZero();
    }

    public void incrementValueFor(Directions direction, GameBoardMark gameBoardMark) {
        Integer original = marksForChecking.get(direction).get(gameBoardMark);
        marksForChecking.get(direction).put(gameBoardMark, original + 1);
    }

    public void resetAllValuesToZero() {
        for(Directions directions : Directions.values()){
            HashMap<GameBoardMark, Integer> playerMarkToInteger = new HashMap<GameBoardMark, Integer>();
            for(GameBoardMark mark : GameBoardMark.values()){
                playerMarkToInteger.put(mark, 0);
            }
            marksForChecking.put(directions, playerMarkToInteger);
        }
    }

    public int countForDirectionAndPlayerMark(Directions direction, GameBoardMark gameBoardMark) {
        return marksForChecking.get(direction).get(gameBoardMark);
    }
}