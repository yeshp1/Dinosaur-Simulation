import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 * @improved by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */
public class Simulator
{
	// Constants representing configuration information for the simulation.
	// The default width for the grid.
	private static final int DEFAULT_WIDTH = Integer.parseInt(Config.getProperty("simulator.DEFAULT_WIDTH"));
	// The default depth of the grid.
	private static final int DEFAULT_DEPTH = Integer.parseInt(Config.getProperty("simulator.DEFAULT_DEPTH"));
	// An instance of the Simulator class.
	private static Simulator instance;
	// List of actors in the field.
	private List<Actor> actors;
	// The current state of the field.
	private Field field;
	// The current step of the simulation.
	private int step;
	// A graphical view of the simulation.
	private SimulatorView view;
	
	/**
	 * Construct a simulation field with default size.
	 */
	private Simulator()
	{
		this(DEFAULT_DEPTH, DEFAULT_WIDTH);
	}
	
	/**
	 * Create a simulation field with the given size.
	 *
	 * @param depth Depth of the field. Must be greater than zero.
	 * @param width Width of the field. Must be greater than zero.
	 */
	private Simulator(int depth, int width)
	{
		if (width <= 0 || depth <= 0) {
			System.out.println("The dimensions must be greater than zero.");
			System.out.println("Using default values.");
			depth = DEFAULT_DEPTH;
			width = DEFAULT_WIDTH;
		}
		
		actors = new ArrayList<>();
		field = new Field(depth, width);
		
		// Create a view of the state of each location in the field.
		view = new SimulatorView(depth, width);
		
		
		// Setup a valid starting point.
		reset();
	}
	
	
	/**
	 * A main method to instantiate the simulation.
	 * A simulation can be long or a single step based on information provided in the config file.
	 */
	public static void main(String[] args)
	{
		Simulator sim = Simulator.getInstance();
		String setting = Config.getProperty("simulator.SIMULATION_TYPE").toLowerCase();
		switch (setting) {
			case "long":
				sim.runLongSimulation();
				break;
			case "step":
				sim.simulateOneStep();
				break;
			default:
				sim.simulate(Integer.parseInt(setting));
				break;
		}
	}
	
	
	/**
	 * Return an instance of the Simulator class.
	 *
	 * @return an instance of the Simulator class.
	 */
	public static Simulator getInstance()
	{
		if (instance == null) instance = new Simulator();
		return instance;
	}
	
	
	/**
	 * Run the simulation from its current state for a reasonably long period.
	 * This period may be set in the config.cfg file.
	 */
	public void runLongSimulation()
	{
		simulate(Integer.parseInt(Config.getProperty("simulator.LONG_SIMULATION_LENGTH")));
	}
	
	/**
	 * Run the simulation from its current state for the given number of steps.
	 * Stop before the given number of steps if it ceases to be viable.
	 *
	 * @param numSteps The number of steps to run for.
	 */
	public void simulate(int numSteps)
	{
		for (int step = 1; step <= numSteps && view.isViable(field); step++) {
			simulateOneStep();
			delay(Integer.parseInt(Config.getProperty("simulator.DELAY")));
		}
	}
	
	/**
	 * Run the simulation from its current state for a single step.
	 * Iterate over the whole field updating the state of each actor.
	 */
	public void simulateOneStep()
	{
		step++;
		Environment.updateEnvironment(step);
		// Provide space for newborn actors.
		List<Actor> newAnimals = new ArrayList<>();
		// Let all actors act.
		for (Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
			Actor animal = it.next();
			animal.act(newAnimals);
			if (!animal.isActive()) {
				it.remove();
			}
		}
		
		// Add the newly born actors to the main lists.
		actors.addAll(newAnimals);
		
		view.showStatus(step, field);
	}
	
	/**
	 * Reset the simulation to a starting position.
	 */
	public void reset()
	{
		step = 0;
		actors.clear();
		PopulationGenerator.populate(field, actors, view);
		
		// Show the starting state in the view.
		view.showStatus(step, field);
	}
	
	/**
	 * Pause for a given time.
	 *
	 * @param millisec The time to pause for, in milliseconds
	 */
	private void delay(int millisec)
	{
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException ie) {
			// wake up
		}
	}
	
	/**
	 * Return the number of steps since the simulation was initiated.
	 *
	 * @return the number of steps since the simulation was initiated.
	 */
	public int getStep()
	{
		return step;
	}
}
