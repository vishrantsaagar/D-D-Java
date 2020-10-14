package game;

public class Player extends Creature{
    
    public Player( )
    {
      System.out.println("Player");
    }

    public void setWeapon(Item sword)
    {
        System.out.println("Player:setWeapon");
    }

    public void setArmor(Item armor)
    {
        System.out.println("Player:setArmor");
    }

    public void setID(int room, int serial)
    {
       System.out.println("Player:setID"+room + "\n" +serial);
 
    }    
}
