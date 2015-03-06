package recipe;

import java.awt.Font;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
	public static class FileConstants{
		/*
		user.home
		RecipeRoladex/resources
		
		user.dir
		src/resources
		*/
		public static final Path WORKING_DIRECTORY = Paths.get(System.getProperty("user.home"));
		public static final Path RESOURCE_DIRECTORY = Paths.get(WORKING_DIRECTORY.toString(), "RecipeRoladex/resources");
		public static final Path RECIPE_DIRECTORY = Paths.get(RESOURCE_DIRECTORY.toString(), "recipes");
		public static final String DELIMITER = ":";
		public static final String INGREDIENTS = "ingredients";
		public static final String INSTRUCTIONS = "instructions";
		public static final String STOP = "end";
		public static final String RECIPE_FILE_EXTENSION = ".txt";
		public static final String RECIPE_FILENAME_SUFFIX = "recipe";
	}
	public static class PrinterConstants{
		private static final String fontFamily = "Serif";
		
		public static final Font HEADER1_FONT = new Font(fontFamily, Font.BOLD, 18);
		public static final Font HEADER2_FONT = new Font(fontFamily, Font.BOLD, 16);
		public static final Font PARAGRPAH_FONT = new Font(fontFamily, Font.PLAIN, 14);
	}
}
