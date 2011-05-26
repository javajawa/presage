package presage.abstractparticipant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import presage.Input;
import presage.Signal;
import presage.abstractparticipant.plan.Plan;
import presage.util.InputQueue;

public class Interpreter {

	public InputQueue inputs = new InputQueue("inputs");

	public ArrayList<Plan> plans = new ArrayList<Plan>();

	//	Set up the Conversation System

	protected Random random;

	public Interpreter(Random random) {
		// super();
		// so are choices are linked to our agents random generator and therefore repeatable
		this.random = random;
	}

	public void printPlans(){

		System.out.println("Plans.length = " + plans.size());

		Iterator it_plans = plans.iterator(); 
		while (it_plans.hasNext()){	
			Plan temp = (Plan)it_plans.next();
			System.out.println(temp.toString());
		}

	}

	public void addPlan(Plan plan){
		plans.add(plan);
	}

	public void removePlan(Plan plan){
		plans.remove(plan);
	}

	public void addInput(Input input){
		inputs.enqueue(input);
	}

	public void addInput(ArrayList<Input> input){
		// add them all to the inputs.
		Iterator iterator = input.iterator();
		while(iterator.hasNext()){
			inputs.enqueue((Input)iterator.next());
		}
	}

	public void handleTimeouts(long time){

		Iterator it_plans = plans.iterator(); 
		while (it_plans.hasNext()){	
			
			Plan temp = (Plan)it_plans.next();

			if (temp.isTimedOut(time)){
			 	this.addInput(new Signal(Plan.TIME_OUT, temp.getMyKey(), time));
			 	System.out.println("Interpreter sent a timeout signal to " + temp.toString());
			}
		}
	}

	public int countPlansOfType(String type){

		int result = 0;
		
		Iterator it_plans = plans.iterator(); 
		while (it_plans.hasNext()){	
			Plan temp = (Plan)it_plans.next();
			
			// System.out.println(temp.toString());
			
			if (temp.getType().equals(type)){
				result++;
			}	
		}
		
		// System.out.println("Number of Plans of Type " + type + " = " + result);
		return result;

	}

	public void processInputs() {

		// System.out.println("Interpreter.processInputs()");

		try {

			while (!inputs.isEmpty()) {
				Input inputReceived = (Input) inputs.dequeue();

				if (inputReceived == null)
					continue;

				System.out.println("	received :" + inputReceived.toString());

				//  System.out.println();

				// Have to check with all the plans if they can/will handle the input
				ArrayList<Plan> canHandleInput = new ArrayList<Plan>();

				Iterator it_plans = plans.iterator(); 
				while (it_plans.hasNext()){	
					Plan temp = (Plan)it_plans.next();
					try {
						if (temp.canHandle(inputReceived))
							canHandleInput.add(temp);
					} catch (Exception e){
						System.err.println("Exception occured in " + temp + ".canhandle("+ inputReceived.toString() +")" );						
					}
				}

				System.out.println( "        	" + canHandleInput.size() + " Plans canhandle it");

				// not all plans are able to run if another plan is already active.
				// the Plan.inhibits() method allows us to do resource control
				ArrayList<Plan> notinhibited = new ArrayList<Plan>();
				// So for all the methods that can handle the input check which inhibit which
				Iterator it_canhandle = canHandleInput.iterator(); 

				while (it_canhandle.hasNext()){
					boolean inhibited = false;
					Plan p0 = (Plan)it_canhandle.next();
					// by calling all the current plans .inhibits() method
					it_plans = canHandleInput.iterator();
					while (it_plans.hasNext()){
						Plan p1 = (Plan)it_plans.next();
						// if they are the same plan they they may inhibit each other!
						if(p1.equals(p0))
							continue;
						try {
							if(p1.inhibits(p0)){
								inhibited = true;
								break;
							}
						} catch (Exception e){
							System.err.println("Exception occured checking if " + p1.toString() + " inhibits " + p0.toString());						
						}

					}
					if (!inhibited) {
						notinhibited.add(p0);
					}
				}

				if (notinhibited.size() <= 0){
					System.out.println( " No Plans Left after inhibition ");
				} else {

					Plan theplan = notinhibited.get(random.nextInt(notinhibited.size()));

					try {
						// if more than one handler can handle then pick one at random
						theplan.handle(inputReceived);
					} catch (Exception e){
						System.err.println("Exception " + e.getClass().getCanonicalName()+ "occured in " + theplan + ".handle("+ inputReceived.toString() +")" );						
					}
					try {
						// Check if we can remove it 
						if (theplan.canRemove())
							removePlan(theplan);
						
					} catch (Exception e){
						System.err.println("Exception " + e.getClass().getCanonicalName()+ "occured in " + theplan + ".canRemove()("+ inputReceived.toString() +")" );						
					}
				}
			}
		} catch (Exception e){
			System.err.println("Exception in Interpreter.processInputs()" + e);
		}
	}
}
