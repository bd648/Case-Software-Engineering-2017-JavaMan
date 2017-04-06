/**
 * @author Brennan McFarland
 * Update handles all updates to the documentation database, both automatic
 * and manual, including error checking and handling, logging, and formatting
 * downloaded documentation
 * */
import java.io.*;
import java.time.LocalDateTime;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.logging.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Update
{
  static boolean autoUpdate;
  static ManPage[] docs;
  static int minAutoUpdateTime = 0; //minimum time to trigger automatic update
  static Logger logger = Logger.getLogger("JavaMan.Update");
  static Handler fileHandler;
  static LoggingFormatter formatter;


/*
CheckAutoUpdateCondition() is called from main before every query to set a flag
if enough time has passed between the time the method is called and the last
successful update in the log file.  It compares the system clock to the
timestamp of the last successful update, if one exists, and if enough time has
elapsed sets the update flag accordingly
*/
  public static boolean checkAutoUpdateCondition()
  {
    //TODO: check system clock with last update time in log file to see if
    //update needed, rn just runs always
    int timeSinceLastUpdate = LocalDateTime.now().compareTo(LocalDateTime.now());
    //-1 if the datetimes are =
    if(timeSinceLastUpdate >= minAutoUpdateTime || timeSinceLastUpdate == -1)
    {
      Debug.printv("Triggered automatic update...");
      autoUpdate = true;
      return true;
    }
    autoUpdate = false;
    return false;
  }

  public static void update(boolean isManual)
  {
    if(checkInternetConnection())
      downloadDocs();
  }

  /*
  check internet connectivity to determine if updated documentation can be
  downloaded
  */
  private static boolean checkInternetConnection()
  {
    try
    {
      URL url = new URL("http://docs.oracle.com/javase/8/docs/api/");
      try
      {
        URLConnection conn = url.openConnection();
        conn.connect();
      }catch(IOException cantconnect)
      {
        displayErrorNoInternet();
        return false;
      }
    }catch(MalformedURLException malformedurl)
    {
      Debug.printv("Error: malformed URL");
      System.exit(1);
    }
    return true;
  }

  private static void downloadDocs()
  {
    Debug.printv("Downloading documentation...");
    //run the docScraper script to pull webpage data
    Runtime rt = Runtime.getRuntime();
    try{
        //Process proc = rt.exec("casperjs docScraper.js");
        //BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        //String line=null;
        //while((line=input.readLine()) != null) {
        //  System.out.println(line);
        //}
        //int exitVal = proc.waitFor();
        //System.out.println("Exited with error code "+exitVal);
    }catch(Exception ex){
        ex.printStackTrace();
    }
    
    Debug.printv("Formatting documentation...");
    //read from that JSON file and convert to plaintext
    try
    {
    	JSONParser parser = new JSONParser();
    	JSONArray a = (JSONArray) parser.parse(new FileReader("Test.json"));
    	for(Object o : a)
    	{
    		JSONObject jClass = (JSONObject) o;
    		String classname = (String)jClass.get("name");
    		//System.out.println("Class: ");
    		//System.out.println("\t" + classname);
    		
    		JSONArray jConstructors = (JSONArray) jClass.get("constructors");
    		//System.out.println("Constructors: ");
    		for(Object b : jConstructors)
    		{
    			JSONObject jConstObj = (JSONObject) b;
    			String constructorName = (String)jConstObj.get("name");
    			String constructorDesc = (String)jConstObj.get("description");
    			//System.out.println("\t" + constructorName + " : " + constructorDesc);
    		}
    		
    		JSONArray jMethods = (JSONArray) jClass.get("methods");
    		//System.out.println("Methods: ");
    		for(Object b : jMethods)
    		{
    			JSONObject jMethodObj = (JSONObject) b;
    			String methodName = (String) jMethodObj.get("name");
    			String methodDesc = (String) jMethodObj.get("description");
    			String modAndType = (String) jMethodObj.get("modAndType");
    			//System.out.println("\t" + methodName + " : " + methodDesc);
    			//System.out.println("\t\t Returns: " + modAndType);
    		}
    	}
    } catch (FileNotFoundException e)
    {
        Debug.printv("Error: JSON file not found");
    } catch (IOException e)
    {
        Debug.printv("Error: IOException reading from JSON file");
    } catch (ParseException e)
    {
        Debug.printv("Error: Cannot parse JSON file");
    }
  }

  private void downloadDoc(String url)
  {

  }

  private static void formatDocs()
  {

  }

  private void formatDoc()
  {

  }

  private static void updateLocalDocs()
  {

  }

  private void updateLocalDoc()
  {

  }

  private static void storeIncompleteUpdatedDocs()
  {

  }

  private void storeIncompleteUpdatedDoc()
  {

  }

  /**
   * Logs if the update is succeful or unsuccessful
   * @param result the string of successful or unsuccessful
   */
  private static void logUpdate(String result)
  {
    try{
      fileHandler = new FileHandler("update.log");
      formatter = new LoggingFormatter();
      fileHandler.setFormatter(formatter);
      logger.addHandler(fileHandler);
      logger.info("Update " + result);
    } catch(Exception e){
      e.printStackTrace();
    }
  }

  private static void displayErrorNoInternet()
  {
    System.out.println("Error: Cannot connect to the online documentation for"
      + " update");
  }

  public static void main(String []args)
  {
	downloadDocs();
    /*checkAutoUpdateCondition();
    if(autoUpdate == true) //TODO: or manual user input
    {
      if(checkInternetConnection() == true)
        update(autoUpdate);
    }*/
  }
}
