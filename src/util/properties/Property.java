package util.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Apply this annotation to fields which you need to load from or store into 
 * <a href="http://download.oracle.com/javase/6/docs/api/java/util/Properties.html">.properties</a> files.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * class Account {
 *     {@literal @}Property
 *     public String name;
 *     
 *     {@literal @}Property
 *     private Integer id;
 *     
 *     // Automatically used by Property (see {@link #getter}):
 *     public Integer getId() {
 *         return id;
 *     }
 *     
 *     // Automatically used by Property (see {@link #setter}):
 *     public void setId(Integer value) {
 *         id = value;
 *         notifyAllThatIdChanged();
 *     }
 *     
 *     ...
 * }
 * </pre>
 * </p>
 * 
 * For a list of supported field types, use {@link PropertiesHandler#getSupportedTypes()}.
 * @see PropertySetter
 * @see PropertyGetter
 * @author Louis Acresti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {
	
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
	 * This is useful only if you need a property to appear with a custom name
	 * 	in the properties file. A good example is for those following a naming
	 * 	convention that doesn't make sense in a properties file (something like
	 * 	"m_SomeMember" might make more sense as "someMember" in the properties 
	 * 	file). 
	 *  
	 * By default, this string is set to "-inherit-" which is used internally by
	 * 	PropertiesHandler to signal that the name should be identical to the 
	 * 	field that this annotation is being applied to.
	 * @return the name of this property as it appears in a properties file
	 */
	String name() default "-inherit-";
	
	/**
	 * Set this string if you are using a non-conventional getter name.
	 * A conventional name is based on the name of the property and uses 
	 * 	the <code>camelCase</code> naming convention like so:
	 *	<pre>
	 *  getterName = "get" + propertyName.substring(0, 1).toUpperCase()
	 *	           + propertyName.substring(1);
	 *	</pre>
	 * 
	 * By default, this is set to "-guess-" which is internally used by
	 * 	PropertiesHandler to signal that the method
	 * 
	 * Here's an example of a valid "guessed" name:
	 * <pre>
	 * class MyClass {
	 *     {@literal @}Property
	 *     private Integer someIntegerValue;
	 * 		
	 *     public Integer getSomeIntegerValue() {
	 *         return someIntegerValue;
	 *     }
	 * 
	 *     ...
	 * }
	 * </pre>
	 * 
	 * And here's an example using this annotation parameter for a field that
	 * 	follows a different naming convention:	
	 * <pre>
	 * class MyClass {
	 *     {@literal @}Property(getter="getSomeIntegerValue")
	 *     private Integer m_someIntegerValue;
	 * 		
	 *     public Integer getSomeIntegerValue() {
	 *         return someIntegerValue;
	 *     }
	 * 
	 *     ...
	 * }
	 * </pre>
	 * 
	 * If the name you provide does not resolve as a publicly-accessible
	 * 	member method that that takes zero (0) arguments and returns a non-void
	 * 	value, PropertiesHandler will throw a PropertyInaccessibleException
	 * 	for the offending property.  
	 * 
	 * @return the name of the getter (accessor) for this property; default is 
	 * 	"-guess-" (see above)
	 * @see #setter()
	 * @see PropertyGetter
	 * @see PropertyInaccessibleException
	 */
	String getter() default "-guess-";
	
	/**
	 * Set this string if you are using a non-conventional setter name.
	 * A conventional name is based on the name of the property and uses 
	 * 	the <code>camelCase</code> naming convention like so:
	 *	<pre>
	 *  setterName = "set" + propertyName.substring(0, 1).toUpperCase()
	 *	           + propertyName.substring(1);
	 *	</pre>
	 * 
	 * By default, this is set to "-guess-" which is internally used by
	 * 	PropertiesHandler to signal that the method
	 * 
	 * Here's an example of a valid "guessed" name:
	 * <pre>
	 * class MyClass {
	 *     {@literal @}Property
	 *     private Integer someIntegerValue;
	 * 		
	 *     public void setSomeIntegerValue(Integer value) {
	 *         someIntegerValue = value;
	 *     }
	 * 
	 *     ...
	 * }
	 * </pre>
	 * 
	 * And here's an example using this annotation parameter for a field that
	 * 	follows a different naming convention:	
	 * <pre>
	 * class MyClass {
	 *     {@literal @}Property(setter="setSomeIntegerValue")
	 *     private Integer m_someIntegerValue;
	 * 		
	 *     public void setSomeIntegerValue(Integer value) {
	 *         someIntegerValue = value;
	 *     }
	 * 
	 *     ...
	 * }
	 * </pre>
	 * 
	 * If the name you provide does not resolve as a publicly-accessible
	 * 	member method that that takes one (1) argument, PropertiesHandler will 
	 * 	throw a PropertyInaccessibleException for the offending property.  
	 * 
	 * @return the name of the setter (accessor) for this property; default is 
	 * 	"-guess-" (see above)
	 * @see #getter()
	 * @see PropertySetter
	 * @see PropertyInaccessibleException
	 */
	String setter() default "-guess-";
}
