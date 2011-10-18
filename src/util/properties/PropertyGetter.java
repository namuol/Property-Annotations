package util.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Apply this annotation to any method which act as a getter (accessor) for a
 *  property. It is only necessary to use this annotation if you want to store
 *  a property but dont want to add an otherwise superfluous field to your 
 *  class.
 * </p>
 * 
 * <p>
 * NOTE: This must be used in conjunction with {@link PropertySetter}
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
 *     {@literal @}PropertyGetter
 *     public String getFullName() {
 *         return firstName + " " + lastName;
 *     }
 *     
 *     ...
 * }
 * </pre>
 * </p>
 * @see Property
 * @see PropertySetter
 * @author Louis Acresti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PropertyGetter {
	/**
	 * <p>
	 * This is useful only if you need a property to appear with a custom name
	 * 	in the properties file.
	 * </p>
	 * 
	 * <p>
	 * By default, this string is set to "-guess-" which is used internally by
	 * 	PropertiesHandler to signal that the name should be based on the name of 
	 * 	the getter method that this annotation is applied to.
	 * </p>
	 * 
	 * <p>
	 * While guessing the name, if the name of the method starts with "get" 
	 * followed by a capital letter
	 * (the expected convention in <code>camelCase</code>), then everything 
	 * following "get", with the first letter made lowercase, is the resulting 
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
	 * {@literal Getter Name        | Property Name}
	 * {@literal -------------------|----------------------}
	 * {@literal "getMyProperty()" -> myProperty}
	 * {@literal "myProperty()" ----> myProperty}
	 * {@literal "GetMyProperty()" -> GetMyProperty (wrong)}
	 * </pre>
	 * </p>
	 * @return the name of this property as it appears in a properties file
	 */
	String name() default "-guess-";
}