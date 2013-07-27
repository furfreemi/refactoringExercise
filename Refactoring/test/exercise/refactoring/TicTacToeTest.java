package exercise.refactoring;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TicTacToeTest {
    TicTacToe ticTacToe;

    @Before
    public void setUp(){
        ticTacToe = new TicTacToe();
    }
    @Test
    public void shouldReturnFalseWhenTypeAndPlaceAreInvalid(){ // characterization test
        assertThat(ticTacToe.anyDiagDown4MatchToMark(0, 0), is(false));
    }

    @Test
    public void shouldReturnTrueAndModifyTempRowForChecksWhenTypeIsCheckMode(){ // characterization test
        int typeIsCheckMode = 0; // check mode is 0 // in order to get into the if, must be anything other than clean_mode which is 3
        ticTacToe.setValueInMarksByAxisByPlayerForChecking(0, 3); // index 0 is set to 3
        ticTacToe.setValueInMarksByAxisByPlayerForChecking(1, 1); // index 1 is set to 1


        boolean result = ticTacToe.anyDiagDown4MatchToMark(typeIsCheckMode, 0);

        assertThat(result, is(true));
        assertThat(ticTacToe.getTempRowForChecks(2), is(1));
    }

//    make mode an enum?

    @Test
    public void shouldModifyTempTableForChecksWhenTypeIsSetFlagsMode(){ // characterization test
        int typeIsSetFlagsMode = 1; // in order to get into the if, must be anything other than clean_mode which is 3
        ticTacToe.setValueInMarksByAxisByPlayerForChecking(0, 3); // index 0 is set to 3
        ticTacToe.setValueInMarksByAxisByPlayerForChecking(1, 1); // index 1 is set to 1


        boolean result = ticTacToe.anyDiagDown4MatchToMark(typeIsSetFlagsMode, 0);

        assertThat(result, is(false));
        assertThat(ticTacToe.getTempRowForChecks(2), is(1));
    }

    //    type is setFlags mode
//    @Test
//    public void shouldReturnTrueAndModifyTempRowForChecksWhenTypeAndPlaceAreValid(){ // characterization test
//        int typeIsCheckMode = 1; // in order to get into the if, must be anything other than clean_mode which is 3
//        ticTacToe.setValueInMarksByAxisByPlayerForChecking(0, 3); // index 0 is set to 3
//        ticTacToe.setValueInMarksByAxisByPlayerForChecking(1, 1); // index 1 is set to 1
//
//
//        boolean result = ticTacToe.anyDiagDown4MatchToMark(anyTypeOtherThanCleanMode, 0);
//
//        assertThat(result, is(false));
//        assertThat(ticTacToe.getTempRowForChecks(2), is(1));
//    }


}
