package example.inputs;

import java.awt.Rectangle;
import java.util.UUID;
import org.simpleframework.xml.Element;
import presage.Input;

public class TargetFailure implements Input, java.io.Serializable {

	@Element
	private long timestamp;
	
	private String performative = "targetfailure";
	
	@Element
	private Target target;
	
	public TargetFailure(Target target, long timestamp){

		this.target = target;
		this.timestamp = timestamp;
	}

	public Target getTarget() {
		return target;
	}

	public long getTimestamp(){
		return this.timestamp;
	}
	
	public void setTimestamp(long timestamp){
		
		this.timestamp = timestamp;
	}

	public String getPerformative() {
		return performative;
	}

	public String toString(){

		return "TargetFailure<" + target.toString()+ ">";
	}
	
}