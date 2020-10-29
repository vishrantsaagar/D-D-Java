package game;

public class Player extends Creature{

    private int room_id;
    
    public Player( )
    {
      System.out.println("Player");
    }

    // @Override
    public int getRoomID(){
      return room_id;
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
       room_id = room;
       System.out.println("Player:setID"+room + "\n" +serial);
    }    
}
