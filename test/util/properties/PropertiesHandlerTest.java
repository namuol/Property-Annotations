package util.properties;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import util.properties.PropertiesHandler;
import util.properties.Property;
import util.properties.PropertyGetter;
import util.properties.PropertyInaccessibleException;
import util.properties.PropertyMissingException;
import util.properties.PropertyParserException;


/**
 * @author lacresti
 *
 */

class TestPropertiesHolder {
	@Property(defaultValue="16")
	public byte testByte;
	
	@Property(defaultValue="3500")
	public Short testShort;
	
	@Property(defaultValue="28540939849")
	public Long testLong;
	
	@Property
	public String requiredString;
	
	@Property
	public String testString;

	@Property
	private int testInt;
	
	@PropertyGetter(name="testInt")
	public int myGetTestInt() {
		return testInt;
	}
	
	public void setTestInt(int value) {
		testInt = value;
	}

	@Property(setter="mySetTestDouble")
	private double testDouble;
	
	public void mySetTestDouble(double value) {
		testDouble = value;
	}
	
	public double getTestDouble() {
		return testDouble;
	}
	
	@Property(defaultValue="")
	public Integer[] numbers;
	
	@Property
	private Set<String> emails;
	
	public Set<String> getEmails() {
		return emails;
	}
	
	public void setEmails(Set<String> newEmails) {
		emails = newEmails;
	}
	
	TestPropertiesHolder() {
	}
	
	static Set<String> expectedPropertyNames = new HashSet<String>();
	static Map<String,Method> expectedGetters = new HashMap<String,Method>();
	static Map<String,Method> expectedSetters = new HashMap<String,Method>();
	static Map<String,Field> expectedFields = new HashMap<String,Field>();
	static Map<String,String> expectedDefaults = new HashMap<String,String>();
	static {
		try {
			// PROPERTIES:
			expectedPropertyNames.add("testByte");
			expectedPropertyNames.add("testShort");
			expectedPropertyNames.add("testLong");
			expectedPropertyNames.add("testString");
			expectedPropertyNames.add("requiredString");
			expectedPropertyNames.add("testInt");
			expectedPropertyNames.add("testDouble");
			expectedPropertyNames.add("emails");
			expectedPropertyNames.add("numbers");
			
			// GETTERS:
			expectedGetters.put("testByte",null);
			expectedGetters.put("testShort",null);
			expectedGetters.put("testLong",null);
			expectedGetters.put("testString",null);
			expectedGetters.put("requiredString",null);
			expectedGetters.put("numbers", null);
			expectedGetters.put("emails",
					TestPropertiesHolder.class.getMethod("getEmails"));
			expectedGetters.put("testInt",
					TestPropertiesHolder.class.getMethod("myGetTestInt"));
			expectedGetters.put("testDouble",
					TestPropertiesHolder.class.getMethod("getTestDouble"));
			
			// SETTERS:
			expectedSetters.put("testByte",null);
			expectedSetters.put("testShort",null);
			expectedSetters.put("testLong",null);
			expectedSetters.put("testString",null);
			expectedSetters.put("requiredString",null);
			expectedSetters.put("numbers", null);
			expectedSetters.put("emails", TestPropertiesHolder.class.getMethod(
					"setEmails", Set.class));
			expectedSetters.put("testInt", TestPropertiesHolder.class
					.getMethod("setTestInt", int.class));
			expectedSetters.put("testDouble", TestPropertiesHolder.class
					.getMethod("mySetTestDouble", double.class));
			
			// FIELDS:
			expectedFields.put("testByte",
					TestPropertiesHolder.class.getDeclaredField("testByte"));
			expectedFields.put("testShort",
					TestPropertiesHolder.class.getDeclaredField("testShort"));
			expectedFields.put("testLong",
					TestPropertiesHolder.class.getDeclaredField("testLong"));
			expectedFields.put("testString",
					TestPropertiesHolder.class.getDeclaredField("testString"));
			expectedFields.put("requiredString", TestPropertiesHolder.class
					.getDeclaredField("requiredString"));
			expectedFields.put("testInt",
					TestPropertiesHolder.class.getDeclaredField("testInt"));
			expectedFields.put("testDouble",
					TestPropertiesHolder.class.getDeclaredField("testDouble"));
			expectedFields.put("numbers",
					TestPropertiesHolder.class.getDeclaredField("numbers"));
			expectedFields.put("emails",
					TestPropertiesHolder.class.getDeclaredField("emails"));
			
			// DEFAULTS:
			expectedDefaults.put("testByte", "16");
			expectedDefaults.put("testShort", "3500");
			expectedDefaults.put("testLong", "28540939849");
			expectedDefaults.put("numbers", "");
			
		} catch (NoSuchMethodException e) {
			// Should only happen if changes were made to TestPropertiesHolder
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// Should only happen if changes were made to TestPropertiesHolder
			e.printStackTrace();
		}
	}
}

