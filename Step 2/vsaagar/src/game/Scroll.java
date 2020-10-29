package game;

public class Scroll extends Item{
    
    private String _name;
    private int room_id;
    
    public Scroll(String name)
    {
        _name = name;
        System.out.println("Scroll" + _name);
    }

    public int getRoomID(){
        return room_id;
    }

    public void setID(int room, int serial)
    {
        room_id = room;
        System.out.println("Scroll:setID" + room + "\n" + serial);
    }
}
