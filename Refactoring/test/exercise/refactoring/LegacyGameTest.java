package exercise.refactoring;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LegacyGameTest {
    @Test
    public void shouldMaintainBehaviorOfCountLargerThanWinLength(){
//        int[][] marksForChecking, Player p, int winLength, Directions direction
        assertThat(new LegacyGame().countLargerThanWinLength(fakeMarksForChecking(), new Player(), 5, Directions.VERTICAL), is(true));
    }

//                    comp player none
//    diag right
//    diag left
//    horiz
//    vert

    private int[][] fakeMarksForChecking(){
        int[][] marksForChecking = new int[GameBoardMark.values().length][Directions.values().length];
        for(GameBoardMark mark : GameBoardMark.values()) {
            for(Directions directions : Directions.values()){
                marksForChecking[mark.index][directions.index] = 0;
            }
        }
        return marksForChecking;
    }
}
