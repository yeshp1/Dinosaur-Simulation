import java.util.List;
import java.util.Random;
import java.util.*;
/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal extends Actor
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;

    // The age of the animal.
    private int age;
    //The gender of the animal.
    public enum Gender {MALE, FEMALE}
    private Gender gender;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The food level of the animal that is increased by eating.
    private int foodLevel;

    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        age = 0;
        setAnimalGender();
    }

    /**
     * @return The maximum age of the animal.
     */
    abstract protected int getMaxAge();

    /**
     * @return The breeding probability of the animal.
     */
    abstract protected double getBreedingProbability();

    /**
     * @return The maximum size of the animal's litter.
     */
    abstract protected int getMaxLitterSize();

    /**
     *@return The breeding age of the animal.
     */
    abstract protected int getBreedingAge();

    /**
     *@return The maximum breeding age of the animal.
     */
    abstract protected int getMaxBreedingAge();

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
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
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Set the gender of the animal in a random manner.
     */
    protected void setAnimalGender()
    {
        Random random = new Random();
        float a = random.nextFloat();
        if (a >= 0.5) gender = Gender.FEMALE;
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
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Increase the age.
     * This could result in the animal's death if the maximum age is reached.
     */
    protected void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }

    /**
     * Generate a number representing the number of births,
     * if the animal can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <=getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    /**
     * An animal can breed if it has reached the breeding age or is not above the
     * maximum breeding age.
     * @return true if the animal can breed, false otherwise.
     */
    private boolean canBreed()
    {

        if(age >= getBreedingAge() && age <= getMaxBreedingAge())
            return true;
        else {
            return false;
        }
    }

    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }

    /**
     * Set the age of the animal.
     * @param newAge The new age we want the animal to have.
     */
    protected void setAge(int newAge)
    {
        age = newAge;
    }

    /**
     * @return The age of the animal.
     */
    protected int getAge() {
        return age;
    }




    /**
     * Check if there is some instance of the type the animal eats.
     * If it is hungry enough it will eat it, else it won't.
     */
    protected void eat(){}
    // Maybe store the types of animals every animal can eat in an arrayList and find
    //if food is available.



    /**
     * Make this animal more hungry.
     * This could result in the animal's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Determines whether two animals are of opposite sexes
     * @Param animal determines whether there is an opposite sex of the animal
     */
    public Boolean genderMeet(Animal animal)
    {
     List<Animal> animals=adjacentAnimal(animal);
     for(Animal x:animals)
         {
             if(! animal.getGender().equals(x.getGender()))
             {
                 return true;
             }
         }
         return false;
    }

    /**
     * Groups together all animals of the sam type
     * @Param animal determines whether there is an opposite sex of the animal
     */
    public List<Animal> adjacentAnimal(Animal animalAtLocation)
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        List<Animal> adjacentAnimals = new ArrayList<>();
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Animal animal = (Animal) field.getObjectAt(where);
            if(animalAtLocation.getClass().isInstance(animal)) {
                adjacentAnimals.add(animal);
            }
        }
        return adjacentAnimals;
    }
}
