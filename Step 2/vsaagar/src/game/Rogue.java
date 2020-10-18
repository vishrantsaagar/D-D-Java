package game;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class Rogue implements Runnable{

    private static ObjectDisplayGrid displayGrid = null;

    public Rogue(int width, int height) {
    //    displayGrid = new ObjectDisplayGrid(width, height);
    }

    @Override
    public void run() {
  //      displayGrid.fireUp();
  //      for (int step = 1; step < WIDTH / 2; step *= 2) {
  //          for (int i = 0; i < WIDTH; i += step) {
  //              for (int j = 0; j < HEIGHT; j += step) {
  //                  displayGrid.addObjectToDisplay(new Char('X'), i, j);
  //              }
  //          }

  //          try {
  //              Thread.sleep(2000);
  //          } catch (InterruptedException e) {
  //              e.printStackTrace(System.err);
  //          }
  //          displayGrid.initializeDisplay();
  //      }
  }  
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
