package util.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Apply this annotation to any method which act as a setter (mutator) for a
 *  property. It is only necessary to use this annotation if you want to load
 *  a property but dont want to add an otherwise superfluous field to your 
 *  class.
 * </p>
 * 
 * <p>
 * NOTE: This must be used in conjunction with {@link PropertyGetter}
 * </p>
 * 
 * When possible, use the {@link Property} annotation for simplicity.
 * 
 * <p>
 * Example usage:
 * <pre>
 * class Account {
 *     private String firstName;
 *     private String lastName;
 *     
 *     ...
 *     
 *     {@literal @}PropertySetter
 *     public String setFullName(String name) {
 *         String[] firstAndLast = name.split(" ");
 *         firstName = firstAndLast[0];
 *         lastName = firstAndLast[1];
 *     }
 *     
 *     ...
 * }
 * </pre>
 * </p>
 * @see Property
 * @see PropertyGetter
 * @author Louis Acresti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PropertySetter {
	/**
	 * <p>
	 * Set this string if you want this property to be optional.
	 * </p>
	 * 
	 * <p>
	 * When {@link PropertiesHandler#applyProperties} is
	 * 	called and this property is not defined in the specified properties
	 * 	file, then this string (defaultValue) will be parsed.
	 * </p>
	 * 
	 * <p>
	 * If this string is not set, it defaults to "-required-" which is used 
	 *  internally by PropertiesHandler to throw a PropertyMissingException 
	 *  if such a property is not defined in the properties file when being
	 *  loaded.
	 * </p>
	 * 
	 * @return the default value, if any, that the property should have when 
	 * 	set in a .properties file
	 * @see PropertyMissingException
	 */
	String defaultValue() default "-required-";
	
	/**
	 * <p>
	 * This is useful only if you need a property to appear with a custom name
	 * 	in the properties file.
	 * </p>
	 * 
	 * <p>
	 * By default, this string is set to "-guess-" which is used internally by
	 * 	PropertiesHandler to signal that the name should be based on the name of 
	 * 	the setter method that this annotation is applied to.
	 * </p>
	 * 
	 * <p>
	 * While guessing the name, if the name of the method starts with "set" 
	 * followed by a capital letter
	 * (the expected convention in <code>camelCase</code>), then everything 
	 * following "set", with the first letter made lowercase, is the resulting 
	 * property name.
	 * </p>
	 * <p>
	 * If the method name does not follow this convention, but the {@link #name}
	 * annotation parameter is not set, then the exact name of the method will
	 * be used as the property name.
	 * </p>
	 * <p>
	 * Examples for clarity:
	 * <pre>
	 * {@literal Setter Name              | Property Name}
	 * {@literal -------------------------|----------------------}
	 * {@literal "setMyProperty(String)" -> myProperty}
	 * {@literal "myProperty(String)" ----> myProperty}
	 * {@literal "SetMyProperty(String)" -> SetMyProperty (wrong)}
	 * </pre>
	 * </p>
	 * @return the name of this property as it appears in a properties file
	 */
	String name() default "-guess-";
}