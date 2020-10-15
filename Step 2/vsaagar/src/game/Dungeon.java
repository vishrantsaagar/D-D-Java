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

    public void getDungeon(String name, int width, int gameHeight)
    {
        _name = name;
        _width = width;
        _gameHeight = gameHeight;
        
        System.out.println("Dungeon:getDungeon" + _name + "\n" + _width +"\n" + _gameHeight);
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