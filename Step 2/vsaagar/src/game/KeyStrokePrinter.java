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
    private int gameHeight;
    private int topHeight;
    private int bottomHeight;
    private int gameWidth;
    private int displayHeight;
    private String pickedItem;
    private Dungeon dungeon;
    private int steps = 0;
    private int phpmoves;

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

                    for(int item = 0; item < item_list.size(); item++){
                        if(item_list.get(item).getPosX().size() == 0){
                            item += 1;
                        }

                        int index_pos = item_list.get(item).getPosX().size() - 1;

                        if((item_list.get(item).getPosX().get(index_pos) == posX) & (item_list.get(item).getPosY().get(index_pos) == posY)){
                            int index = displayGrid.getObjectGrid()[posX][posY].size() - 2;

                            if(displayGrid.getObjectGrid()[posX][posY].get(index).getChar() == '|'){
                                p1.setWeapon(item_list.get(item));
                                item_list.get(item).setOwner(p1);

                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                                item_list.get(item).SetPosX(-1);
                                item_list.get(item).SetPosY(-1);

                                break;
                            }

                            else if(displayGrid.getObjectGrid()[posX][posY].get(index).getChar() == ']'){
                                p1.setArmor(item_list.get(item));
                                item_list.get(item).setOwner(p1);

                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.removeObjectFromDisplay(new Char(' '), posX, posY);
                                displayGrid.addObjectToDisplay(new Char('@'), posX, posY);

                                item_list.get(item).SetPosX(-1);
                                item_list.get(item).SetPosY(-1);
                                break;

                            }

                            else if(displayGrid.getObjectGrid()[posX][posY].get(index).getChar() == '?'){
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
                    }

                    else if(idx >= item_str_stack.size() | idx < 0){
                        System.out.println("No item at id: " + idx);
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
                        }

                        else if(item_it instanceof Armor){
                            item = ((Armor)item_it).getName();
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
