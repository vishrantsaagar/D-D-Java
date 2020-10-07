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
            String dunName = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            dungeon.getDungeon(dunName, gameHeight, width);

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

            String monName = attributes.getValue("name");
            int monRoom = Integer.parseInt(attributes.getValue("room"));
            int monSerial = Integer.parseInt(attributes.getValue("serial"));
            
            Monster m1 = new Monster();
            m1.setName(monName);
            m1.setID(monRoom, monSerial);

            dungeon.addCreature(m1);

        //Why is visible, posX, and posY here again? Its already up in line 55
        } else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;
        } else if(qName.equalsIgnoreCase("type")) {
            btype = true;
        } else if(qName.equalsIgnoreCase("hp")) {
            bhp = true;
        } else if(qName.equalsIgnoreCase("maxhit")) {
            bmaxhit = true;

        } else if(qName.equalsIgnoreCase("CreatureAction")) {
          //doubt

            String creatureName = attributes.getValue("name");
            String creatureType = attributes.getValue("type");

            if(creatureName == "Remove"){
                //Remove r1 = new Remove() what parameters do we pass through remove - what is name and owner

            }

            else if(creatureName == "YouWin"){
                //YouWin yw1 = new YouWin() what parameters do we pass through YouWin - what is name and owner
            }
        } 

        else if(qName.equalsIgnoreCase("actionMessage")) {
            bactionMessage = true;

        } else if(qName.equalsIgnoreCase("actionIntValue")) {
            bactionIntValue = true;
        
        } else if(qName.equalsIgnoreCase("actionCharValue")) {
            bactionCharValue = true;

        } else if(qName.equalsIgnoreCase("Player")) {
            String player_name = attributes.getValue("name");
            String player_room = attributes.getValue("room");
            String player_serial = attributes.getValue("serial");

            //which class should we use for this?

        } 

        else if(qName.equalsIgnoreCase("Scroll")) {

        }

        else if(qName.equalsIgnoreCase("Passage")){
            String room1 = attributes.getValue("room1");
            String room2 = attributes.getValue("room2");

            //Object?
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Room room;
        Monster monster;
        CreatureAction creature_action;

        if(bvisible){
            
        }

        else if(bposX){

        }

        else if(bposY){

        }

        else if(bwidth){

        }

        else if(bheight){

        }

        else if(bhp){

        }

        else if(bmaxhit){

        }

        else if(bactionMessage){

        }

        else if(bactionCharValue){

        }

        else if(bItemIntValue){

        }

        else if(btype){

        }

        else if(bhpMoves){

        }
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