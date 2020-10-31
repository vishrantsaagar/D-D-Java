package game;

public class Player extends Creature{

    private int room_id;
    private int pos_X;
    private int pos_Y;
    private Player player;
    private int startX;
    private int startY;

    public void setPlayer(Player _player)
    {
      player = _player;
      System.out.println("Player:" + player);
    }

    public Player getPlayer()
    {
      return player;
    }

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

    public int getX()
    {
      pos_X = getPosX().get(0);
      return pos_X;
    }
    
    public int getY()
    {
      pos_Y = getPosY().get(0);
      return pos_Y;
    }

    public int getstartingX()
    {
      return startX;
    } 

    public int getstartingY()
    {
      return startY;
    }

    public void setstartingX(int x)
    {
      startX = getPosX().get(0) + x;
    } 

    public void setstartingY(int y)
    {
      startY = getPosY().get(0) + y;
    }

    //public void moveUP()
    //{
    //  pos_Y = getPosY().get(0);
    //  int new_Y = pos_Y - 1;
    //}
//
    //public void moveDOWN()
    //{
    //  pos_Y = getPosY().get(0);
    //  int new_Y = pos_Y + 1;
    //}
//
    //public void moveLEFT()
    //{
    //  pos_X = getPosY().get(0);
    //  int new_X = pos_X - 1;
    //}
//
    //public void moveRIGHT()
    //{
    //  pos_X = getPosY().get(0);
    //  int new_X = pos_X + 1;
    //}
}
