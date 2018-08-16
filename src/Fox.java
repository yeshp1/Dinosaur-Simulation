import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Fox extends Animal
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age at which the rabbit stops breeding.
    private static final int MAX_BREEDING_AGE = 125;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    //private static final int RABBIT_FOOD_VALUE = 9;

    // The amount of steps an animal can go after eating the fox
    private static final int CALORIES = 20;


    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(getRand().nextInt(MAX_AGE));
            //Decide where we will get the initial food level.
            setFoodLevel(getRand().nextInt(Rabbit.getCalories()));
        }
        else {
            //20 is just a number that we may change.
            setFoodLevel(20);
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Actor> newFoxes)
    {
        incrementAge();
        incrementHunger();
        if(isActive()) {
            giveBirth(newFoxes);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isActive()) {
                    rabbit.setDead();
                    setFoodLevel(Rabbit.getCalories());
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<Actor> newFoxes)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Fox young = new Fox(false, field, loc);
            newFoxes.add(young);
        }
    }

    /**
     * @return The max age of the fox.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * @return The breeding probability of the fox.
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return The maximum size of the fox's litter.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * @return The breeding age of the fox.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return The maximum breeding age of the fox.
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
