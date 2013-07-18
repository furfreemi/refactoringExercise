public int someWierdMethodName(int playerMark, int x, int type)
{
    int j, k, l;
    int position = 0, position2 = 0;

    for (l = 0; l &lt; 6; l++) {
        for (j = 0; j &lt; SQUARES_PER_SIDE; j++) /* horiz &amp; vert */
        {
            resetAllMarksAlongAxesForFirstHalfOfBoard();

            position = checkFor5AlongHorizAxis(playerMark, x, j, l, position);

            if (marksByAxisByPlayerForChecking[0] == 3 &amp;&amp; marksByAxisByPlayerForChecking[1] == 2) {
                if (type == SETFLAGS_MODE) {
                    tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                    tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                }
                if (type == CLEAN_MODE) return tempRowForChecks[0];
            }

            if (marksByAxisByPlayerForChecking[0] == 4 &amp;&amp; marksByAxisByPlayerForChecking[1] == 1 &amp;&amp; type == CHECK_MODE)
                                    return position;

            position = checkFor5AlongVertAxis(playerMark, x, j, l, position);

            if (marksByAxisByPlayerForChecking[2] == 3 &amp;&amp; marksByAxisByPlayerForChecking[3] == 2) {
                if (type == SETFLAGS_MODE) {
                    tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                    tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                }
                if (type == CLEAN_MODE)
                    return tempRowForChecks[0];
            }
            if (marksByAxisByPlayerForChecking[2] == 4 &amp;&amp; marksByAxisByPlayerForChecking[3] == 1 &amp;&amp; type == CHECK_MODE)
                return position;
        }

        for (j = 0; j &lt; 6; j++) {
            resetAllMarksAlongAxesForFirstHalfOfBoard();

            for (k = 0; k &lt; 5; k++)
            {
                position  = checkFor5AlongDiagDownRightAxis(playerMark, x, j, k, l, position);
                position2 = checkFor5AlongDiagUpRightAxis(playerMark, x, j, k, l, position2);
            }

            if (marksByAxisByPlayerForChecking[0] == 3 &amp;&amp; marksByAxisByPlayerForChecking[1] == 2) {
                if (type == SETFLAGS_MODE) {
                    tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                    tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                }
                if (type == CLEAN_MODE) return tempRowForChecks[0];
            }
            if (marksByAxisByPlayerForChecking[0] == 4 &amp;&amp; marksByAxisByPlayerForChecking[1] == 1 &amp;&amp; type == CHECK_MODE) return position;

            if (marksByAxisByPlayerForChecking[2] == 3 &amp;&amp; marksByAxisByPlayerForChecking[3] == 2) {
                if (type == SETFLAGS_MODE) {
                    tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
                    tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
                }
                if (type == CLEAN_MODE) return tempRowForChecks[0];
            }
            if (marksByAxisByPlayerForChecking[2] == 4 &amp;&amp; marksByAxisByPlayerForChecking[3] == 1 &amp;&amp; type == CHECK_MODE) return position2;
        }
    }
    return (NONE);
}