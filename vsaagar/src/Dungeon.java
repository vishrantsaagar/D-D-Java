public class Dungeon {
   
    private String _name;
    private int _width;
    private int _gameHeight;

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
    }

    public void addCreature(Creature c1)
    {
        System.out.println("Dungeon:addCreature");
    }

    public void addPassage(Passage p1)
    {
        System.out.println("Dungeon:addPassage");
    }

    public void addItem(Item item)
    {
        System.out.println("Dungeon:addItem");
    }
}