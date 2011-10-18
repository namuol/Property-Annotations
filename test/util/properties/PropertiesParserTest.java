/**
 * 
 */
package util.properties;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import util.properties.PropertiesParser;


/**
 * @author lacresti
 *
 */
public class PropertiesParserTest {

	/**
	 * Test method for {@link util.properties.PropertiesParser#parse(java.lang.reflect.Type, java.lang.String)}.
	 * @throws InvocationTargetException 
	 * @throws ClassCastException 
	 */
	@Test
	public void testParseBasicTypes() 
		throws ClassCastException,
			   InvocationTargetException {
		assertEquals(PropertiesParser.parse(Boolean.class, "true"), true);
		assertEquals(PropertiesParser.parse(Boolean.class, "false"), false);
		assertEquals(PropertiesParser.parse(boolean.class, "true"), true);
		assertEquals(PropertiesParser.parse(boolean.class, "false"), false);
		
		assertEquals(PropertiesParser.parse(Byte.class, "16"),
				((Integer)16).byteValue());
		assertEquals(PropertiesParser.parse(byte.class, "16"),
				((Integer)16).byteValue());
		
		assertEquals(PropertiesParser.parse(Short.class, "1534"),
				((Integer)1534).shortValue());
		assertEquals(PropertiesParser.parse(short.class, "1534"),
				((Integer)1534).shortValue());
		
		assertEquals(PropertiesParser.parse(Integer.class, "42321"), 42321);
		assertEquals(PropertiesParser.parse(int.class, "42321"), 42321);
		
		assertEquals(PropertiesParser.parse(Long.class, "42532432343824"),
				42532432343824L);
		assertEquals(PropertiesParser.parse(long.class, "42532432343824"),
				42532432343824L);
		
		assertEquals(PropertiesParser.parse(Float.class, "42.532"), 42.532F);
		assertEquals(PropertiesParser.parse(float.class, "42.532"), 42.532F);
		
		assertEquals(PropertiesParser.parse(Double.class, "42000000.000000532"),
				42000000.000000532);
		assertEquals(PropertiesParser.parse(double.class, "42000000.000000532"),
				42000000.000000532);
	}
	
	/**
	 * Test method for {@link util.properties.PropertiesParser#parse(java.lang.reflect.Type, java.lang.String)}. 
	 * @throws InvocationTargetException 
	 * @throws ClassCastException 
	 */
	@Test
	public void testParseArrays() 
		throws ClassCastException,
			   InvocationTargetException {
		String[] testStringArray = {"asd","321","djfsk","asd","283","fjd"};
		assertArrayEquals(testStringArray,
			(String[])PropertiesParser
						.parse(String[].class, "asd,321,djfsk,asd,283,fjd"));
		
		Boolean[] testBooleanArray = {false,true,true,false,false,false,true};
		assertArrayEquals(testBooleanArray,
			(Boolean[])PropertiesParser
						.parse(Boolean[].class,
								"false,true,true,false,false,false,true"));
		
		Byte[] testByteArray = {27,43,123,17,127,27};
		assertArrayEquals(testByteArray,
			(Byte[])PropertiesParser
						.parse(Byte[].class, "27,43,123,17,127,27"));
		
		Short[] testShortArray = {17,4321,123,17,283,17};
		assertArrayEquals(testShortArray,
			(Short[])PropertiesParser
						.parse(Short[].class, "17,4321,123,17,283,17"));
		
		Integer[] testIntegerArray = {22317,443,0,22317,2722,2722};
		assertArrayEquals(testIntegerArray,
			(Integer[])PropertiesParser
						.parse(Integer[].class, "22317,443,0,22317,2722,2722"));
		
		Long[] testLongArray 
			={22317231234321L,443L,0L,22317231234321L,2722L,2722L};
		assertArrayEquals(testLongArray,
			(Long[])PropertiesParser
						.parse(Long[].class,
							"22317231234321,443,0,22317231234321,2722,2722"));
		
		Float[] testFloatArray = {123.321F,32132.31232F,0F,328F,328F};
		assertArrayEquals(testFloatArray,
			(Float[])PropertiesParser
						.parse(Float[].class, "123.321,32132.31232,0,328,328"));
		
		Double[] testDoubleArray 
			= {12300000.00000321,32132.31232,0.,328.,328.00};
		assertArrayEquals(testDoubleArray,
			(Double[])PropertiesParser
						.parse(Double[].class,
							"12300000.00000321,32132.31232,0.,328.,328.00"));
	}
	
