import java.util.List;

/**
 * A simple model of a stegosaurus.
 * Stegosaurus age, move, reproduce, and die. Its behavior may be affected by the time of day or the weather.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2018.02.21
 * @improved by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */
public class Stegosaurus extends AnimalSeasonalBreeding
{
	// Property prefix used in the config file.
	private static String propertyPrefix = Stegosaurus.class.getName().toLowerCase() + ".";
	// The age at which a stegosaurus can start to breed.
	private static final int REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "REPRODUCTION_AGE"));
	// The age at which the stegosaurus stops breeding.
	private static final int MAX_REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_REPRODUCTION_AGE"));
	// The age to which a stegosaurus can live.
	private static final int MAX_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_AGE"));
	// The likelihood of a stegosaurus breeding.
	private static final double REPRODUCTION_PROBABILITY = Float.parseFloat(Config.getProperty(propertyPrefix + "REPRODUCTION_PROBABILITY"));
	// The maximum number of births a stegosaurus can have at a single step.
	private static final int MAX_OFFSPRING = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_OFFSPRING"));
	// The amount of steps an animal can go after eating the stegosaurus.
	private static final int CALORIES = Integer.parseInt(Config.getProperty(propertyPrefix + "CALORIES"));
	// The level of food the stegosaurus needs to reach in order to stop eating.
	private static final int MAX_FOOD_VALUE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_FOOD_VALUE"));
	// The type of reproduction the stegosaurus has.
	private static final boolean GENDERED_REPRODUCTION = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "GENDERED_REPRODUCTION"));
	// A list of the type of food the stegosaurus can eat.
	private static final String[] CAN_EAT = Config.getProperty(propertyPrefix + "CAN_EAT").split(", ");
	// Whether the stegosaurus needs to eat food in order to survive.
	private static final boolean DISABLED_HUNGER = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "DISABLE_HUNGER"));
	// The weather during which the stegosaurus is active.
	private static final String[] ACTIVE_WEATHER = Config.getProperty(propertyPrefix + "ACTIVE_WEATHER").split(", ");
	
	
	/**
	 * Create a stegosaurus. A stegosaurus can be created as a new born (age zero
	 * and not hungry) or with a random age and food level.
	 * There is a probability that the stegosaurus will be born diseased.
	 *
	 * @param randomAge If true, the stegosaurus will have random age and hunger level.
	 * @param field     The field currently occupied by the stegosaurus.
	 * @param location  The location of the stegosaurus within the field.
	 */
	public Stegosaurus(boolean randomAge, Field field, Location location)
	{
		super(randomAge, field, location);
	}
	
	/**
	 * @return The calories of the stegosaurus.
	 */
	public static int getCalories()
	{
		return CALORIES;
	}
	
	
	/**
	 * Check whether or not this stegosaurus is to give birth at this step.
	 * New births will be made into free adjacent locations.
	 *
	 * @param newStegosaurus A list to return newly born stegosauruses.
	 */
	protected void giveBirth(List<Actor> newStegosaurus)
	{
		// New stegosauruses are born into adjacent locations.
		// Get a list of adjacent free locations.
		List<Location> free = getField().getFreeAdjacentLocations(getLocation());
		int births = reproduce();
		//Only breeds if there are two stegosaurus of opposite sex in adjacent locations
		for (int b = 0; b < births && free.size() > 0; b++) {
			Location loc = free.remove(0);
			Stegosaurus young = new Stegosaurus(false, getField(), loc);
			newStegosaurus.add(young);
		}
	}
	
	/**
	 * @return The maximum food value of the stegosaurus.
	 */
	public int getMaxFoodValue()
	{
		return MAX_FOOD_VALUE;
	}
	
	/**
	 * @return The max age of the stegosaurus.
	 */
	public int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * @return The breeding probability of the stegosaurus.
	 */
	public double getReproductionProbability()
	{
		return REPRODUCTION_PROBABILITY;
	}
	
	/**
	 * @return The maximum size of the stegosaurus's litter.
	 */
	public int getMaxOffspring()
	{
		return MAX_OFFSPRING;
	}
	
	/**
	 * @return The breeding age of the stegosaurus.
	 */
	public int getReproductionAge()
	{
		return REPRODUCTION_AGE;
	}
	
	/**
	 * @return The maximum breeding age of the stegosaurus.
	 */
	public int getMaxReproductionAge()
	{
		return MAX_REPRODUCTION_AGE;
	}
	
	/**
	 * @return A list of what the stegosaurus can eat.
	 */
	public String[] getCanEat()
	{
		return CAN_EAT;
	}
	
	/**
	 * @return A list of the weather during which the stegosaurus is active.
	 */
	protected String[] getActiveWeather()
	{
		return ACTIVE_WEATHER;
	}
	
	/**
	 * @return true If the stegosaurus reproduces sexually.
	 */
	public boolean isGenderedReproduction()
	{
		return GENDERED_REPRODUCTION;
	}
	
	/**
	 * Check if the provided actor is an instance of the Stegosaurus class.
	 *
	 * @param actor The class we compare to.
	 * @return true If the actor is an instance of the Stegosaurus class.
	 */
	public boolean isSameSpecie(Actor actor)
	{
		return actor instanceof Stegosaurus;
	}
	
	/**
	 * @return true If the stegosaurus does not need to eat in order to survive.
	 */
	protected boolean getDisabledHunger()
	{
		return DISABLED_HUNGER;
	}
}
