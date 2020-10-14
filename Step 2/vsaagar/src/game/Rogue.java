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
            switch (args.length) {
            case 1:
               // note that the relative file path may depend on what IDE you are
           // using.  This worked for NetBeans.
               fileName = "xmlfiles/" + args[0]; //../xmlfiles. + args[0]
               break;
            default:
               System.out.println("java Test <xmlfilename>");
           return;
            }
    
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
