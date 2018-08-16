import java.util.List;


/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age at which the rabbit stops breeding.
    private static final int MAX_BREEDING_AGE = 35;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.12;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The amount of steps an animal can go after eating the fox
    private static final int CALORIES = 9;



    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(getRand().nextInt(MAX_AGE));
            //Decide where we will get the initial food level.
            setFoodLevel(getRand().nextInt());
        }
        else {
            // 10 is just a number that we may change.
            setFoodLevel(10);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Actor> newRabbits)
    {
        incrementAge();
        if(isActive()) {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Actor> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc);
            newRabbits.add(young);
        }
    }

    /**
     * @return The max age of the rabbit.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * @return The breeding probability of the rabbit.
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return The maximum size of the rabbit's litter.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * @return The breeding age of the rabbit.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return The maximum breeding age of the rabbit.
     */
    public int getMaxBreedingAge()
    {
        return MAX_BREEDING_AGE;
    }

    /**
     * @return The calories of the animal.
     */
    public static int getCalories()
    {
        return CALORIES;
    }
}
