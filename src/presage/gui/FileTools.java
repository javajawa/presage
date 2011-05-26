package presage.gui;

import java.io.File;

/**
 * Replacement for filename parsing tools that were missing from the code base
 * @author Benedict Harcourt
 */
public class FileTools
{
	public static String getExtension(String name)
	{
		int mid= name.lastIndexOf(".");
		if (mid == -1) return name;
		return name.substring(mid);
	}
	
	public static String getNameWithoutExtension(String name)
	{
		int mid= name.lastIndexOf(".");
		if (mid == -1) return name;
		return name.substring(0,mid);
	}
}
