/*
 * Created on 19-Jan-2005
 *
 */
package presage.util;

import java.util.StringTokenizer;
import java.util.ArrayList;
/**
 * @author Brendan
 * 
 */
public class StringParseTools {

	public static int wordCount(String text, String token){
		StringTokenizer parser = new StringTokenizer(text, token);
		return parser.countTokens();
	}
	
	public static String[] readTokens(String text, String token) {
		StringTokenizer parser = new StringTokenizer(text, token);
		int numTokens = parser.countTokens();
		String[] list = new String[numTokens];

		for (int i = 0; i < numTokens; i++) {
			list[i] = parser.nextToken();
		}
		return list;
	} //ends the readTokens method
	
	public static String extensionForFilename(String filename) {
		if (filename.lastIndexOf(".") == -1 )
			return "";
		else
			return filename.substring(filename.lastIndexOf(".")+1, filename.length());
	}

	public static String filenameNoExtension(String filename) {
		if (filename.lastIndexOf(".") == -1 )
			return "";
		else
			return filename.substring(0, filename.lastIndexOf("."));
	}
	
	public static String arraylistToString(ArrayList<String> arraylist){
		
		String result = "";
		for (int i = 0; i < arraylist.size(); i++) {
			result += "<" + arraylist.get(i) + ">";
		}
		return result;
		
		
	}
	public static ArrayList<String> stringToArrayList(String string){
	
	ArrayList<String> list = new ArrayList<String>();
	String[] roles = StringParseTools.readTokens( string.substring(1,  string.length() - 1), "><");
	for (int i = 0; i < roles.length; i++) {
		list.add(roles[i]);
	}
	return list;
	
	
	}
	
	
}