import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DungeonXMLHandler extends DefaultHandler {
    
    private StringBuilder data = null;

    private ArrayList<Displayable> disparray = null;
    private ArrayList<Action> actarray = null;

    private Dungeon dungeon = null;
    private Room currRoom = null;

    public Dungeon getDungeon(){
        return dungeon;
    }

    private boolean bvisible = false;
    private boolean bposX = false;
    private boolean bposY = false;
    private boolean bwidth = false;
    private boolean bheight = false;
    private boolean bhp = false;
    private boolean bmaxhit = false;
    private boolean bactionMessage = false;
    private boolean bactionIntValue = false;
    private boolean bactionCharValue = false;
    private boolean bItemIntValue = false;
    private boolean btype = false;
    private boolean bhpMoves = false;

    public DungeonXMLHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    
        if (qName.equalsIgnoreCase("Dungeon")) {
            String name = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            dungeon.getDungeon(name, gameHeight, width);
        } else if(qName.equalsIgnoreCase("Rooms")) { //order followed in testDrawing.xml

        } else if(qName.equalsIgnoreCase("Room")) {
            int roomid = Integer.parseInt(attributes.getValue("room"));
            Room room = new Room(roomid);
            dungeon.addRoom(room);
            //add it to the arraylist
        } else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;
        } else if(qName.equalsIgnoreCase("width")) {
            bwidth = true;
        } else if(qName.equalsIgnoreCase("height")) {
            bheight = true;
        } else if(qName.equalsIgnoreCase("Monster")) {

        } else if(qName.equalsIgnoreCase("Player")) {

        } else if(qName.equalsIgnoreCase("Scroll")) {

        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    }


    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    //@Override
    //public String toString(){
    //    String str = "DungeonXMLHandler\n";

    //    for(int i = 0; i < rooms.size(); i++){
    //        str += rooms.get(i).toString() + "\n";
    //    }

    //    str += "roomsBeingParsed: " + roomsParsed.toString() + "\n";
    //    //str += "creaturesBeingParsed: " + creaturesParsed + "\n";
    //    str += "bvisible: " + bvisible + "\n";
    //    str += "bposX: " + bposX + "\n";
    //    str += "bposY: " + bposY + "\n";
    //    str += "bwidth: " + bwidth + "\n";
    //    str += "bheight: " + bheight + "\n";

    //    //Need to still add rooms, room, monster, player, and scroll
    //    return str;
    //}

}