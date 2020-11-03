package game;

public class Item extends Displayable{
	private int PosX;
	private int PosY;

    public void setOwner(Creature owner)
    {
        System.out.println("Item");
    }

    public void addItemAction(ItemAction itemaction)
    {
        System.out.println("ItemAction");
    }

    public void setPosX(int posX){
    	PosX = posX;
    	System.out.println("Item:setPosX");
    }

    public int get_PosX(){
    	System.out.println("Item:getPosX");
    	return PosX;
    }

    public void setPosY(int posY){
    	PosY = posY;
    	System.out.println("Item:setPosY");
    }

    public int get_PosY(){
    	System.out.println("Item:getPosX");
    	return PosY;
    }

}
