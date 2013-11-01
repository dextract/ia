package circuit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;




public class circuitTest {
	
	int popSize = 50;
	String input = "";
	
	
	String file = "cincolinha.txt";
	
	try (InputStream in = newInputStream(file);
	    BufferedReader reader =
	      new BufferedReader(new InputStreamReader(in))) {
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	        System.out.println(line);
	    }
	} catch (IOException x) {
	    System.err.println(x);
	}

}