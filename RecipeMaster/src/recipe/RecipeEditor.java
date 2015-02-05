package recipe;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RecipeEditor extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	
	private Recipe recipe;
	
	public static void main(String[] args) {
		try {
			RecipeEditor dialog = new RecipeEditor();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public RecipeEditor() {
		recipe = new Recipe();
		recipe.setIngredients(new ArrayList<RecipeIngredient>());
		recipe.setInstructions(new ArrayList<Instruction>());
		initialize();
	}
	
	public RecipeEditor(Recipe recipe) {
		this.recipe = recipe;
		initialize();
	}
	
	private void initialize(){
		setBounds(100, 100, 700, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{35, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		GridBagConstraints gbc_panelName = new GridBagConstraints();
		gbc_panelName.insets = new Insets(0, 0, 5, 0);
		gbc_panelName.fill = GridBagConstraints.BOTH;
		gbc_panelName.gridx = 0;
		gbc_panelName.gridy = 0;
		contentPanel.add(namePanel(), gbc_panelName);
		
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		contentPanel.add(splitPane(), gbc_splitPane);
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						recipe.setName(textFieldName.getText());
						recipe.setCategory("");
						firePropertyChange("newrecipe", true, recipe);
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		
	}
	
	private JPanel namePanel(){
		JPanel panelName = new JPanel();
		GridBagLayout gbl_panelName = new GridBagLayout();
		gbl_panelName.columnWidths = new int[]{0, 0, 0};
		gbl_panelName.rowHeights = new int[]{0, 0};
		gbl_panelName.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
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
		}
		{
			textFieldName = new JTextField();
			if(recipe.getName() != null && recipe.getName().length() > 0){
				textFieldName.setText(recipe.getName());
			}
			GridBagConstraints gbc_textFieldName = new GridBagConstraints();
			gbc_textFieldName.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldName.gridx = 1;
			gbc_textFieldName.gridy = 0;
			panelName.add(textFieldName, gbc_textFieldName);
		}
		return panelName;
	}
	
	private JSplitPane splitPane(){
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		
		RecipeIngredientEditor recipeIngredientEditor = new RecipeIngredientEditor(recipe);
		GridBagLayout gridBagLayout = (GridBagLayout) recipeIngredientEditor.getLayout();
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0};
		splitPane.setLeftComponent(recipeIngredientEditor);
		
		InstructionEditor instructionEditor = new InstructionEditor(recipe);
		GridBagLayout gridBagLayout_1 = (GridBagLayout) instructionEditor.getLayout();
		gridBagLayout_1.rowWeights = new double[]{1.0, 0.0, 0.0};
		splitPane.setRightComponent(instructionEditor);
		
		return splitPane;
	}
}
