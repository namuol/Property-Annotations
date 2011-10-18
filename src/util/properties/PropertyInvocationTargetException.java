package util.properties;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Louis Acresti
 */
@SuppressWarnings("serial")
public class PropertyInvocationTargetException 
	extends InvocationTargetException {
	
	PropertyInvocationTargetException(String propName, 
			String propValueString,
			Object propValue,
			Throwable cause) {
		super(cause);
		propertyName = propName;
		propertyValue = propValue;
		propertyValueString = propValueString;
	}
	
	/**
	 * @return the name of the property which is responsible for throwing an 
	 * exception upon invoking its setter or getter method
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the string value of the property when this exception occurred 
	 * (this is only meaningful when the exception is thrown while invoking the
	 * property's setter method -- will be "-UNKNOWN-" when invoking the getter)
	 */
	public String getPropertyValueString() {
		return propertyValueString;
	}

	/**
	 * @return the parsed value of the property when this exception occurred 
	 * (this is only meaningful when the exception is thrown while invoking the
	 * property's setter method -- will be null if thrown while invoking the
	 * getter)
	 */
	public Object getPropertyValue() {
		return propertyValue;
	}

	private String propertyName;
	private Object propertyValue;
	private String propertyValueString;
}
