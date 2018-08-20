import java.util.List;

/**
 * A simple model of a dodo.
 * Dodos age, move, reproduce, and die regardless of time of day or their environment.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2018.02.21
 * @improved by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */
public class Dodo extends Animal
{
	// Property prefix used in the config file.
	private static String propertyPrefix = Dodo.class.getName().toLowerCase() + ".";
	// The age at which a dodo can start to breed.
	private static final int REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "REPRODUCTION_AGE"));
	// The age at which the dodo stops breeding.
	private static final int MAX_REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_REPRODUCTION_AGE"));
	// The age to which a dodo can live.
	private static final int MAX_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_AGE"));
	// The likelihood of a dodo breeding.
	private static final double REPRODUCTION_PROBABILITY = Float.parseFloat(Config.getProperty(propertyPrefix + "REPRODUCTION_PROBABILITY"));
	// The maximum number of births a dodo can have per step.
	private static final int MAX_OFFSPRING = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_OFFSPRING"));
	// The amount of steps an animal can go after eating the dodo.
	private static final int CALORIES = Integer.parseInt(Config.getProperty(propertyPrefix + "CALORIES"));
	// The level of food the dodo needs to reach in order to stop eating.
	private static final int MAX_FOOD_VALUE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_FOOD_VALUE"));
	// The type of reproduction the dodo has.
	private static final boolean GENDERED_REPRODUCTION = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "GENDERED_REPRODUCTION"));
	// A list of the type of food the dodo can eat.
	private static final String[] CAN_EAT = Config.getProperty(propertyPrefix + "CAN_EAT").split(", ");
	// Whether the dodo needs to eat food in order to survive.
	private static final boolean DISABLED_HUNGER = Boolean.parseBoolean(Config.getProperty(propertyPrefix + "DISABLE_HUNGER"));
	
	
	/**
	 * Create a dodo. A dodo can be created as a new born (age zero
	 * and not hungry) or with a random age and food level.
	 * There is a probability that the dodo will be born diseased.
	 *
	 * @param randomStats If true, the dodo will have random age and hunger level.
	 * @param field       The field currently occupied by the dodo.
	 * @param location    The location of the dodo within the field.
	 */
	public Dodo(boolean randomStats, Field field, Location location)
	{
		super(randomStats, field, location);
	}
	
	/**
	 * Check whether or not this sodo is to give birth at this step.
	 * New births will be made into free adjacent locations.
	 *
	 * @param newDodos A list to return newly born dodos.
	 */
	protected void giveBirth(List<Actor> newDodos)
	{
		// New Dodos are born into adjacent locations.
		// Get a list of adjacent free locations.
		List<Location> free = getField().getFreeAdjacentLocations(getLocation());
		int births = reproduce();
		//Only breeds if there are two dodos of opposite sex in adjacent locations
		for (int b = 0; b < births && free.size() > 0; b++) {
			Location loc = free.remove(0);
			Dodo young = new Dodo(false, getField(), loc);
			newDodos.add(young);
		}
	}
	
	/**
	 * @return The maximum food value of the dodo.
	 */
	public int getMaxFoodValue()
	{
		return MAX_FOOD_VALUE;
	}
	
	/**
	 * @return The max age of the dodo.
	 */
	public int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * @return The breeding probability of the dodo.
	 */
	public double getReproductionProbability()
	{
		return REPRODUCTION_PROBABILITY;
	}
	
	/**
	 * @return The maximum size of the dodo's litter.
	 */
	public int getMaxOffspring()
	{
		return MAX_OFFSPRING;
	}
	
	/**
	 * @return The breeding age of the dodo.
	 */
	public int getReproductionAge()
	{
		return REPRODUCTION_AGE;
	}
	
	/**
	 * @return The maximum breeding age of the dodo.
	 */
	public int getMaxReproductionAge()
	{
		return MAX_REPRODUCTION_AGE;
	}
	
	/**
	 * @return A list of what the dodo can eat.
	 */
	public String[] getCanEat()
	{
		return CAN_EAT;
	}
	
	/**
	 * @return true If the dodo reproduces sexually.
	 */
	public boolean isGenderedReproduction()
	{
		return GENDERED_REPRODUCTION;
	}
	
	/**
	 * Check if the provided actor is an instance of the Dodo class.
	 *
	 * @param actor The class we compare to.
	 * @return true If the actor is an instance of the Dodo class.
	 */
	public boolean isSameSpecie(Actor actor)
	{
		return actor instanceof Dodo;
	}
	
	/**
	 * @return true If the animal does not need to eat in order to survive.
	 */
	protected boolean getDisabledHunger()
	{
		return DISABLED_HUNGER;
	}
}
