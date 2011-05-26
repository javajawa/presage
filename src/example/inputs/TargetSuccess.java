package example.inputs;

import java.awt.Rectangle;
import java.util.UUID;
import org.simpleframework.xml.Element;
import presage.Input;

public class TargetSuccess implements Input, java.io.Serializable {

	@Element
	private long timestamp;
	
	private String performative = "targetsuccess";
	
	@Element
	private Target target;
	
	public TargetSuccess(Target target, long timestamp){

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

		return "TargetSuccess<" + target.toString()+ ">";
	}
	
}