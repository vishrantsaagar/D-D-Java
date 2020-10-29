package game;

//import java.util.ArrayList;

public class Room extends Structure{
    
    private int _r1;
    // private ArrayList<Displayable> creatures = new ArrayList<Displayable>();
    // private ArrayList<Displayable> items = new ArrayList<Displayable>();

    public Room(int r1)
    {
        _r1 = r1;
        System.out.println("Room:Room" + _r1);
    }

    public void setId(int room)
    {
        System.out.println("Room:setID" + room);
    }

    public void setCreature(Creature c1)
    {
        // creatures.add(c1);
        System.out.println("Room:setCreature");
    }

    public void setItem(Item item){
        // items.add(item);
        System.out.println("Room:setItem");
    }
}
