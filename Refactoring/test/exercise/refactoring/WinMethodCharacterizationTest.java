package exercise.refactoring;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WinMethodCharacterizationTest {
	
	private LegacyGame game;
	
	@Before
	public void setUp() {
		game = new LegacyGame();
	}

	@Test
	public void computerCanWinWithSixInARowHorizontal() {
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 2, 2, 2, 2, 2, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(2, result);
	}

	@Test
	public void computerCannotWinWithEmptyGapInARowHorizontal() {
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 2, 2, 2, 0, 2, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}
	
	@Test
	public void computerCannotWinWithPlayerGapARowHorizontal() {
		
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 2, 1, 2, 2, 2, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void computerCanWinWithSixInARowVertical() {
		
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(2, result);
	}

	@Test
	public void computerCannotWinWithPlayerGapInARowVertical() {
		
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}
	
	@Test
	public void computerCanWinWithSixInARowDiagonalRight() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 2, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(2, result);
	}

	@Test
	public void computerCannotWinWithEmptyGapInARowDiagonalRight() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void computerCanWinWithSixInARowDiagonalLeft() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
				 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
				 0, 0, 0, 2, 0, 0, 0, 0, 0, 0,
				 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(2, result);
	}

	@Test
	public void computerCannotWinWithEmptyGapInARowDiagonalLeft() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
				 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 2, 0, 0, 0, 0, 0, 0,
				 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void computerCannotWinWithHorizontalWrapAround() {
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 2, 2, 2, 2, 2,
				2, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void computerCannotWinWithHorizontalWrapAround2() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 2, 2, 2, 2,
				 2, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void playerCanWinWithFiveInARowHorizontal() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 1, 1, 1, 1, 1, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(1, result);
	}

	@Test
	public void playerCannotWinWithEmptyGapInARowHorizontal() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 1, 1, 1, 0, 1, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void playerCannotWinWithComputerGapARowHorizontal() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 1, 2, 1, 1, 1, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void playerCanWinWithFiveInARowVertical() {
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(1, result);
	}

	@Test
	public void playerCannotWinWithComputerGapInARowVertical() {
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}
	
	@Test
	public void playerCanWinWithFiveInARowDiagonalRight() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(1, result);
	}

	@Test
	public void playerCannotWinWithEmptyGapInARowDiagonalRight() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void playerCanWinWithFiveInARowDiagonalLeft() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
				 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
				 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(1, result);
	}

	@Test
	public void playrCannotWinWithEmptyGapInARowDiagonalLeft() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
				 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void playerCannotWinWithHorizontalWrapAround() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
				 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}
	
	@Test
	public void computerCannotWinWithFiveInARowHorizontal() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 2, 2, 2, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void computerCannotWinWithFiveInARowVertical() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}
	
	@Test
	public void computerCannotWinWithFiveInARowDiagonalRight() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 2, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void computerCannotWinWithFiveInARowDiagonalLeft() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
				 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
				 0, 0, 0, 2, 0, 0, 0, 0, 0, 0,
				 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}
	
	@Test
	public void playerCannotWinWithFourInARowHorizontal() {
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 1, 1, 1, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void playerCannotWinWithFourInARowVertical() {
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}
	
	@Test
	public void playerCannotWinWithFourInARowDiagonalRight() {
	   game.gameBoard = new int[][]{
		{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void playerCannotWinWithFourInARowDiagonalLeft() {
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
				 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
				 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};
		
		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void computerCannotWinWithDiagonalRightWrapAround(){
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 2, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 2, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}

	@Test
	public void computerCannotWinWithDiagonalLeftWrapAround(){
		game.gameBoard = new int[][]{
		 {
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
				 0, 0, 0, 0, 0, 0, 0, 0, 2, 0,
				 0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
				 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
				 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 },
		 {}, {}};

		int result = game.checkToSeeIfEitherSideHasWon();
		assertEquals(0, result);
	}
}
