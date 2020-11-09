package game;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Stack;

public class KeyStrokePrinter implements InputObserver, Runnable {

    private static int DEBUG = 1;
    private static String CLASSID = "KeyStrokePrinter";
    private static Queue<Character> inputQueue;
    private ObjectDisplayGrid displayGrid;
    private DungeonXMLHandler handler;
    private int posX;
    private int posY;
    private int pmaxhit;
    private int php;
    private Player p1;
    private ArrayList<Item> item_list;
    private Stack<Item> item_stack;
    private Stack<String> item_str_stack;
    private ArrayList<Displayable> rooms;
    private int gameHeight;
    private int topHeight;
    private int bottomHeight;
    private int gameWidth;
    private int displayHeight;
    private String pickedItem;
    private Dungeon dungeon;

    // private int playerPositionX;
    // private int playerPositionY;


    public KeyStrokePrinter(ObjectDisplayGrid grid, Player _p1, DungeonXMLHandler _handler) {
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
        p1 = _p1;
        handler = _handler;

        posX = p1.getstartingX();
        posY = p1.getstartingY();
        php = p1.getHp();
        pmaxhit = p1.getMaxHit();

        dungeon = handler.getDungeon();

        gameHeight = dungeon.get_gameHeight();
        gameWidth = dungeon.get_width();
        topHeight = dungeon.gettopheight();
        bottomHeight = dungeon.getBottomheight();
        displayHeight = gameHeight + topHeight + bottomHeight;
        
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
                    else if(displayGrid.getObjectGrid()[posX][posY-1].peek().getChar()== 'T' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'S' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'H'){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posY = posY - 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                        // System.out.println("PLAYER POSITION X: " + playerPositionX + " ,Y: " + playerPositionY);


                    }
                }

                else if(ch == 'h'){
                    if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == ' '){}
                    else if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar()== 'T' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'S' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'H'){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posX = posX - 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                    }
                }

                else if(ch == 'l'){
                    if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == ' '){}
                    else if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar()== 'T' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'S' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'H'){
                    }
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posX = posX + 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                    }
                }

                else if(ch == 'j'){
                    if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == ' '){}
                    else if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar()== 'T' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'S' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'H'){}
                    else {
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posY = posY + 1;
                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                    }
                }

                else if(ch == 'p'){
                    int room_id = p1.getRoomID();
                    rooms = dungeon.getRooms();

                    for(int i = 1; i <= rooms.size(); i++){
                        if(room_id == i){
                            item_list = ((Room)rooms.get(i - 1)).getItems();
                        }
                    }

                    for(int i = 0; i < item_list.size(); i++){
                        System.out.println(item_list.get(i));
                    }

                    for(int item = 0; item < item_list.size(); item++){
                        if(item_list.get(item).getPosX().size() == 0){
                            item += 1;
                        }

                        int index_pos = item_list.get(item).getPosX().size() - 1;

                        if((item_list.get(item).getPosX().get(index_pos) == posX) & (item_list.get(item).getPosY().get(index_pos) == posY)){
                            if(displayGrid.getObjectGrid()[posX][posY].get(3).getChar() == '|'){
                                p1.setWeapon(item_list.get(item));
                                item_list.get(item).setOwner(p1);

                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                                break;
                            }

                            else if(displayGrid.getObjectGrid()[posX][posY].get(3).getChar() == ']'){
                                p1.setArmor(item_list.get(item));
                                item_list.get(item).setOwner(p1);

                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                                break;

                            }

                            else if(displayGrid.getObjectGrid()[posX][posY].get(3).getChar() == '?'){
                                p1.setScroll(item_list.get(item));
                                item_list.get(item).setOwner(p1);

                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                                break;
                            }
                        }
                    }
                }

                else if(ch == 'd'){
                    int idx = -1;

                    boolean chk = false;

                    while(inputQueue.peek() == null){
                        chk = true;
                    }

                    if(chk == true){
                        idx = Integer.parseInt(String.valueOf(inputQueue.poll()));
                    }

                    item_stack = p1.getItem();
                    item_str_stack = p1.getStrItem();

                    if(item_str_stack.size() == 0){
                        System.out.println("There is nothing in the pack!");
                    }

                    else{
                        Item dropItem = item_stack.get(idx);
                        String dropItem_str = item_str_stack.get(idx);

                        item_stack.remove(idx);
                        item_str_stack.remove(idx);

                        if(dropItem instanceof Sword){
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                            displayGrid.addObjectToDisplay(new Char('|'), posX, posY);
                            displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                            dropItem.SetPosX(posX);
                            dropItem.SetPosY(posY);
                        }

                        else if(dropItem instanceof Armor){
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                            displayGrid.addObjectToDisplay(new Char(']'), posX, posY);
                            displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                            dropItem.SetPosX(posX);
                            dropItem.SetPosY(posY);
                        }

                        else if(dropItem instanceof Scroll){
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                            displayGrid.addObjectToDisplay(new Char('?'), posX, posY);
                            displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                            dropItem.SetPosX(posX);
                            dropItem.SetPosY(posY);
                        }
                    }

                        // if(ch == 'i'){
                        //     for(int i = 6; i <= 30; i++){
                        //         displayGrid.addObjectToDisplay(new Char(' '), i ,displayHeight - bottomHeight - 1);
                        //     }


                        //     int offset = 6;
                        //     for(int i = 0; i < item_str_stack.size(); i++){
                        //         pickedItem = item_str_stack.get(i);
                        //         int add_punc = 0;

                        //         for(int j = 0; j < pickedItem.length(); j++){
                        //             displayGrid.addObjectToDisplay(new Char(pickedItem.charAt(j)), j + offset, displayHeight - bottomHeight - 1);
                        //             add_punc = j + offset;
                        //         }
                                
                        //         // displayGrid.addObjectToDisplay(new Char(','), add_punc + 1, displayHeight - bottomHeight - 1);
                        //         displayGrid.addObjectToDisplay(new Char(' '), add_punc + 2, displayHeight - bottomHeight - 1);

                        //         offset += pickedItem.length() + 1;
                        //     }
                        // }
                    // }
                }

                else if(ch == 'i'){
                    displayGrid.addObjectToDisplay(new Char('P'), 0, displayHeight - bottomHeight - 1);
                    displayGrid.addObjectToDisplay(new Char('a'), 1, displayHeight - bottomHeight - 1);
                    displayGrid.addObjectToDisplay(new Char('c'), 2, displayHeight - bottomHeight - 1);
                    displayGrid.addObjectToDisplay(new Char('k'), 3, displayHeight - bottomHeight - 1);
                    displayGrid.addObjectToDisplay(new Char(':'), 4, displayHeight - bottomHeight - 1);

                    for(int i = 6; i <= gameWidth - 1; i++){
                        displayGrid.addObjectToDisplay(new Char(' '), i, displayHeight - bottomHeight - 1);
                    }

                    item_str_stack = p1.getStrItem();

                    int offset = 6;
                    for(int i = 0; i < item_str_stack.size(); i++){
                        String item = item_str_stack.get(i);
                        int add_space = 0;

                        item = Integer.toString(i) + ": " + item;

                        for(int j = 0; j < item.length(); j++){
                            displayGrid.addObjectToDisplay(new Char(item.charAt(j)), j + offset, displayHeight - bottomHeight - 1);
                            add_space = j + offset;
                        }

                        displayGrid.addObjectToDisplay(new Char(' '), add_space + 2, displayHeight - bottomHeight - 1);

                        offset += item.length() + 1;
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
