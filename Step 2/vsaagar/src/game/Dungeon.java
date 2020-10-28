package game;

import java.util.ArrayList;

public class Dungeon {
   
    private String _name;
    private int _width;
    private int _gameHeight;
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<Creature> creatures = new ArrayList<Creature>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Passage> passages = new ArrayList<Passage>();
    private ArrayList<Displayable> list = new ArrayList<Displayable>();


    public void getDungeon(String name, int width, int gameHeight)
    {
        _name = name;
        _width = width;
        _gameHeight = gameHeight;
        
        System.out.println("Dungeon:getDungeon" + _name + "\n" + _width +"\n" + _gameHeight);
    }

    public void retrieveStack(){
        list.add(rooms);
        list.add(creatures);
        list.add(items);
        list.add(passages);
    }

    public int get_gameHeight() {
        return _gameHeight;
    }

    public int get_width() {
        return _width;
    }
    
    public void addRoom(Room r1)
    {
        System.out.println("Dungeon:addRoom");
        rooms.add(r1);
    }

    public void addCreature(Creature c1)
    {
        System.out.println("Dungeon:addCreature");
        creatures.add(c1);
    }

    public void addPassage(Passage p1)
    {
        System.out.println("Dungeon:addPassage");
        passages.add(p1);
    }

    public void addItem(Item it)
    {
        System.out.println("Dungeon:addItem");
        items.add(it);
    }
}