package exercise.refactoring;

import java.util.HashMap;

public class MarksForChecking {
    HashMap<Directions, HashMap<GameBoardMark, Integer>> marksForChecking = new HashMap<Directions, HashMap<GameBoardMark, Integer>>();

    public MarksForChecking() {
    }

    public void incrementValueAt(Directions direction, GameBoardMark gameBoardMark) {
        Integer original = marksForChecking.get(direction).get(gameBoardMark);
        marksForChecking.get(direction).put(gameBoardMark, original + 1);
    }

    public boolean isLargerThan(int winLength, Directions direction, GameBoardMark gameBoardMark) {
        return marksForChecking.get(direction).get(gameBoardMark) >= winLength;
    }
}