public class PropertiesHandlerTest {
	static String testProperties1
		= "testString = This is a test\n"
		+ "testInt = 42\n"
		+ "emails = test@test.com,dude@test.com,fake@blah.org\n"
		+ "testDouble = 432.234\n"
		+ "requiredString = This string is *required*\n";
	
	static String testProperties2
		= "testString = This is a test\n"
		+ "testInt = 42\n"
		+ "emails = test@test.com,dude@test.com,fake@blah.org\n"
		+ "testDouble = 432.234\n"
		// Remove the definition of the required string: (commented for clarity) // <-- Meta-comment // <-- Meta-meta-comment
		+ "#requiredString = This string is *required*\n";
	

	/**
	 * Test method for
	 * {@link PropertiesHandler#PropertiesHandler(java.lang.Object)}.
	 * 
	 * @throws NoSuchMethodException
	 * @throws PropertyInaccessibleException
	 * @throws InvocationTargetException
	 * @throws PropertyMissingException
	 */
	@Test
	public void testPropertiesHandler() 
		throws NoSuchMethodException, 
			   PropertyInaccessibleException,
			   PropertyMissingException {
		TestPropertiesHolder testObj = new TestPropertiesHolder();
		PropertiesHandler testHandler = new PropertiesHandler(testObj);

		assertEquals(TestPropertiesHolder.expectedGetters,
				testHandler.getGetterMap());
		assertEquals(TestPropertiesHolder.expectedSetters,
				testHandler.getSetterMap());
		assertEquals(TestPropertiesHolder.expectedFields,
				testHandler.getFieldMap());
		assertEquals(TestPropertiesHolder.expectedDefaults,
				testHandler.getDefaults());
		assertEquals(TestPropertiesHolder.expectedPropertyNames,
				testHandler.getPropertyNames());
	}
	
	@Test
	public void testExceptionThrowingForNoSetterDefined() {
		// Test for invalid annotation use:
		class NoSetterDefined {
			@Property
			private int testInt;
			
			@SuppressWarnings("unused")
			public int getTestInt() {
				return testInt;
			}
		}
		
		NoSetterDefined invalidObj = new NoSetterDefined();
		Boolean setterNotDefinedWasThrown = false;
		try {
			new PropertiesHandler(invalidObj);
		} catch (PropertyInaccessibleException e) {
			setterNotDefinedWasThrown = true;
			System.out
				.println("PropertyInaccessibleException *correctly* thrown:");
			System.out.println(e.getMessage());
		}
		assertTrue(setterNotDefinedWasThrown);
	}
	
	@Test
	public void testExceptionThrowingForNoGetterDefined() {
		// Test for invalid annotation use:
		class NoGetterDefined {
			@SuppressWarnings("unused")
			@Property
			private int testInt;
	
			@SuppressWarnings("unused")
			public void setTestInt(int value) {
				testInt = value;
			}
		}
		
		NoGetterDefined invalidObj2 = new NoGetterDefined();
		Boolean getterNotDefinedWasThrown = false;
		try {
			new PropertiesHandler(invalidObj2);
		} catch(PropertyInaccessibleException e) {
			getterNotDefinedWasThrown = true;
			System.out
				.println("PropertyInaccessibleException *correctly* thrown:");
			System.out.println(e.getMessage());
		}
		assertTrue(getterNotDefinedWasThrown);
	}
	
