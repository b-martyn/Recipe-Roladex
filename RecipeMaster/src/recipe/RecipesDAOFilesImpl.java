package recipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static recipe.Resources.*;
import static java.nio.file.FileVisitResult.*;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class RecipesDAOFilesImpl implements RecipesDAO{
	
	@Override
	public Map<String, Collection<Recipe>> getRecipes(){
		FileFinder fileFinder = new FileFinder(FileSystems.getDefault().getPathMatcher("glob:*" + RECIPE_FILENAME_SUFFIX + RECIPE_FILE_EXTENSION));
		
		Map<String, Collection<Recipe>> recipes = new HashMap<>();
		try{
			Files.walkFileTree(RECIPE_DIRECTORY, fileFinder);
			for(String category : fileFinder.recipeFiles.keySet()){
				Collection<Recipe> recipeCategory = new ArrayList<>();
				for(Path recipePath : fileFinder.recipeFiles.get(category)){
					recipeCategory.add(createRecipe(recipePath));
				}
				recipes.put(category, recipeCategory);
			}
		} catch (NoSuchFileException e){
			try {
				Files.createDirectories(RECIPE_DIRECTORY);
				return getRecipes();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return recipes;
	}
	
	@Override
	public boolean deleteRecipe(Recipe recipe, String category) {
		try {
			Files.delete(Paths.get(RECIPE_DIRECTORY.toString(), category, recipe.getName() + RECIPE_FILENAME_SUFFIX + RECIPE_FILE_EXTENSION));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean saveRecipe(Recipe recipe){
		try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(RECIPE_DIRECTORY.toString(), recipe.getCategory(), recipe.getName() + RECIPE_FILENAME_SUFFIX + RECIPE_FILE_EXTENSION), CREATE, TRUNCATE_EXISTING)){
			bw.write(recipe.getName() + "\n");
			bw.write(INGREDIENTS + "\n");
			for(RecipeIngredient recipeIngredient : recipe.getIngredients()){
				bw.write(recipeIngredient.getMeasurement().getAmount() + DELIMITER + recipeIngredient.getMeasurement().getType().toString() + DELIMITER + recipeIngredient.getIngredient().getName() + "\n");
			}
			bw.write(STOP + "\n");
			bw.write(INSTRUCTIONS + "\n");
			for(Instruction instruction : recipe.getInstructions()){
				bw.write(instruction.getStepNumber() + DELIMITER + instruction.getMessage() + "\n");
			}
			bw.write(STOP + "\n");
			return true;
		} catch (NoSuchFileException e){
			newCategory(recipe.getCategory());
			return saveRecipe(recipe);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void saveRecipes(Map<String, Collection<Recipe>> recipes){
		for(String category : recipes.keySet()){
			for(Recipe recipe : recipes.get(category)){
				saveRecipe(recipe);
			}
		}
	}
	
	private void newCategory(String category){
		try {
			Files.createDirectory(RECIPE_DIRECTORY.resolve(category));
		} catch(FileAlreadyExistsException e) {
			//Do nothing
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Recipe createRecipe(Path path){
		Recipe newRecipe = new Recipe();
		try(BufferedReader bf = Files.newBufferedReader(path)){
			String line = null;
			newRecipe.setName(bf.readLine());
			newRecipe.setCategory(path.getParent().getFileName().toString());
			
			ExecutorService ingredientPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			Set<Future<RecipeIngredient>> ingredientSet = new HashSet<Future<RecipeIngredient>>();
			Set<Future<Instruction>> instructionSet = new HashSet<Future<Instruction>>();
			while((line = bf.readLine()) != null){
				if(line.contains(INGREDIENTS)){
					while(!(line = bf.readLine()).contains(STOP)){
						Future<RecipeIngredient> future = ingredientPool.submit(new IngredientCreator(line));
						ingredientSet.add(future);
					}
				}else if(line.contains(INSTRUCTIONS)){
					while(!(line = bf.readLine()).contains(STOP)){
						Future<Instruction> future = ingredientPool.submit(new InstructionCreator(line));
						instructionSet.add(future);
					}
				}
			}
			
			List<RecipeIngredient> ingredients = new ArrayList<>();
			for(Future<RecipeIngredient> future : ingredientSet){
				ingredients.add(future.get());
			}
			newRecipe.setIngredients(ingredients);
			
			List<Instruction> instructions = new ArrayList<>();
			for(Future<Instruction> instruction : instructionSet){
				instructions.add(instruction.get());
			}
			Collections.sort(instructions);
			newRecipe.setInstructions(instructions);
			
			ingredientPool.shutdown();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return newRecipe;
	}
	
	private class IngredientCreator implements Callable<RecipeIngredient>{
		private String string;
		
		IngredientCreator(String string){
			this.string = string;
		}
		
		@Override
		public RecipeIngredient call() throws Exception {
			String[] parts = string.split(DELIMITER);
			
			Measurement measurement = new Measurement(Double.valueOf(parts[0]), parts[1].toUpperCase());
			Ingredient ingredient = new Ingredient();
			ingredient.setName(parts[2]);
			
			return new RecipeIngredient(ingredient, measurement);
		}
	}
	
	private class InstructionCreator implements Callable<Instruction>{
		private String string;
		
		InstructionCreator(String string){
			this.string = string;
		}
		
		@Override
		public Instruction call() throws Exception {
			Instruction instruction = new Instruction();
			
			String[] instructionParts = string.split(DELIMITER);
			instruction.setStepNumber(Integer.valueOf(instructionParts[0]));
			instruction.setMessage(instructionParts[1]);
			return instruction;
		}
		
	}
	
	private class FileFinder extends SimpleFileVisitor<Path>{
		private PathMatcher matcher;
		private Map<String, List<Path>> recipeFiles = new HashMap<>();
		
		FileFinder(PathMatcher matcher){
			this.matcher = matcher;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr){
			if(attr.isRegularFile()){
				if(matcher.matches(file.getFileName())){
					if(recipeFiles.get(file.getParent().getFileName().toString()) != null){
						recipeFiles.get(file.getParent().getFileName().toString()).add(file);
					}else{
						List<Path> newCategory = new ArrayList<>();
						newCategory.add(file);
						recipeFiles.put(file.getParent().getFileName().toString(), newCategory);
					}
				}
			}
			return CONTINUE;
		}
	}
	
}
