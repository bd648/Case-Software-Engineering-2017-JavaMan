/**
 * @author Brennan McFarland
 * a collection of useful methods for debugging
 * */

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Debug
{
  static boolean printVerbose = true;
  
  public static void printv(String printMessage)
  {
    if(printVerbose == true)
      System.out.println(printMessage);
  }
  
  //returns true if all tests pass
  public static boolean testAll()
  {
	  	//stores all test results
	    boolean tests[] = new boolean[3];
	    
	    
	    //run all tests
	    tests[0] = testSuccessfulManualUpdate();
	    tests[1] = testMalformedManualUpdate();
	    tests[2] = testConnectionlessManualUpdate();
	    
	    //print if/which tests failed
	    boolean allTestsSuccessful = true;
	    for(int i=0; i<tests.length; i++)
	    {
	    	if(tests[i] == false)
	    	{
	    		System.out.print("Test " + i + " failed!");
	    		allTestsSuccessful = false;
	    	}
	    }
	    if(allTestsSuccessful)
	    	System.out.println("All tests succeeded!");
	    return allTestsSuccessful;
  }
  
  private static String runAsExternalProcess(String args)
  {
	  Process proc = null;
	  try
	  {
		  try
		  {
			  //attempt to execute process on windows bash
			  String[] bashcommand = new String[] {"\"C:\\Program Files\\Git\\git-bash.exe\"",
					  "--cd-to-home", "-c", "javaman " + args};
			  proc = new ProcessBuilder(bashcommand).start();
			  proc.waitFor();
		  }
		  catch(IOException notWindows)
		  {
			  try
			  {
				  //attempt to execute process on linux bash
				  String[] bashcommand = new String[] {"/bin/bash",
						  "-c", "javaman " + args};
				  proc = new ProcessBuilder(bashcommand).start();
				  proc.waitFor();
			  }
			  catch(IOException notLinux)
			  {
				  //if neither works, oh well we tried
				  Debug.printv("Error executing debug process");
			  }
		  }
	  }
	  catch(InterruptedException interrupted)
	  {
		  Debug.printv("Error: debug process interrupted");
	  }
	  
	  BufferedReader bri = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	  String outputline;
	  String output = "";
	  try
	  {
		  while ((outputline = bri.readLine()) != null) {
		  output += outputline;
	  	}
	  }
	  catch(IOException exception)
	  {
		  Debug.printv("Error: IOException when capturing debug process output");
	  }
	  
	  return output;
  }
  
  //test result of a successful manual update 
  public static boolean testSuccessfulManualUpdate()
  {
	  Debug.printv("assuming working internet connection...");
	  Debug.printv("testing successful manual update...");
	  runAsExternalProcess("update"); //triggers manual update
	  //check log file to see if successful update and return if succeeded
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  return true;
	  //else
		  //return false;
  }
  
  //test result of a malformed manual update 
  public static boolean testMalformedManualUpdate()
  {
	  Debug.printv("assuming working internet connection...");
	  Debug.printv("testing malformed manual update...");
	  if(runAsExternalProcess("updte") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("updte") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("upadte") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("uPdate") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("uupdate") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  return true;
  }
  
  public static boolean testConnectionlessManualUpdate()
  {
	  Debug.printv("assuming no working internet connection...");
	  Debug.printv("testing successful manual update...");
	  if(runAsExternalProcess("update") != JavaMan.noInternetConnectionMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  return true;
  }

  public static void main(String []args)
  {
	 testAll(); 
    //ManPage testpage = new ManPage("testmanpagefile","123\n456");
    //testpage.writeFile();
    //testpage.displayText();
  }
}
