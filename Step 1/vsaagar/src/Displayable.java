package src;

public class Displayable {
    
    public Displayable()
    {
        System.out.println("Displayable");
    }

    public void setInvisible(int v)
    {
        System.out.println("Displayable:Invisible");
    }

    public void setVisible(int v)
    {
        System.out.println("Displayable:setVisible");

    }

    public void setMaxHit(int maxHit)
    {
        System.out.println("Displayable:setMaxHit" + maxHit);
    }

    public void setHpMove(int hpMoves)
    {
        System.out.println("Displayable:setHpMove" + hpMoves);

    }

    public void setHp(int Hp)
    {
        System.out.println("Displayable:setHp" + Hp);

    }

    public void setType(char t)
    {
        System.out.println("Displayable:setType" + t);

    }

    public void setIntValue(int v)
    {
        System.out.println("Displayable:setIntValue" + v);
    }

    public void SetPosX(int x)
    {
        System.out.println("Displayable:SetPosX" + x);
    }

    public void setPosY(int y)
    {
        System.out.println("Displayable:setPosY" + y);
    }

    public void SetWidth(int x)
    {
        System.out.println("Displayable:setWidth" + x);
    }

    public void setHeight(int y)
    {
        System.out.println("Displayable:setHeight" + y);

    }

}
