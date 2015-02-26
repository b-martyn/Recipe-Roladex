package recipe;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JSplitPane;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;

public class RecipeDetails extends JPanel {
	private static final long serialVersionUID = 1L;
	private Recipe recipe;
	
	public RecipeDetails(Recipe recipe) {
		this.recipe = recipe;
		initialize();
	}
	
	private void initialize(){

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel labelRecipeName = new JLabel(recipe.getName());
		GridBagConstraints gbc_labelRecipeName = new GridBagConstraints();
		gbc_labelRecipeName.insets = new Insets(0, 0, 5, 0);
		gbc_labelRecipeName.gridx = 0;
		gbc_labelRecipeName.gridy = 0;
		add(labelRecipeName, gbc_labelRecipeName);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		add(splitPane, gbc_splitPane);
		
		RecipeIngredientList ingredientPanel = new RecipeIngredientList(recipe.getIngredients());
		splitPane.setLeftComponent(ingredientPanel);
		
		InstructionList instructionPanel = new InstructionList(recipe.getInstructions());
		splitPane.setRightComponent(instructionPanel);
	}
}
