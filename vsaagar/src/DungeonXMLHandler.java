import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DungeonXMLHandler extends DefaultHandler {
    
    private StringBuilder data = null;

    private Dungeon dungeon;
    private ArrayList<Room> rooms;
    
    private Room roomsParsed = null;

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
    private boolean btype = false;

    private boolean bhpMoves = false;

	public ArrayList<Room> getRoom() {
		return rooms;
    }

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
            dungeon.getDungtion(name, gameHeight, width);
        } else if(qName.equalsIgnoreCase("Rooms")) { //order followed in testDrawing.xml
            //What do you put here?
        } else if(qName.equalsIgnoreCase("Room")) {
            int roomid = Integer.parseInt(attributes.getValue("room"));
            //How do you declare room here?
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

    private void addRoom(Room room) {
    rooms.add(room); 
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    @Override
    public String toString(){
        String str = "DungeonXMLHandler\n";

        for(int i = 0; i < rooms.size(); i++){
            str += rooms.get(i).toString() + "\n";
        }

        str += "roomsBeingParsed: " + roomsParsed.toString() + "\n";
        str += "creaturesBeingParsed: " + creaturesParsed.toString() + "\n";
        str += "bvisible: " + bvisible.toString() + "\n";
        str += "bposX: " + bposX.toString() + "\n";
        str += "bposY: " + bposY.toString() + "\n";
        str += "bwidth: " + bwidth.toString() + "\n";
        str += "bheight: " + bheight.toString() + "\n";

        //Need to still add rooms, room, monster, player, and scroll
    }

}