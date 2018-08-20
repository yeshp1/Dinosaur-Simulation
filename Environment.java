import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * The Environment class controls the environment of the simulation.
 * At the moment it controls the time of day and weather.
 * It can be extended to control more aspects of the environment.
 * <p>
 * Times of day contain DAY and NIGHT.
 * <p>
 * Weather includes CLEAR, SUNNY, and RAINY.
 *
 * @author David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 * @version 2018.02.21
 */
public class Environment
{
	//The length of one Day/Night cycle in the simulation.
	private static final int LENGTH_OF_DAY_CYCLE = Integer.parseInt(Config.getProperty("environment.LENGTH_OF_DAY_CYCLE"));
	// The current time of day (Day/Night).
	private static TimeOfDay currentTime = TimeOfDay.DAY;
	// The current weather (Sunny, Clear, Rainy).
	private static Weather currentWeather = Weather.SUNNY;
	// The steps the current weather will be on before changing.
	private static int currentWeatherLength = 0;
	
	/**
	 * Sets the time of day based on the number of steps.
	 * Calls a method to update the weather.
	 *
	 * @param step The number of steps since initiating the simulation.
	 */
	public static void updateEnvironment(int step)
	{
		currentTime = (step % LENGTH_OF_DAY_CYCLE < LENGTH_OF_DAY_CYCLE / 2) ? TimeOfDay.DAY : TimeOfDay.NIGHT;
		updateWeather();
	}
	
	/**
	 * Updates the current weather. The new weather is picked from the Weather enum.
	 * If the weather is incompatible with the time of day it is changed or not assigned.
	 * If the weather has not been on for it's unique number of steps it is not changed.
	 */
	public static void updateWeather()
	{
		if (incompatibleTimeAndWeather(currentWeather)) {
			currentWeatherLength = 0;
		}
		if (currentWeatherLength > 0) {
			currentWeatherLength--;
		} else {
			List<Weather> list = new ArrayList<>(Arrays.asList(Weather.values()));
			list.remove(currentWeather);
			Random rand = Randomizer.getRandom();
			currentWeatherLength = rand.nextInt(50) + 5;
			Weather newWeather = list.get(rand.nextInt(list.size()));
			if (incompatibleTimeAndWeather(newWeather)) {
				list.remove(newWeather);
				newWeather = list.get(rand.nextInt(list.size()));
			}
			currentWeather = newWeather;
		}
	}
	
	/**
	 * @return The current time of day as a string starting with a capital letter.
	 */
	public static String getCurrentTimeOfDayString()
	{
		String curTime = currentTime.name();
		return curTime.substring(0, 1) + curTime.substring(1).toLowerCase();
	}
	
	/**
	 * @return The current weather name as a string starting with a capital letter.
	 */
	public static String getCurrentWeatherString()
	{
		String curWeather = currentWeather.name();
		return curWeather.substring(0, 1) + curWeather.substring(1).toLowerCase();
	}
	
	/**
	 * @return The current time of day.
	 */
	public static TimeOfDay getCurrentTime()
	{
		return currentTime;
	}
	
	/**
	 * @return The current weather.
	 */
	public static Weather getCurrentWeather()
	{
		return currentWeather;
	}
	
	/**
	 * Check whether the time of day and the weather are compatible.
	 *
	 * @param weather The weather we checking the comparability with the time of day of.
	 * @return true If the weather and time of day are incompatible.
	 */
	private static boolean incompatibleTimeAndWeather(Weather weather)
	{
		return currentTime == TimeOfDay.NIGHT && weather == Weather.SUNNY;
	}
	
	/**
	 * Enum containing all possible values of time of day.
	 */
	protected enum TimeOfDay
	{
		DAY, NIGHT
	}
	
	/**
	 * Enum containing all possible values of weather.
	 */
	protected enum Weather
	{
		SUNNY, RAINY, CLEAR
	}
	
}
