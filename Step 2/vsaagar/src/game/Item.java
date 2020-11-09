package game;

import java.util.ArrayList;

public class Item extends Displayable {

    private Creature owner;
    private Creature c = new Creature();
    private ItemAction action = new ItemAction(c);
    private ArrayList<Integer> PosX = new ArrayList<Integer>();
	private ArrayList<Integer> PosY= new ArrayList<Integer>();

    public void setOwner(Creature _owner)
    {
        System.out.println("Item");
        owner = _owner;
    }

    public Creature getOwner(){
        return owner;
    }

    public void addItemAction(ItemAction itemaction)
    {
        System.out.println("ItemAction");
        action = itemaction;
    }

    public void SetPosX(int x)
    {
        PosX.add(x);
        System.out.println("Displayable:SetPosX" + x);
    }

    public void SetPosY(int y)
    {
        PosY.add(y);
        System.out.println("Displayable:setPosY" + y);
    }

    public ArrayList<Integer> getPosX(){
        return PosX;
    }

    public ArrayList<Integer> getPosY(){
        return PosY;
    }
}
