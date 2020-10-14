package src;

public class Creature extends Displayable{
    
    public Creature()
    {
        System.out.println("Creature");
    }

    public void setHp(int h)
    {
        System.out.println("Creature:setHp" + h);
    }

    public void setHpMoves(int hpm)
    {
        System.out.println("Creature:setHpMoves" + hpm);
    }
    
    public void setDeathAction(CreatureAction da)
    {
        System.out.println("Creature:setDeathAction");
    }

    public void setHitAction(CreatureAction ha)
    {
        System.out.println("Creature:setHitAction");
    }

}
