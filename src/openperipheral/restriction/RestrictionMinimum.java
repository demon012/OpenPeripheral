package openperipheral.restriction;

import openperipheral.IRestriction;
import argo.jdom.JsonNode;

public class RestrictionMinimum implements IRestriction {

	private int minimum;
	
	public RestrictionMinimum(JsonNode json) {
		minimum = Integer.parseInt(json.getText());
	}
	
	@Override
	public boolean isValid(Object value) {
		if (value instanceof Byte) {
			return (Byte)value >= minimum;
		}else if (value instanceof Double) {
			return (Double)value >= minimum;
		}
		return (Integer)value >= minimum;
	}

	@Override
	public String getErrorMessage(int paramOffset) {
		return String.format("Arguments %s must no less than than %s", paramOffset, minimum);
	}
}
