/**
 * A class representing the breeding limitations of the animal based on the weather.
 *
 * @author David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 * @version 2018.02.21
 */

public abstract class AnimalSeasonalBreeding extends Animal
{
	protected AnimalSeasonalBreeding(boolean randomStats, Field field, Location location)
	{
		super(randomStats, field, location);
	}
	
	
	@Override
	/**
	 * An animal can reproduce if it has reached the breeding age or is not above the
	 * maximum breeding age. It also needs to be in its unique optimal weather conditions, specified in the
	 * config file.
	 * If breeding is sexual for the animal, it needs to find a partner from the opposite gender.
	 *
	 * @return true if the animal can reproduce, false otherwise.
	 */
	protected boolean canReproduce()
	{
		for (String weather : getActiveWeather()) {
			if (Environment.getCurrentWeatherString().equals(weather)) {
				break;
			} else {
				return false;
			}
		}
		boolean isWithinBreedingAge = getAge() >= getReproductionAge() && getAge() <= getMaxReproductionAge();
		//if the animal is gendered, check for a possible mate nearby
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
	 * Return a list of the weathers that the animal breeds in.
	 *
	 * @return A list of the weathers that the animal breeds in.
	 */
	abstract protected String[] getActiveWeather();
}

