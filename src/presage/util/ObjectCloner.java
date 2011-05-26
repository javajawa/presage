package presage.util;

	/**
	 * The following code is copied (cloned :)) from the example found at:
	 * 
	 * http://www.javaworld.com/javaworld/javatips/jw-javatip76.html
	
	Java Tip 76: An alternative to the deep copy technique
	Use serialization to make deep copies and avoid extensive manual editing or extending of classes

	By Dave Miller, JavaWorld.com, 08/06/99

	 */

	import java.io.*;
	import java.util.*;
	import java.awt.*;
	public class ObjectCloner
	{
	   // so that nobody can accidentally create an ObjectCloner object
	   private ObjectCloner(){}
	   // returns a deep copy of an object
	   static public Object deepCopy(Object oldObj) throws Exception
	   {
	      ObjectOutputStream oos = null;
	      ObjectInputStream ois = null;
	      try
	      {
	         ByteArrayOutputStream bos = 
	               new ByteArrayOutputStream(); // A
	         oos = new ObjectOutputStream(bos); // B
	         // serialize and pass the object
	         oos.writeObject(oldObj);   // C
	         oos.flush();               // D
	         ByteArrayInputStream bin = 
	               new ByteArrayInputStream(bos.toByteArray()); // E
	         ois = new ObjectInputStream(bin);                  // F
	         // return the new object
	         return ois.readObject(); // G
	      }
	      catch(Exception e)
	      {
	         System.out.println("Exception in ObjectCloner = " + e);
	         throw(e);
	      }
	      finally
	      {
	         oos.close();
	         ois.close();
	      }
	   }
	   
	}
