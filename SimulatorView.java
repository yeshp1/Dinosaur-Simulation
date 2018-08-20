import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * Weather information is displayed in the Container contents, while Current time
 * info is displayed in the infoPane.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2018.02.21
 * @improved by David Simon Tetruashvili, Yeshvanth Prabakar, Emiliyana Tsanova
 */
public class SimulatorView extends JFrame
{
	// Colors used for empty locations.
	private static final Color EMPTY_COLOR = Color.white;
	
	// Color used for objects that have no defined color.
	private static final Color UNKNOWN_COLOR = Color.gray;
	
	private final String STEP_PREFIX = "Step: ";
	private final String POPULATION_PREFIX = "Population: ";
	private final String TIME_PREFIX = "Time: ";
	private final String WEATHER_PREFIX = "Weather: ";
	private final Container contents = getContentPane();
	private final JPanel infoPane = new JPanel(new BorderLayout());
	private final JPanel statusPane = new JPanel(new BorderLayout());
	private FieldView fieldView;
	private JLabel stepLabel, population, infoLabel, timeLabel, weatherLabel;
	
	// A map for storing colors for participants in the simulation
	private Map<Class, Color> colors;
	// A statistics object computing and storing simulation information
	private FieldStats stats;
	// A hashMap containing the name of the time of day as a String and its corresponding color.
	private HashMap<String, Color> timeColors;
	// A hashMap containing the name of the weather as a String and its corresponding color.
	private HashMap<String, Color> weatherColors;
	
	/**
	 * Create a view of the given width and height.
	 *
	 * @param height The simulation's height.
	 * @param width  The simulation's width.
	 */
	public SimulatorView(int height, int width)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		stats = new FieldStats();
		colors = new LinkedHashMap<>();
		
		timeColors = new HashMap<>();
		
		//Specify colors to be used for time of day
		timeColors.put("Day", new Color(220, 211, 130));
		timeColors.put("Night", new Color(126, 163, 177));
		//Specify colors to be used for the different weathers
		weatherColors = new HashMap<>();
		weatherColors.put("Sunny", new Color(255, 246, 103));
		weatherColors.put("Rainy", new Color(60, 126, 119));
		weatherColors.put("Clear", Color.white);
		
		setTitle("Simulation");
		stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
		infoLabel = new JLabel("  ", JLabel.CENTER);
		population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
		timeLabel = new JLabel(TIME_PREFIX, JLabel.CENTER);
		weatherLabel = new JLabel(WEATHER_PREFIX, JLabel.CENTER);
		
		setLocation(100, 50);
		