	@Test
	public void testExceptionThrowingForPrivateAccess() {
		class PrivateAccessTest {
			@Property
			private int testInt;
			
			@SuppressWarnings("unused")
			private int getTestInt() {
				return testInt;
			}
			
			@SuppressWarnings("unused")
			private void setTestInt(int value) {
				testInt = value;
			}
		}
		
		PrivateAccessTest invalidObj3 = new PrivateAccessTest();
		Boolean inaccessiblePropertyThrown = false;
		try {
			new PropertiesHandler(invalidObj3);
		} catch (PropertyInaccessibleException e) {
			inaccessiblePropertyThrown = true;
			System.out
				.println("PropertyInaccessibleException *correctly* thrown:");
			System.out.println(e.getMessage());
		}
		assertTrue(inaccessiblePropertyThrown);
	}
	
	@Test
	public void testExceptionThrowingForBadGetter() {
		class BadGetterTest {
			@Property
			private int testInt;
			
			@SuppressWarnings("unused")
			public int getTestInt(int superfluousInt) {
				return testInt;
			}
			
			@SuppressWarnings("unused")
			public void setTestInt(int value) {
				testInt = value;
			}
		}
		
		BadGetterTest invalidObj = new BadGetterTest();
		Boolean inaccessiblePropertyThrown = false;
		try {
			new PropertiesHandler(invalidObj);
		} catch (PropertyInaccessibleException e) {
			inaccessiblePropertyThrown = true;
			System.out
				.println("PropertyInaccessibleException *correctly* thrown:");
			System.out.println(e.getMessage());
		}
		assertTrue(inaccessiblePropertyThrown);
	}
	
	@Test
	public void testExceptionThrowingForBadSetter() {
		class BadSetterTest {
			@Property
			private int testInt;
			
			@SuppressWarnings("unused")
			public int getTestInt() {
				return testInt;
			}
			
			@SuppressWarnings("unused")
			public void setTestInt(int value, int superfluousInt) {
				testInt = value;
			}
		}
		
		BadSetterTest invalidObj = new BadSetterTest();
		Boolean inaccessiblePropertyThrown = false;
		try {
			new PropertiesHandler(invalidObj);
		} catch (PropertyInaccessibleException e) {
			inaccessiblePropertyThrown = true;
			System.out
				.println("PropertyInaccessibleException *correctly* thrown:");
			System.out.println(e.getMessage());
		}
		assertTrue(inaccessiblePropertyThrown);
	}
	
