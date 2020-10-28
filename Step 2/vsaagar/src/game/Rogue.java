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
    private int game_width;
    private int game_height;
    private int width;
    private int height;
    private int posX;
    private int posY;
    private char type;
    private int HP;

    public char ch;
    private ArrayList<Displayable> list = new ArrayList<Displayable>();
    private ArrayList<Displayable> subList;

    public Rogue(Dungeon dungeon) {
        list = dungeon.getList();
        game_height = dungeon.get_gameHeight();
        game_width = dungeon.get_game_width();
        displayGrid = new ObjectDisplayGrid(game_width, game_height);
    }

    @Override
    public void run() { 
        //Monsters = Trolls: T, Snakes: S, Hob: H, 
        //player: @ 
        //rooms: Walls - X and Floor - ., 
        //Passages - #, 
        //Connection between Passage and Room - +,
        //list[0] = rooms
        //list[1] = creature
        //list[2] = items
        //list[3] = passage
        displayGrid.fireUp();
    //     for (int step = 1; step < game_width / 2; step *= 2) {
    //         for (int i = 0; i < game_width; i += step) {
    //             for (int j = 0; j < game_height; j += step) {

    //                 displayGrid.addObjectToDisplay(new Char('X'), i, j);
    //             }
    //         }
        
    //     try {
    //         Thread.sleep(2000);
    //     } catch (InterruptedException e) {
    //         e.printStackTrace(System.err);
    //     }
    //     displayGrid.initializeDisplay();
    // }

        for(int i = 0; i < list.size(); i++){
            subList = list.get(i);

            for(int j = 0; j < subList.size(); i++){
                width = subList.get(j).getWidth();
                height = subList.get(j).getHeight();
                posX = subList.get(j).getPosX();
                posY = subList.get(j).getPosY();
                type = subList.get(j).getType();
            }
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
