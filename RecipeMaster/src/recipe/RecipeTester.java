package recipe;

public class RecipeTester {

	public static void main(String[] args) {
		int length = 4;
		int[] myIntArray = new int[length + 1];
		for(int i = 0; i < length; i++){
			myIntArray[i] = 2;
		}
		System.out.print("\n");
		for(int i = 0; i < myIntArray.length; i++){
			System.out.print(myIntArray[i] + ":");
		}
	}
	
	/*
	void recipeManagerImpTester(){
		/*
		RecipeTester rt = new RecipeTester();
		RecipeManagerImpl rm = rt.new RecipeManagerImpl();
		Recipe[] myRecipes = rm.findRecipes();
		for(Recipe recipe : myRecipes){
			rm.addRecipe(recipe);
		}
		
		System.out.println(rm.findRecipe("Bread")[0]);
	}
	
	
	private class RecipeManagerImpl implements RecipeManager{
		private final String workingDirectory = System.getProperty("user.dir");
		private final String resourceDirectory = "src/resources";
		private final Path resources = Paths.get(workingDirectory, resourceDirectory);
		
		private static final String DELIMITER = ":";
		private static final String INGREDIENTS = "ingredients";
		private static final String INSTRUCTIONS = "instructions";
		private static final String STOP = "end";
		private static final String RECIPE_FILE_EXTENSION = ".txt";
		private static final String RECIPE_FILENAME_SUFFIX = "recipe";
		
		@Override
		public Recipe[] findRecipes() {
			RecipeFinder recipeFinder = new RecipeFinder(FileSystems.getDefault().getPathMatcher("glob:*" + RECIPE_FILENAME_SUFFIX + RECIPE_FILE_EXTENSION));
			
			return recipeFinder(recipeFinder);
		}

		@Override
		public Recipe[] findRecipe(String name) {
			RecipeFinder recipeFinder = new RecipeFinder(FileSystems.getDefault().getPathMatcher("glob:*" + name + "*" + RECIPE_FILENAME_SUFFIX + RECIPE_FILE_EXTENSION));
			
			return recipeFinder(recipeFinder);
		}
		
		private Recipe[] recipeFinder(RecipeFinder recipeFinder){
			List<Recipe> recipes = new ArrayList<>();
			try {
				Files.walkFileTree(resources, recipeFinder);
				for(Path path : recipeFinder.getRecipeFiles()){
					System.out.println(path);
					recipes.add(createRecipe(path));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return recipes.toArray(new Recipe[recipes.size()]);
		}

		@Override
		public void addRecipe(Recipe recipe) {
			try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(resources.resolve(recipe.getName() + RECIPE_FILENAME_SUFFIX + RECIPE_FILE_EXTENSION).toString()), CREATE, TRUNCATE_EXISTING)){
				
				bw.write(recipe.getName() + "\n");
				bw.write(INGREDIENTS + "\n");
				for(Ingredient ingredient : recipe.getIngredients().keySet()){
					Measurement measurement = recipe.getIngredients().get(ingredient);
					bw.write(measurement.getAmount() + DELIMITER + measurement.getType().toString() + DELIMITER + ingredient.getName() + "\n");
				}
				bw.write(STOP + "\n");
				bw.write(INSTRUCTIONS + "\n");
				for(Instruction instruction : recipe.getInstructions()){
					bw.write(instruction.getStepNumber() + DELIMITER + instruction.getMessage() + "\n");
				}
				bw.write(STOP + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private Recipe createRecipe(Path path){
			Recipe newRecipe = new Recipe();
			try(BufferedReader bf = Files.newBufferedReader(path)){
				String line = null;
				newRecipe.setName(bf.readLine());
				
				ExecutorService ingredientPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
				Set<Future<Map<Ingredient, Measurement>>> ingredientSet = new HashSet<Future<Map<Ingredient, Measurement>>>();
				Set<Future<Instruction>> instructionSet = new HashSet<Future<Instruction>>();
				while((line = bf.readLine()) != null){
					if(line.contains(INGREDIENTS)){
						while(!(line = bf.readLine()).contains(STOP)){
							Future<Map<Ingredient, Measurement>> future = ingredientPool.submit(new IngredientCreator(line));
							ingredientSet.add(future);
						}
					}else if(line.contains(INSTRUCTIONS)){
						while(!(line = bf.readLine()).contains(STOP)){
							Future<Instruction> future = ingredientPool.submit(new InstructionCreator(line));
							instructionSet.add(future);
						}
					}
				}
				Map<Ingredient, Measurement> ingredients = new HashMap<>();
				for(Future<Map<Ingredient, Measurement>> future : ingredientSet){
					ingredients.putAll(future.get());
				}
				newRecipe.setIngredients(ingredients);
				
				SortedMap<Integer, Instruction> instructions = new TreeMap<>();
				for(Future<Instruction> instruction : instructionSet){
					Instruction i = instruction.get();
					instructions.put(i.getStepNumber(), i);
				}
				List<Instruction> instructions2 = new ArrayList<>();
				for(Integer i : instructions.keySet()){
					instructions2.add(instructions.get(i));
				}
				newRecipe.setInstructions(instructions2);
				
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
		
		private class IngredientCreator implements Callable<Map<Ingredient, Measurement>>{
			private String string;
			
			IngredientCreator(String string){
				this.string = string;
			}
			
			@Override
			public Map<Ingredient, Measurement> call() throws Exception {
				Map<Ingredient, Measurement> ingredient = new HashMap<>();
				
				String[] parts = string.split(DELIMITER);
				
				Measurement m = new Measurement(Double.valueOf(parts[0]), Measurement.Type.valueOf(parts[1].toUpperCase()));
				Ingredient i = new Ingredient();
				i.setName(parts[2]);
				
				ingredient.put(i, m);
				return ingredient;
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
		
		private class RecipeFinder extends SimpleFileVisitor<Path>{
			private PathMatcher matcher;
			private List<Path> recipeFiles = new ArrayList<>();
			
			RecipeFinder(PathMatcher matcher){
				this.matcher = matcher;
			}
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attr){
				if(attr.isRegularFile()){
					if(matcher.matches(file.getFileName())){
						recipeFiles.add(file);
					}
				}
				return CONTINUE;
			}
			
			public List<Path> getRecipeFiles(){
				return recipeFiles;
			}
		}
	}*/
}
