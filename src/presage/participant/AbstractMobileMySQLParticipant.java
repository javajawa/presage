// FIXME: Removed Code
//package presage.participant;
//
//import java.util.ArrayList;
//import presage.distributed.*;
//import presage.AbstractParticipantDataModel;
//import presage.EnvironmentConnector;
//
//import org.simpleframework.xml.Root;
//
//@Root
//public class AbstractMobileMySQLParticipant extends AbstractMySQLParticipant implements MobileParticipant {
//
//	
//	byte[] mysqldata;
//	
//	@Deprecated
//	public AbstractMobileMySQLParticipant(){}
//			
//	public AbstractMobileMySQLParticipant(String id, String rolesString, long randomseed) {
//		super(id, rolesString, randomseed);
//		// TODO Auto-generated constructor stub
//	}
//
//	public AbstractMobileMySQLParticipant(String id, ArrayList<String> roles,
//			long randomseed) {
//		super(id, roles, randomseed);
//		// TODO Auto-generated constructor stub
//	}
//
//
//	public void onExecute() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public AbstractParticipantDataModel getInternalDataModel() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onActivation() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onDeActivation() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void updatePerception(Object object) {
//		// TODO Auto-generated method stub
//	}
//
//	public void beforeMigrate(){
//		
//		// will I want this to deregister from the environment and reregister after migrate ?
//		
//		mysqldata = mysql.getDatabaseData(mysqlDbname, mysqlHost);		
//		mysql.executeUpdate("Drop database " + mysqlDbname);	
//		
//	}
//	
//	// Give it a new connection to the environment as the old one won't work anymore... 
//	// Plus you shouldn't migrate the environment so any participant you create should 
//	// declare the agents environment connection as transient.
//	public void afterMigrate(EnvironmentConnector newEnvironment){
//		
//		mysql.restoreDataBase( mysqlDbname, mysqldata );			
//		mysql.setDatabaseConnection(mysqlDbname, mysqlHost);	
//	}
//
//}
