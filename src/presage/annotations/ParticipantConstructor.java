package presage.annotations;

import java.lang.annotation.*; 

@Retention(RetentionPolicy.RUNTIME)
// @ParameterNames()
public @interface ParticipantConstructor {
	String[] value();
}