	/**
	 * Test method for {@link util.properties.PropertiesParser#parse(java.lang.reflect.Type, java.lang.String)}.
	 * @throws InvocationTargetException 
	 * @throws ClassCastException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testParseSets()
		throws ClassCastException,
			   InvocationTargetException {
		
		Set<String> testStringSet = new HashSet<String>();
		String[] strings = {"asd","321","djfsk","asd","283","fjd"};
		for(String element : strings) {
			testStringSet.add(element);
		}
		assertEquals(testStringSet,(Set<String>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("StringSet"),
				"asd,321,djfsk,asd,283,fjd"));
		
		Set<Byte> testByteSet = new HashSet<Byte>();
		Byte[] bytes = {27,43,123,17,127,27};
		for(Byte element : bytes) {
			testByteSet.add(element);
		}
		assertEquals(testByteSet,(Set<Byte>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("ByteSet"),
				"27,43,123,17,127,27"));
		
		Set<Short> testShortSet = new HashSet<Short>();
		Short[] shorts = {17,4321,123,17,283,17};
		for(Short element : shorts) {
			testShortSet.add(element);
		}
		assertEquals(testShortSet,(Set<Short>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("ShortSet"),
				"17,4321,123,17,283,17"));
		
		Set<Integer> testIntegerSet = new HashSet<Integer>();
		Integer[] Integers = {22317,443,0,22317,2722,2722};
		for(Integer element : Integers) {
			testIntegerSet.add(element);
		}
		assertEquals(testIntegerSet,(Set<Integer>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("IntegerSet"),
				"22317,443,0,22317,2722,2722"));
		
		Set<Long> testLongSet = new HashSet<Long>();
		Long[] Longs = {22317231234321L,443L,0L,22317231234321L,2722L,2722L};
		for(Long element : Longs) {
			testLongSet.add(element);
		}
		assertEquals(testLongSet,(Set<Long>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("LongSet"),
				"22317231234321,443,0,22317231234321,2722,2722"));
		
		Set<Float> testFloatSet = new HashSet<Float>();
		Float[] Floats = {123.321F,32132.31232F,0F,328F,328F};
		for(Float element : Floats) {
			testFloatSet.add(element);
		}
		assertEquals(testFloatSet,(Set<Float>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("FloatSet"),
				"123.321,32132.31232,0,328,328"));
		
		Set<Double> testDoubleSet = new HashSet<Double>();
		Double[] Doubles = {12300000.00000321,32132.31232,0.,328.,328.00};
		for(Double element : Doubles) {
			testDoubleSet.add(element);
		}
		assertEquals(testDoubleSet,(Set<Double>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("DoubleSet"),
				"12300000.00000321,32132.31232,0.,328.,328.00"));
	}

	/**
	 * Test method for {@link util.properties.PropertiesParser#parse(java.lang.reflect.Type, java.lang.String)}.
	 * @throws InvocationTargetException 
	 * @throws ClassCastException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testParseLists()
		throws ClassCastException,
			   InvocationTargetException {
		
		List<String> testStringList = new ArrayList<String>();
		String[] strings = {"asd","321","djfsk","asd","283","fjd"};
		for(String element : strings) {
			testStringList.add(element);
		}
		assertEquals(testStringList,(List<String>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("StringList"),
				"asd,321,djfsk,asd,283,fjd"));
		
		List<Byte> testByteList = new ArrayList<Byte>();
		Byte[] bytes = {27,43,123,17,127,27};
		for(Byte element : bytes) {
			testByteList.add(element);
		}
		assertEquals(testByteList,(List<Byte>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("ByteList"),
				"27,43,123,17,127,27"));
		
		List<Short> testShortList = new ArrayList<Short>();
		Short[] shorts = {17,4321,123,17,283,17};
		for(Short element : shorts) {
			testShortList.add(element);
		}
		assertEquals(testShortList,(List<Short>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("ShortList"),
				"17,4321,123,17,283,17"));

		List<Integer> testIntegerList = new ArrayList<Integer>();
		Integer[] Integers = {22317,443,0,22317,2722,2722};
		for(Integer element : Integers) {
			testIntegerList.add(element);
		}
		assertEquals(testIntegerList,(List<Integer>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("IntegerList"),
				"22317,443,0,22317,2722,2722"));
		
		List<Long> testLongList = new ArrayList<Long>();
		Long[] Longs = {22317231234321L,443L,0L,22317231234321L,2722L,2722L};
		for(Long element : Longs) {
			testLongList.add(element);
		}
		assertEquals(testLongList,(List<Long>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("LongList"),
				"22317231234321,443,0,22317231234321,2722,2722"));
		
		List<Float> testFloatList = new ArrayList<Float>();
		Float[] Floats = {123.321F,32132.31232F,0F,328F,328F};
		for(Float element : Floats) {
			testFloatList.add(element);
		}
		assertEquals(testFloatList,(List<Float>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("FloatList"),
				"123.321,32132.31232,0,328,328"));
		
		List<Double> testDoubleList = new ArrayList<Double>();
		Double[] Doubles = {12300000.00000321,32132.31232,0.,328.,328.00};
		for(Double element : Doubles) {
			testDoubleList.add(element);
		}
		assertEquals(testDoubleList,(List<Double>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("DoubleList"),
				"12300000.00000321,32132.31232,0.,328.,328.00"));
	}
	
	/**
	 * Test method for {@link util.properties.PropertiesParser#parse(java.lang.reflect.Type, java.lang.String)}.
	 * @throws InvocationTargetException 
	 * @throws ClassCastException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testParseEmptySets() throws ClassCastException,
			InvocationTargetException {
	Set<String> testStringSet = new HashSet<String>();
		String[] strings = {};
		for(String element : strings) {
			testStringSet.add(element);
		}
		assertEquals(testStringSet,(Set<String>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("StringSet"), ""));
		
		Set<Integer> testIntegerSet = new HashSet<Integer>();
		Integer[] Integers = {};
		for(Integer element : Integers) {
			testIntegerSet.add(element);
		}
		assertEquals(testIntegerSet,(Set<Integer>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("IntegerSet"), ""));
	}

	/**
	 * Test method for {@link util.properties.PropertiesParser#parse(java.lang.reflect.Type, java.lang.String)}.
	 * @throws InvocationTargetException 
	 * @throws ClassCastException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testParseEmptyLists() throws ClassCastException,
			InvocationTargetException {
	List<String> testStringList = new ArrayList<String>();
		String[] strings = {};
		for(String element : strings) {
			testStringList.add(element);
		}
		assertEquals(testStringList,(List<String>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("StringList"), ""));
		
		List<Integer> testIntegerList = new ArrayList<Integer>();
		Integer[] Integers = {};
		for(Integer element : Integers) {
			testIntegerList.add(element);
		}
		assertEquals(testIntegerList,(List<Integer>)PropertiesParser.parse(
				PropertiesParser.getSpecialType("IntegerList"), ""));
	}
	
	/**
	 * Test method for {@link util.properties.PropertiesParser#parse(java.lang.reflect.Type, java.lang.String)}.
	 * @throws InvocationTargetException 
	 * @throws ClassCastException 
	 */
	@Test
	public void testParseEmptyArrays() throws ClassCastException,
			InvocationTargetException {
	String[] strings = {};
		assertArrayEquals(strings,(String[])PropertiesParser.parse(
				String[].class, ""));
		
		Integer[] Integers = {};
		assertArrayEquals(Integers,(Integer[])PropertiesParser.parse(
				Integer[].class, ""));
	}
}
