package presage.util;

import java.lang.reflect.AnnotatedElement;

public class ConstructorInfo {

	// public boolean annotated;
	
	
	
	public String classname;
	
	public String simplename;

	// public String methodname;
	
	public Class<?>[] parameterclasses;

	// public String[] parametervaluestrings;

	// for the GUI will get these using the @EventConstructor annotation.
	public String[] parameternames;
	
	public ConstructorInfo(String classname, String simplename, Class<?>[] parameterclasses, String[] parameternames ){
		
		this.classname = classname;
		this.simplename = simplename;
		this.parameterclasses = parameterclasses;
		this.parameternames = parameternames;
	
	}
	
	public String toString() {
		return simplename + "(" + printparameters() + ");";
	}

	public String printparameters(){

		String parameterString = "";

		for (int i = 0; i < parameterclasses.length; i++){
			parameterString = parameterString + parameterclasses[i].getSimpleName() + " " + parameternames[i] + ", ";
		}

		if (parameterString.length() >= 2)
			parameterString  = parameterString.substring(0, parameterString.length()-2);

		return parameterString;
	}
	
}
