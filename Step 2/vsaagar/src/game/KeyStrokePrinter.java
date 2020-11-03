package game;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;

public class KeyStrokePrinter implements InputObserver, Runnable {

    private static int DEBUG = 1;
    private static String CLASSID = "KeyStrokePrinter";
    private static Queue<Character> inputQueue;
    private ObjectDisplayGrid displayGrid;
    private int posX;
    private int posY;
    private Player p1;
    private DungeonXMLHandler handler;
    private ArrayList<Displayable> rooms;
    private ArrayList<Item> item_list;

    public KeyStrokePrinter(ObjectDisplayGrid grid, Player _p1, DungeonXMLHandler _handler) {
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
        p1 = _p1;
        handler = _handler;

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
                else if(ch == 'k'){
                    if(displayGrid.getObjectGrid()[posX][posY-1].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX][posY-1].peek().getChar() == ' '){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posY = posY - 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 'h'){
                    if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == ' '){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posX = posX - 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 'l'){
                    if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == ' '){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posX = posX + 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 'j'){
                    if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == ' '){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posY = posY + 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 'p'){
                    int room_id = p1.getRoomID();
                    Dungeon dungeon = handler.getDungeon();
                    rooms = dungeon.getRooms();

                    for(int i = 1; i <= rooms.size(); i++){
                        if(room_id == i){
                            item_list = ((Room)rooms.get(i - 1)).getItems();
                        }
                    }

                    System.out.println("HIIII");
                    for(int i = 0; i < item_list.size(); i++){
                        System.out.println(item_list.get(i));
                    }

                    for(int item = 0; item < item_list.size(); item++){
                        if((item_list.get(item).get_PosX() == posX) & (item_list.get(item).get_PosY() == posY)){
                            if(item_list.get(item) instanceof Sword){
                                p1.setWeapon(item_list.get(item));
                                System.out.println("Player has Weapon: " + item_list.get(item));
                            }

                            else if(item_list.get(item) instanceof Armor){
                                p1.setArmor(item_list.get(item));
                                System.out.println("Player has Armor: " + item_list.get(item));

                            }

                            else if(item_list.get(item) instanceof Scroll){
                                p1.setScroll(item_list.get(item));
                                System.out.println("Player has Scroll: " + item_list.get(item));

                            }
                        }

                        else{
                            System.out.println("There are no items near player");
                        }
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
