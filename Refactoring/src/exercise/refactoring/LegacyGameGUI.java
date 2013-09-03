package exercise.refactoring;

import java.applet.Applet;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class LegacyGameGUI extends Applet implements Runnable {
    private static final long serialVersionUID = 4873261510528018302L;

    MediaTracker tracker;
    Thread thread;

    boolean loadedImages = false;
    Image xMark;
    Image oMark;
    Image filledOMark;
    Image emptySquare;
    Image winningImage;
    Image losingImage;
    Image yourTurnImage;
    Image newGameImage;

    private Game game;

    public void init() {

        tracker = new MediaTracker(this);
        loadAllGameGraphics(tracker);
        game = new Game();
        game.resetMainGameBoard(0);
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        thread = null;
    }

    public void run() {
        try // main game engine
        {
            tracker.waitForAll();
            loadedImages = true;
            game.run();
        } catch (InterruptedException e) {
            return;
        }
        repaint();
    }

    public void paint(Graphics g) {
        if (game.moveNumber.isDefault()) {
            setBackground(Color.white);
            if (!loadedImages) {
                g.drawRect(40, 110, 220, 215);
                for (int i = 0; i < 4; i++)
                    g.fillRect(50 + i * 57, 285, 30, 30);
                for (int i = 0; i < 3; i++)
                    g.fillRect(50, 120 + i * 50, 200, 40);
                g.drawString("LOADING IMAGES, PLEASE WAIT...", 20, 20);
                g.drawImage(xMark, 50, 285, this);
                g.drawImage(filledOMark, 107, 285, this);
                g.drawImage(oMark, 164, 285, this);
                g.drawImage(emptySquare, 221, 285, this);
                g.drawImage(winningImage, 50, 220, this);
                g.drawImage(losingImage, 50, 170, this);
                g.drawImage(yourTurnImage, 50, 120, this);
                return;
            }
        } else {
            for (int r = 0; r < GameBoard.SQUARES_PER_SIDE; r++) {
                for (int c = 0; c < GameBoard.SQUARES_PER_SIDE; c++) {
                    if (game.gameBoard.valueOnMainBoardAtPositionMatches(r * GameBoard.SQUARES_PER_SIDE + c, GameBoardMark.X_MARK_FOR_PLAYER.index)) g.drawImage(xMark, c * 30, r * 30 + 40, this);
                    else if (game.gameBoard.valueOnMainBoardAtPositionMatches(r * GameBoard.SQUARES_PER_SIDE + c, GameBoardMark.ZERO_MARK_FOR_COMPUTER.index)) {
                        if (r * GameBoard.SQUARES_PER_SIDE + c == game.mostRecentComputerMove.getRaw()) g.drawImage(filledOMark, c * 30, r * 30 + 40, this);
                        else g.drawImage(oMark, c * 30, r * 30 + 40, this);
                    } else g.drawImage(emptySquare, c * 30, r * 30 + 40, this);
                }
            }
        }

        g.drawImage(newGameImage, 0, 0, this);
        if (game.hasNoWinner()) g.drawImage(yourTurnImage, GameBoard.TOTAL_SQUARES_PER_BOARD, 0, this);
        else if (game.wonByPlayer()) g.drawImage(winningImage, GameBoard.TOTAL_SQUARES_PER_BOARD, 0, this);
        else if (game.wonByComputer()) g.drawImage(losingImage, GameBoard.TOTAL_SQUARES_PER_BOARD, 0, this);
    }

    public boolean mouseUp(Event evt, int x, int y) {
        if (!loadedImages) return true;

        if (y < 40) {
            if (x < GameBoard.TOTAL_SQUARES_PER_BOARD) // new game
            {
                game.run();
                repaint();
            }
            return true;
        }

        x = x / 30;
        y = (y - 40) / 30;

        int playerMove = y * GameBoard.SQUARES_PER_SIDE + x;

        System.out.println("playerMove = " + playerMove);
        System.out.println("player x = " + x);
        System.out.println("player y = " + y);

        if (!game.gameBoard.hasEmptyValueAt(0, playerMove) || game.hasAWinner() || game.moveNumber.isOver(49)) // polje
        {
            return true;
        }
        game.respondToMouseUp(new GamePosition(playerMove), new RawPlayerMove(x, y));

        repaint();
        return true;
    }

    private void loadAllGameGraphics(MediaTracker tracker) {
        String graphicsDirectory = "./graphics/";
        System.out.println(getCodeBase());
        try {
            System.out.println(getCodeBase().toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        xMark = getImage(getCodeBase(), graphicsDirectory + "xMark.jpg");
        oMark = getImage(getCodeBase(), graphicsDirectory + "oMark.jpg");
        filledOMark = getImage(getCodeBase(), graphicsDirectory + "oMarkFilled.jpg");
        emptySquare = getImage(getCodeBase(), graphicsDirectory + "emptySquare.jpg");
        winningImage = getImage(getCodeBase(), graphicsDirectory + "win.jpg");
        losingImage = getImage(getCodeBase(), graphicsDirectory + "lose.jpg");
        yourTurnImage = getImage(getCodeBase(), graphicsDirectory + "yourTurn.jpg");
        newGameImage = getImage(getCodeBase(), graphicsDirectory + "newgame.jpg");

        tracker.addImage(xMark, 0);
        tracker.addImage(oMark, 0);
        tracker.addImage(filledOMark, 0);
        tracker.addImage(emptySquare, 0);
        tracker.addImage(winningImage, 0);
        tracker.addImage(losingImage, 0);
        tracker.addImage(yourTurnImage, 0);
        tracker.addImage(newGameImage, 0);
    }

    public URL getCodeBase(){
        try {
            return new URL("file:/usr/local/tmp/refactoring_graphics/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
