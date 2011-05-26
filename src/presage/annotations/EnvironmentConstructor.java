package presage.annotations;

import java.lang.annotation.*; 

@Retention(RetentionPolicy.RUNTIME)
// @ParameterNames()
public @interface EnvironmentConstructor {
	String[] value();
}



