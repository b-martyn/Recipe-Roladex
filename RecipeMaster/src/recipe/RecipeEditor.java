package recipe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RecipeEditor extends JDialog implements ActionListener, WindowListener, FocusListener{
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JButton btnCategorySelect;
	private Set<String> categories;
	private RecipeInstructionEditor instructionEditor;
	private RecipeIngredientEditor recipeIngredientEditor;
	
	private Recipe recipe;
	private boolean cancelled = true;
	
	public RecipeEditor(Set<String> categories) {
		this.categories = categories;
		recipe = new Recipe();
		recipe.setIngredients(new ArrayList<RecipeIngredient>());
		recipe.setInstructions(new ArrayList<Instruction>());
		initialize();
	}
	
	public RecipeEditor(Set<String> categories, Recipe recipe) {
		this.categories = categories;
		this.recipe = recipe;
		initialize();
		btnCategorySelect.setText(recipe.getCategory());
		textFieldName.setText(recipe.getName());
	}
	
	public Recipe getRecipe(){
		return recipe;
	}
	
	public boolean isCancelled(){
		return cancelled;
	}
	
	private void initialize(){
		setBounds(100, 100, 700, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{35, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		{
			JPanel panelName = new JPanel();
			GridBagLayout gbl_panelName = new GridBagLayout();
			gbl_panelName.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_panelName.rowHeights = new int[]{0, 0};
			gbl_panelName.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panelName.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panelName.setLayout(gbl_panelName);
			{
				JLabel labelName = new JLabel("Recipe Name:");
				GridBagConstraints gbc_labelName = new GridBagConstraints();
				gbc_labelName.insets = new Insets(0, 0, 0, 5);
				gbc_labelName.anchor = GridBagConstraints.EAST;
				gbc_labelName.gridx = 0;
				gbc_labelName.gridy = 0;
				panelName.add(labelName, gbc_labelName);
			
				textFieldName = new JTextField();
				GridBagConstraints gbc_textFieldName = new GridBagConstraints();
				gbc_textFieldName.insets = new Insets(0, 0, 0, 5);
				gbc_textFieldName.fill = GridBagConstraints.HORIZONTAL;
				gbc_textFieldName.gridx = 1;
				gbc_textFieldName.gridy = 0;
				panelName.add(textFieldName, gbc_textFieldName);
			}
			{
				JLabel labelCategory = new JLabel("Recipe Category:");
				GridBagConstraints gbc_labelCategory = new GridBagConstraints();
				gbc_labelCategory.insets = new Insets(0, 5, 0, 5);
				gbc_labelCategory.anchor = GridBagConstraints.EAST;
				gbc_labelCategory.gridx = 2;
				gbc_labelCategory.gridy = 0;
				panelName.add(labelCategory, gbc_labelCategory);
				
				btnCategorySelect = new JButton("Select");
				btnCategorySelect.setActionCommand("btnCategorySelect");
				btnCategorySelect.addActionListener(this);
				GridBagConstraints gbc_btnCategorySelect = new GridBagConstraints();
				gbc_btnCategorySelect.gridx = 3;
				gbc_btnCategorySelect.gridy = 0;
				panelName.add(btnCategorySelect, gbc_btnCategorySelect);
			}
			
			GridBagConstraints gbc_panelName = new GridBagConstraints();
			gbc_panelName.anchor = GridBagConstraints.NORTH;
			gbc_panelName.insets = new Insets(0, 0, 5, 0);
			gbc_panelName.fill = GridBagConstraints.HORIZONTAL;
			gbc_panelName.gridx = 0;
			gbc_panelName.gridy = 0;
			contentPanel.add(panelName, gbc_panelName);
		}
		{
			JSplitPane splitPane = new JSplitPane();
			
			recipeIngredientEditor = new RecipeIngredientEditor(recipe.getIngredients());
			splitPane.setLeftComponent(recipeIngredientEditor);
			
			instructionEditor = new RecipeInstructionEditor(recipe.getInstructions());
			splitPane.setRightComponent(instructionEditor);
			
			GridBagConstraints gbc_splitPane = new GridBagConstraints();
			gbc_splitPane.insets = new Insets(0, 0, 5, 0);
			gbc_splitPane.fill = GridBagConstraints.BOTH;
			gbc_splitPane.gridx = 0;
			gbc_splitPane.gridy = 1;
			contentPanel.add(splitPane, gbc_splitPane);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			
			JButton okButton = new JButton("OK");
			okButton.setActionCommand("OK");
			okButton.addActionListener(this);
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setActionCommand("Cancel");
			cancelButton.addActionListener(this);
			buttonPane.add(cancelButton);
			
			GridBagConstraints gbc_controlPanel = new GridBagConstraints();
			gbc_controlPanel.anchor = GridBagConstraints.SOUTH;
			gbc_controlPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_controlPanel.gridx = 0;
			gbc_controlPanel.gridy = 2;
			contentPanel.add(buttonPane, gbc_controlPanel);
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch(actionEvent.getActionCommand()){
			case "OK":
				verify();
				break;
			case "Cancel":
				dispose();
				break;
			case "btnCategorySelect":
				selectRecipeCategory((Component)actionEvent.getSource());
				break;
			case "categoryselected":
				btnCategorySelect.setText(((JMenuItem)actionEvent.getSource()).getText());
				break;
			case "newcategoryselected":
				newCategory();
				break;
		}
	}
	
	private void newCategory(){
		NewCategory newCategoryDialog = new NewCategory();
		newCategoryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		newCategoryDialog.addWindowListener(this);
		newCategoryDialog.setVisible(true);
	}
	
	private void selectRecipeCategory(Component initiator){
		RecipeCategoryEditor categoryEditor = new RecipeCategoryEditor(recipe, categories);
		categoryEditor.show(initiator, 0, initiator.getHeight());
		categoryEditor.addActionListener(this);
	}
	
	private void verify(){
		if(validateRecipe()){
			recipe.setName(textFieldName.getText());
			recipe.setCategory(btnCategorySelect.getText());
			recipe.setInstructions(instructionEditor.getInstructions());
			cancelled = false;
			dispose();
		}
	}
	
	private boolean validateRecipe(){
		boolean isValid = true;
		
		if(textFieldName.getText().isEmpty()){
			textFieldName.setText("Enter value");
			textFieldName.setForeground(Color.RED);
			textFieldName.addFocusListener(this);
			isValid = false;
		}
		if(btnCategorySelect.getText().equals("Select")){
			btnCategorySelect.setBackground(Color.RED);
			btnCategorySelect.addFocusListener(this);
			isValid = false;
		}
		return isValid;
	}

	@Override
	public void windowActivated(WindowEvent windowEvent) {
		// Do Nothing
	}

	@Override
	public void windowClosed(WindowEvent windowEvent) {
		btnCategorySelect.setText(((NewCategory)windowEvent.getSource()).getText());
	}

	@Override
	public void windowClosing(WindowEvent windowEvent) {
		// Do Nothing
	}

	@Override
	public void windowDeactivated(WindowEvent windowEvent) {
		// Do Nothing
	}

	@Override
	public void windowDeiconified(WindowEvent windowEvent) {
		// Do Nothing
	}

	@Override
	public void windowIconified(WindowEvent windowEvent) {
		// Do Nothing
	}

	@Override
	public void windowOpened(WindowEvent windowEvent) {
		// Do Nothing
	}

	@Override
	public void focusGained(FocusEvent focusEvent) {
		if(focusEvent.getSource() instanceof JTextField){
			JTextField field = (JTextField)focusEvent.getSource();
			field.setText("");
			field.setForeground(Color.BLACK);
		}else if(focusEvent.getSource() instanceof JButton){
			((JButton)focusEvent.getSource()).setBackground(Color.WHITE);
		}
	}

	@Override
	public void focusLost(FocusEvent focusEvent) {
		// Do Nothing
	}
}