		fieldView = new FieldView(height, width);
		
		
		infoPane.add(stepLabel, BorderLayout.WEST);
		infoPane.add(infoLabel, BorderLayout.CENTER);
		infoPane.add(timeLabel, BorderLayout.EAST);
		contents.add(infoPane, BorderLayout.NORTH);
		contents.add(fieldView, BorderLayout.CENTER);
		contents.add(statusPane, BorderLayout.SOUTH);
		statusPane.add(population, BorderLayout.WEST);
		statusPane.add(weatherLabel, BorderLayout.EAST);
		pack();
		setVisible(true);
	}
	
	/**
	 * Define a color to be used for a given class of actor.
	 *
	 * @param animalClass The actor's Class object.
	 * @param color       The color to be used for the given class.
	 */
	public void setColor(Class animalClass, Color color)
	{
		colors.put(animalClass, color);
	}
	
	/**
	 * Display a short information label at the top of the window.
	 */
	public void setInfoText(String text)
	{
		infoLabel.setText(text);
	}
	
	/**
	 * @return The color to be used for a given class of actor.
	 */
	private Color getColor(Class animalClass)
	{
		Color col = colors.get(animalClass);
		if (col == null) {
			// no color defined for this class
			return UNKNOWN_COLOR;
		} else {
			return col;
		}
	}
	
	/**
	 * TODO: make the simulator either show the gender of the animal or not based on the config file.
	 * Show the current status of the field.
	 *
	 * @param step  Which iteration step it is.
	 * @param field The field whose status is to be displayed.
	 */
	public void showStatus(int step, Field field)
	{
		Boolean DISPLAY_GENDER = Boolean.parseBoolean(Config.getProperty("simulator.DISPLAY_GENDER"));
		if (!isVisible()) {
			setVisible(true);
		}
		
		stepLabel.setText(STEP_PREFIX + step);
		//Retrieve current time and weather information from environment class and display in JLabel
		timeLabel.setText(TIME_PREFIX + Environment.getCurrentTimeOfDayString());
		weatherLabel.setText(WEATHER_PREFIX + Environment.getCurrentWeatherString());
		//Set the colors for current time and weather
		infoPane.setBackground(getCurrentTimeColor());
		statusPane.setBackground(getCurrentWeatherColor());
		stats.reset();
		
		fieldView.preparePaint();
		
		for (int row = 0; row < field.getDepth(); row++) {
			for (int col = 0; col < field.getWidth(); col++) {
				Actor actor = field.getActorAt(row, col);
				if (actor != null) {
					stats.incrementCount(actor.getClass());
					fieldView.drawMark(col, row, getColor(actor.getClass()));
					if (!actor.isHealthy()) {
						fieldView.drawMark(col, row, getColor(actor.getClass()).darker());
					}
					if (actor instanceof Animal && DISPLAY_GENDER) {
						if (((Animal) actor).getGender() == Animal.Gender.MALE)
							fieldView.drawOval(col, row, Color.white);
						else fieldView.drawOval(col, row, Color.black);
					}
				} else {
					fieldView.drawMark(col, row, EMPTY_COLOR);
				}
			}
		}
		stats.countFinished();
		
		population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
		fieldView.repaint();
	}
	
	/**
	 * Determine whether the simulation should continue to run.
	 *
	 * @return true If there is more than one actor alive.
	 */
	public boolean isViable(Field field)
	{
		return stats.isViable(field);
	}
	
	
	/**
	 * @return a HashMap of the colors associated with a particular weather.
	 */
	private Color getCurrentWeatherColor()
	{
		return weatherColors.get(Environment.getCurrentWeatherString());
	}
	
	/**
	 * @return a HashMap of the colors linked to current time (DAY/NIGHT).
	 */
	private Color getCurrentTimeColor()
	{
		return timeColors.get(Environment.getCurrentTimeOfDayString());
	}
	
	/**
	 * Provide a graphical view of a rectangular field. This is
	 * a nested class (a class defined inside a class) which
	 * defines a custom component for the user interface. This
	 * component displays the field.
	 */
	private class FieldView extends JPanel
	{
		private final int GRID_VIEW_SCALING_FACTOR = 6;
		Dimension size;
		private int gridWidth, gridHeight;
		private int xScale, yScale;
		private Graphics g;
		private Image fieldImage;
		
		/**
		 * Create a new FieldView component.
		 */
		public FieldView(int height, int width)
		{
			gridHeight = height;
			gridWidth = width;
			size = new Dimension(0, 0);
		}
		
		/**
		 * Tell the GUI manager how big we would like to be.
		 */
		public Dimension getPreferredSize()
		{
			return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
					gridHeight * GRID_VIEW_SCALING_FACTOR);
		}
		
		/**
		 * Prepare for a new round of painting. Since the component
		 * may be resized, compute the scaling factor again.
		 */
		public void preparePaint()
		{
			if (!size.equals(getSize())) {  // if the size has changed...
				size = getSize();
				fieldImage = fieldView.createImage(size.width, size.height);
				g = fieldImage.getGraphics();
				
				xScale = size.width / gridWidth;
				if (xScale < 1) {
					xScale = GRID_VIEW_SCALING_FACTOR;
				}
				yScale = size.height / gridHeight;
				if (yScale < 1) {
					yScale = GRID_VIEW_SCALING_FACTOR;
				}
			}
		}
		
		/**
		 * Paint on grid location on this field a rectangular in a given color.
		 */
		public void drawMark(int x, int y, Color color)
		{
			g.setColor(color);
			g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
		}
		
		/**
		 * Paint on grid location on this field an oval in a given color.
		 */
		public void drawOval(int x, int y, Color color)
		{
			g.setColor(color);
			g.fillOval(x * xScale, y * yScale, xScale - 3, yScale - 3);
		}
		
		
		/**
		 * The field view component needs to be redisplayed. Copy the
		 * internal image to screen.
		 */
		public void paintComponent(Graphics g)
		{
			if (fieldImage != null) {
				Dimension currentSize = getSize();
				if (size.equals(currentSize)) {
					g.drawImage(fieldImage, 0, 0, null);
				} else {
					// Rescale the previous image.
					g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
				}
			}
		}
	}
}
