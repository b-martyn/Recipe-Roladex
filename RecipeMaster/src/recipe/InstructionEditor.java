package recipe;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InstructionEditor extends RecipeComponentEditor<Instruction> {
	private static final long serialVersionUID = 1L;
	
	private JTextArea txtAreaInstruction;
	//private Recipe recipe;
	private Instruction editingInstruction;
	
	public InstructionEditor(Recipe recipe){
		super(recipe);
		addListeners();
	}

	@Override
	protected RecipeComponentList<Instruction> componentList() {
		return new InstructionList(recipe);
	}
	
	@Override
	protected JPanel componentInput(){
		JPanel componentPanel = new JPanel();
		
		GridBagLayout gbl_panelInstructions = new GridBagLayout();
		gbl_panelInstructions.columnWidths = new int[]{0, 0};
		gbl_panelInstructions.rowHeights = new int[]{0, 0};
		gbl_panelInstructions.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelInstructions.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		componentPanel.setLayout(gbl_panelInstructions);
		
		txtAreaInstruction = new JTextArea();
		txtAreaInstruction.setLineWrap(true);
		txtAreaInstruction.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_textAreaInstruction = new GridBagConstraints();
		gbc_textAreaInstruction.insets = new Insets(0, 0, 5, 0);
		gbc_textAreaInstruction.fill = GridBagConstraints.BOTH;
		gbc_textAreaInstruction.gridx = 0;
		gbc_textAreaInstruction.gridy = 0;
		componentPanel.add(txtAreaInstruction, gbc_textAreaInstruction);
		
		return componentPanel;
	}
	
	protected void addListeners(){
		//Selection Listener
		componentList.getList().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(componentList.getList().getSelectedValue() != null){
					editingInstruction = componentList.getList().getSelectedValue();
				}
			}
		});
		
		//Add button
		addPropertyChangeListener(((JButton)controlPanel.getComponents()[0]).getText(), new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(!txtAreaInstruction.getText().isEmpty()){
					if(editingInstruction == null){
						recipe.getInstructions().add(new Instruction(recipe.getInstructions().size() + 1, txtAreaInstruction.getText()));
					}else{
						recipe.getInstructions().remove(editingInstruction);
						editingInstruction.setMessage(txtAreaInstruction.getText());
						recipe.getInstructions().add(editingInstruction);
						editingInstruction = null;
					}
					componentList.loadListModel();
					txtAreaInstruction.setText("");
				}
			}
		});
		
		//Edit button
		addPropertyChangeListener(((JButton)controlPanel.getComponents()[1]).getText(), new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(componentList.getList().getSelectedValue() != null){
					txtAreaInstruction.setText(componentList.getList().getSelectedValue().getMessage());
					editingInstruction = componentList.getList().getSelectedValue();
				}
			}
		});
		
		//Delete button
		addPropertyChangeListener(((JButton)controlPanel.getComponents()[2]).getText(), new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(componentList.getList().getSelectedValue() != null){
					recipe.getInstructions().remove(componentList.getList().getSelectedValue());
					int count = 1;
					for(Instruction instruction : recipe.getInstructions()){
						instruction.setStepNumber(count++);
					}
					componentList.loadListModel();
				}
			}
		});
	}
}
