package recipe;

import static recipe.Constants.PrinterConstants.*;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipePrinter{
	//Variable controls
	private int leftAdjust = 50;
	
	private Recipe recipe;
	private int numOfPages = 1;
	private Map<Integer, List<String>> ingredientMap = new HashMap<>();
	private Map<Integer, List<String>> instructionMap = new HashMap<>();
	
	private Graphics graphics;
	private PageFormat pageFormat;
	private FontMetrics fontMetrics;
	private int heightOffset = 0;
		
	public RecipePrinter(Recipe recipe, Graphics graphics, PageFormat pageFormat){
		this.recipe = recipe;
		this.graphics = graphics;
		this.pageFormat = pageFormat;
		initialize();
	}
	
	private void initialize(){
		graphics.setFont(HEADER1_FONT);
		fontMetrics = graphics.getFontMetrics();
		//1 line for Recipe Name
		int headerHeight = fontMetrics.getHeight();
		//1 line for Recipe Name and 1 for Category
		graphics.setFont(HEADER2_FONT);
		fontMetrics = graphics.getFontMetrics();
		int titleHeight = headerHeight + fontMetrics.getHeight();
		
		graphics.setFont(PARAGRPAH_FONT);
		fontMetrics = graphics.getFontMetrics();
		int baseIngredientHeight = recipe.getIngredients().size() * fontMetrics.getHeight();
		List<String> instructionStringList = convertInstructions();
		int baseInstructionHeight = instructionStringList.size() * fontMetrics.getHeight();
		
		if((titleHeight + baseIngredientHeight + baseInstructionHeight) < (pageFormat.getHeight())){//Will recipe fits on 1 page?
			singleIngredientList();
			instructionMap.put(0, instructionStringList);
			return;
		}else if((titleHeight + (baseIngredientHeight / 2) + baseInstructionHeight) < (pageFormat.getHeight())){//Will split ingredient list with instructions fit on 1 page?
			splitIngredientList();
			instructionMap.put(0, instructionStringList);
		}else{
			if((titleHeight + baseIngredientHeight) < pageFormat.getHeight()){//Will split ingredient list with instructions fit on 1 page?
				singleIngredientList();
			}else{
				splitIngredientList();
			}
			
			if((headerHeight + baseInstructionHeight) < pageFormat.getHeight()){
				numOfPages = 2;
				instructionMap.put(1, instructionStringList);
			}else{
				splitInstructions(instructionStringList, headerHeight);
				numOfPages = instructionMap.size() + 1;
			}
		}
	}
	
	private void singleIngredientList(){
		List<String> ingredientList = new ArrayList<>();
		for(RecipeIngredient ingredient : recipe.getIngredients()){
			ingredientList.add(ingredient.toString());
		}
		ingredientMap.put(0, ingredientList);
	}
	
	private void splitIngredientList(){
		graphics.setFont(PARAGRPAH_FONT);
		fontMetrics = graphics.getFontMetrics();
		
		int listNumber = 0;
		List<String> leftColumnList = new ArrayList<>();
		List<String> rightColumnList = new ArrayList<>();
		List<String> oversizedIngredientList = new ArrayList<>();
		for(RecipeIngredient ingredient : recipe.getIngredients()){
			if(fontMetrics.stringWidth(ingredient.toString()) > ((pageFormat.getWidth() / 2) - leftAdjust)){
				oversizedIngredientList.add(ingredient.toString());
				continue;
			}
			if(listNumber % 2 == 0){
				leftColumnList.add(ingredient.toString());
				listNumber = 1;
			}else{
				rightColumnList.add(ingredient.toString());
				listNumber = 0;
			}
		}
		ingredientMap.put(0, leftColumnList);
		ingredientMap.put(1, rightColumnList);
		ingredientMap.put(2, oversizedIngredientList);
	}
	
	private void splitInstructions(List<String> instructionStrings, int headerHeight){
		graphics.setFont(PARAGRPAH_FONT);
		fontMetrics = graphics.getFontMetrics();
		int pageNumber = 1;
		
		List<String> list = new ArrayList<>();
		List<String> fullInstruction = new ArrayList<>();
		for(String string : instructionStrings){
			if(string.matches("^\\d*:.*$")){//String Matches Digit[0-9] followed by ':'
				//System.out.printf("HeaderHeight:%s FullInstructionSize:%s ListSize:%s TotalHeight:%s FontMetricsHeight:%s\n", headerHeight, (fullInstruction.size() * fontMetrics.getHeight()), (list.size() * fontMetrics.getHeight()), pageFormat.getHeight(), fontMetrics.getHeight());
				
				//pageFormat.getHeight() - 57: 57 is buffer of bottom of page
				if((headerHeight + (fullInstruction.size() * fontMetrics.getHeight()) + (list.size() * fontMetrics.getHeight())) < pageFormat.getHeight() - 57){//Can add instruction to list without overflow?
					list.addAll(fullInstruction);
				}else{
					instructionMap.put(pageNumber, list);
					pageNumber++;
					list = new ArrayList<>();
					list.addAll(fullInstruction);
				}
				fullInstruction = new ArrayList<>();
				fullInstruction.add(string);
			}else{
				fullInstruction.add(string);
			}
		}
		instructionMap.put(pageNumber, list);
	}
	
	public int getNumOfPages(){
		return numOfPages;
	}
	
	public void renderPage(int pageNumber){
		heightOffset = 0;
		fontMetrics = graphics.getFontMetrics();
		{//Header
			if(pageNumber == 0){
				renderTitle();
			}else{
				renderHeader();
			}
		}
		{//Ingredients
			//ASSUMPTION_1 = no recipe ingredient list will be longer than can fit on the first page
			//ASSUMPTION_2 = max of 2 columns will be needed for ASSUMPTION_1
			if(pageNumber == 0){//ASSUMPTION_1
				graphics.setFont(HEADER2_FONT);
				fontMetrics = graphics.getFontMetrics();
				heightOffset += fontMetrics.getHeight();
				String ingredientHeader = "Ingredients";
				int ingredientHeaderLength = fontMetrics.stringWidth(ingredientHeader);
				graphics.drawString(ingredientHeader, (int)(pageFormat.getWidth()/2) - (ingredientHeaderLength/2), heightOffset);
				graphics.drawLine(leftAdjust, heightOffset += 5, (int)pageFormat.getWidth() - leftAdjust, heightOffset);
				heightOffset += 15;
				
				graphics.setFont(PARAGRPAH_FONT);
				fontMetrics = graphics.getFontMetrics();
				if(ingredientMap.size() == 1){
					for(String ingredient : ingredientMap.get(pageNumber)){
						int ingredientLength = fontMetrics.stringWidth(ingredient.toString());
						heightOffset += fontMetrics.getHeight();
						graphics.drawString(ingredient.toString(), (int)(pageFormat.getWidth()/2) - (ingredientLength/2), heightOffset);
					}
				}else{
					int heightOffsetReset = heightOffset;
					for(Integer key : ingredientMap.keySet()){
						int leftOffset = 0;
						switch(key){
							case 2:
								if(ingredientMap.get(0).size() == ingredientMap.get(1).size()){
									//Do nothing the heightOffset will be adjusted correctly
								}else{
									//Lists can only be different by max of 1 so this corrects the height offset for uneven ingredient lists
									heightOffset += fontMetrics.getHeight();
								}
							case 0:
								leftOffset = leftAdjust;
								break;
							case 1:
								heightOffset = heightOffsetReset;
								leftOffset = (int)(pageFormat.getWidth() / 2);
								break;
						}
						
						for(String ingredient : ingredientMap.get(key)){
							heightOffset += fontMetrics.getHeight();
							graphics.drawString(ingredient, leftOffset, heightOffset);
						}
					}
				}
			}
		}
		{//Instructions
			if(instructionMap.get(pageNumber) != null){
				graphics.setFont(HEADER2_FONT);
				fontMetrics = graphics.getFontMetrics();
				heightOffset += fontMetrics.getHeight();
				String instructioHeader = "Instructions";
				int instructioHeaderLength = fontMetrics.stringWidth(instructioHeader);
				graphics.drawString(instructioHeader, (int)(pageFormat.getWidth()/2) - (instructioHeaderLength/2), heightOffset);
				heightOffset += 5;
				graphics.drawLine(leftAdjust, heightOffset, (int)pageFormat.getWidth() - leftAdjust, heightOffset);
				heightOffset += 15;
				
				graphics.setFont(PARAGRPAH_FONT);
				fontMetrics = graphics.getFontMetrics();
				for(String instruction : instructionMap.get(pageNumber)){
					heightOffset += fontMetrics.getHeight();
					graphics.drawString(instruction, leftAdjust, heightOffset);
				}
			}
		}
	}
	
	private void renderTitle(){
		graphics.setFont(HEADER1_FONT);
		fontMetrics = graphics.getFontMetrics();
		heightOffset += fontMetrics.getHeight();
		int nameLength = fontMetrics.stringWidth(recipe.getName());
		graphics.drawString(recipe.getName(), (int)(pageFormat.getWidth()/2) - (nameLength/2), heightOffset);
		
		graphics.setFont(HEADER2_FONT);
		fontMetrics = graphics.getFontMetrics();
		heightOffset += fontMetrics.getHeight();
		int categoryLength = fontMetrics.stringWidth("(" + recipe.getCategory() + ")");
		graphics.drawString("(" + recipe.getCategory() + ")", (int)(pageFormat.getWidth()/2) - (categoryLength/2), heightOffset);
	}
	
	private void renderHeader(){
		graphics.setFont(HEADER1_FONT);
		fontMetrics = graphics.getFontMetrics();
		heightOffset += fontMetrics.getHeight();
		int nameLength = fontMetrics.stringWidth(recipe.getName());
		graphics.drawString(recipe.getName(), (int)(pageFormat.getWidth()/2) - (nameLength/2), heightOffset);
	}
	
	private List<String> convertInstructions(){
		List<String> instructionList = new ArrayList<>();
		
		for(Instruction instruction : recipe.getInstructions()){
			int instructionLength = fontMetrics.stringWidth(instruction.toString());
			if(instructionLength > (pageFormat.getWidth() - (leftAdjust * 2))){
				String tempString = instruction.toString();
				int index = 0;
				for(; index < instruction.toString().length();){
					int limit = 0;
					int stringAdvanceWidth = fontMetrics.stringWidth(tempString);
					if((pageFormat.getWidth() - (leftAdjust * 2)) < stringAdvanceWidth){
						limit = (int)(pageFormat.getWidth() - (leftAdjust * 2));
					}else{
						instructionList.add(tempString);
						break;
					}
					
					int lastIndex = 0;
					Pattern pattern = Pattern.compile("\\s");
					Matcher matcher = pattern.matcher(tempString);
					while(matcher.find()){
						if(fontMetrics.stringWidth(tempString.substring(0, matcher.start())) > limit){
							break;
						}
						lastIndex = matcher.start();
					}
					instructionList.add(tempString.substring(0, lastIndex));
					tempString = instruction.toString().substring(index += (lastIndex + 1));
				}
			}else{
				instructionList.add(instruction.toString());
			}
		}
		
		return instructionList;
	}
}
