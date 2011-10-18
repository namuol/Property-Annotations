package util.properties;

/**
 * @author Louis Acresti
 */
@SuppressWarnings("serial")
public class PropertyInaccessibleException extends Exception {
	
	private String propertyName;
	
	/**
	 * @return the name of the property which cannot be accessed
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
	public PropertyInaccessibleException(String propName, String msg) {
		super(msg);
		this.propertyName = propName;
	}
	
	static final String helpMsg(String prefix, String propName) {
		return 
		  "There are four ways to solve this problem:\n"
		+ " 1. Create a conventionally-named, "
			+ "publicly-accessible "+prefix+"ter method named '"
			+ PropertiesHandler.getMethodNameFromPropertyName(prefix, propName) 
			+ "'\n"
		+ " 2. Specify a custom "+prefix+"ter method name with in the "
			+ " @Property annotation parameter list.\n"
		+ "    Example:"
		+ "      @Property("+prefix+"ter=\"my"+prefix+"ter\")\n"
		+ " 3. Specify a custom "+prefix+"ter method using the "
			+ "@PropertySetter/@PropertyGetter "
			+ "annotation on the desired method.\n"
		+ " 4. Simply use @Property on a public field.\n"
		+ " NOTE: Getters and Setters must adopt the following parameter "
			+ "conventions:\n"
		+ "   SETTER: public void setProperty(<propertyType> newValue)\n"
		+ "   GETTER: public <propertyType> getProperty()\n";
	}

}
