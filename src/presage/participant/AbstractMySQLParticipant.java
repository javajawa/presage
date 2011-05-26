package presage.participant;

import java.util.ArrayList;
import presage.AbstractParticipantDataModel;
import presage.EnvironmentConnector;
import presage.abstractparticipant.AbstractParticipant;
import presage.comm.mysql.MySQLConnector;


/**
 * 
 * @author Brendan
 *
 *
 *
 */


import org.simpleframework.xml.Root;

@Root
public abstract class AbstractMySQLParticipant extends AbstractParticipant {

	String mysqlHost;
	String mysqlDbname;
	transient MySQLConnector mysql;
	
	@Deprecated
	public AbstractMySQLParticipant(){}
	
	public AbstractMySQLParticipant(String id, String rolesString, long randomseed) {
		super(id, rolesString, randomseed);
		// TODO Auto-generated constructor stub
	}

	public AbstractMySQLParticipant(String id, ArrayList<String> roles, long randomseed) {
		super(id, roles, randomseed);
		// TODO Auto-generated constructor stub
	}

	public void onInitialise() {
		// TODO Auto-generated method stub
	}
	
	public void onSimulationComplete() {
		// TODO Auto-generated method stub
	}

	abstract public void onActivation() ;

	abstract public void onDeActivation();

	abstract public AbstractParticipantDataModel getInternalDataModel();

	abstract public void updatePerception(Object object);

}
