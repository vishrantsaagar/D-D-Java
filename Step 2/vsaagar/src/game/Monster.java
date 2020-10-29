package game;

public class Monster extends Creature{

   private int room_id;
   
   public Monster( )
   {
      System.out.println("Monster");
   }

   // @Override
   public int getRoomID(){
      System.out.println("Monster:RoomID");

      return room_id;
   }

   public void setName(String name)
   {
      System.out.println("Monster:setName"+name);

   }
    
   public void setID(int room, int serial)
   {
      room_id = room;
      // System.out.println("Monster:setID"+room + "\n" +serial);

   }
}
