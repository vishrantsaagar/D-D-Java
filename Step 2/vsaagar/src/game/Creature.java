package game;

import java.util.ArrayList;

public class Creature extends Displayable {
    
    private int hp;
    private int HpMoves;
    private int maxHit;
    private ArrayList<CreatureAction> cact = new ArrayList<CreatureAction>();
    private CreatureAction dact;
    private Item weapon;
    private Item armor;
    
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

    @Override
    public void setMaxHit(int _maxHit) {
        maxHit = _maxHit;
    }

    public int getMaxHit() {
        return maxHit;
    }
    
    public void setHpMoves(int hpm)
    {
        System.out.println("Creature:setHpMoves" + hpm);
        HpMoves = hpm;
    }

    public int getHpMoves() {
        return HpMoves;
    }
    
    public void setDeathAction(CreatureAction da)
    {
        System.out.println("Creature:setDeathAction");
        dact = da;
    }

    public CreatureAction getDact() {
        return dact;
    }

    public void setHitAction(CreatureAction ha)
    {
        System.out.println("Creature:setHitAction");
        cact.add(ha);
    }

    public ArrayList<CreatureAction> getCact() {
        return cact;
    }

}
