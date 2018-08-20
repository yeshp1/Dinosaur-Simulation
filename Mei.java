import java.util.List;

/**
 * A simple model of a Mei dinosaur.
 * Mei dinosaurs age, move, eat, and die. Their behavior may be affected by the environment.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2018.02.21
 * @improved by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */
public class Mei extends AnimalTimeSensitive
{
	// Property prefix used in the config file.
	private static String propertyPrefix = Mei.class.getName().toLowerCase() + ".";
	// The age at which a mei dinosaur can start to reproduce.
	private static final int REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "REPRODUCTION_AGE"));
	// The age at which the mei dinosaur stops breeding.
	private static final int MAX_REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_REPRODUCTION_AGE"));
	// The age to which a mei dinosaur can live.
	private static final int MAX_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_AGE"));
	// The likelihood of a mei dinosaur breeding.
	private static final double REPRODUCTION_PROBABILITY = Double.parseDouble(Config.getProperty(propertyPrefix + "REPRODUCTION_PROBABILITY"));
	// The maximum number of births a mei dinosaur can have at a single step.
	private static final int MAX_OFFSPRING = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_OFFSPRING"));
	// The amount of steps an animal can go after eating the mei dinosaur.
	private static final int CALORIES = Integer.parseInt(Config.getProperty(propertyPrefix + "CALORIES"));
	// The level of food the mei dinosaur needs to reach in order to stop eating.
	private static final int MAX_FOOD_VALUE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_FOOD_VALUE"));
	// The type of reproduction the mei dinosaur has.
	private static final boolean GENDERED_REPRODUCTION = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "GENDERED_REPRODUCTION"));
	// A string specifying the active time of day for the mei dinosaur.
	private static final String ACTIVE_TIME = Config.getProperty(propertyPrefix + "ACTIVE_TIME");
	// A list of the type of food the mei dinosaur can eat.
	private static final String[] CAN_EAT = Config.getProperty(propertyPrefix + "CAN_EAT").split(", ");
	// Whether the mei dinosaur needs to eat food in order to survive.
	private static final boolean DISABLED_HUNGER = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "DISABLE_HUNGER"));
	
	/**
	 * Create a new mei dinosaur. A mei dinosaur may be created with age
	 * zero (a new born) or with a random age.
	 * There is a probability that a mei dinosaur will be created diseased.
	 *
	 * @param randomAge If true, the mei dinosaur will have a random age.
	 * @param field     The field currently occupied by the mei dinosaur.
	 * @param location  The location of the mei dinosaur within the field.
	 */
	public Mei(boolean randomAge, Field field, Location location)
	{
		super(randomAge, field, location);
	}
	
	/**
	 * @return The calories of the mei dinosaur.
	 */
	public static int getCalories()
	{
		return CALORIES;
	}
	
	/**
	 * Check whether or not this mei dinosaur is to give birth at this step.
	 * New births will be made into free adjacent locations.
	 *
	 * @param newMei A list to return newly born mei dinosaurs.
	 */
	protected void giveBirth(List<Actor> newMei)
	{
		// New mei dinosaur are born into adjacent locations.
		// Get a list of adjacent free locations.
		List<Location> free = getField().getFreeAdjacentLocations(getLocation());
		int births = reproduce();
		for (int i = 0; i < births && free.size() > 0; i++) {
			Location loc = free.remove(0);
			Mei young = new Mei(false, getField(), loc);
			newMei.add(young);
		}
	}
	
	/**
	 * @return The maximum food value of the mei dinosaur.
	 */
	public int getMaxFoodValue()
	{
		return MAX_FOOD_VALUE;
	}
	
	/**
	 * @return The max age of the mei dinosaur.
	 */
	public int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * @return The breeding probability of the mei dinosaur.
	 */
	public double getReproductionProbability()
	{
		return REPRODUCTION_PROBABILITY;
	}
	
	/**
	 * @return The maximum size of the mei dinosaur's litter.
	 */
	public int getMaxOffspring()
	{
		return MAX_OFFSPRING;
	}
	
	/**
	 * @return The breeding age of the mei dinosaur.
	 */
	public int getReproductionAge()
	{
		return REPRODUCTION_AGE;
	}
	
	/**
	 * @return The maximum breeding age of the mei dinosaur.
	 */
	public int getMaxReproductionAge()
	{
		return MAX_REPRODUCTION_AGE;
	}
	
	/**
	 * @return A list of what the mei dinosaur can eat.
	 */
	public String[] getCanEat()
	{
		return CAN_EAT;
	}
	
	/**
	 * @return true If the mei dinosaur reproduces sexually.
	 */
	public boolean isGenderedReproduction()
	{
		return GENDERED_REPRODUCTION;
	}
	
	/**
	 * Check if the provided actor is an instance of the Mei class.
	 *
	 * @param actor The class we compare to.
	 * @return true If the actor is an instance of the Mei class.
	 */
	public boolean isSameSpecie(Actor actor)
	{
		return actor instanceof Mei;
	}
	
	/**
	 * Return the active time of day for the mei dinosaur as a string.
	 *
	 * @return active time of day as string.
	 */
	protected String getActiveTime()
	{
		return ACTIVE_TIME;
	}
	
	/**
	 * @return true If the mei dinosaur does not need to eat in order to live
	 */
	protected boolean getDisabledHunger()
	{
		return DISABLED_HUNGER;
	}
}

