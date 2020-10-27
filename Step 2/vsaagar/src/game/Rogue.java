package game;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class Rogue implements Runnable{

    public static final int FRAMESPERSECOND = 60;
    public static final int TIMEPERLOOP = 1000000000 / FRAMESPERSECOND;
    private static ObjectDisplayGrid displayGrid = null;
    private Thread keyStrokePrinter;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    public static Dungeon dungeon;
    public static DungeonXMLHandler handler;
    public char ch;

    public Rogue(int width, int height) {
        displayGrid = new ObjectDisplayGrid(width, height);
    }

    @Override
    public void run() {
        displayGrid.fireUp();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            displayGrid.initializeDisplay();
        }
    
    public static void main(String[] args) {
        
        // check if a filename is passed in.  If not, print a usage message.
        // If it is, open the file
            String fileName = null;
               fileName = "game/xmlfiles/" + "testDrawing.xml"; //../xmlfiles. + args[0]
        
    
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    
            try {
                SAXParser saxParser = saxParserFactory.newSAXParser();
                DungeonXMLHandler handler = new DungeonXMLHandler();
                saxParser.parse(new File(fileName), handler);

                handler.toString();
                Dungeon dungeon =  handler.getDungeon();

            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace(System.out);
            }
        }
}
