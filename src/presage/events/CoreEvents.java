package presage.events;

import org.simpleframework.xml.Element;

import presage.*;
import presage.annotations.EventConstructor;

public class CoreEvents {

	public static class ActivateParticipant implements Event {

		@Element
		String participantname;

		public ActivateParticipant(){
		
		}
		@EventConstructor("participantname")
		public ActivateParticipant(String participantname){
			this.participantname = participantname;
		}

		public void execute(Simulation sim) {		
			sim.activateParticipant(participantname);
		}
		
		public String toString(){
			return getShortLabel();
		}
		
		public String getShortLabel()
		{ // This is worth doing if you want the gui components to look right
			return this.getClass().getName() + "(" + participantname + ");";		
		}

	}
	
	public static class DeactivateParticipant implements Event {

		@Element
		String participantname;

		public DeactivateParticipant(){}
		
		@EventConstructor("participantname")
		public DeactivateParticipant(String participantname){
			this.participantname = participantname;
		}

		public void execute(Simulation sim){
			// deactivate a participant
			sim.deActivateParticipant(participantname);
		}
		
		public String toString(){
			return getShortLabel();
		}


		public String getShortLabel()
		{ // This is worth doing if you want the gui components to look right
			return this.getClass().getName() + "(" + participantname + ");";		
		}

	}

	
	public static class ChangeExperimentLength implements Event {

		@Element
		int length;

		public ChangeExperimentLength(){
		
		}
		@EventConstructor("newlength")
		public ChangeExperimentLength(int length){
			this.length = length;
		}

		public void execute(Simulation sim) {		
			sim.setExperimentLength(length);
		}
		
		public String toString(){
			return getShortLabel();
		}
		
		public String getShortLabel()
		{ // This is worth doing if you want the gui components to look right
			return this.getClass().getName() + "(" + length + ");";		
		}

	}
	
	
//	public static class TestOutput implements Event {
//
//		@Element
//		String comment;
//
//		public TestOutput(){}
//		
//		@EventConstructor("comment")
//		public TestOutput(String comment){
//			this.comment = comment;
//		}
//
//		public void execute(Simulation sim) {
//
//			System.out.println(getShortLabel() + ": Executing");
//
//		}
//		public String toString(){
//			return getShortLabel();
//		}
//		
//		public String getShortLabel()
//		{ // This is worth doing if you want the gui components to look right!
//			return this.getClass().getName() + "(" + comment + ");";		
//		}
//
//	}
//
//	public static class TestOutput2 implements Event {
//
//		// The xml annotation lets presage construct the Event from the input file.
//		@Element
//		String comment;
//
//		@Element
//		int testint;
//
//		@Element
//		double testdouble;
//
//		public TestOutput2(){}
//		
//		// This lets you construct the Event from the user interface. 
//		@EventConstructor({"comment", "testint", "testdouble"})
//		public TestOutput2(String comment, int testint, double testdouble ){
//			
//			this.comment = comment;
//			this.testint = testint;
//			this.testdouble = testdouble;
//		
//		}
//
//		// This is where the code you want to execute goes.
//		public void execute(Simulation sim) {
//			System.out.println(getShortLabel() + ": Executing");
//		}
//		
//		public String toString(){
//			return getShortLabel();
//		}
//
//		// This is worth doing if you want the gui components to look right!
//		public String getShortLabel()
//		{ 
//			return this.getClass().getName() + "(" + comment + "," + testint + "," + testdouble +");";		
//		}
//	}

}
