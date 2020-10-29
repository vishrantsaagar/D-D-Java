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
    private char type = '@';
    private int HP;
    private int topHeight;

    public char ch;
    private ArrayList<ArrayList<Displayable>> list;
    private ArrayList<Displayable> subList;

    public Rogue(Dungeon dungeon) {
        list = dungeon.getList();
        game_height = dungeon.get_gameHeight();
        game_width = dungeon.get_width();
        displayGrid = new ObjectDisplayGrid(game_width, game_height);
        topHeight = displayGrid.gettopheight();
    }

    @Override
    public void run() { 
        
       displayGrid.fireUp();
        //0 - rooms, 1 - creatues, 2 - items, 3 - passages
       //Monsters = Trolls: T, Snakes: S, Hob: H, 
        //player: @ 
        //rooms: Walls - X and Floor - ., 
        //Passages - #, 
        //Connection between Passage and Room - +,
        //list[0] = rooms
        //list[1] = creature
        //list[2] = items
        //list[3] = passage
        for(int i = 0; i < list.size(); i++){
            subList = list.get(i);
            for(int j = 0; j < subList.size(); j++){
                width = subList.get(j).getWidth();
                height = subList.get(j).getHeight();
                posX = subList.get(j).getPosX();
                posY = subList.get(j).getPosY();
                type = subList.get(j).getType();
                System.out.println(topHeight);
                if(i == 0)
                {
                    for(int y = posY; y < posY + height; y++)
                    {
                        displayGrid.addObjectToDisplay(new Char('x'), posX, y + topHeight);
                        displayGrid.addObjectToDisplay(new Char('x'), posX + width - 1, y + topHeight);
                    }
                    for(int x = posX; x < posX + width; x++)
                    {
                        displayGrid.addObjectToDisplay(new Char('x'), x, posY + topHeight);
                        displayGrid.addObjectToDisplay(new Char('x'), x, posY + height - 1 + topHeight);
                    }
                }
            }
        }

        try {
             Thread.sleep(10000000);
        } catch (InterruptedException e) {
             e.printStackTrace(System.err);
        }

        displayGrid.initializeDisplay();

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
