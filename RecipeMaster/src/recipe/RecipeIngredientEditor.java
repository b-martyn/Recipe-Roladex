/*
 * Makes a copy of the input Collection, does not modify the given collection.
 */

package recipe;

import java.awt.Color;
import java.awt.Dimension;
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
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RecipeIngredientEditor extends JPanel implements ListSelectionListener, ActionListener, FocusListener, WindowListener{
	private static final long serialVersionUID = 1L;
	
	private List<RecipeIngredient> ingredientList = new ArrayList<>();
	private RecipeIngredient selectedIngredient = null;
	
	private RecipeIngredientList recipeIngredientList;
	private JTextField txtFieldAmount;
	private JComboBox<String> comboBoxMeasurementType;
	private JTextField txtFieldIngredient;
	private JButton btnAdd;
	private JButton btnCancel;
	private JButton btnEdit;
	private JButton btnDelete;
	
	public RecipeIngredientEditor(Collection<RecipeIngredient> ingredientList){
		copyIngredients(ingredientList);
		initialize();
	}
	
	private void copyIngredients(Collection<RecipeIngredient> list){
		for(RecipeIngredient ingredient : list){
			ingredientList.add(ingredient.clone());
		}
	}
	
	private void initialize(){
		setPreferredSize(new Dimension(400, 0));
		setMinimumSize(new Dimension(0, 0));
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[]{0, 0};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowHeights = new int[]{0, 0, 0, 0};
		gbl.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl);
		
		{
			recipeIngredientList = new RecipeIngredientList(ingredientList);
			recipeIngredientList.addListSelectionListener(this);
			GridBagConstraints gbc_recipeIngredientList = new GridBagConstraints();
			gbc_recipeIngredientList.insets = new Insets(0, 0, 0, 0);
			gbc_recipeIngredientList.fill = GridBagConstraints.BOTH;
			gbc_recipeIngredientList.gridx = 0;
			gbc_recipeIngredientList.gridy = 0;
			add(recipeIngredientList, gbc_recipeIngredientList);
		}
		{
			JPanel inputPanel = new JPanel();
			GridBagLayout gbl_inputPanel = new GridBagLayout();
			gbl_inputPanel.columnWidths = new int[]{0, 0, 0, 0};
			gbl_inputPanel.rowHeights = new int[]{0, 0, 0};
			gbl_inputPanel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
			gbl_inputPanel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			inputPanel.setLayout(gbl_inputPanel);
			
			JLabel lblAmount = new JLabel("Amount");
			GridBagConstraints gbc_lblAmount = new GridBagConstraints();
			gbc_lblAmount.insets = new Insets(0, 0, 5, 5);
			gbc_lblAmount.gridx = 0;
			gbc_lblAmount.gridy = 0;
			inputPanel.add(lblAmount, gbc_lblAmount);
			
			txtFieldAmount = new JTextField();
			GridBagConstraints gbc_textFieldAmount = new GridBagConstraints();
			gbc_textFieldAmount.insets = new Insets(0, 0, 5, 5);
			gbc_textFieldAmount.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldAmount.gridx = 0;
			gbc_textFieldAmount.gridy = 1;
			inputPanel.add(txtFieldAmount, gbc_textFieldAmount);
			
			JLabel lblMeasurement = new JLabel("Measurement");
			GridBagConstraints gbc_lblMeasurement = new GridBagConstraints();
			gbc_lblMeasurement.insets = new Insets(0, 0, 5, 5);
			gbc_lblMeasurement.gridx = 1;
			gbc_lblMeasurement.gridy = 0;
			inputPanel.add(lblMeasurement, gbc_lblMeasurement);
			
			comboBoxMeasurementType = new JComboBox<String>(MeasurementTypes.getInstance().getTypes().toArray(new String[MeasurementTypes.getInstance().getTypes().size()]));
			comboBoxMeasurementType.addItem("New");
			/**/
			comboBoxMeasurementType.addActionListener(this);
			GridBagConstraints gbc_comboBoxMeasurementType = new GridBagConstraints();
			gbc_comboBoxMeasurementType.insets = new Insets(0, 0, 5, 5);
			gbc_comboBoxMeasurementType.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxMeasurementType.gridx = 1;
			gbc_comboBoxMeasurementType.gridy = 1;
			inputPanel.add(comboBoxMeasurementType, gbc_comboBoxMeasurementType);
			
			JLabel lblIngredient = new JLabel("Ingredient");
			GridBagConstraints gbc_lblIngredient = new GridBagConstraints();
			gbc_lblIngredient.insets = new Insets(0, 0, 5, 0);
			gbc_lblIngredient.gridx = 2;
			gbc_lblIngredient.gridy = 0;
			inputPanel.add(lblIngredient, gbc_lblIngredient);
			
			txtFieldIngredient = new JTextField();
			GridBagConstraints gbc_textFieldIngredient = new GridBagConstraints();
			gbc_textFieldIngredient.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldIngredient.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldIngredient.gridx = 2;
			gbc_textFieldIngredient.gridy = 1;
			inputPanel.add(txtFieldIngredient, gbc_textFieldIngredient);
			
			GridBagConstraints gbc_inputPanel = new GridBagConstraints();
			gbc_inputPanel.insets = new Insets(0, 0, 0, 0);
			gbc_inputPanel.fill = GridBagConstraints.BOTH;
			gbc_inputPanel.gridx = 0;
			gbc_inputPanel.gridy = 1;
			add(inputPanel, gbc_inputPanel);
		}
		{
			JPanel controlPanel = new JPanel();
			GridBagLayout gbl_controlPanel = new GridBagLayout();
			gbl_controlPanel.columnWidths = new int[]{0, 0, 0, 0};
			gbl_controlPanel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
			gbl_controlPanel.rowHeights = new int[]{0, 0};
			gbl_controlPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			controlPanel.setLayout(gbl_controlPanel);
			
			btnEdit = new JButton("Edit");
			btnEdit.setActionCommand("btnEdit");
			btnEdit.addActionListener(this);
			btnEdit.setVisible(false);
			GridBagConstraints gbc_btnEdit = new GridBagConstraints();
			gbc_btnEdit.gridx = 0;
			gbc_btnEdit.gridy = 0;
			controlPanel.add(btnEdit, gbc_btnEdit);
			
			btnCancel = new JButton("Cancel");
			btnCancel.setActionCommand("btnCancel");
			btnCancel.addActionListener(this);
			btnCancel.setVisible(false);
			GridBagConstraints gbc_btnCancel = new GridBagConstraints();
			gbc_btnCancel.gridx = 0;
			gbc_btnCancel.gridy = 0;
			controlPanel.add(btnCancel, gbc_btnCancel);
			
			btnDelete = new JButton("Delete");
			btnDelete.setActionCommand("btnDelete");
			btnDelete.addActionListener(this);
			btnDelete.setVisible(false);
			GridBagConstraints gbc_btnDelete = new GridBagConstraints();
			gbc_btnDelete.gridx = 1;
			gbc_btnDelete.gridy = 0;
			controlPanel.add(btnDelete, gbc_btnDelete);
			
			btnAdd = new JButton("Add New");
			btnAdd.setActionCommand("btnAdd");
			btnAdd.addActionListener(this);
			GridBagConstraints gbc_btnAdd = new GridBagConstraints();
			gbc_btnAdd.gridx = 2;
			gbc_btnAdd.gridy = 0;
			controlPanel.add(btnAdd, gbc_btnAdd);
			
			GridBagConstraints gbc_controlPanel = new GridBagConstraints();
			gbc_controlPanel.insets = new Insets(0, 0, 0, 0);
			gbc_controlPanel.fill = GridBagConstraints.BOTH;
			gbc_controlPanel.gridx = 0;
			gbc_controlPanel.gridy = 2;
			add(controlPanel, gbc_controlPanel);
		}
	}
	
	public Collection<RecipeIngredient> getIngredients(){
		return ingredientList;
	}
	
	public JComboBox<String> getComboBoxMeasurementType(){
		return comboBoxMeasurementType;
	}
	
	private void newIngredient(){

		
		if(inputVerified()){
			Ingredient ingredient = new Ingredient();
			ingredient.setName(txtFieldIngredient.getText());
			Measurement measurement = new Measurement(Double.parseDouble(txtFieldAmount.getText()), (String)comboBoxMeasurementType.getSelectedItem());
			if(selectedIngredient != null && !btnEdit.isVisible()){
				selectedIngredient.setIngredient(ingredient);
				selectedIngredient.setMeasurement(measurement);
			}else{
				ingredientList.add(new RecipeIngredient(ingredient, measurement));
			}
			resetPanel();
		}
	}
	
	private void editIngredient(){
		txtFieldAmount.setText(String.valueOf(selectedIngredient.getMeasurement().getAmount()));
		comboBoxMeasurementType.setSelectedItem(selectedIngredient.getMeasurement().getType());
		txtFieldIngredient.setText(selectedIngredient.getIngredient().getName());
		btnAdd.setText("OK");
		btnEdit.setVisible(false);
		btnCancel.setVisible(true);
	}
	
	private void resetPanel(){
		selectedIngredient = null;
		txtFieldAmount.setText("");
		txtFieldIngredient.setText("");
		btnAdd.setText("Add New");
		btnCancel.setVisible(false);
		btnDelete.setVisible(false);
		btnEdit.setVisible(false);
		recipeIngredientList.reload();
	}
	
	private void removeIngredient(){
		ingredientList.remove(selectedIngredient);
		resetPanel();
	}
	
	private boolean inputVerified(){
		try{
			if(txtFieldAmount.getText().isEmpty()){
				throw new NumberFormatException();
			}
			Double.parseDouble(txtFieldAmount.getText());
		}catch(NumberFormatException nfe){
			txtFieldAmount.setForeground(Color.RED);
			txtFieldAmount.setText("Enter a Number");
			txtFieldAmount.addFocusListener(this);
		}
		
		if(comboBoxMeasurementType.getSelectedItem() == null){
			comboBoxMeasurementType.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			comboBoxMeasurementType.addFocusListener(this);
		}
		
		if(txtFieldIngredient.getText().isEmpty()){
			txtFieldIngredient.setForeground(Color.RED);
			txtFieldIngredient.setText("Missing Ingredient");
			txtFieldIngredient.addFocusListener(this);
		}
		
		if(txtFieldAmount.getForeground() != Color.RED 
				&& txtFieldIngredient.getForeground() != Color.RED  
				&& comboBoxMeasurementType.getSelectedItem() != null){
			return true;
		}
		return false;
	}
	
	private void newMeasurementType() {
		NewStringInput newMeasurementTypeDialog = new NewStringInput("New Measurement Type");
		newMeasurementTypeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		newMeasurementTypeDialog.addWindowListener(this);
		newMeasurementTypeDialog.setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(ListSelectionEvent listSelectionEvent) {
		if(recipeIngredientList.getSelectedIngredient() != null){
			selectedIngredient = (RecipeIngredient)((JList<RecipeIngredient>)listSelectionEvent.getSource()).getSelectedValue();
			btnEdit.setVisible(true);
			btnDelete.setVisible(true);
		}
	}

	@Override
	public void focusGained(FocusEvent focusEvent) {
		if(focusEvent.getSource() instanceof JTextField){
			JTextField field = ((JTextField)focusEvent.getSource());
			field.setForeground(Color.BLACK);
			field.setText("");
			field.removeFocusListener(this);
		}else if(focusEvent.getSource() instanceof JComboBox){
			comboBoxMeasurementType.setBorder(BorderFactory.createEmptyBorder());
			comboBoxMeasurementType.removeFocusListener(this);
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// Do Nothing
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch(actionEvent.getActionCommand()){
			case "btnAdd":
				System.out.println(ingredientList.size());
				newIngredient();
				System.out.println(ingredientList.size());
				break;
			case "btnEdit":
				editIngredient();
				break;
			case "btnCancel":
				resetPanel();
				break;
			case "btnDelete":
				removeIngredient();
				break;
			case "comboBoxChanged":
				if(((JComboBox<String>)actionEvent.getSource()).getSelectedItem() != null && ((JComboBox<String>)actionEvent.getSource()).getSelectedItem().equals("New")){
					newMeasurementType();
				}
				break;
			default:
				System.out.println("Missed ActionCommand: " + actionEvent.getActionCommand());
				break;
		}
	}

	@Override
	public void windowActivated(WindowEvent windowEvent) {
		// Do Nothing
	}

	@Override
	public void windowClosed(WindowEvent windowEvent) {
		if(windowEvent.getSource() instanceof NewStringInput){
			if(!((NewStringInput)windowEvent.getSource()).isCancelled()){
				comboBoxMeasurementType.removeItemAt(comboBoxMeasurementType.getItemCount() - 1);
				comboBoxMeasurementType.addItem(((NewStringInput)windowEvent.getSource()).getText());
				comboBoxMeasurementType.addItem("New");
				comboBoxMeasurementType.setSelectedItem(((NewStringInput)windowEvent.getSource()).getText());
			}
		}
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
}