	@Test
	public void testExceptionThrowingForParsingFailure() 
		throws PropertyInaccessibleException, 
			   IOException,
			   PropertyMissingException,
			   InvocationTargetException,
			   SecurityException,
			   NoSuchFieldException {
		
		@SuppressWarnings("unused")
		class ParserFailTest {
			@Property
			public int testInt;
			
			@Property
			public Set<Double> testDoubleSet;
		}
		
		ParserFailTest testObj = new ParserFailTest();
		PropertiesHandler testHandler = new PropertiesHandler(testObj);
		InputStream propertiesStream = new ByteArrayInputStream(
			("testInt=asd\n"
			+"testDoubleSet=12,13.4,17.232").getBytes());

		Properties loadedProperties = new Properties();
		loadedProperties.load(propertiesStream);
		Boolean exceptionThrown = false;
		try{
			testHandler.applyProperties(loadedProperties);
		} catch (PropertyParserException e) {
			exceptionThrown = true;
			assertEquals("testInt", e.getPropertyName());
			assertEquals("asd", e.getPropertyValueString());
			assertEquals(int.class, e.getPropertyType());
		}
		
		assertTrue(exceptionThrown);
		
		propertiesStream = new ByteArrayInputStream(
				("testInt=42\n"
				+"testDoubleSet=12,13.4,17.232,asd").getBytes());

		loadedProperties = new Properties();
		loadedProperties.load(propertiesStream);
		exceptionThrown = false;
		
		try{
			testHandler.applyProperties(loadedProperties);
		} catch (PropertyParserException e) {
			exceptionThrown = true;
			assertEquals("testDoubleSet", e.getPropertyName());
			assertEquals("12,13.4,17.232,asd", e.getPropertyValueString());
			assertEquals(ParserFailTest.class
						.getField("testDoubleSet").getGenericType(),
							e.getPropertyType());
		}
		
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testExceptionThrowingForPropertyMissing()
		throws PropertyInaccessibleException,
			   IOException,
			   PropertyParserException,
			   InvocationTargetException {
		
		@SuppressWarnings("unused")
		class PropertyMissingTest {
			@Property
			public int testInt;
		}
		
		PropertyMissingTest testObj = new PropertyMissingTest();
		PropertiesHandler testHandler = new PropertiesHandler(testObj);
		InputStream propertiesStream 
			= new ByteArrayInputStream(("").getBytes());

		Properties loadedProperties = new Properties();
		loadedProperties.load(propertiesStream);
		Boolean exceptionThrown = false;
		try{
			testHandler.applyProperties(loadedProperties);
		} catch (PropertyMissingException e) {
			exceptionThrown = true;
			assertEquals("testInt", e.getPropertyName());
		}
		
		assertTrue(exceptionThrown);
	}

	/**
	 * Test method for
	 * {@link PropertiesHandler#extractProperties(java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws PropertyInaccessibleException
	 */
	@Test
	public void testExtractProperties()
		throws IOException,
			   PropertyInaccessibleException, 
			   InvocationTargetException {
		TestPropertiesHolder testObj = new TestPropertiesHolder();
		PropertiesHandler testHandler = new PropertiesHandler(testObj);
		Properties output = new Properties();

		testObj.setTestInt(240);
		testObj.testString = "Store me, please. kthx bye.";
		testObj.mySetTestDouble(123.321);
		String[] emailsArray={"someEmail@blah.org","someother_Email@blah.org"};
		Set<String> emails = new HashSet<String>();
		for(String email : emailsArray) {
			emails.add(email);
		}
		testObj.setEmails(emails);
		testObj.numbers = new Integer[3];
		testObj.numbers[0] = 3;
		testObj.numbers[1] = 2;
		testObj.numbers[2] = 1;
		
		output = testHandler.extractProperties();
				
		System.out.println("testStoreProperties output:");
		System.out.println(output);
		
		// Non-set variables that are not required should not be saved:
		assertFalse(output.toString().contains("testShort="));
		assertFalse(output.toString().contains("testLong="));

		// Everything else should be:
		assertEquals(output.getProperty("testString"), testObj.testString);
		assertEquals(output.getProperty("testDouble"),
				"" + testObj.getTestDouble());
		assertEquals(output.getProperty("testInt"), "" 
				+ testObj.myGetTestInt());
		assertEquals(output.getProperty("testByte"), "" + testObj.testByte);
		assertTrue(output.getProperty("emails").contains("someEmail@blah.org"));
		assertTrue(output.getProperty("emails").contains(
				"someother_Email@blah.org"));
	}

	/**
	 * Test method for
	 * {@link PropertiesHandler#applyProperties(java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws PropertyInaccessibleException
	 * @throws PropertyMissingException
	 * @throws PropertyParserException
	 */
	@Test
	public void testApplyProperties()
		throws IOException,
			   PropertyInaccessibleException,
			   InvocationTargetException,
			   PropertyMissingException,
			   PropertyParserException {
		TestPropertiesHolder testObj = new TestPropertiesHolder();
		PropertiesHandler testHandler = new PropertiesHandler(testObj);
		InputStream propertiesStream = new ByteArrayInputStream(
												testProperties1.getBytes() );

		Properties loadedProperties = new Properties();
		loadedProperties.load(propertiesStream);
		
		testHandler.applyProperties(loadedProperties);
		assertEquals("This is a test", testObj.testString);
		assertEquals(42, testObj.myGetTestInt());
		assertEquals(432.234, testObj.getTestDouble(), 0.0000001);

		Set<String> expectedEmails = new HashSet<String>();
		expectedEmails.addAll(
				Arrays.asList("test@test.com","dude@test.com","fake@blah.org"));
		assertEquals(testObj.getEmails(), expectedEmails);
		
		Boolean propMissingThrown = false;
		
		propertiesStream = new ByteArrayInputStream(
				testProperties2.getBytes() );
		try {
			loadedProperties = new Properties();
			loadedProperties.load(propertiesStream);
			testHandler.applyProperties(loadedProperties);
		} catch (PropertyMissingException e) {
			// This *should* happen:
			propMissingThrown = true; 
		}
		
		assertTrue(propMissingThrown);
	}

}
