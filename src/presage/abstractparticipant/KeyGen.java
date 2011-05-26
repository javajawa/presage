
package presage.abstractparticipant;
/**
 * Created on 26-Sep-2003
 * Brendan Neville
 * Information Systems and Networks Research Group
 * Electrical and Electronic Engineering
 * Imperial College London
 */
public class KeyGen implements java.io.Serializable{
	
	// TODO this needs to be serielizable to XML to support intrupt and mobile agent code
	
	String myId;
	int currentKeyIndex;

	public KeyGen(String myId) {
		this.myId = myId;
		currentKeyIndex = 0;
	} // ends constructor KeyGen

	/** returns a Key String to Specification
	 * @deprecated
	 * */
	public String getKey(String myId, long systemtime, String KeyIndex) {
		return myId +"_"+ systemtime +"_"+ KeyIndex;
	} 

	/** returns a Key with the next avalible index */
	public String getKey() {		
		currentKeyIndex++;
		return myId +"_"+ System.currentTimeMillis() +"_"+ currentKeyIndex;		
	} // ends method getKey()

} //ends ConvKeyGen
