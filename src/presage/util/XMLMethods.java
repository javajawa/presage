package presage.util;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;
import java.io.File;


public class XMLMethods {

	
	public static void write(Object object, String path){
		
		try {
			Serializer serializer = new Persister();		
			File result = new File(path);
			serializer.write(object, result);

		} catch (Exception e){
			System.err.println("Write XML file failed " + e);
		}
		
	}
	
	
}
