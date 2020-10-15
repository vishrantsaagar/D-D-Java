package game;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class Rogue {
    public static void main(String[] args) {
        
        // check if a filename is passed in.  If not, print a usage message.
        // If it is, open the file
            String fileName = null;
               fileName = "game/xmlfiles/" + "testDrawing.xml"; //../xmlfiles. + args[0]
        
    
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    
            try {
                SAXParser saxParser = saxParserFactory.newSAXParser();
                DungeonXMLHandler handler = new DungeonXMLHandler();
                saxParser.parse(new File(fileName), handler);

                handler.toString();
                Dungeon dungeon =  handler.getDungeon();

            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace(System.out);
            }
        }
}
