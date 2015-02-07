package recipe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RecipeEditor extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JButton btnSelect;
	private Set<String> categories;
	private boolean newCategory = false;
	private String type;
	
	private Recipe recipe;
	
	public RecipeEditor(Set<String> categories) {
		this.categories = categories;
		this.type = "newrecipe";
		recipe = new Recipe();
		recipe.setIngredients(new ArrayList<RecipeIngredient>());
		recipe.setInstructions(new ArrayList<Instruction>());
		initialize();
	}
	
	public RecipeEditor(Set<String> categories, Recipe recipe) {
		this.categories = categories;
		this.recipe = recipe;
		this.type = "editrecipe";
		initialize();
		btnSelect.setText(recipe.getCategory());
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
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		GridBagConstraints gbc_panelName = new GridBagConstraints();
		gbc_panelName.insets = new Insets(0, 0, 5, 0);
		gbc_panelName.fill = GridBagConstraints.BOTH;
		gbc_panelName.gridx = 0;
		gbc_panelName.gridy = 0;
		contentPanel.add(namePanel(), gbc_panelName);
		
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
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
						if(textFieldName.getText().isEmpty()){
							textFieldName.setText("Enter value");
							textFieldName.setForeground(Color.RED);
							textFieldName.addFocusListener(new FocusListener(){
								@Override
								public void focusGained(FocusEvent arg0) {
									textFieldName.setText("");
									textFieldName.setForeground(Color.BLACK);
								}
								@Override
								public void focusLost(FocusEvent arg0) {
									// Do nothing
								}
							});
							return;
						}
						recipe.setName(textFieldName.getText());
						if(btnSelect.getText().equals("Select")){
							btnSelect.setBackground(Color.RED);
							btnSelect.addFocusListener(new FocusListener(){
								@Override
								public void focusGained(FocusEvent arg0) {
									btnSelect.setBackground(Color.WHITE);
								}
								@Override
								public void focusLost(FocusEvent arg0) {
									// Do nothing
								}
							});
							return;
						}
						recipe.setCategory(btnSelect.getText());
						if(newCategory){
							firePropertyChange("newcategory", true, btnSelect.getText());
						}
						firePropertyChange(type, true, recipe);
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
			if(recipe.getName() != null && recipe.getName().length() > 0){
				textFieldName.setText(recipe.getName());
			}
			GridBagConstraints gbc_textFieldName = new GridBagConstraints();
			gbc_textFieldName.insets = new Insets(0, 0, 0, 5);
			gbc_textFieldName.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldName.gridx = 1;
			gbc_textFieldName.gridy = 0;
			panelName.add(textFieldName, gbc_textFieldName);
		}
		{
			JLabel labelName = new JLabel("Recipe Category:");
			GridBagConstraints gbc_labelName = new GridBagConstraints();
			gbc_labelName.insets = new Insets(0, 5, 0, 5);
			gbc_labelName.anchor = GridBagConstraints.EAST;
			gbc_labelName.gridx = 2;
			gbc_labelName.gridy = 0;
			panelName.add(labelName, gbc_labelName);
			
			btnSelect = new JButton("Select");
			btnSelect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JPopupMenu categoryList = new CategoryList(categories);
					categoryList.show(btnSelect, 0, btnSelect.getHeight());
					categoryList.addPropertyChangeListener("categoryselected", new PropertyChangeListener(){
						@Override
						public void propertyChange(PropertyChangeEvent pce) {
							recipe.setCategory((String)pce.getNewValue());
							btnSelect.setText((String)pce.getNewValue());
						}
					});
					categoryList.addPropertyChangeListener("newcategory", new PropertyChangeListener(){
						@Override
						public void propertyChange(PropertyChangeEvent pce) {
							recipe.setCategory((String)pce.getNewValue());
							btnSelect.setText((String)pce.getNewValue());
							newCategory = true;
						}
					});
				}
			});
			GridBagConstraints gbc_btnSelect = new GridBagConstraints();
			gbc_btnSelect.gridx = 3;
			gbc_btnSelect.gridy = 0;
			panelName.add(btnSelect, gbc_btnSelect);
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
