import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of all animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2018.02.21
 * @imporved by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */
public abstract class Animal extends Actor
{
	// The gender of the animal.
	private Gender gender;
	// The food level of the animal that is increased by eating.
	private int foodLevel;
	
	/**
	 * Create an animal. An animal can be created as a new born (age zero
	 * and not hungry) or with a random age and food level.
	 * There is a probability that the animal will be born diseased.
	 *
	 * @param randomStats If true, the animal will have random age and hunger level.
	 * @param field       The field currently occupied by the animal.
	 * @param location    The location of the animal within the field.
	 */
	protected Animal(boolean randomStats, Field field, Location location)
	{
		super(randomStats, field, location);
		setAnimalGender();
		if (randomStats) {
			setFoodLevel(getRand().nextInt(getMaxFoodValue()));
		} else {
			setFoodLevel(getMaxFoodValue());
		}
	}
	
	/**
	 * A method representing what the animals do - move, look for food and possibly breed.
	 * In the process, it might die of hunger, get diseased (from mutation
	 * or from eating a diseased prey), die of disease, or of old age.
	 *
	 * @param newAnimals A list to return newly born animals.
	 */
	public void act(List<Actor> newAnimals)
	{
		incrementAge();
		incrementHunger();
		if (isActive()) {
			Location newLocation = null;
			if (isHealthy()) {
				giveBirth(newAnimals);
				// Move towards a source of food if found.
				newLocation = findFood();
			} else {
				incrementSickStepCount();
				infect();
			}
			if (newLocation == null) {
				// No food found - try to move to a free location.
				newLocation = getField().freeAdjacentLocation(getLocation());
			}
			// See if it was possible to move.
			if (newLocation != null) {
				setLocation(newLocation);
			}
			if (newLocation == null || getSickStepCount() >= getDiseasedStepsTillDeath()) {
				setDead();
			}
		}
	}
	
	/**
	 * @return The max food value of the animal.
	 */
	abstract protected int getMaxFoodValue();
	
	/**
	 * @return true If a breeding is possible only between animals in opposite gender.
	 */
	abstract protected boolean isGenderedReproduction();
	
	/**
	 * @return A list of what the animal can eat.
	 */
	abstract protected String[] getCanEat();
	
	/**
	 * Check whether or not this animal is to give birth at this step.
	 * New births will be made into free adjacent locations.
	 *
	 * @param newAnimals A list to return newly born animals.
	 */
	abstract protected void giveBirth(List<Actor> newAnimals);
	
	/**
	 * @return true If the animal does not need to eat in order to survive.
	 */
	abstract protected boolean getDisabledHunger();
	
	/**
	 * Look for food that the animal can eat in locations adjacent to the current location.
	 * Only the first live prey is eaten.
	 *
	 * @return Where food was found, or null if it wasn't.
	 */
	protected Location findFood()
	{
		List<Location> adjacent = getField().adjacentLocations(getLocation());
		Iterator<Location> it = adjacent.iterator();
		while (it.hasNext()) {
			Location where = it.next();
			Actor animal = getField().getActorAt(where);
			for (String preyString : getCanEat()) {
				if (animal != null && preyString.equals(animal.getClass().getName())) {
					if (animal.isActive()) {
						animal.setDead();
						setFoodLevel(Integer.parseInt(Config.getProperty(preyString.toLowerCase() + ".CALORIES")));
						return where;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Set the gender of the animal in a random manner.
	 */
	protected void setAnimalGender()
	{
		Random rand = Randomizer.getRandom();
		
		if (rand.nextDouble() >= 0.5) gender = Gender.FEMALE;
		else {
			gender = Gender.MALE;
		}
	}
	
	/**
	 * @return The gender of the animal.
	 */
	protected Gender getGender()
	{
		return gender;
	}
	
	/**
	 * @return The food level of the animal.
	 */
	protected int getFoodLevel()
	{
		return foodLevel;
	}
	
	/**
	 * Set the food level of the animal.
	 */
	protected void setFoodLevel(int newFoodLevel)
	{
		foodLevel = newFoodLevel;
	}
	
	/**
	 * An animal can reproduce if it has reached the breeding age or is not above the
	 * maximum breeding age. If it breeds sexually, it needs to find a partner from the opposite gender as well.
	 *
	 * @return true if the animal can reproduce, false otherwise.
	 */
	protected boolean canReproduce()
	{
		boolean isWithinBreedingAge = getAge() >= getReproductionAge() && getAge() <= getMaxReproductionAge();
		// If the animal is gendered, check for a possible mate nearby
		if (isGenderedReproduction()) {
			boolean hasMateNearby = false;
			for (Location location : getField().adjacentLocations(getLocation())) {
				Actor actor = getField().getActorAt(location);
				if (actor != null && isSameSpecie(actor)) {
					Animal animal = (Animal) actor;
					if (getGender() != animal.getGender())
						hasMateNearby = true;
				}
			}
			return isWithinBreedingAge && hasMateNearby;
		}
		return isWithinBreedingAge;
	}
	
	/**
	 * If the animal needs to eat in order to survive, make this animal more hungry.
	 * This could result in the animal's death.
	 */
	protected void incrementHunger()
	{
		if (!getDisabledHunger()) {
			foodLevel--;
			if (foodLevel <= 0) {
				setDead();
			}
		}
	}
	
	/**
	 * Enum containing all possible values of the animals gender.
	 */
	public enum Gender
	{
		MALE, FEMALE
	}
}