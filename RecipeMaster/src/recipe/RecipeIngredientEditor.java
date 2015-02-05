package recipe;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RecipeIngredientEditor extends	RecipeComponentEditor<RecipeIngredient> {
	private static final long serialVersionUID = 1L;
	
	private RecipeIngredient editingIngredient;
	private JTextField txtFieldAmount;
	private JComboBox<Measurement.Type> comboBoxMeasurementType;
	private JTextField txtFieldIngredient;
	
	public RecipeIngredientEditor(Recipe recipe){
		super(recipe);
		addActionListeners();
	}

	@Override
	protected RecipeComponentList<RecipeIngredient> componentList() {
		return new RecipeIngredientList(recipe);
	}
	
	@Override
	protected JPanel componentInput(){
		JPanel componentPanel = new JPanel();
		
		GridBagLayout gbl_componentPanel = new GridBagLayout();
		gbl_componentPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_componentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_componentPanel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_componentPanel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		componentPanel.setLayout(gbl_componentPanel);
		
		txtFieldAmount = new JTextField();
		GridBagConstraints gbc_textFieldAmount = new GridBagConstraints();
		gbc_textFieldAmount.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldAmount.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldAmount.gridx = 0;
		gbc_textFieldAmount.gridy = 0;
		componentPanel.add(txtFieldAmount, gbc_textFieldAmount);
		
		comboBoxMeasurementType = new JComboBox<Measurement.Type>(Measurement.Type.values());
		//comboBoxMeasurementType = new JComboBox<Measurement.Type>();
		GridBagConstraints gbc_comboBoxMeasurementType = new GridBagConstraints();
		gbc_comboBoxMeasurementType.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxMeasurementType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxMeasurementType.gridx = 1;
		gbc_comboBoxMeasurementType.gridy = 0;
		componentPanel.add(comboBoxMeasurementType, gbc_comboBoxMeasurementType);
		
		txtFieldIngredient = new JTextField();
		GridBagConstraints gbc_textFieldIngredient = new GridBagConstraints();
		gbc_textFieldIngredient.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldIngredient.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldIngredient.gridx = 2;
		gbc_textFieldIngredient.gridy = 0;
		componentPanel.add(txtFieldIngredient, gbc_textFieldIngredient);
		
		JLabel lblAmount = new JLabel("Amount");
		GridBagConstraints gbc_lblAmount = new GridBagConstraints();
		gbc_lblAmount.insets = new Insets(0, 0, 5, 5);
		gbc_lblAmount.gridx = 0;
		gbc_lblAmount.gridy = 1;
		componentPanel.add(lblAmount, gbc_lblAmount);
		
		JLabel lblMeasurement = new JLabel("Measurement");
		GridBagConstraints gbc_lblMeasurement = new GridBagConstraints();
		gbc_lblMeasurement.insets = new Insets(0, 0, 5, 5);
		gbc_lblMeasurement.gridx = 1;
		gbc_lblMeasurement.gridy = 1;
		componentPanel.add(lblMeasurement, gbc_lblMeasurement);
		
		JLabel lblIngredient = new JLabel("Ingredient");
		GridBagConstraints gbc_lblIngredient = new GridBagConstraints();
		gbc_lblIngredient.insets = new Insets(0, 0, 5, 0);
		gbc_lblIngredient.gridx = 2;
		gbc_lblIngredient.gridy = 1;
		componentPanel.add(lblIngredient, gbc_lblIngredient);
		
		return componentPanel;
	}
	
	private void addActionListeners(){
		//Selection Listener
		componentList.getList().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(componentList.getList().getSelectedValue() != null){
					editingIngredient = componentList.getList().getSelectedValue();
				}
			}
		});
		
		//Add button
		addPropertyChangeListener(((JButton)controlPanel.getComponents()[0]).getText(), new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(validateIngredient()){
					double amount = Double.parseDouble(txtFieldAmount.getText());
					Measurement.Type measurementType = (Measurement.Type) comboBoxMeasurementType.getSelectedItem();
					Ingredient ingredient = new Ingredient();
					ingredient.setName(txtFieldIngredient.getText());
					
					if(editingIngredient == null){
						recipe.getIngredients().add(new RecipeIngredient(ingredient, new Measurement(amount, measurementType)));
					}else{
						recipe.getIngredients().remove(editingIngredient);
						editingIngredient.setMeasurement(new Measurement(amount, measurementType));
						editingIngredient.setIngredient(ingredient);
						recipe.getIngredients().add(editingIngredient);
						editingIngredient = null;
					}
					componentList.loadListModel();
					txtFieldAmount.setText("");
					txtFieldIngredient.setText("");
				}
			}
		});

		//Edit button
		addPropertyChangeListener(((JButton)controlPanel.getComponents()[1]).getText(), new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(componentList.getList().getSelectedValue() != null){
					txtFieldAmount.setText(String.valueOf(componentList.getList().getSelectedValue().getMeasurement().getAmount()));
					comboBoxMeasurementType.setSelectedItem(componentList.getList().getSelectedValue().getMeasurement().getType());
					txtFieldIngredient.setText(componentList.getList().getSelectedValue().getIngredient().getName());
					editingIngredient = componentList.getList().getSelectedValue();
				}
			}
		});

		//Delete button
		addPropertyChangeListener(((JButton)controlPanel.getComponents()[2]).getText(), new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(componentList.getList().getSelectedValue() != null){
					recipe.getIngredients().remove(componentList.getList().getSelectedValue());
					componentList.loadListModel();
				}
			}
		});
	}
	
	private boolean validateIngredient(){
		try{
			if(txtFieldAmount.getText().isEmpty()){
				throw new NumberFormatException();
			}
			Double.parseDouble(txtFieldAmount.getText());
		}catch(NumberFormatException nfe){
			txtFieldAmount.setForeground(Color.RED);
			txtFieldAmount.setText("Enter a Number");
			txtFieldAmount.addFocusListener(new InvalidFocusAdapter());
		}
		
		if(comboBoxMeasurementType.getSelectedItem() == null){
			comboBoxMeasurementType.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			comboBoxMeasurementType.addFocusListener(new FocusAdapter(){
				@Override
				public void focusGained(FocusEvent fe){
					comboBoxMeasurementType.setBorder(BorderFactory.createEmptyBorder());
					comboBoxMeasurementType.removeFocusListener(this);
				}
			});
		}
		
		if(txtFieldIngredient.getText().isEmpty()){
			txtFieldIngredient.setForeground(Color.RED);
			txtFieldIngredient.setText("Missing Ingredient");
			txtFieldIngredient.addFocusListener(new InvalidFocusAdapter());
		}
		
		if(txtFieldAmount.getForeground() != Color.RED 
				&& txtFieldIngredient.getForeground() != Color.RED  
				&& comboBoxMeasurementType.getSelectedItem() != null){
			return true;
		}
		return false;
	}
	
	private class InvalidFocusAdapter extends FocusAdapter{
		@Override
		public void focusGained(FocusEvent fe){
			JTextField field = ((JTextField)fe.getSource());
			field.setForeground(Color.BLACK);
			field.setText("");
			field.removeFocusListener(this);
		}
	}
}
