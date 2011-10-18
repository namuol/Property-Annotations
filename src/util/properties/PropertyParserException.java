package util.properties;

import java.lang.reflect.Type;

/**
 * @author Louis Acresti
 */
@SuppressWarnings("serial")
public class PropertyParserException extends Exception {
	
	PropertyParserException(Type propType, String propName,
			String propValueString, Throwable cause) {
		super(
			"The property named '" + propName + "' could not be"
				+ " cast from the string '" + propValueString 
				+ "'.\n" 
			+ "This either means that the string is malformed, or "
			+ "that the type '"+ propType 
			+"' simply has no "
			+ "support for being cast from a String.\n"
			+ "One elegant solution is to change the property "
				+ "to use only "
				+ "@PropertyGetter/@PropertySetter and to be of "
				+ "type 'String'.\nThen, in your getter/setter "
				+ "methods, you can specify your own "
				+ "serializing/parsing logic to manipulate your "
				+ "underlying object of type '" + propType
				+ "'.\n ", cause);
		propertyType = propType;
		propertyName = propName;
		propertyValueString = propValueString;
	}
	
	/**
	 * @return the Type of the property which had problems being parsed
	 */
	public Type getPropertyType() {
		return propertyType;
	}
	
	/**
	 * @return the name of the property which had problems being parsed
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the string which couldn't be parsed
	 */
	public String getPropertyValueString() {
		return propertyValueString;
	}
	
	private Type propertyType;
	private String propertyName;
	private String propertyValueString;
}
