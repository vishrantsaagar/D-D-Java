package game;

import game.asciiPanel.AsciiPanel;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ObjectDisplayGrid extends JFrame implements KeyListener, InputSubject{

    private static final String CLASSID = ".ObjectDisplayGrid";

    private static AsciiPanel terminal;
    // private Char[][] objectGrid = null;
    Stack<Char[][]> objectGrid = new Stack<Char[][]>(); //how do you use the two stacks in the game?

    // private List<InputObserver> inputObservers = null;
    Stack<InputObserver> inputObservers = new Stack<InputObserver>();

    private static int gameheight;
    private static int width;

    public ObjectDisplayGrid(int _width, int _height) {
       width = _width;
       gameheight = _height;

       terminal = new AsciiPanel(width, gameheight);

       objectGrid.push(new Integer[][] {{width, gameheight}});

       initializeDisplay();

       super.add(terminal);
       super.setSize(width * 9, gameheight * 16);
       super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // super.repaint();
       // terminal.repaint( );
       super.setVisible(true);
       terminal.setVisible(true);
       super.addKeyListener(this);
       super.repaint();
   }
    
    public static void getObjectDisplayGrid(int gameHeight, int width, int topHeight, int bottomHeight)
    {
        System.out.println("ObjectDisplayGrid: getObjectDisplayGrid" + gameHeight + "\n" + width + "\n" + topHeight + "\n" + bottomHeight);
    }

    public void setTopMessageHeight(int topHeight){
        System.out.println("ObjectDisplayGrid: setTopMessageHeight" + topHeight);
    }

  	public void registerInputObserver(KeyStrokePrinter keyStrokePrinter) {
          //gotta modify a bunch of stuff in here
  	}

    @Override
    public void registerInputObserver(InputObserver observer) {
        inputObservers.push(observer);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());
    }

    private void notifyInputObservers(char ch) {
        for (InputObserver observer : inputObservers) {
            observer.observerUpdate(ch);
        }
    }

    // we have to override, but we don't use this
    @Override
    public void keyPressed(KeyEvent even) {
    }

    // we have to override, but we don't use this
    @Override
    public void keyReleased(KeyEvent e) {
    }


    public final void initializeDisplay() {
        Char ch = new Char('.');
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < gameheight; j++) {
                addObjectToDisplay(ch, i, j);
            }
        }
        terminal.repaint();
    }

    public void fireUp() {
        if (terminal.requestFocusInWindow()) {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow Succeeded");
        } else {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow FAILED");
        }
    }

    public void addObjectToDisplay(Char ch, int x, int y) {
        if ((0 <= x) && (x < objectGrid.size())) {
            if ((0 <= y) && (y < objectGrid.peek().length)) {


              // objectGrid[x][y] = ch; //how to change value in stack
              writeToTerminal(x, y);
            }
        }
    }

    private void writeToTerminal(int x, int y) {
        char ch = objectGrid.pop().getChar();
        terminal.write(ch, x, y);
        terminal.repaint();
    }
}
