import java.util.List;

/**
 * A simple model of a Tyrannosaurus.
 * Tyrannosauruses age, move, eat, and die. Their behavior may be affected by the time of day and the weather.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2018.02.21
 * @imporved by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */
public class Tyrannosaurus extends AnimalTimeSensitive
{
	// Property prefix used in the config file.
	private static String propertyPrefix = Tyrannosaurus.class.getName().toLowerCase() + ".";
	// The age at which a tyrannosaurus can start to reproduce.
	private static final int REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "REPRODUCTION_AGE"));
	// The age at which the tyrannosaurus stops breeding.
	private static final int MAX_REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_REPRODUCTION_AGE"));
	// The age to which a tyrannosaurus can live.
	private static final int MAX_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_AGE"));
	// The likelihood of a tyrannosaurus breeding.
	private static final double REPRODUCTION_PROBABILITY = Double.parseDouble(Config.getProperty(propertyPrefix + "REPRODUCTION_PROBABILITY"));
	// The maximum number of births a tyrannosaurus can have at a single step.
	private static final int MAX_OFFSPRING = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_OFFSPRING"));
	// The amount of steps an animal can go after eating the tyrannosaurus.
	private static final int CALORIES = Integer.parseInt(Config.getProperty(propertyPrefix + "CALORIES"));
	// The level of food the tyrannosaurus needs to reach in order to stop eating.
	private static final int MAX_FOOD_VALUE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_FOOD_VALUE"));
	// The type of reproduction the tyrannosaurus has.
	private static final boolean GENDERED_REPRODUCTION = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "GENDERED_REPRODUCTION"));
	// A string specifying the active time of day for the tyrannosaurus.
	private static final String ACTIVE_TIME = Config.getProperty(propertyPrefix + "ACTIVE_TIME");
	// A list of the type of food the tyrannosaurus can eat.
	private static final String[] CAN_EAT = Config.getProperty(propertyPrefix + "CAN_EAT").split(", ");
	// Whether the tyrannosaurus needs to eat food in order to survive.
	private static final boolean DISABLED_HUNGER = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "DISABLE_HUNGER"));
	
	/**
	 * Create a new Tyrannosaurus. A tyrannosaurus may be created with age
	 * zero (a new born) or with a random age.
	 * There is a possibility that the tyrannosaurus will be born diseased.
	 *
	 * @param randomAge If true, the tyrannosaurus will have a random age.
	 * @param field     The field currently occupied by the tyrannosaurus.
	 * @param location  The location of the tyrannosaurus within the field.
	 */
	public Tyrannosaurus(boolean randomAge, Field field, Location location)
	{
		super(randomAge, field, location);
	}
	
	/**
	 * @return The calories of the tyrannosaurus.
	 */
	public static int getCalories()
	{
		return CALORIES;
	}
	
	/**
	 * Check whether or not this tyrannosaurus is to give birth at this step.
	 * New births will be made into free adjacent locations.
	 *
	 * @param newTyrannosauruses A list to return newly born tyrannosauruses.
	 */
	protected void giveBirth(List<Actor> newTyrannosauruses)
	{
		// New tyrannosauruses are born into adjacent locations.
		// Get a list of adjacent free locations.
		List<Location> free = getField().getFreeAdjacentLocations(getLocation());
		int births = reproduce();
		for (int i = 0; i < births && free.size() > 0; i++) {
			Location loc = free.remove(0);
			Tyrannosaurus young = new Tyrannosaurus(false, getField(), loc);
			newTyrannosauruses.add(young);
		}
	}
	
	/**
	 * @return The maximum food value of the tyrannosaurus.
	 */
	public int getMaxFoodValue()
	{
		return MAX_FOOD_VALUE;
	}
	
	/**
	 * @return The max age of the tyrannosaurus.
	 */
	public int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * @return The breeding probability of the ryrannosaurus.
	 */
	public double getReproductionProbability()
	{
		return REPRODUCTION_PROBABILITY;
	}
	
	/**
	 * @return The maximum size of the tyrannosaurus's litter.
	 */
	public int getMaxOffspring()
	{
		return MAX_OFFSPRING;
	}
	
	/**
	 * @return The breeding age of the tyrannosaurus.
	 */
	public int getReproductionAge()
	{
		return REPRODUCTION_AGE;
	}
	
	/**
	 * @return The maximum breeding age of the tyrannosaurus.
	 */
	public int getMaxReproductionAge()
	{
		return MAX_REPRODUCTION_AGE;
	}
	
	/**
	 * @return A list of what the tyrannosaurus can eat.
	 */
	public String[] getCanEat()
	{
		return CAN_EAT;
	}
	
	/**
	 * @return true If the tyrannosaurus reproduces sexually.
	 */
	public boolean isGenderedReproduction()
	{
		return GENDERED_REPRODUCTION;
	}
	
	/**
	 * Check if the provided actor is an instance of the Tyrannosaurus class.
	 *
	 * @param actor The class we compare to.
	 * @return true If the actor is an instance of the Tyrannosaurus class.
	 */
	public boolean isSameSpecie(Actor actor)
	{
		return actor instanceof Tyrannosaurus;
	}
	
	/**
	 * Return the active time of day for the tyrannosaurus as a string.
	 *
	 * @return active time of day as string.
	 */
	protected String getActiveTime()
	{
		return ACTIVE_TIME;
	}
	
	/**
	 * @return true If the tyrannosaurus does not need to eat in order to survive.
	 */
	protected boolean getDisabledHunger()
	{
		return DISABLED_HUNGER;
	}
}
