package util.properties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Louis Acresti
 *
 */
@SuppressWarnings("unused")
class PropertiesParser {
	
	@SuppressWarnings("unchecked")
	public static Object parse(Type type, String string) 
		throws ClassCastException,
			   InvocationTargetException {
		if(type == null) return null;
		
		Method parser;
		if(type instanceof ParameterizedType) {
			// Advanced: we have something like Set<> or List<>
			ParameterizedType ptype = (ParameterizedType)type;
			
			Type[] typeParams = ptype.getActualTypeArguments();
			if(typeParams.length != 1) {
				// Ensure that this is a single-type-param type (ie Set<String>)
				return null;
			} else if(!(typeParams[0] instanceof Class<?>)) {
				// Ensure that the type parameter is _not_ a primitive type,
				//  as they have allocation issues.
				return null;
			}
			
			Class<?> rawType = (Class<?>)ptype.getRawType(); // Set, List, etc.
			String specialName = ((Class<?>)typeParams[0]).getSimpleName()
								 + rawType.getSimpleName();
			
			Type specialType = getSpecialType(specialName);
			if(rawType == Set.class) {
				@SuppressWarnings("rawtypes")
				Set set =
					parseCollection(string, (Class<?>)typeParams[0], "Set");
				return set;
			} else if(rawType == List.class) {
				@SuppressWarnings("rawtypes")
				List list =
					parseCollection(string, (Class<?>)typeParams[0], "List");
				return list;
			} else {
				parser = null;
			}
		} else {
			// Basic: a simple primitive.
			parser = parserMap.get(type);
		}
		
		if(parser != null) {
			try {
				return parser.invoke(null, string);
			} catch (IllegalArgumentException e) {
				e.printStackTrace(); // Should never happen.
			} catch (IllegalAccessException e) {
				e.printStackTrace(); // Should never happen.
			}
		} else {
			// No parser is found; let's try to cast the string directly:
			return ((Class<?>)type).cast(string);
		}
		return null;
	}
	
