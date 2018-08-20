/**
 * Class that models a single property line as seen in the config.cfg file.
 * <p>
 * It is needed as the syntax used in this program differs from common
 * usage and therefore had to be extended.
 * <p>
 * An example property line in the config.cfg file is as follows:
 * <p>
 * *className*.*propertyName* = *value*
 * <p>
 * Note that the className within the config.cfg file is unqualified,
 * (it is not written with a capitol letter.)
 *
 * @author David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 * @version 2018.02.21
 */
class PropertyLine
{
	private String classString;
	private String classStringQualified;
	private String propertyName;
	private String value;
	
	/**
	 * Constructor for the PropertyLine. It deconstructs the given property
	 * line string into its subordinate pieces and assigns them to appropriate
	 * field within the object instance.
	 *
	 * @param propertyLine - string property line in the following format
	 *                     *className*.*propertyName* = *value*
	 */
	public PropertyLine(String propertyLine)
	{
		this.classString = propertyLine.split("\\.")[0];
		this.classStringQualified = classString.substring(0, 1).toUpperCase() + classString.substring(1);
		this.propertyName = propertyLine.split("=")[0].trim();
		this.value = Config.getProperty(propertyName);
	}
	
	/**
	 * Returns the unqualified class name of the given property line.
	 *
	 * @return - string unqualified class name.
	 */
	public String getClassString()
	{
		return classString;
	}
	
	/**
	 * Returns the qualified class name of the given property line.
	 *
	 * @return - string qualified class name.
	 */
	public String getClassStringQualified()
	{
		return classStringQualified;
	}
	
	/**
	 * Returns the property name of the given property line.
	 *
	 * @return - string property name.
	 */
	public String getPropertyName()
	{
		return propertyName;
	}
	
	/**
	 * Returns the value of the given property line as a string.
	 *
	 * @return - value of a property as a string.
	 */
	public String getValue()
	{
		return value;
	}
}