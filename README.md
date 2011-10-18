Account.java:

```java
class Account {
   @Property
   public Integer id;
   
   @Property
   public String name;
}
```

someAccount.properties:

```
 # A contrived example of a .properties file
 id=24
 name=Douglas Adams
```

Somewhere in your code:

```java
//...

Account someAccount = new Account();

PropertiesHandler propHandler = new PropertiesHandler(someAccount);
InputStream propStream = this.getContextClassLoader()
                             .getResourceAsStream("someAccount.properties");

Properties someAccountProperties = new Properties();
someAccountProperties.load(propStream);

propHandler.applyProperties(someAccountProperties);

System.out.println(someAccount.id);   // "24"
System.out.println(someAccount.name); // "Douglas Adams"

someAccount.id = 42;

someAccountProperties = propHandler.extractProperties();

someAccountProperties.store(
   new FileWriter("someAccount.properties"),
   "An updated contrived example of a .properties file"
);
```
 
Now, someAccount.properties looks like this:

```
# ... TimeStamp ...
# An updated contrived example of a .properties file
id=42
name=Douglas Adams
```

All primitive types (and their Object companions) are supported, as well as simple arrays, sets, and lists of such types.

See the [javadocs](http://namuol.github.com/Property-Annotations/) for details.
