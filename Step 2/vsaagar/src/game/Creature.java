package game;

public class Creature extends Displayable{
    
    private int hp;

    public Creature()
    {
        System.out.println("Creature");
    }

    public void setHp(int h)
    {
        hp = h;
    }

    public int getHp()
    {
        return hp;
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
