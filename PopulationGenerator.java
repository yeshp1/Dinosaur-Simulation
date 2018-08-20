import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/**
 * A class containing only static methods that sets the initial population of the simulation.
 *
 * @author David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 * @version 2018.02.21
 */

public class PopulationGenerator
{
	//
	private static LinkedHashMap<Class, Double> creationProbabilities = new LinkedHashMap<>();
	
	/**
	 * Populates the field through iteration.
	 *
	 * @param field  The current field to be populated.
	 * @param actors The list of the actors that would be added to the field.
	 * @param view   The GUI.
	 */
	public static void populate(Field field, List<Actor> actors, SimulatorView view)
	{
		collectProbabilities();
		setClassColors(view);
		
		Random rand = Randomizer.getRandom();
		field.clear();
		
		for (int row = 0; row < field.getDepth(); row++) {
			for (int col = 0; col < field.getWidth(); col++) {
				for (Class<?> cls : creationProbabilities.keySet()) {
					if (rand.nextDouble() <= creationProbabilities.get(cls)) {
						Location location = new Location(row, col);
						Actor actorInstance = null;
						try {
							//Dynamically creates an instance of Actor subclass using its own creation probability via Reflection API.
							actorInstance = (Actor) cls.getConstructor(Boolean.TYPE, Field.class, Location.class).newInstance(true, field, location);
						} catch (NoSuchMethodException |
								IllegalAccessException |
								InstantiationException |
								InvocationTargetException e) {
							e.printStackTrace();
						}
						actors.add(actorInstance);
					}
				}
				// else leave the location empty.
			}
		}
	}
	
	/**
	 * Extract the creation probabilities of classes from the config file and adds them in a hashMap.
	 */
	private static void collectProbabilities()
	{
		String propertySubName = "CREATION_PROBABILITY";
		HashSet<String> creationProbProperties = Config.getPropertiesContaining(propertySubName);
		for (String propertyLine : creationProbProperties) {
			PropertyLine property = new PropertyLine(propertyLine);
			
			try {
				Class<?> cls = Class.forName(property.getClassStringQualified());
				creationProbabilities.put(cls, Double.parseDouble(property.getValue()));
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Extracts the colors of the classes from the config file and store them in a hashMap.
	 *
	 * @param view The GUI.
	 */
	private static void setClassColors(SimulatorView view)
	{
		String propertySubName = "CLASS_COLOR";
		HashSet<String> colorProperties = Config.getPropertiesContaining(propertySubName);
		for (String propertyLine : colorProperties) {
			PropertyLine property = new PropertyLine(propertyLine);
			
			try {
				Class<?> cls = Class.forName(property.getClassStringQualified());
				Color color = (Color) Color.class.getField(property.getValue()).get(null);
				view.setColor(cls, color);
			} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		
	}
}
