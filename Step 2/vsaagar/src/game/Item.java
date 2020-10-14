package game;

public class Item extends Displayable{
    public void setOwner(Creature owner)
    {
        System.out.println("Item");
    }

    public void addItemAction(ItemAction itemaction)
    {
        System.out.println("ItemAction");
    }

}
