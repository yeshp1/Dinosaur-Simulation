import java.util.List;

/**
 * A simple model of a velociraptor.
 * Velociraptors age, move, eat, and die. Their behavior may be affected by the time of day and the environment.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2018.02.21
 * @imporved by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */

public class Velociraptor extends AnimalTimeSensitive
{
	// Property prefix used in the config file.
	private static String propertyPrefix = Velociraptor.class.getName().toLowerCase() + ".";
	// The age at which a velociraptor can start to reproduce.
	private static final int REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "REPRODUCTION_AGE"));
	// The age at which the velociraptor stops breeding.
	private static final int MAX_REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_REPRODUCTION_AGE"));
	// The age to which a velociraptor can live.
	private static final int MAX_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_AGE"));
	// The likelihood of a velociraptor breeding.
	private static final double REPRODUCTION_PROBABILITY = Double.parseDouble(Config.getProperty(propertyPrefix + "REPRODUCTION_PROBABILITY"));
	// The maximum number of births a velociraptor can have at a single step.
	private static final int MAX_OFFSPRING = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_OFFSPRING"));
	// The amount of steps an animal can go after eating the velociraptor.
	private static final int CALORIES = Integer.parseInt(Config.getProperty(propertyPrefix + "CALORIES"));
	// The level of food the velociraptor needs to reach in order to stop eating.
	private static final int MAX_FOOD_VALUE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_FOOD_VALUE"));
	// The type of reproduction the velociraptor has.
	private static final boolean GENDERED_REPRODUCTION = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "GENDERED_REPRODUCTION"));
	// A string specifying the active time of day for the velociraptor.
	private static final String ACTIVE_TIME = Config.getProperty(propertyPrefix + "ACTIVE_TIME");
	// A list of the type of food the velociraptor can eat.
	private static final String[] CAN_EAT = Config.getProperty(propertyPrefix + "CAN_EAT").split(", ");
	// Whether the velociraptor needs to eat food in order to survive.
	private static final boolean DISABLED_HUNGER = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "DISABLE_HUNGER"));
	
	/**
	 * Create a new Velociraptor. A velociraptor may be created with age
	 * zero (a new born) or with a random age.
	 * There is a possibility that the velociraptor will be born diseased.
	 *
	 * @param randomAge If true, the velociraptor will have a random age.
	 * @param field     The field currently occupied by the velociraptor.
	 * @param location  The location of the velociraptor within the field.
	 */
	public Velociraptor(boolean randomAge, Field field, Location location)
	{
		super(randomAge, field, location);
	}
	
	/**
	 * @return The calories of the velociraptor.
	 */
	public static int getCalories()
	{
		return CALORIES;
	}
	
	/**
	 * Check whether or not this velociraptor is to give birth at this step.
	 * New births will be made into free adjacent locations.
	 *
	 * @param newVelociraptors A list to return newly born velociraptors.
	 */
	protected void giveBirth(List<Actor> newVelociraptors)
	{
		// New velociraptors are born into adjacent locations.
		// Get a list of adjacent free locations.
		List<Location> free = getField().getFreeAdjacentLocations(getLocation());
		int births = reproduce();
		for (int i = 0; i < births && free.size() > 0; i++) {
			Location loc = free.remove(0);
			Velociraptor young = new Velociraptor(false, getField(), loc);
			newVelociraptors.add(young);
		}
	}
	
	/**
	 * @return The maximum food value of the velociraptor.
	 */
	public int getMaxFoodValue()
	{
		return MAX_FOOD_VALUE;
	}
	
	/**
	 * @return The max age of the velociraptor.
	 */
	public int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * @return The breeding probability of the velociraptor.
	 */
	public double getReproductionProbability()
	{
		return REPRODUCTION_PROBABILITY;
	}
	
	/**
	 * @return The maximum size of the velociraptor's litter.
	 */
	public int getMaxOffspring()
	{
		return MAX_OFFSPRING;
	}
	
	/**
	 * @return The breeding age of the velociraptor.
	 */
	public int getReproductionAge()
	{
		return REPRODUCTION_AGE;
	}
	
	/**
	 * @return The maximum breeding age of the velociraptor.
	 */
	public int getMaxReproductionAge()
	{
		return MAX_REPRODUCTION_AGE;
	}
	
	/**
	 * @return A list of what the velociraptor can eat.
	 */
	public String[] getCanEat()
	{
		return CAN_EAT;
	}
	
	/**
	 * @return true If the velociraptor reproduces sexually.
	 */
	public boolean isGenderedReproduction()
	{
		return GENDERED_REPRODUCTION;
	}
	
	/**
	 * Check if the provided actor is an instance of the Velociraptor class.
	 *
	 * @param actor The class we compare to.
	 * @return true If the actor is an instance of the velociraptor class.
	 */
	public boolean isSameSpecie(Actor actor)
	{
		return actor instanceof Velociraptor;
	}
	
	/**
	 * Return the active time of day for the velociraptor as a string.
	 *
	 * @return active time of day as string.
	 */
	protected String getActiveTime()
	{
		return ACTIVE_TIME;
	}
	
	/**
	 * @return true If the velociraptor does not need to eat in order to survive.
	 */
	protected boolean getDisabledHunger()
	{
		return DISABLED_HUNGER;
	}
}

