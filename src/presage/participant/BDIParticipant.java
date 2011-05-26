package presage.participant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import oldstuff.AbstractIntention;

import presage.Simulation;
import presage.abstractparticipant.AbstractParticipant;

public class BDIParticipant extends AbstractParticipant {

	@Override
	public void onActivation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeActivation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void physicallyAct() {
		// TODO Auto-generated method stub

	}

	@Override
	public void proActiveBehaviour() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub

	}

	
	public void handleIntentions() {
		printIntentions();
		executeIntentions();
		removeExecutedIntentions();
	}
	
	
	public void executeIntentions() {
		AbstractIntention currentIntention;
		IntentionSet intentionsTemp = (IntentionSet) intentions.clone();
		Iterator iterator = intentionsTemp.iterator();
		long currentTime = Simulation.getTime();
		while (iterator.hasNext()) {
			currentIntention = (AbstractIntention) iterator.next();
			// Test and see if its time to execute the intention!
			if (currentIntention.executionTime < currentTime) {
				System.out.println("Executing Intention - "
						+ currentIntention.toString());
				
				
				currentIntention.setExecuted();
				try {
					Class c = this.getClass();
					Method m = c.getDeclaredMethod(currentIntention.method,
							new Class[] { Object[].class });
					Object tmp = m.invoke(this,
							new Object[] { currentIntention.variables });
				} catch (NoSuchMethodException e2) {
					System.err
					.println("executeIntentions: NoSuchMethodException - "
							+ e2);
				} catch (IllegalAccessException e3) {
					System.err
					.println("executeIntentions: IllegalAccessException - "
							+ e3);
				} catch (InvocationTargetException e4) {
					System.err
					.println("executeIntentions: InvocationTargetException - "
							+ e4);
				}
				
				
			}
		}
	} // ends method executeIntentions
	public void printIntentions() {
		/** Prints out all the player's currentIntentions */
		AbstractIntention currentIntention;
		Iterator iterator = intentions.iterator();
		System.out.print(myId + ": " + intentions.size() + " intentions: ");
		while (iterator.hasNext()) {
			currentIntention = (AbstractIntention) iterator.next();
			System.out.print(currentIntention.toString() + " ");
		}
		System.out.print("\n");
	} // ends printIntentions
	public void removeExecutedIntentions() {
		AbstractIntention currentIntention;
		Iterator iterator = intentions.iterator();
		IntentionSet removeSet = new IntentionSet();
		while (iterator.hasNext()) {
			currentIntention = (AbstractIntention) iterator.next();
			if (currentIntention.executed) {
				// then add key to list of conversations to be deleted
				removeSet.addIntention(currentIntention);
				System.out.println("Removing Intention - "
						+ currentIntention.toString());
			}
		}
		// now do the removing
		iterator = removeSet.iterator();
		while (iterator.hasNext()) {
			intentions.removeIntention((AbstractIntention) iterator.next());
		}
	} // removeExecutedIntentions()
	
	
}
