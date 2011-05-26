package infection;

import presage.Input;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.Plan;

public class SensorFusion extends Plan {

	// So we can see all the function of the ArbiterDataModel
	// we override the AplayerDataModel with ours.
	AgentDataModel dm;

	public SensorFusion(AgentDataModel dm, Interpreter interpreter) {
		super(dm, interpreter, dm.keyGen.getKey(), SensorFusion.class.getCanonicalName());
		// TODO Auto-generated constructor stub
		this.updateTimeout(Plan.NO_TIMEOUT);
		this.dm = dm;
	}

	@Override
	public boolean canHandle(Input input) {

		if (input instanceof InfectedInput){
			return true;	
		} else if (input instanceof SecurityPatch){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canRemove() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle(Input input) {
		// TODO Auto-generated method stub

		if (input instanceof InfectedInput){
			
			System.out.println("SensorFusion handling InfectedInput");
			
			InfectedInput ci = (InfectedInput)input;
			
			// Update the agents beliefs
			// i.e. it may be you want the agent to be aware that its infected? 
			
			// Take any actions or create plans as required 
			
			
		} else if (input instanceof SecurityPatch){
			
			// you get the idea
			
		} 
	}

	@Override
	public boolean inhibits(Plan ihandler) {
		// TODO Auto-generated method stub
		return true;
	}

}
