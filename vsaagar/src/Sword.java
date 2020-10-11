package src;

public class Sword extends Item{
  private String _name;

    public Sword(String name)
    {
        _name = name;
        System.out.println("Sword:Sword" + _name);
    }

    public void setID(int room, int serial)
    {
      System.out.println("Sword:setID" + room + "\n" + serial);
    }
}
