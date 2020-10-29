package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

//Notes:To make player moves: 
//Files to mess with: Player, Dungeon, ObjectDisplayGrid, KeyStrokePrinter
//in player we can add like 4 functions to move in 4 directions
//in dungeon we can check if a space is moveable to
//in keyStrokePrinter we can use processInput() function in keystroke printer. Use the if statements to move based on the input.
//Objectdisplaygrid we can first use a stack instead and then use addobjecttodisplay and create new function like remove objectobjecttodisplay to push and pop the character from position to position

public class Rogue implements Runnable {

    public static final int FRAMESPERSECOND = 60;
    public static final int TIMEPERLOOP = 1000000000 / FRAMESPERSECOND;
    private static ObjectDisplayGrid displayGrid = null;
    private Thread keyStrokePrinter;
    private int game_width;
    private int game_height;
    private int width;
    private int height;
    private int room_id;
    private ArrayList<Integer> posX;
    private ArrayList<Integer> posY;
    private char type = '@';
    private int HP;
    private int topHeight;
    private int bottomHeight;

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
                room_id = subList.get(j).getRoomID();

                if(i == 0)
                {
                    int l = 0;
                    for(int y = posY.get(0); y < posY.get(0) + height; y++)
                    {
                        for(l =  posX.get(0) + 1; l < posX.get(0) + width - 1; l++)
                        {
                            displayGrid.addObjectToDisplay(new Char('.'), l, y + topHeight);
                        }

                        displayGrid.addObjectToDisplay(new Char('X'), posX.get(0), y + topHeight);
                        displayGrid.addObjectToDisplay(new Char('X'), posX.get(0) + width - 1, y + topHeight);
                    }
                    for(int x = posX.get(0); x < posX.get(0) + width; x++)
                    {
                        displayGrid.addObjectToDisplay(new Char('X'), x, posY.get(0) + topHeight);
                        displayGrid.addObjectToDisplay(new Char('X'), x, posY.get(0) + height - 1 + topHeight);
                    }
                }

                else if(i == 1){

                    int relativeY = -1;
                    int relativeX = -1;

                    for(int id = 1; id <= list.get(0).size(); id++){
                        if(room_id == id){
                            relativeX = list.get(0).get(id - 1).getPosX().get(0) + posX.get(0);
                            relativeY = list.get(0).get(id - 1).getPosY().get(0) + posY.get(0);

                        }
                    }

                    if(type == 'T' | type == 'S' | type == 'H'){
                        // System.out.println("Monster: PosX: " + posX.get(0) + ", PosY: " + posY.get(0));
                        //id = subList.get(j).getRoomID()
                        //if(id = 1): list[0].sublist[0].posX and list[0].subList[0].posY

                        displayGrid.addObjectToDisplay(new Char(type), relativeX, relativeY + topHeight);
                    } //Monster 

                    else{
                        // System.out.println("Player: PosX: " + posX.get(0) + ", PosY: " + posY.get(0));
                        displayGrid.addObjectToDisplay(new Char('@'), relativeX, relativeY + topHeight);
                    } //Player
                }

                else if(i == 2){
                    if(posX.size() != 0 & posY.size() != 0){

                        int relativeY = -1;
                        int relativeX = -1;

                        for(int id = 1; id <= list.get(0).size(); id++){
                            if(room_id == id){
                                relativeX = list.get(0).get(id - 1).getPosX().get(0) + posX.get(0);
                                relativeY = list.get(0).get(id - 1).getPosY().get(0) + posY.get(0);

                            }
                        }

                        if(subList.get(j) instanceof Scroll){
                            displayGrid.addObjectToDisplay(new Char('?'), relativeX, relativeY + topHeight);
                        }

                        else if(subList.get(j) instanceof Sword){
                            displayGrid.addObjectToDisplay(new Char('|'), relativeX, relativeY + topHeight);
                        }

                        else if(subList.get(j) instanceof Armor){
                            displayGrid.addObjectToDisplay(new Char(']'), relativeX, relativeY + topHeight);
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

                            //System.out.println("CURR: " + currX + "AND " + currY);

                            //System.out.println("NEXT: " + nextX + "AND " + nextY);
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