	///// Kludge kludge kludge: ////////////////////////////////////////////////
	// Unfortunately, Java's "erasure" prevents us from being able to specify
	//  generic types at runtime (that is to say, "Set<String>" becomes "Set" at 
	//	runtime), but there's a workaround: if you have a field or a member
	//  method whose argument or return value is of the specific type you need,
	//  you can access it using the "getGenericType" method on the corresponding
	//  Field object using Java's reflection library, meaning you *can* specify
	//  this generic type at runtime, but only by pointing to predefined
	//  instances that were specified at compiletime.
	//
	// So in order to specify such types, I created a bunch of unusable fields 
	//  and return a call to "getGenericType()" on their corresponding Field
	//  objects in a method called "getSpecialType" which accepts a string that
	//  matches the type's Field name (following a convention Type<Subtype> -> 
	//  "SubtypeType"). This isn't meant to be used outside of the
	//  PropertiesParser package.
	static Type getSpecialType(String name) {
		try {
			return (Type)PropertiesParser.class.getDeclaredField(name)
										 	   .getGenericType();
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
	
	private static final Set<String> StringSet = null;
	private static final Set<Byte> ByteSet = null;
	private static final Set<Short> ShortSet = null;
	private static final Set<Integer> IntegerSet = null;
	private static final Set<Long> LongSet = null;
	private static final Set<Float> FloatSet = null;
	private static final Set<Double> DoubleSet = null;
	
	private static final List<String> StringList = null;
	private static final List<Byte> ByteList = null;
	private static final List<Short> ShortList = null;
	private static final List<Integer> IntegerList = null;
	private static final List<Long> LongList = null;
	private static final List<Float> FloatList = null;
	private static final List<Double> DoubleList = null;
	
	static final Map<Type,Method> allocatorMap = new HashMap<Type,Method>();
	static {
		try {
			allocatorMap.put(String.class,
					PropertiesParser.class.getDeclaredMethod("newStringArray",int.class));
			allocatorMap.put(Boolean.class,
					PropertiesParser.class.getDeclaredMethod("newBooleanArray",int.class));
			allocatorMap.put(Byte.class,
					PropertiesParser.class.getDeclaredMethod("newByteArray",int.class));
			allocatorMap.put(Short.class,
					PropertiesParser.class.getDeclaredMethod("newShortArray",int.class));
			allocatorMap.put(Integer.class,
					PropertiesParser.class.getDeclaredMethod("newIntegerArray",int.class));
			allocatorMap.put(Long.class,
					PropertiesParser.class.getDeclaredMethod("newLongArray",int.class));
			allocatorMap.put(Float.class,
					PropertiesParser.class.getDeclaredMethod("newFloatArray",int.class));
			allocatorMap.put(Double.class,
					PropertiesParser.class.getDeclaredMethod("newDoubleArray",int.class));

			allocatorMap.put(getSpecialType("StringSet"),
					PropertiesParser.class.getDeclaredMethod("newStringSet",int.class));
			allocatorMap.put(getSpecialType("ByteSet"),
					PropertiesParser.class.getDeclaredMethod("newByteSet",int.class));
			allocatorMap.put(getSpecialType("ShortSet"),
					PropertiesParser.class.getDeclaredMethod("newShortSet",int.class));
			allocatorMap.put(getSpecialType("IntegerSet"),
					PropertiesParser.class.getDeclaredMethod("newIntegerSet",int.class));
			allocatorMap.put(getSpecialType("LongSet"),
					PropertiesParser.class.getDeclaredMethod("newLongSet",int.class));
			allocatorMap.put(getSpecialType("FloatSet"),
					PropertiesParser.class.getDeclaredMethod("newFloatSet",int.class));
			allocatorMap.put(getSpecialType("DoubleSet"),
					PropertiesParser.class.getDeclaredMethod("newDoubleSet",int.class));
			
			allocatorMap.put(getSpecialType("StringList"),
					PropertiesParser.class.getDeclaredMethod("newStringList",int.class));
			allocatorMap.put(getSpecialType("BooleanList"),
					PropertiesParser.class.getDeclaredMethod("newBooleanList",int.class));
			allocatorMap.put(getSpecialType("ByteList"),
					PropertiesParser.class.getDeclaredMethod("newByteList",int.class));
			allocatorMap.put(getSpecialType("ShortList"),
					PropertiesParser.class.getDeclaredMethod("newShortList",int.class));
			allocatorMap.put(getSpecialType("IntegerList"),
					PropertiesParser.class.getDeclaredMethod("newIntegerList",int.class));
			allocatorMap.put(getSpecialType("LongList"),
					PropertiesParser.class.getDeclaredMethod("newLongList",int.class));
			allocatorMap.put(getSpecialType("FloatList"),
					PropertiesParser.class.getDeclaredMethod("newFloatList",int.class));
			allocatorMap.put(getSpecialType("DoubleList"),
					PropertiesParser.class.getDeclaredMethod("newDoubleList",int.class));
		} catch (NoSuchMethodException e) {
			// Should never happen.
			e.printStackTrace();
		}
	}
	
	// Allocators required for generic code:
	// ARRAYS:
	private static String[] newStringArray(int length) { return new String[length]; }
	private static Boolean[] newBooleanArray(int length) { return new Boolean[length]; }
	private static Byte[] newByteArray(int length) { return new Byte[length]; }
	private static Short[] newShortArray(int length) { return new Short[length]; }
	private static Integer[] newIntegerArray(int length) { return new Integer[length]; }
	private static Long[] newLongArray(int length) { return new Long[length]; }
	private static Float[] newFloatArray(int length) { return new Float[length]; }
	private static Double[] newDoubleArray(int length) { return new Double[length]; }
	
	// SETS:
	private static Set<String> newStringSet(int length) { return new HashSet<String>(); }
	private static Set<Byte> newByteSet(int length) { return new HashSet<Byte>(); }
	private static Set<Short> newShortSet(int length) { return new HashSet<Short>(); }
	private static Set<Integer> newIntegerSet(int length) { return new HashSet<Integer>(); }
	private static Set<Long> newLongSet(int length) { return new HashSet<Long>(); }
	private static Set<Float> newFloatSet(int length) { return new HashSet<Float>(); }
	private static Set<Double> newDoubleSet(int length) { return new HashSet<Double>(); }
	
	// LISTS:
	private static List<String> newStringList(int length) { return new ArrayList<String>(); }
	private static List<Boolean> newBooleanList(int length) { return new ArrayList<Boolean>(); }
	private static List<Byte> newByteList(int length) { return new ArrayList<Byte>(); }
	private static List<Short> newShortList(int length) { return new ArrayList<Short>(); }
	private static List<Integer> newIntegerList(int length) { return new ArrayList<Integer>(); }
	private static List<Long> newLongList(int length) { return new ArrayList<Long>(); }
	private static List<Float> newFloatList(int length) { return new ArrayList<Float>(); }
	private static List<Double> newDoubleList(int length) { return new ArrayList<Double>(); }
	//
	////// END Kludge kludge kludge. ///////////////////////////////////////////
	
	static final Map<Type,Method> parserMap = new HashMap<Type,Method>();
	static {
		try {
			parserMap.put(boolean.class,Boolean.class.getMethod("parseBoolean", String.class));
			parserMap.put(Boolean.class, Boolean.class.getMethod("parseBoolean", String.class));
			parserMap.put(byte.class, Byte.class.getMethod("parseByte", String.class));
			parserMap.put(Byte.class, Byte.class.getMethod("parseByte", String.class));
			parserMap.put(short.class, Short.class.getMethod("parseShort", String.class));
			parserMap.put(Short.class, Short.class.getMethod("parseShort", String.class));
			parserMap.put(int.class, Integer.class.getMethod("parseInt", String.class));
			parserMap.put(Integer.class, Integer.class.getMethod("parseInt", String.class));
			parserMap.put(long.class, Long.class.getMethod("parseLong", String.class));
			parserMap.put(Long.class, Long.class.getMethod("parseLong", String.class));
			parserMap.put(float.class, Float.class.getMethod("parseFloat", String.class));
			parserMap.put(Float.class, Float.class.getMethod("parseFloat", String.class));
			parserMap.put(double.class, Double.class.getMethod("parseDouble", String.class));
			parserMap.put(Double.class, Double.class.getMethod("parseDouble", String.class));
			
			// Array types:
			parserMap.put(String[].class, 
					PropertiesParser.class.getDeclaredMethod("parseStringArray", String.class));
			parserMap.put(Boolean[].class, 
					PropertiesParser.class.getDeclaredMethod("parseBooleanArray", String.class));
			parserMap.put(Byte[].class, 
					PropertiesParser.class.getDeclaredMethod("parseByteArray", String.class));
			parserMap.put(Short[].class, 
					PropertiesParser.class.getDeclaredMethod("parseShortArray", String.class));
			parserMap.put(Integer[].class, 
					PropertiesParser.class.getDeclaredMethod("parseIntegerArray", String.class));
			parserMap.put(Long[].class, 
					PropertiesParser.class.getDeclaredMethod("parseLongArray", String.class));
			parserMap.put(Float[].class, 
					PropertiesParser.class.getDeclaredMethod("parseFloatArray", String.class));
			parserMap.put(Double[].class, 
					PropertiesParser.class.getDeclaredMethod("parseDoubleArray", String.class));
		} catch (NoSuchMethodException e) {
			// This should never happen.
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T[] parseArray(String string, Class<T> type)
			throws InvocationTargetException {
		String[] splitString;
		Method parser;
		Method allocator;
		Integer size;
		
		if(string == null || string.length() == 0) {
			size = 0;
			splitString = new String[0];
		} else {
			splitString = string.split(",");
			size = splitString.length;
		}
		
		allocator = allocatorMap.get(type);
		parser = parserMap.get(type);
		
		T[] array = null;
		try {
			array = (T[]) allocator.invoke(null, splitString.length);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		int i=0;
		for(String sub : splitString) {
			if(sub.length() == 0) {
				break; // Skip over empties
			}
			
			try {
				T obj;
				if(parser == null) {
					obj = type.cast(sub); // Attempt to cast to T
				} else {
					obj = type.cast(parser.invoke(type, sub));
				}
				array[i] = obj;
				++i;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return array;
	}
	
	@SuppressWarnings("unchecked")
	private static <E, T extends Collection<E>> T parseCollection(
			String string, Class<E> type, String collectionType)
			throws InvocationTargetException {
		Type t = getSpecialType(type.getSimpleName() + collectionType);
		Method allocator = allocatorMap.get(t);
		E[] array = parseArray(string, type);
		T collection = null;
		try {
			collection = (T)allocator.invoke(null, array.length);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		for(E element : array) {
			collection.add(element);
		}
		
		return collection;
	}
	
	private static String[] parseStringArray(String string)
			throws InvocationTargetException {
		return PropertiesParser.parseArray(string, String.class);
	}

	private static Boolean[] parseBooleanArray(String string)
			throws InvocationTargetException {
		return PropertiesParser.parseArray(string, Boolean.class);
	}

	private static Byte[] parseByteArray(String string)
			throws InvocationTargetException {
		return PropertiesParser.parseArray(string, Byte.class);
	}

	private static Short[] parseShortArray(String string)
			throws InvocationTargetException {
		return PropertiesParser.parseArray(string, Short.class);
	}

	private static Integer[] parseIntegerArray(String string)
			throws InvocationTargetException {
		return PropertiesParser.parseArray(string, Integer.class);
	}

	private static Long[] parseLongArray(String string)
			throws InvocationTargetException {
		return PropertiesParser.parseArray(string, Long.class);
	}

	private static Float[] parseFloatArray(String string)
			throws InvocationTargetException {
		return PropertiesParser.parseArray(string, Float.class);
	}

	private static Double[] parseDoubleArray(String string)
			throws InvocationTargetException {
		return PropertiesParser.parseArray(string, Double.class);
	}
}
