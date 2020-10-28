package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class Rogue implements Runnable {

    public static final int FRAMESPERSECOND = 60;
    public static final int TIMEPERLOOP = 1000000000 / FRAMESPERSECOND;
    private static ObjectDisplayGrid displayGrid = null;
    private Thread keyStrokePrinter;
    private static int width;
    private static int height;
    public char ch;
    private ArrayList<Displayable> list = new ArrayList<Displayable>();

    public Rogue(Dungeon dungeon) {
        list = dungeon.getList();
        height = dungeon.get_gameHeight();
        width = dungeon.get_width();
        displayGrid = new ObjectDisplayGrid(width, height);
    }

    @Override
    public void run() { 
        //Monsters = Trolls: T, Snakes: S, Hob: H, 
        //player: @ 
        //rooms: Walls - X and Floor - ., 
        //Passages - #, 
        //Connection between Passage and Room - +,

        displayGrid.fireUp();
        for (int step = 1; step < width / 2; step *= 2) {
            for (int i = 0; i < width; i += step) {
                for (int j = 0; j < height; j += step) {

                    displayGrid.addObjectToDisplay(new Char('X'), i, j);
                }
            }
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
        displayGrid.initializeDisplay();
    }
}
    public static void main(String[] args) throws Exception {

        // check if a filename is passed in. If not, print a usage message.
        // If it is, open the file
        String fileName = null;
        fileName = "game/xmlfiles/" + "testDrawing.xml"; // ../xmlfiles. + args[0]

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DungeonXMLHandler handler = new DungeonXMLHandler();
            saxParser.parse(new File(fileName), handler);

        Dungeon dungeon = handler.getDungeon();
        Rogue rog = new Rogue(dungeon);
        Thread rogue = new Thread(rog);
        rogue.start();

        rog.keyStrokePrinter = new Thread(new KeyStrokePrinter(displayGrid));
        rog.keyStrokePrinter.start();

        rogue.join();
        rog.keyStrokePrinter.join();
    
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
