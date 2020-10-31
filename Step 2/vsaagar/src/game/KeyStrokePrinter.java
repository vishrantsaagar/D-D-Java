package game;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyStrokePrinter implements InputObserver, Runnable {

    private static int DEBUG = 1;
    private static String CLASSID = "KeyStrokePrinter";
    private static Queue<Character> inputQueue;
    private ObjectDisplayGrid displayGrid;
    private int posX;
    private int posY;
    private Player p1;

    public KeyStrokePrinter(ObjectDisplayGrid grid, Player _p1) {
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
        p1 = _p1;

        posX = p1.getstartingX();
        posY = p1.getstartingY();

        System.out.println("Coordinates:" + posX + "," + posY);
    }

    @Override
    public void observerUpdate(char ch) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".observerUpdate receiving character " + ch);
        }
        inputQueue.add(ch);
    }

    private void rest() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean processInput() {

        char ch;
        boolean processing = true;
        while (processing) {
            if (inputQueue.peek() == null) {
                processing = false;
            } else {
                ch = inputQueue.poll();
                if (ch == 'X') {
                    System.out.println("got an X, ending input checking");
                }
                else if(ch == 'w'){
                    if(displayGrid.getObjectGrid()[posX][posY-1].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX][posY-1].peek().getChar() == 	' '){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posY = posY - 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 'a'){
                    if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 	' '){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posX = posX - 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 'd'){
                    if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == 	' '){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posX = posX + 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 's'){
                    if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == 	' '){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posY = posY + 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }
                
                else {
                    System.out.println("character " + ch + " entered on the keyboard");
                }
            }
        }
        return true;
    }

    @Override
    public void run() {
        displayGrid.registerInputObserver(this);
        boolean working = true;
        while (working) {
            rest();
            working = (processInput( ));
        }
    }
}
