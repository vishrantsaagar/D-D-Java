package game;

public class Action {

  private int itemActionInt;
  private char itemActionChar; 
      
    public void setMessage(String msg) 
   {
    System.out.println("Action: msg" + msg);
   }

   public void setIntValue(int v)
   {
    itemActionInt = v;
    System.out.println("Action:v" + v);
   }

   public int getIntValue(){
    System.out.println("Action:getIntValue");
    return itemActionInt;
   }

   public void setCharValue(char c)
   {
    itemActionChar = c;
    System.out.println("Action:c" + c);
   }

   public char getCharValue(){
    System.out.println("Action:getCharValue");
    return itemActionChar;
   }
}
