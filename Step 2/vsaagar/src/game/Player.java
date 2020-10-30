package game;

public class Player extends Creature{

    private int room_id;
    private int pos_X;
    private int pos_Y;

    public Player( )
    {
      System.out.println("Player");
    }

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

    public void get_coord()
    {
      pos_X = getPosX().get(0);
      pos_Y = getPosY().get(0);
      System.out.println("Player: posx:" + pos_X +"and posy:" + pos_Y);
    }

    public void moveUP()
    {
      pos_Y = getPosY().get(0);
      int new_Y = pos_Y - 1;
    }

    public void moveDOWN()
    {
      pos_Y = getPosY().get(0);
      int new_Y = pos_Y + 1;
    }

    public void moveLEFT()
    {
      pos_X = getPosY().get(0);
      int new_X = pos_X - 1;
    }

    public void moveRIGHT()
    {
      pos_X = getPosY().get(0);
      int new_X = pos_X + 1;
    }
}
