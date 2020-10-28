package game;

import java.util.ArrayList;

public class Dungeon {
   
    private String _name;
    private int _width;
    private int _gameHeight;
    private ArrayList<Displayable> rooms = new ArrayList<Displayable>();
    private ArrayList<Displayable> creatures = new ArrayList<Displayable>();
    private ArrayList<Displayable> items = new ArrayList<Displayable>();
    private ArrayList<Displayable> passages = new ArrayList<Displayable>();
    private ArrayList<ArrayList<Displayable>> list = new ArrayList<ArrayList<Displayable>>();


    public void getDungeon(String name, int width, int gameHeight)
    {
        _name = name;
        _width = width;
        _gameHeight = gameHeight;
        
        System.out.println("Dungeon:getDungeon" + _name + "\n" + _width +"\n" + _gameHeight);
    }

    public ArrayList<ArrayList<Displayable>> getList() {
        list.add(rooms);
        list.add(creatures);
        list.add(items);
        list.add(passages);

        return list;
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