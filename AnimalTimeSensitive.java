import java.util.List;

/**
 * A class representing the Day/Night specific behaviour of animals.
 *
 * @author David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 * @version 2018.02.21
 */

public abstract class AnimalTimeSensitive extends Animal
{
	protected AnimalTimeSensitive(boolean randomStats, Field field, Location location)
	{
		super(randomStats, field, location);
	}
	
	/**
	 * Animal always grows in age and hunger. If the currentTime matches the specified activeTime (either Day/Night)
	 * animal is able to give birth, find food and moves to a new location provided it is not sick.
	 * It can die from overcrowding or disease.
	 *
	 * @param newAnimals A list to return newly born animals.
	 */
	@Override
	public void act(List<Actor> newAnimals)
	{
		incrementAge();
		incrementHunger();
		if (isActive()) {
			Location newLocation = null;
			
			if (Environment.getCurrentTimeOfDayString().equals(getActiveTime())) {
				if (isHealthy()) {
					//if healthy and current time matches specified activeTime: exhibits the following additional behaviour
					giveBirth(newAnimals);
					// Move towards a source of food if found.
					newLocation = findFood();
					if (newLocation == null) {
						// No food found - try to move to a free location.
						newLocation = getField().freeAdjacentLocation(getLocation());
					} else {
						newLocation = getField().freeAdjacentLocation(getLocation());
					}
				}
			} else {
				// Stay in current place.
				newLocation = getLocation();
			}
			
			// If the animals is sick, infect nearby actors and increments sick counter.
			if (!isHealthy()) {
				incrementSickStepCount();
				infect();
			}
			// See if it was possible to move.
			if (newLocation != null) {
				setLocation(newLocation);
			}
			// Death conditions: either Overcrowding or Disease.
			if (newLocation == null || getSickStepCount() >= getDiseasedStepsTillDeath()) {
				setDead();
			}
		}
	}
	
	/**
	 * Return a list of the time of day that the animal is active in.
	 *
	 * @return A list of the time of day that the animal is active in.
	 */
	protected abstract String getActiveTime();
}

