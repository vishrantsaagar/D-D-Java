package game;

public class Displayable {

    private int hp;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private char type;
    
    public Displayable()
    {
        System.out.println("Displayable");
    }

    public int getHp(){
        return hp;
    }

    public int getPosX(){
        return posX;
    }

    public int getPosY(){
        return posY;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public char getType(){
        return type;
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
        hp = Hp;
        System.out.println("Displayable:setHp" + Hp);

    }

    public void setType(char t)
    {
        type = t;
        System.out.println("Displayable:setType" + t);

    }

    public void setIntValue(int v)
    {
        System.out.println("Displayable:setIntValue" + v);
    }

    public void SetPosX(int x)
    {
        posX = x;
        System.out.println("Displayable:SetPosX" + x);
    }

    public void setPosY(int y)
    {
        posY = y;
        System.out.println("Displayable:setPosY" + y);
    }

    public void SetWidth(int x)
    {
        width = x;
        System.out.println("Displayable:setWidth" + x);
    }

    public void setHeight(int y)
    {
        height = y;
        System.out.println("Displayable:setHeight" + y);

    }

}
