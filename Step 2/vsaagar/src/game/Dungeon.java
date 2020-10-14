package game;

import java.util.ArrayList;

public class Dungeon {
   
    private String _name;
    private int _width;
    private int _gameHeight;
    private ArrayList<Room> room;
    private ArrayList<Creature> creature;
    private ArrayList<Item> item;
    private ArrayList<Passage> passage;

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
        room.add(r1);
    }

    public void addCreature(Creature c1)
    {
        System.out.println("Dungeon:addCreature");
        creature.add(c1);
    }

    public void addPassage(Passage p1)
    {
        System.out.println("Dungeon:addPassage");
        passage.add(p1);
    }

    public void addItem(Item it)
    {
        System.out.println("Dungeon:addItem");
        item.add(it);
    }
}