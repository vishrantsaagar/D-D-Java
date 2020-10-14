package src;

public class Armor extends Item{

    private String _name;

    public Armor(String name)
    {
        _name = name;
        System.out.println("Armor:Armor"+_name);
    }

    public void setName(String name)
    {
        System.out.println("Armor:setName" + name);
    }

    public void setID(int room, int serial)
    {
        System.out.println("Armor:setID:room:" + room + "\nserial" + serial);
    }
}
