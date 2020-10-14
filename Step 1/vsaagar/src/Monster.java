package src;

public class Monster extends Creature{
   
   public Monster( )
   {
      System.out.println("Monster");
   }

   public void setName(String name)
   {
      System.out.println("Monster:setName"+name);

   }
    
   public void setID(int room, int serial)
   {
      System.out.println("Monster:setID"+room + "\n" +serial);

   }
}
