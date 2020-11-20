package game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.lang.model.util.ElementScanner6;

import java.util.Random;
import java.util.Stack;

public class KeyStrokePrinter implements InputObserver, Runnable {

    private static int DEBUG = 1;
    private static String CLASSID = "KeyStrokePrinter";
    private static Queue<Character> inputQueue;
    private ObjectDisplayGrid displayGrid;
    private DungeonXMLHandler handler;
    private int posX;
    private int posY;
    private Player p1;
    private ArrayList<Item> item_list;
    private Stack<Item> item_stack;
    private Stack<String> item_str_stack;
    private ArrayList<Displayable> rooms;
    private ArrayList<Displayable> creatures;
    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    private Item worn_armor;
    private int itemIntAction;
    private char itemCharAction;
    private int gameHeight;
    private int topHeight;
    private int bottomHeight;
    private int gameWidth;
    private int displayHeight;
    private String pickedItem;
    private Dungeon dungeon;
    private int steps = 0;
    private int phpmoves;

    private Stack<Item> takenoffitems = new Stack<Item>(); //to store the items we wear for when we insert it back into the stack
    private Stack<String> takenoffstrings = new Stack<String>(); 

    public KeyStrokePrinter(ObjectDisplayGrid grid, Player _p1, DungeonXMLHandler _handler) {
    
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
        handler = _handler;

        dungeon = handler.getDungeon();

        gameHeight = dungeon.get_gameHeight();
        gameWidth = dungeon.get_width();
        topHeight = dungeon.gettopheight();
        bottomHeight = dungeon.getBottomheight();
        displayHeight = gameHeight + topHeight + bottomHeight;

        creatures = dungeon.getCreatures();
        
        for (int i = 0; i < creatures.size(); i++)
        {
            if (creatures.get(i) instanceof Monster) {
                monsters.add((Monster)creatures.get(i));
            }
            else if(creatures.get(i) instanceof Player){
                p1 = (Player) creatures.get(i);
            }
        }

        phpmoves = p1.getHpMoves();
        System.out.println(p1);
        posX = p1.getstartingX();
        posY = p1.getstartingY(); 
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
                    else if(displayGrid.getObjectGrid()[posX][posY-1].peek().getChar()== 'T' || displayGrid.getObjectGrid()[posX][posY-1].peek().getChar() == 'S' || displayGrid.getObjectGrid()[posX][posY-1].peek().getChar() == 'H'){
                        
                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }

                        ArrayList<Integer> monX = new ArrayList<>();
                        ArrayList<Integer> monY = new ArrayList<>();
                        Monster target = null;

                        for(int ind = 0; ind < monsters.size(); ind++)
                        {
                            monX.add(monsters.get(ind).getstartingX());
                            monY.add(monsters.get(ind).getstartingY());
                        }

                        for(int j = 0; j < monsters.size(); j++)
                        {
                           if(monX.get(j) == posX && monY.get(j) == (posY-1))
                           {
                                target = monsters.get(j);
                           }
                        }

                        int mhp = target.getHp();
                        int mMaxhit = target.getMaxHit();
                        int php = p1.getHp();
                        int pmaxhit = p1.getMaxHit(); 

                        Random random = new Random();
                        int randphit = random.nextInt(pmaxhit + 1); //player
                        int randmhit = random.nextInt(mMaxhit + 1); //monster

                        target.setHp(mhp - randphit);
                        p1.setHp(php - randmhit);

                        int newhp = p1.getHp();

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }
                        
                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }

                        System.out.println("Player HP remaining:" + newhp + "Damage recieved:" + randmhit);

                        System.out.println("Monster HP remaining:" + target.getHp());
                        if(target.getHp() <= 0)
                        {
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY - 1);
                        }

                        String nums = Integer.toString(randphit);
                        int z = 6;
                        
                        for(char h : nums.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  z, displayHeight - 1);
                                z++;
                            }

                        int o = z + 19;

                        String numh = Integer.toString(randmhit);
                        for(char h : numh.toCharArray()) {
                            displayGrid.addObjectToDisplay(new Char(h),  o, displayHeight - 1);
                            o++;
                        }

                        if(p1.getHp() <= 0)
                        {
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                            System.out.println("Game Over!\n\nThanks for playing!");
                            displayGrid.addObjectToDisplay(new Char(' '), z - 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('G'), z, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('A'), z + 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('M'), z + 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('E'), z + 3, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 4, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('O'), z + 5, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('V'), z + 6, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('E'), z + 7, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('R'), z + 8, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('!'), z + 9, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 10, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 11, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 12, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 13, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 14, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 15, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 16, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 17, displayHeight - 1);

                            displayGrid.addObjectToDisplay(new Char(' '), o - 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o - 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 3, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 4, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 5, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 6, displayHeight - 1);

                            displayGrid.addObjectToDisplay(new Char(' '), o + 8, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 9, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 10, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 11, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 12, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 13, displayHeight - 1);

                            return false;

                            }                           

                        displayGrid.addObjectToDisplay(new Char('D'), z + 1, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), z + 2, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('M'), z + 3, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), z + 4, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('G'), z + 5, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), z + 6, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('I'), z + 8, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('N'), z + 9, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('F'), z + 10, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('L'), z + 11, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('I'), z + 12, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('C'), z + 13, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('T'), z + 14, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), z + 15, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('D'), z + 16, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('!'), z + 17, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('D'), o + 1, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 2, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('M'), o + 3, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 4, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('G'), o + 5, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), o + 6, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('T'), o + 8, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 9, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('K'), o + 10, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), o + 11, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('N'), o + 12, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('!'), o + 13, displayHeight - 1);
                    }
                    else {

                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }

                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posY = posY - 1;
                        steps = steps + 1;
                        System.out.println(steps);
                        int php = p1.getHp();

                        if(steps == (phpmoves))
                        {
                            System.out.println("1+");
                            p1.setHp(php + 1);
                            steps = 0;
                        }

                        int newhp = p1.getHp();

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }
                        
                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }

                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }


                }

                else if(ch == 'h'){
                    if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == ' '){}
                    else if(displayGrid.getObjectGrid()[posX-1][posY].peek().getChar()== 'T' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'S' || displayGrid.getObjectGrid()[posX-1][posY].peek().getChar() == 'H'){

                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }

                        ArrayList<Integer> monX = new ArrayList<>();
                        ArrayList<Integer> monY = new ArrayList<>();
                        Monster target = null;

                        for(int ind = 0; ind < monsters.size(); ind++)
                        {
                            monX.add(monsters.get(ind).getstartingX());
                            monY.add(monsters.get(ind).getstartingY());
                        }

                        for(int j = 0; j < monsters.size(); j++)
                        {
                           if(monX.get(j) == posX - 1 && monY.get(j) == (posY))
                           {
                                target = monsters.get(j);
                           }
                        }

                        int mhp = target.getHp();
                        int mMaxhit = target.getMaxHit();
                        int php = p1.getHp();
                        int pmaxhit = p1.getMaxHit();

                        Random random = new Random();
                        int randphit = random.nextInt(pmaxhit + 1); //player
                        int randmhit = random.nextInt(mMaxhit + 1); //monster

                        target.setHp(mhp - randphit);
                        p1.setHp(php - randmhit);
                        
                        int newhp = p1.getHp();

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }
                        
                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }


                        System.out.println("Player HP remaining:" + newhp + "Damage recieved:" + randmhit);

                        //displayGrid.addObjectToDisplay(new Char('D'), z + 1, displayHeight - 1);

                        System.out.println("Monster HP remaining:" + target.getHp());
                        if(target.getHp() <= 0)
                        {
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX - 1, posY);
                        }

                        String nums = Integer.toString(randphit);
                        int z = 6;
                        for(char h : nums.toCharArray()) {
                            displayGrid.addObjectToDisplay(new Char(h),  z, displayHeight - 1);
                            z++;
                        }

                        int o = z + 19;

                        String numh = Integer.toString(randmhit);
                        for(char h : numh.toCharArray()) {
                            displayGrid.addObjectToDisplay(new Char(h),  o, displayHeight - 1);
                            o++;
                        }

                        if(p1.getHp() <= 0)
                        {
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                            System.out.println("Game Over!\n\nThanks for playing!");
                            displayGrid.addObjectToDisplay(new Char(' '), z - 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('G'), z, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('A'), z + 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('M'), z + 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('E'), z + 3, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 4, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('O'), z + 5, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('V'), z + 6, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('E'), z + 7, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('R'), z + 8, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('!'), z + 9, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 10, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 11, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 12, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 13, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 14, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 15, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 16, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 17, displayHeight - 1);

                            displayGrid.addObjectToDisplay(new Char(' '), o - 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o - 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 3, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 4, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 5, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 6, displayHeight - 1);

                            displayGrid.addObjectToDisplay(new Char(' '), o + 8, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 9, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 10, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 11, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 12, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 13, displayHeight - 1);

                            return false;

                            }    

                        displayGrid.addObjectToDisplay(new Char('D'), z + 1, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), z + 2, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('M'), z + 3, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), z + 4, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('G'), z + 5, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), z + 6, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('I'), z + 8, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('N'), z + 9, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('F'), z + 10, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('L'), z + 11, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('I'), z + 12, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('C'), z + 13, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('T'), z + 14, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), z + 15, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('D'), z + 16, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('!'), z + 17, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('D'), o + 1, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 2, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('M'), o + 3, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 4, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('G'), o + 5, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), o + 6, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('T'), o + 8, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 9, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('K'), o + 10, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), o + 11, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('N'), o + 12, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('!'), o + 13, displayHeight - 1);
                    }
                    else {

                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }

                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posX = posX - 1;

                        steps = steps + 1;
                        System.out.println(steps);
                        int php = p1.getHp();

                        if(steps == (phpmoves))
                        {
                            System.out.println("1+");
                            p1.setHp(php + 1);
                            steps = 0;
                        }

                        int newhp = p1.getHp();

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }
                        
                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }

                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 'l'){
                    if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == ' '){}
                    else if(displayGrid.getObjectGrid()[posX+1][posY].peek().getChar()== 'T' || displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == 'S' || displayGrid.getObjectGrid()[posX+1][posY].peek().getChar() == 'H'){

                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }

                        ArrayList<Integer> monX = new ArrayList<>();
                        ArrayList<Integer> monY = new ArrayList<>();
                        Monster target = null;

                        for(int ind = 0; ind < monsters.size(); ind++)
                        {
                            monX.add(monsters.get(ind).getstartingX());
                            monY.add(monsters.get(ind).getstartingY());
                        }

                        for(int j = 0; j < monsters.size(); j++)
                        {
                           if(monX.get(j) == posX + 1 && monY.get(j) == (posY))
                           {
                                target = monsters.get(j);
                           }
                        }

                        int mhp = target.getHp();
                        int mMaxhit = target.getMaxHit();
                        int php = p1.getHp();
                        int pmaxhit = p1.getMaxHit();

                        Random random = new Random();
                        int randphit = random.nextInt(pmaxhit + 1); //player
                        int randmhit = random.nextInt(mMaxhit + 1); //monster

                        target.setHp(mhp - randphit);
                        p1.setHp(php - randmhit);
                        
                        int newhp = p1.getHp();

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }
                        
                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }


                        System.out.println("Player HP remaining:" + newhp + "Damage recieved:" + randmhit);

                        //displayGrid.addObjectToDisplay(new Char('D'), z + 1, displayHeight - 1);

                        System.out.println("Monster HP remaining:" + target.getHp());
                        if(target.getHp() <= 0)
                        {
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX + 1, posY);
                        }

                        String nums = Integer.toString(randphit);
                        int z = 6;
                        for(char h : nums.toCharArray()) {
                            displayGrid.addObjectToDisplay(new Char(h),  z, displayHeight - 1);
                            z++;
                        }

                        int o = z + 19;

                        String numh = Integer.toString(randmhit);
                        for(char h : numh.toCharArray()) {
                            displayGrid.addObjectToDisplay(new Char(h),  o, displayHeight - 1);
                            o++;
                        }

                        if(p1.getHp() <= 0)
                        {
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                            System.out.println("Game Over!\n\nThanks for playing!");
                            displayGrid.addObjectToDisplay(new Char(' '), z - 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('G'), z, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('A'), z + 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('M'), z + 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('E'), z + 3, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 4, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('O'), z + 5, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('V'), z + 6, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('E'), z + 7, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('R'), z + 8, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('!'), z + 9, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 10, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 11, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 12, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 13, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 14, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 15, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 16, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 17, displayHeight - 1);

                            displayGrid.addObjectToDisplay(new Char(' '), o - 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o - 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 3, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 4, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 5, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 6, displayHeight - 1);

                            displayGrid.addObjectToDisplay(new Char(' '), o + 8, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 9, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 10, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 11, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 12, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 13, displayHeight - 1);

                            return false;

                            }   

                        displayGrid.addObjectToDisplay(new Char('D'), z + 1, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), z + 2, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('M'), z + 3, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), z + 4, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('G'), z + 5, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), z + 6, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('I'), z + 8, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('N'), z + 9, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('F'), z + 10, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('L'), z + 11, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('I'), z + 12, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('C'), z + 13, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('T'), z + 14, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), z + 15, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('D'), z + 16, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('!'), z + 17, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('D'), o + 1, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 2, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('M'), o + 3, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 4, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('G'), o + 5, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), o + 6, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('T'), o + 8, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 9, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('K'), o + 10, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), o + 11, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('N'), o + 12, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('!'), o + 13, displayHeight - 1);
                    }
                    else {

                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }

                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posX = posX + 1;

                        steps = steps + 1;
                        int php = p1.getHp();
                        System.out.println(steps);

                        if(steps == (phpmoves))
                        {
                            System.out.println("1+");
                            p1.setHp(php + 1);
                            steps = 0;
                        }

                        int newhp = p1.getHp();

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }
                        
                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }

                        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);
                    }
                }

                else if(ch == 'j'){
                    if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == 'X'){}
                    else if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == ' '){}
                    else if(displayGrid.getObjectGrid()[posX][posY+1].peek().getChar()== 'T' || displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == 'S' || displayGrid.getObjectGrid()[posX][posY+1].peek().getChar() == 'H'){
                        
                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }

                        ArrayList<Integer> monX = new ArrayList<>();
                        ArrayList<Integer> monY = new ArrayList<>();
                        Monster target = null;

                        for(int ind = 0; ind < monsters.size(); ind++)
                        {
                            monX.add(monsters.get(ind).getstartingX());
                            monY.add(monsters.get(ind).getstartingY());
                        }

                        for(int j = 0; j < monsters.size(); j++)
                        {
                           if(monX.get(j) == posX && monY.get(j) == (posY + 1))
                           {
                                target = monsters.get(j);
                           }
                        }

                        int mhp = target.getHp();
                        int mMaxhit = target.getMaxHit();
                        int php = p1.getHp();
                        int pmaxhit = p1.getMaxHit();

                        Random random = new Random();
                        int randphit = random.nextInt(pmaxhit + 1); //player
                        int randmhit = random.nextInt(mMaxhit + 1); //monster

                        target.setHp(mhp - randphit);
                        p1.setHp(php - randmhit);
                        
                        int newhp = p1.getHp();

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }
                        
                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }

                        System.out.println("Player HP remaining:" + newhp + "Damage recieved:" + randmhit);

                        //displayGrid.addObjectToDisplay(new Char('D'), z + 1, displayHeight - 1);

                        System.out.println("Monster HP remaining:" + target.getHp());
                        if(target.getHp() <= 0)
                        {
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY + 1);
                        }

                        String nums = Integer.toString(randphit);
                        int z = 6;
                        for(char h : nums.toCharArray()) {
                            displayGrid.addObjectToDisplay(new Char(h),  z, displayHeight - 1);
                            z++;
                        }

                        int o = z + 19;

                        String numh = Integer.toString(randmhit);
                        for(char h : numh.toCharArray()) {
                            displayGrid.addObjectToDisplay(new Char(h),  o, displayHeight - 1);
                            o++;
                        }

                        if(p1.getHp() <= 0)
                        {
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                            System.out.println("Game Over!\n\nThanks for playing!");
                            displayGrid.addObjectToDisplay(new Char(' '), z - 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('G'), z, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('A'), z + 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('M'), z + 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('E'), z + 3, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 4, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('O'), z + 5, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('V'), z + 6, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('E'), z + 7, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('R'), z + 8, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char('!'), z + 9, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 10, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 11, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 12, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 13, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 14, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 15, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 16, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), z + 17, displayHeight - 1);

                            displayGrid.addObjectToDisplay(new Char(' '), o - 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o - 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 1, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 2, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 3, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 4, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 5, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 6, displayHeight - 1);

                            displayGrid.addObjectToDisplay(new Char(' '), o + 8, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 9, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 10, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 11, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 12, displayHeight - 1);
                            displayGrid.addObjectToDisplay(new Char(' '), o + 13, displayHeight - 1);

                            return false;

                        }                                   

                        displayGrid.addObjectToDisplay(new Char('D'), z + 1, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), z + 2, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('M'), z + 3, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), z + 4, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('G'), z + 5, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), z + 6, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('I'), z + 8, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('N'), z + 9, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('F'), z + 10, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('L'), z + 11, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('I'), z + 12, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('C'), z + 13, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('T'), z + 14, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), z + 15, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('D'), z + 16, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('!'), z + 17, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('D'), o + 1, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 2, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('M'), o + 3, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 4, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('G'), o + 5, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), o + 6, displayHeight - 1);

                        displayGrid.addObjectToDisplay(new Char('T'), o + 8, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('A'), o + 9, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('K'), o + 10, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('E'), o + 11, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('N'), o + 12, displayHeight - 1);
                        displayGrid.addObjectToDisplay(new Char('!'), o + 13, displayHeight - 1);
                    }
                    else {

                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }
                        
                        displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                        posY = posY + 1;

                        steps = steps + 1;
                        System.out.println(steps);
                        int php = p1.getHp();

                        if(steps == (phpmoves))
                        {
                            System.out.println("1+");
                            p1.setHp(php + 1);
                            steps = 0;
                        }

                        int newhp = p1.getHp();

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }
                        
                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }

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

                    for(int i = 0; i < displayGrid.getObjectGrid()[posX][posY].size(); i++){
                        System.out.println("---------------------------------------------------");
                        System.out.println(displayGrid.getObjectGrid()[posX][posY].get(i).getChar());
                        System.out.println("---------------------------------------------------");

                    }

                    for(int item = 0; item < item_list.size(); item++){
                        if(item_list.get(item).getPosX().size() == 0){
                            item += 1;
                        }

                        int index_pos = item_list.get(item).getPosX().size() - 1;

                        if((item_list.get(item).getPosX().get(index_pos) == posX) & (item_list.get(item).getPosY().get(index_pos) == posY)){
                            int index = displayGrid.getObjectGrid()[posX][posY].size() - 2;

                            if(item_list.get(item) instanceof Sword & displayGrid.getObjectGrid()[posX][posY].get(index).getChar() == '|'){
                                p1.setWeapon(item_list.get(item));
                                item_list.get(item).setOwner(p1);

                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                                item_list.get(item).SetPosX(-1);
                                item_list.get(item).SetPosY(-1);

                                break;
                            }

                            else if(item_list.get(item) instanceof Armor & displayGrid.getObjectGrid()[posX][posY].get(index).getChar() == ']'){
                                p1.setArmor(item_list.get(item));
                                item_list.get(item).setOwner(p1);

                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                                item_list.get(item).SetPosX(-1);
                                item_list.get(item).SetPosY(-1);
                                break;

                            }

                            else if(item_list.get(item) instanceof Scroll & displayGrid.getObjectGrid()[posX][posY].get(index).getChar() == '?'){
                                p1.setScroll(item_list.get(item));
                                item_list.get(item).setOwner(p1);

                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                                item_list.get(item).SetPosX(-1);
                                item_list.get(item).SetPosY(-1);
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
                        try{
                            idx = Integer.parseInt(String.valueOf(inputQueue.poll())) - 1;
                        } catch(NumberFormatException e){
                            System.out.println("Valid Id not entered");
                        }
                    }

                    item_stack = p1.getItem();
                    item_str_stack = p1.getStrItem();

                    if(item_str_stack.size() == 0){
                        System.out.println("There is nothing in the pack!");
                        //Display this in INFO
                    }

                    else if(idx >= item_str_stack.size() | idx < 0){
                        System.out.println("No item at id: " + idx);
                        //Display this in INFO
                    }

                    else{
                        Item dropItem = item_stack.get(idx);
                        String dropItem_str = item_str_stack.get(idx);

                        if(dropItem == p1.getWieldSword()){
                            p1.wieldSword(null);
                        }

                        if(dropItem == p1.getWornArmor()){
                            p1.wearArmor(null);
                        }
                        
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
                    item_stack = p1.getItem();

                    int offset = 6;
                    for(int i = 1; i <= item_str_stack.size(); i++){
                        String item = item_str_stack.get(i - 1);
                        Item item_it = item_stack.get(i - 1);
                        int add_space = 0;

                        if(item_it instanceof Sword){
                            item = ((Sword)item_it).getName();

                            if(item_it == p1.getWieldSword()){
                                item += "(w)";
                            }
                        }

                        else if(item_it instanceof Armor){
                            item = ((Armor)item_it).getName();

                            if(item_it == p1.getWornArmor()){
                                item += "(a)";
                            }
                        }

                        else if(item_it instanceof Scroll){
                            item = ((Scroll)item_it).getName();
                        }

                        item = Integer.toString(i) + ": " + item;

                        for(int j = 0; j < item.length(); j++){
                            displayGrid.addObjectToDisplay(new Char(item.charAt(j)), j + offset, displayHeight - bottomHeight - 1);
                            add_space = j + offset;
                        }

                        displayGrid.addObjectToDisplay(new Char(' '), add_space + 2, displayHeight - bottomHeight - 1);

                        offset += item.length() + 1;
                    }
                }

                else if(ch == 'w'){
                    int idx = -1;

                    boolean chk = false;

                    while(inputQueue.peek() == null){
                        chk = true;
                    }

                    if(chk == true){
                        try{
                            idx = Integer.parseInt(String.valueOf(inputQueue.poll())) - 1;
                        } catch(NumberFormatException e){
                            System.out.println("Valid Id not entered");
                        }
                    }

                    item_stack = p1.getItem();
                    item_str_stack = p1.getStrItem();

                    if(item_str_stack.size() == 0){
                        System.out.println("There is nothing in the pack!");

                        String message = "THERE IS NOTHING IN PACK!";
                            int length = message.length();

                            int offset = 6;

                            for(int i = 0; i < 50; i++){
                                displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                            }

                            for(int i = 0; i < length; i++){
                                displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                            }
                    }

                    else if(idx >= item_str_stack.size() | idx < 0){
                        System.out.println("No item at id: " + idx);
                        String message = "NO ITEM AT " + (idx + 1);
                            int length = message.length();

                            int offset = 6;

                            for(int i = 0; i < 50; i++){
                                displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                            }

                            for(int i = 0; i < length; i++){
                                displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                            }
                    }

                    else{
                        if(item_stack.get(idx) instanceof Armor){

                            if(p1.getWornArmor() == null){
                    
                                takenoffitems.add(item_stack.get(idx)); //storage
                                takenoffstrings.add(item_str_stack.get(idx)); //storage
                            
                                p1.wearArmor(item_stack.get(idx));
                                int armorhp = p1.getHp() + item_stack.get(idx).getintvalue();
                                p1.setHp(armorhp);

                                int newhp = p1.getHp();

                                String message = "ARMOR WORN:" + "+" + item_stack.get(idx).getintvalue() + " HP";
                                int length = message.length();

                                for(int i = 0; i < 50; i++){
                                    displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                                }

                                for(int i = 0; i < length; i++){
                                    displayGrid.addObjectToDisplay(new Char(message.charAt(i)), 6 + i, displayHeight - 1);
                                }

                                String num = Integer.toString(newhp);
                                int p = 3;

                                if(num.length() == 1)
                                {
                                    for(char h : num.toCharArray()) {
                                        displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                        p++;
                                    }
                                    displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                                }

                                else if(num.length() == 2)
                                {
                                    for(char h : num.toCharArray()) {
                                        displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                        p++;
                                    }
                                    displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                                }

                                else if(num.length() > 1)
                                {
                                    for(char h : num.toCharArray()) {
                                        displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                        p++;
                                    }
                                }

                                item_stack.remove(idx);
                                item_str_stack.remove(idx);

                            }

                            else {
                                String message = "ARMOR ALREADY WORN";
                                int length = message.length();

                                for(int i = 0; i < 50; i++){
                                    displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                                }

                                for(int i = 0; i < length; i++){
                                    displayGrid.addObjectToDisplay(new Char(message.charAt(i)), 6 + i, displayHeight - 1);
                                }
                            }

                        }

                        else{
                            String message = "THE ITEM SELECTED IS NOT AN ARMOR";
                            int length = message.length();

                            int offset = 6;

                            for(int i = 0; i < 50; i++){
                                displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                            }

                            for(int i = 0; i < length; i++){
                                displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                            }
                        }
                    }

                }

                else if(ch == 'c'){
                    worn_armor = p1.getWornArmor();

                    if(takenoffitems != null)
                    {
                        item_stack.add(takenoffitems.firstElement());
                        item_str_stack.add(takenoffstrings.firstElement());
                        takenoffitems.pop();
                        takenoffstrings.pop();
                    }
                    else
                    {}

                    if(worn_armor == null){

                        String message = "NO ARMOR IS CURRENTLY WORN";
                        int length = message.length();

                        int offset = 6;

                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                        }

                        for(int i = 0; i < length; i++){
                            displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                        }                    
                    
                    }

                    else{

                        int armorhp = p1.getHp() - worn_armor.getintvalue();
                        p1.setHp(armorhp);

                        int newhp = p1.getHp();

                        String message = "ARMOR TAKEN OFF:" + "-" + worn_armor.getintvalue() + " HP";

                        int length = message.length();

                        for(int i = 0; i < 50; i++){
                            displayGrid.addObjectToDisplay(new Char(' '), 6 + i, displayHeight - 1);
                        }

                        for(int i = 0; i < length; i++){
                            displayGrid.addObjectToDisplay(new Char(message.charAt(i)), 6 + i, displayHeight - 1);
                        }

                        String num = Integer.toString(newhp);
                        int p = 3;

                        if(num.length() == 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() == 2)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                        }

                        else if(num.length() > 1)
                        {
                            for(char h : num.toCharArray()) {
                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                p++;
                            }
                        }

                        p1.wearArmor(null);

                        }
                    }

                else if(ch == 'T' || ch == 't'){
                    int idx = -1;

                    boolean chk = false;

                    while(inputQueue.peek() == null){
                        chk = true;
                    }

                    if(chk == true){
                        try{
                            idx = Integer.parseInt(String.valueOf(inputQueue.poll())) - 1;
                        } catch(NumberFormatException e){
                            System.out.println("Valid Id not entered");
                        }
                    }

                    item_stack = p1.getItem();
                    item_str_stack = p1.getStrItem();

                    if(item_str_stack.size() == 0){
                        
                        System.out.println("There is nothing in the pack!");
                        String message = "THERE IS NOTHING IN PACK!";
                            
                        int length = message.length();

                        int offset = 6;
                        for(int f = 0; f < 50; f++){
                            displayGrid.addObjectToDisplay(new Char(' '), offset + f, displayHeight - 1);
                        }
                        for(int j = 0; j < length; j++){
                            displayGrid.addObjectToDisplay(new Char(message.charAt(j)), offset + j, displayHeight - 1);
                        }
                    }

                    else if(idx >= item_str_stack.size() | idx < 0){
                        System.out.println("No item at id: " + idx);
                        String message = "NO ITEM SELECTED";
                        int length = message.length();

                        int offset = 6;

                        for(int j = 0; j < 50; j++){
                            displayGrid.addObjectToDisplay(new Char(' '), offset + j, displayHeight - 1);
                        }

                        for(int f = 0; f < length; f++){
                            displayGrid.addObjectToDisplay(new Char(message.charAt(f)), offset + f, displayHeight - 1);
                        }
                    }

                    else{
                        if(item_stack.get(idx) instanceof Sword){
                            p1.wieldSword(item_stack.get(idx));
                            int damage = item_stack.get(idx).getintvalue();
                            int pmaxhit = p1.getMaxHit();

                            p1.setMaxHit(pmaxhit + damage);
                            System.out.println(damage);

                            String message = "SWORD WEILDED - DAMAGE INCREASE:" + "+" + damage;
                            int length = message.length();
    
                            int offset = 6;
    
                            for(int i = 0; i < 100; i++){
                                displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                            }
    
                            for(int i = 0; i < length; i++){
                                displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                            }
                        }

                        else{
                            String message = "ITEM IS NOT A SWORD";
                            int length = message.length();
    
                            int offset = 6;
    
                            for(int i = 0; i < 100; i++){
                                displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                            }
    
                            for(int i = 0; i < length; i++){
                                displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                            }                                  
                        }
                    }
                }

                else if(ch == 'r'){
                    int idx = -1;

                    boolean chk = false;

                    while(inputQueue.peek() == null){
                        chk = true;
                    }

                    if(chk == true){
                        try{
                            idx = Integer.parseInt(String.valueOf(inputQueue.poll())) - 1;
                        } catch(NumberFormatException e){
                            System.out.println("Valid Id not entered");
                        }
                    }

                    item_stack = p1.getItem();
                    item_str_stack = p1.getStrItem();

                    if(item_str_stack.size() == 0){
                        System.out.println("There is nothing in the pack!");
                    }

                    else if(idx >= item_str_stack.size() | idx < 0){
                        System.out.println("No item at id: " + idx);
                    }

                    else{
                        Item scroll = item_stack.get(idx);

                        if(scroll instanceof Scroll){
                            itemIntAction = scroll.getItemAction().getIntValue();
                            itemCharAction = scroll.getItemAction().getCharValue();

                            if(scroll.getItemAction() instanceof BlessCurseOwner){
                                if(itemCharAction == 'w'){
                                    //Sword
                                    Item sword_wielded = p1.getWieldSword();

                                    if(sword_wielded == null){
                                        for(int i = 0; i < item_stack.size(); i++){
                                            if(item_stack.get(i) instanceof Sword){
                                                sword_wielded = item_stack.get(i);
                                                break;
                                            }
                                        }

                                        String message = "scroll of cursing does nothing because " + ((Sword)sword_wielded).getName() + " not being used";

                                        //Do we use message from pdf file for step 4 & 5 or actionMessage

                                        int offset = 6;
                                        
                                        for(int i = 0; i < 100; i++){
                                            displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                                        }
                
                                        for(int i = 0; i < message.length(); i++){
                                            displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                                        }
                                    }

                                    else{
                                        int swordHpDamage = p1.getHp() + itemIntAction;
                                        p1.setHp(swordHpDamage);

                                        item_stack.remove(idx);
                                        item_str_stack.remove(idx);

                                        int newhp = p1.getHp();

                                        String message = ((Sword)sword_wielded).getName() + " cursed! " + String.valueOf(itemIntAction) + " taken from its effectiveness";
                                        int length = message.length();
                
                                        int offset = 6;
                
                                        for(int i = 0; i < 100; i++){
                                            displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                                        }
                
                                        for(int i = 0; i < length; i++){
                                            displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                                        }

                                        String num = Integer.toString(newhp);
                                        int p = 3;

                                        if(num.length() == 1)
                                        {
                                            for(char h : num.toCharArray()) {
                                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                                p++;
                                            }
                                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                                        }

                                        else if(num.length() == 2)
                                        {
                                            for(char h : num.toCharArray()) {
                                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                                p++;
                                            }
                                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                                        }

                                        else if(num.length() > 1)
                                        {
                                            for(char h : num.toCharArray()) {
                                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                                p++;
                                            }
                                        }

                                    }
                                }

                                else if(itemCharAction == 'a'){
                                    //Armour
                                    worn_armor = p1.getWornArmor();

                                    if(worn_armor == null){
                                        for(int i = 0; i < item_stack.size(); i++){
                                            if(item_stack.get(i) instanceof Armor){
                                                worn_armor = item_stack.get(i);
                                                break;
                                            }
                                        }


                                        String message = "scroll of cursing does nothing because " + ((Armor)worn_armor).getName() + " not being used";

                                        int offset = 6;

                                        for(int i = 0; i < 100; i++){
                                            displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                                        }
                
                                        for(int i = 0; i < message.length(); i++){
                                            displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                                        }
                                    }

                                    else{
                                        int armorHpDamage = p1.getHp() + itemIntAction;
                                        p1.setHp(armorHpDamage);

                                        int newhp = p1.getHp();

                                        String message = ((Armor)worn_armor).getName() + " cursed! " + String.valueOf(itemIntAction) + " taken from its effectiveness";
                                        int length = message.length();
                
                                        int offset = 6;
                
                                        for(int i = 0; i < 100; i++){
                                            displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                                        }
                
                                        for(int i = 0; i < length; i++){
                                            displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                                        }

                                        String num = Integer.toString(newhp);
                                        int p = 3;

                                        if(num.length() == 1)
                                        {
                                            for(char h : num.toCharArray()) {
                                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                                p++;
                                            }
                                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                                        }

                                        else if(num.length() == 2)
                                        {
                                            for(char h : num.toCharArray()) {
                                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                                p++;
                                            }
                                            displayGrid.addObjectToDisplay(new Char(' '),  p, 0);
                                        }

                                        else if(num.length() > 1)
                                        {
                                            for(char h : num.toCharArray()) {
                                                displayGrid.addObjectToDisplay(new Char(h),  p, 0);
                                                p++;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        else{
                            String message = "ITEM SELECTED IS NOT A SCROLL";

                            int offset = 6;

                            for(int i = 0; i < 100; i++){
                                displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                            }
    
                            for(int i = 0; i < message.length(); i++){
                                displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                            }
                        }
                    }
                }

                else if(ch == '?'){
                    //Display all commands in INFO
                    String message = "h, j, k, l, p, d, i, c, w, E, ?, H, r, T";
                    //check this?

                    int offset = 6;

                    for(int i = 0; i < 100; i++){
                        displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                    }

                    for(int i = 0; i < message.length(); i++){
                        displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                    }
                }

                else if(ch == 'H'){
                    boolean chk = false;
                    char instruction = '\0';
                    String message = "";

                    while(inputQueue.peek() == null){
                        chk = true;
                    }

                    if(chk == true){
                        instruction = String.valueOf(inputQueue.poll()).charAt(0);

                        if(instruction == 'h' | instruction == 'j' | instruction == 'k' | instruction == 'l' | instruction == 'p' | instruction == 'i' | instruction == 'd' | instruction == 'c' | instruction == 'w' | instruction == 'E' | instruction == '?' | instruction == 'H' | instruction == 'r' | instruction == 'T'){
                        }

                        else{
                            instruction = '\0';
                        }
                    }

                    switch(instruction){
                        case 'h': 
                            message = "Move player left";
                            break;
                        case 'j':
                            message = "Move player down";
                            break;
                        case 'k':
                            message = "Move player up";
                            break;
                        case 'l':
                            message = "Move player right";
                            break;
                        case 'p':
                            message = "Pick up item";
                            break;
                        case 'd':
                            message = "Drop item <id>";
                            break;
                        case 'i':
                            message = "Display pack";
                            break;
                        case 'c':
                            message = "Take off armor";
                            break;
                        case 'w':
                            message = "Wear armor <id>";
                            break;
                        case 'E':
                            message = "End the game <Y or y>";
                            break;
                        case '?':
                            message = "Display list of commands";
                            break;
                        case 'H':
                            message = "Info on command <command>";
                            break;
                        case 'r':
                            message = "Read scroll <id>";
                            break;
                        case 'T':
                            message = "Wield sword <id>";
                            break;
                        default: message = "Wrong input entered";
                    }

                    int offset = 6;
                    for(int i = 0; i < 100; i++){
                        displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                    }

                    for(int i = 0; i < message.length(); i++){
                        displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                    }

                }

                else if(ch == 'E'){
                    boolean chk = false;
                    char endgame = '\0';

                    while(inputQueue.peek() == null){
                        chk = true;
                    }

                    if(chk == true){
                        endgame = String.valueOf(inputQueue.poll()).charAt(0);

                        if(endgame == 'Y' | endgame == 'y'){ 
                            displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                            String message = "Game ended since user entered the command Y/y";

                            int offset = 6;
                            for(int i = 0; i < 100; i++){
                                displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                            }
                        
                            for(int i = 0; i < message.length(); i++){
                                displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                            }

                            return false;

                        }

                        else{
                            endgame = '\0';
                        }
                    }

                    //String message = "Game ended since user entered the command Y/y";
//
                    //int offset = 6;
                    //for(int i = 0; i < 100; i++){
                    //    displayGrid.addObjectToDisplay(new Char(' '), offset + i, displayHeight - 1);
                    //}
//
                    //for(int i = 0; i < message.length(); i++){
                    //    displayGrid.addObjectToDisplay(new Char(message.charAt(i)), offset + i, displayHeight - 1);
                    //}

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
