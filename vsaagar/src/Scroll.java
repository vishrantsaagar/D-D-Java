public class Scroll extends Item{
    
    private String _name;
    
    public Scroll(String name)
    {
        _name = name;
        System.out.println("Scroll" + _name);
    }

    public void setID(int room, int serial)
    {
        System.out.println("Scroll:setID" + room + "\n" + serial);
    }
}
