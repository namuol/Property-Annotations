package util.properties;

/**
 * @author Louis Acresti
 */
@SuppressWarnings("serial")
public class PropertyMissingException extends Exception {
	
	public PropertyMissingException(String propertyName) {
		super("The required property '" 
				+ propertyName + "' was not defined and has no default "
				+"value to fall back on.");
		this.propertyName = propertyName;
	}

	/**
	 * @return the name of the required property that was not defined
	 * @see Property#defaultValue()
	 * @see PropertySetter#defaultValue()
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
	private String propertyName;
}
