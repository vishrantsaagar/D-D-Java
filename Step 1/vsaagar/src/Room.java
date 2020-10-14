package src;

public class Room extends Structure{
    
    private int _r1;

    public Room(int r1)
    {
        _r1 = r1;
        System.out.println("Room:Room" + _r1);
    }

    public void setId(int room)
    {
        System.out.println("Room:setID" + room);
    }

    public void setCreature(Creature Monster)
    {
        System.out.println("Room:setCreature");
    }
}
