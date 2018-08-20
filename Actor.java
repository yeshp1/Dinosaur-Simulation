import java.util.List;
import java.util.Random;


/**
 * This is a class that describes an actor in the simulation.
 * This sets the base specifications of the animal and plant subclasses.
 * <p>
 * All actors are either alive or dead, have an age, Location within the Field
 * of the simulation, and are either healthy or diseased.
 * <p>
 * This class specifies the most rudimental behaviours of an actor and also
 * ensures that the necessary but diversified behaviour is implemented in its
 * subclasses via its abstract methods.
 *
 * @version 2018.02.21
 * @authors David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */
public abstract class Actor
{
	// A shared random number generator.
	private static final Random rand = Randomizer.getRandom();
	// The amount of steps the actor can leave after being diseased.
	private static final int DISEASED_STEPS_TILL_DEATH = Integer.parseInt(Config.getProperty("disease.DISEASED_STEPS_TILL_DEATH"));
	// The probability of an actor being created sick.
	private static final double DISEASE_PROBABILITY = Double.parseDouble(Config.getProperty("disease.DISEASE_PROBABILITY"));
	// The probability of an actor getting sick from another actor.
	private static final double DISEASE_SPREAD_PROBABILITY = Double.parseDouble(Config.getProperty("disease.DISEASED_SPREAD_PROBABILITY"));
	// The probability of an actor getting sick randomly.
	private static final double DISEASE_MUTATION_PROBABILITY = Double.parseDouble(Config.getProperty("disease.DISEASE_MUTATION_PROBABILITY"));
	
	// The age of the actor.
	private int age;
	// Whether the actor is alive or not.
	private boolean alive;
	// The actor's field.
	private Field field;
	// The actor's position in the field.
	private Location location;
	// Whether the actor is healthy or not.
	private Boolean isHealthy = true;
	// The number of steps the actor has been sick.
	private int sickStepCount = 0;
	
	/**
	 * A constructor for an instance of an actor.
	 *
	 * @param randomStats If true, the actor is created with random age and
	 *                    possibly diseased.
	 * @param field       The field within which the actor exists.
	 * @param location    The current location of the actor within the field.
	 */
	public Actor(boolean randomStats, Field field, Location location)
	{
		alive = true;
		this.field = field;
		setLocation(location);
		age = 0;
		
		if (randomStats) {
			age = rand.nextInt(getMaxAge());
			setDisease(DISEASE_PROBABILITY);
		} else setDisease(DISEASE_MUTATION_PROBABILITY);
	}
	
	/**
	 * Return the number of steps that the actor has been active since getting
	 * diseased.
	 *
	 * @return the number of steps since becoming diseased.
	 */
	public static int getDiseasedStepsTillDeath()
	{
		return DISEASED_STEPS_TILL_DEATH;
	}
	
	/**
	 * @return true If the actor is healthy.
	 */
	protected boolean isHealthy()
	{
		return isHealthy;
	}
	
	/**
	 * Check whether the actor is active or not.
	 *
	 * @return true if the actor is still active.
	 */
	protected boolean isActive()
	{
		return alive;
	}
	
	/**
	 * @return The shared random number.
	 */
	protected Random getRand()
	{
		return rand;
	}
	
	/**
	 * Indicate that the actor is no longer active.
	 * It is removed from the field.
	 */
	protected void setDead()
	{
		alive = false;
		if (location != null) {
			field.clear(location);
			location = null;
			field = null;
		}
	}
	
	/**
	 * Returns the number of steps taken by the actor after becoming diseased.
	 *
	 * @return number of steps taken after becoming diseased.
	 */
	protected int getSickStepCount()
	{
		return sickStepCount;
	}
	
	/**
	 * Increments the steps taken after becoming diseased by one.
	 */
	protected void incrementSickStepCount()
	{
		sickStepCount++;
	}
	
	/**
	 * Return the actor's location.
	 *
	 * @return The actor's location.
	 */
	protected Location getLocation()
	{
		return location;
	}
	
	/**
	 * Place the actor at the new location in the given field.
	 *
	 * @param newLocation The actor's new location.
	 */
	protected void setLocation(Location newLocation)
	{
		if (location != null) {
			field.clear(location);
		}
		location = newLocation;
		field.place(this, newLocation);
	}
	
	/**
	 * Return the actor's field.
	 *
	 * @return The actor's field.
	 */
	protected Field getField()
	{
		return field;
	}
	
	/**
	 * @return The age of the actor.
	 */
	protected int getAge()
	{
		return age;
	}
	
	/**
	 * Set the age of the actor.
	 *
	 * @param newAge The new age of the actor.
	 */
	protected void setAge(int newAge)
	{
		age = newAge;
	}
	
	/**
	 * Increase the age of the actor.
	 * This could result in the actor's death if the maximum age is reached.
	 */
	protected void incrementAge()
	{
		age++;
		if (age > getMaxAge()) {
			setDead();
		}
	}
	
	/**
	 * Set the actor to being diseased or not based on a probability.
	 *
	 * @param probability The probability of the actor getting diseased.
	 */
	protected void setDisease(double probability)
	{
		if (getRand().nextDouble() <= probability) {
			isHealthy = false;
		}
	}
	
	/**
	 * Iterates through the surrounding locations of the actor.
	 * If there is an actor in the surrounding locations there is a probability of them getting infected.
	 */
	protected void infect()
	{
		List<Location> adjLoc = field.adjacentLocations(location);
		for (Location location : adjLoc) {
			Actor actor = field.getActorAt(location);
			if (actor != null) {
				actor.setDisease(DISEASE_SPREAD_PROBABILITY);
			}
			
		}
	}
	
	/**
	 * Generate a number representing the number of births,
	 * if the actor can reproduce at this step.
	 *
	 * @return The number of births (may be zero).
	 */
	protected int reproduce()
	{
		int births = 0;
		if (canReproduce() && getRand().nextDouble() <= getReproductionProbability()) {
			births = getRand().nextInt(getMaxOffspring()) + 1;
		}
		return births;
	}
	
	/**
	 * Make this actor act - that is: make it do
	 * whatever it wants/needs to do.
	 *
	 * @param newActors A list to receive newly born actors.
	 */
	abstract protected void act(List<Actor> newActors);
	
	/**
	 * Check if the current actor is from the same class as the provided actor.
	 *
	 * @param actor The class we compare to.
	 * @return true If the actors are from the same class.
	 */
	abstract protected boolean isSameSpecie(Actor actor);
	
	/**
	 * @return The maximum age of the actor.
	 */
	abstract protected int getMaxAge();
	
	
	abstract protected boolean canReproduce();
	
	/**
	 * @return The breeding probability of the actor.
	 */
	abstract protected double getReproductionProbability();
	
	/**
	 * @return The maximum size of the actor's litter.
	 */
	abstract protected int getMaxOffspring();
	
	/**
	 * @return The breeding age of the actor.
	 */
	abstract protected int getReproductionAge();
	
	/**
	 * @return The maximum breeding age of the actor.
	 */
	abstract protected int getMaxReproductionAge();
	
}
