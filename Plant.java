import java.util.List;

/**
 * This class models a generic plant.
 * Plants do not move, however they do spread, age, and die depending on their environment
 * and the weather in particular.
 * <p>
 * Plants may be born with a disease, but they may never spread it.
 *
 * @author by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 * @version 2018.02.21
 */
public class Plant extends Actor
{
	// String prefix for all config file properties for this class.
	private static final String propertyPrefix = Plant.class.getName().toLowerCase() + ".";
	// The maximum age of the plant.
	private static final int MAX_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_AGE"));
	// The minimum age to the plants needs to reach to start reproducing.
	private static final int REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "REPRODUCTION_AGE"));
	// The maximum age the plant needs to reach to stop reproducing.
	private static final int MAX_REPRODUCTION_AGE = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_REPRODUCTION_AGE"));
	// Maximum offspring that could be created by the plant in one step.
	private static final int MAX_OFFSPRING = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_OFFSPRING"));
	// The spreading probability of the plant.
	private static final double REPRODUCTION_PROBABILITY = Double.parseDouble(Config.getProperty(propertyPrefix + "REPRODUCTION_PROBABILITY"));
	// The maximum water level the plant can reach.
	private static final int MAX_WATER_LEVEL = Integer.parseInt(Config.getProperty(propertyPrefix + "MAX_WATER_LEVEL"));
	// The amount of steps an animal can go after eating the plant.
	private static final int CALORIES = Integer.parseInt(Config.getProperty(propertyPrefix + "CALORIES"));
	
	// The water level of the plant.
	private int water_level;
	
	/**
	 * Create an instance of a Plant.
	 * A plant can be created as a new born (age zero
	 * and water level of 20) or with a random age and water level.
	 * There is a probability that the plant will be born diseased.
	 *
	 * @param randomAge If true, the plant will have a random age, and water level.
	 * @param field     The field in which to create the plant.
	 * @param location  The location at which to create the plant.
	 */
	public Plant(boolean randomAge, Field field, Location location)
	{
		super(randomAge, field, location);
		if (randomAge) {
			setWaterLevel(getRand().nextInt(MAX_WATER_LEVEL));
		} else {
			setWaterLevel(20);
		}
	}
	
	
	/**
	 * The plant does not move. Sometimes it spreads around.
	 * It may dye from lack of water, from old age or disease.
	 *
	 * @param newPlants A list to return newly born plants.
	 */
	public void act(List<Actor> newPlants)
	{
		incrementAge();
		if (isActive()) {
			if (!(Environment.getCurrentWeather() == Environment.Weather.RAINY)) {
				decreaseWaterLevel();
				if (isActive()) {
					spreadAround(newPlants);
				}
			} else {
				increaseWaterLevel();
			}
			if (!isHealthy()) {
				incrementSickStepCount();
			}
			if (getSickStepCount() >= getDiseasedStepsTillDeath()) {
				setDead();
			}
		}
	}
	
	/**
	 * Check whether or not this plant is to spread at this step.
	 * New plants will be placed into free adjacent locations.
	 *
	 * @param newPlants A list to return new .
	 */
	private void spreadAround(List<Actor> newPlants)
	{
		// New plants are placed into adjacent locations.
		// Get a list of adjacent free locations.
		List<Location> free = getField().getFreeAdjacentLocations(getLocation());
		int plants = reproduce();
		for (int b = 0; b < plants && free.size() > 0; b++) {
			Location loc = free.remove(0);
			Plant young = new Plant(false, getField(), loc);
			newPlants.add(young);
		}
	}
	
	
	/**
	 * Set the water level of the plant.
	 *
	 * @param newWaterLevel The new water level of the plant.
	 */
	public void setWaterLevel(int newWaterLevel)
	{
		if (newWaterLevel >= 0) {
			water_level = newWaterLevel;
		} else {
			water_level = getRand().nextInt(MAX_WATER_LEVEL);
		}
	}
	
	/**
	 * Increase the water level of the plant.
	 * If cannot go above the maximum water level.
	 */
	public void increaseWaterLevel()
	{
		water_level++;
		if (water_level >= MAX_WATER_LEVEL) {
			water_level = MAX_WATER_LEVEL;
		}
	}
	
	/**
	 * Decrease the water level of the plant.
	 * If it reaches 0 the plant dies.
	 */
	public void decreaseWaterLevel()
	{
		water_level--;
		if (water_level <= 0) {
			setDead();
		}
	}
	
	/**
	 * Check if the provided actor is from the Plant class.
	 *
	 * @param actor The actor to compare to.
	 * @return true If the actor is an instance of the Plant class.
	 */
	public boolean isSameSpecie(Actor actor)
	{
		return actor instanceof Plant;
	}
	
	/**
	 * @return The maximum age of the plant.
	 */
	public int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * @return The minimum age for a plant to be able to reproduce.
	 */
	public int getReproductionAge()
	{
		return REPRODUCTION_AGE;
	}
	
	/**
	 * @return The maximum age for a plant to be able to reproduce.
	 */
	public int getMaxReproductionAge()
	{
		return MAX_REPRODUCTION_AGE;
	}
	
	/**
	 * @return Whether a plant can reproduce at this step.
	 */
	public boolean canReproduce()
	{
		return getAge() >= getReproductionAge() && getAge() <= getMaxReproductionAge();
	}
	
	/**
	 * @return The probability that a plant will reproduce during a step.
	 */
	public double getReproductionProbability()
	{
		return REPRODUCTION_PROBABILITY;
	}
	
	/**
	 * @return The maximum number of offspring a plant can produce per step.
	 */
	public int getMaxOffspring()
	{
		return MAX_OFFSPRING;
	}
}
