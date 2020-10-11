package src;

public class Remove extends CreatureAction{
   
    private String _name;

    public Remove(String name, Creature owner)
    {
        super(owner);
        _name = name;
        System.out.println("Remove:Remove" + _name);
    }
}
