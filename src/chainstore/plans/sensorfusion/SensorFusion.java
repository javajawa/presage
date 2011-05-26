package chainstore.plans.sensorfusion;

import presage.Input;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.Plan;
import chainstore.ChainDataModel;
import chainstore.environment.*;
import chainstore.plans.conversations.hello.HelloConvFSM;

import java.util.Iterator;
import java.util.ArrayList;

public class SensorFusion extends Plan {

	// So we can see all the function of the ArbiterDataModel
	// we override the AplayerDataModel with ours.
	ChainDataModel dm;

	public SensorFusion(ChainDataModel dm, Interpreter interpreter) {
		super(dm, interpreter, dm.keyGen.getKey(), SensorFusion.class.getCanonicalName());
		// TODO Auto-generated constructor stub
		this.updateTimeout(Plan.NO_TIMEOUT);
		this.dm = dm;
	}

	@Override
	public boolean canHandle(Input input) {

		if (input instanceof ConnectionsInput){
			return true;	
		} else if (input instanceof DisconnectionsInput){
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

		if (input instanceof ConnectionsInput){
			
			System.out.println("SensorFusion handling ConnectionsInput");
			
			ConnectionsInput ci = (ConnectionsInput)input;
			
			Iterator it = ci.getConnections().iterator();
			while (it.hasNext()){
				String theirId = (String)it.next();
				
				if(dm == null){
					System.err.println("SensorFusion datamodel is null!");
				}
				
				if (!dm.contactKnown(theirId)){
					
					dm.addNewContact(theirId, new ArrayList<String>(), true);
					// start a helloConvFSM
					
					HelloConvFSM conv = new HelloConvFSM(dm, interpreter, dm.keyGen.getKey(), theirId, null);
					conv.initiate();
					interpreter.addPlan(conv);
				}
			}
		} else if (input instanceof DisconnectionsInput){

			// Given this is a fully connected static network there won't be any disconnections!
			
		} 

	}

	@Override
	public boolean inhibits(Plan ihandler) {
		// TODO Auto-generated method stub
		return true;
	}

}
