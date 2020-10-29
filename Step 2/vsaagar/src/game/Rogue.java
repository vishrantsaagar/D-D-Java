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
    private ArrayList<Integer> posX;
    private ArrayList<Integer> posY;
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
                if(i == 0)
                {
                    for(int y = posY.get(0); y < posY.get(0) + height; y++)
                    {
                        displayGrid.addObjectToDisplay(new Char('x'), posX.get(0), y + topHeight);
                        displayGrid.addObjectToDisplay(new Char('x'), posX.get(0) + width - 1, y + topHeight);
                    }
                    for(int x = posX.get(0); x < posX.get(0) + width; x++)
                    {
                        displayGrid.addObjectToDisplay(new Char('x'), x, posY.get(0) + topHeight);
                        displayGrid.addObjectToDisplay(new Char('x'), x, posY.get(0) + height - 1 + topHeight);
                    }
                }

                else if(i == 1){
                    if(type == 'T' | type == 'S' | type == 'H'){
                        // System.out.println("Monster: PosX: " + posX.get(0) + ", PosY: " + posY.get(0));
                        displayGrid.addObjectToDisplay(new Char(type), posX.get(0), posY.get(0) + topHeight);
                    } //Monster 

                    else{
                        // System.out.println("Player: PosX: " + posX.get(0) + ", PosY: " + posY.get(0));
                        displayGrid.addObjectToDisplay(new Char('@'), posX.get(0), posY.get(0) + topHeight);
                    } //Player
                }

                else if(i == 2){
                    if(posX.size() != 0 & posY.size() != 0){
                        if(subList.get(j) instanceof Scroll){
                            displayGrid.addObjectToDisplay(new Char('?'), posX.get(0), posY.get(0) + topHeight);
                        }

                        else if(subList.get(j) instanceof Sword){
                            System.out.println(posX.get(0) + "Sword" + posY.get(0));
                            displayGrid.addObjectToDisplay(new Char('|'), posX.get(0), posY.get(0) + topHeight);
                        }

                        else if(subList.get(j) instanceof Armor){
                            displayGrid.addObjectToDisplay(new Char(']'), posX.get(0), posY.get(0) + topHeight);
                        }
                    }
                }

                else if(i == 3){
                    for(int k = 0; k < posX.size(); k++){
                        // System.out.println("------------------------------------------");
                        // System.out.println("PosX: " + posX.get(k) + ", PosY: " + posY.get(k));
                        displayGrid.addObjectToDisplay(new Char('+'), posX.get(k), posY.get(k) + topHeight);

                        int currX = posX.get(k);
                        int currY = posY.get(k) + topHeight;
                        int nextX = -1;
                        int nextY = -1;

                        if(k < posX.size() - 1){
                            nextX = posX.get(k + 1);
                            nextY = posY.get(k + 1) + topHeight;

                            System.out.println("CURR: " + currX + "AND " + currY);

                            System.out.println("NEXT: " + nextX + "AND " + nextY);
                        }


                        int relativeY = nextY - currY;
                        int relativeX = nextX - currX;

                        if(relativeX == 0 & relativeY > 0){
                            for(int step = currY + 1; step < nextY; step++){
                                displayGrid.addObjectToDisplay(new Char('#'), currX, step);
                            }
                        }

                        else if(relativeX == 0 & relativeY < 0){
                            for(int step = currY - 1; step > nextY; step--){
                                displayGrid.addObjectToDisplay(new Char('#'), currX, step);
                            }
                        }

                        else if(relativeX > 0 & relativeY == 0){
                            for(int step = currX + 1; step < nextX; step++){
                                displayGrid.addObjectToDisplay(new Char('#'), step, currY);
                            }
                        }

                        else if(relativeX < 0 & relativeY == 0){
                            for(int step = currX - 1; step > nextX; step--){
                                displayGrid.addObjectToDisplay(new Char('#'), step, currY);
                            }
                        }
                        
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
