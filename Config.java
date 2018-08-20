import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;

/**
 * Singleton class to load and add retrieval functionality to a configuration
 * file 'config.cfg' in the source directory using the Java properties API.
 * <p>
 * This is a singleton class.
 *
 * @author David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 * @version 2018.02.21
 */
public class Config
{
	// The instance of the Config class
	private static Config instance;
	// Loaded information from the config.cfg file.
	private static Properties configFile;
	
	/**
	 * Load the property list from config.cfg.
	 */
	private Config()
	{
		configFile = new java.util.Properties();
		try {
			configFile.load(getClass().getClassLoader().getResourceAsStream("config.cfg"));
		} catch (Exception eta) {
			eta.printStackTrace();
		}
	}
	
	/**
	 * Returns the string value associated with given string key.
	 * If an instance of the Config class has not yet been created, and therefore
	 * the properties not loaded, instantiate Config object.
	 *
	 * @param key - string property name that is associated with a value.
	 * @return the value associated with given string key, also as a string.
	 */
	public static String getProperty(String key)
	{
		if (instance == null) instance = new Config();
		return instance.getValue(key);
	}
	
	/**
	 * Returns a Set of strings containing all property lines whose property
	 * names contain a given substring.
	 *
	 * @param substring - substring to search for in property names.
	 * @return Set containing all property lines whose property
	 * names contain the substring.
	 */
	public static HashSet<String> getPropertiesContaining(String substring)
	{
		HashSet<String> set = new HashSet<>();
		Enumeration<?> enumeration = Config.configFile.propertyNames();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			if (key.contains(substring)) set.add(key);
		}
		return set;
	}
	
	/**
	 * Returns the string value associated with given string key.
	 *
	 * @param key - string property name that is associated with a value.
	 * @return the value associated with given string key, also as a string.
	 */
	private String getValue(String key)
	{
		return configFile.getProperty(key);
	}
}

