/*
 * Makes a copy of the input Collection, does not modify the given collection.
 */

package recipe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RecipeInstructionEditor extends JPanel implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = 1L;
	
	private List<Instruction> list = new ArrayList<>();
	private Instruction selectedInstruction = null;
	
	private JTextArea txtAreaInstruction;
	private JComboBox<Integer> comboBoxStepNumbers;
	private InstructionList instructionsList;
	private JButton btnAdd;
	private JButton btnCancel;
	private JButton btnEdit;
	private JButton btnDelete;
	
	public RecipeInstructionEditor(Collection<Instruction> instructionList){
		copyInstructions(instructionList);
		initialize();
	}
	
	private void copyInstructions(Collection<Instruction> list){
		for(Instruction instruction : list){
			this.list.add(instruction.clone());
		}
	}
	
	private void initialize(){
		setPreferredSize(new Dimension(0, 0));
		setMinimumSize(new Dimension(0, 0));
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[]{0, 0};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowHeights = new int[]{0, 0, 0, 0};
		gbl.rowWeights = new double[]{1.0, 0.5, 0.0, Double.MIN_VALUE};
		setLayout(gbl);
		
		{
			instructionsList = new InstructionList(list);
			instructionsList.addListSelectionListener(this);
			GridBagConstraints gbc_instructionsList = new GridBagConstraints();
			gbc_instructionsList.insets = new Insets(0, 0, 0, 0);
			gbc_instructionsList.fill = GridBagConstraints.BOTH;
			gbc_instructionsList.gridx = 0;
			gbc_instructionsList.gridy = 0;
			add(instructionsList, gbc_instructionsList);
		}
		{
			JPanel inputPanel = new JPanel();
			GridBagLayout gbl_inputPanel = new GridBagLayout();
			gbl_inputPanel.columnWidths = new int[]{0, 0, 0};
			gbl_inputPanel.rowHeights = new int[]{0, 0};
			gbl_inputPanel.columnWeights = new double[]{0, 1.0, Double.MIN_VALUE};
			gbl_inputPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			inputPanel.setLayout(gbl_inputPanel);
			
			Integer[] stepNumbers = new Integer[list.size() + 1];
			for(int i = 0; i < stepNumbers.length;){
				stepNumbers[i] = ++i;
			}
			comboBoxStepNumbers = new JComboBox<>(stepNumbers);
			GridBagConstraints gbc_comboBoxStepNumbers = new GridBagConstraints();
			gbc_comboBoxStepNumbers.insets = new Insets(0, 0, 5, 0);
			gbc_comboBoxStepNumbers.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBoxStepNumbers.gridx = 0;
			gbc_comboBoxStepNumbers.gridy = 0;
			inputPanel.add(comboBoxStepNumbers, gbc_comboBoxStepNumbers);
			
			txtAreaInstruction = new JTextArea();
			txtAreaInstruction.setWrapStyleWord(true);
			txtAreaInstruction.setLineWrap(true);
			txtAreaInstruction.setBorder(new LineBorder(new Color(0, 0, 0)));
			GridBagConstraints gbc_textAreaInstruction = new GridBagConstraints();
			gbc_textAreaInstruction.insets = new Insets(0, 0, 5, 0);
			gbc_textAreaInstruction.fill = GridBagConstraints.BOTH;
			gbc_textAreaInstruction.gridx = 1;
			gbc_textAreaInstruction.gridy = 0;
			inputPanel.add(txtAreaInstruction, gbc_textAreaInstruction);
			
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
	
	public Collection<Instruction> getInstructions(){
		return list;
	}
	
	public class InstructionListRenderer implements ListCellRenderer<Instruction> {
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Instruction> jList, Instruction value, int index,	boolean isSelected, boolean cellHasFocus) {
			
			JLabel renderer = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(jList, value, index, isSelected, cellHasFocus);
			if(renderer != null){
				renderer.setText("Step " + value.getStepNumber() + ": " + value.getMessage());
			}
			return renderer;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent listSelectionEvent) {
		selectionChange();
	}
	
	private void selectionChange() {
		if(instructionsList.getSelectedInstruction() != null){
			selectedInstruction = instructionsList.getSelectedInstruction();
			btnEdit.setVisible(true);
			btnDelete.setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch(actionEvent.getActionCommand()){
			case "btnAdd":
				newInstruction();
				break;
			case "btnEdit":
				editInstruction();
				break;
			case "btnCancel":
				resetPanel();
				break;
			case "btnDelete":
				removeInstruction();
				break;
			default:
				break;
		}
	}
	
	private void newInstruction(){
		if(inputVerified()){
			if(selectedInstruction != null && !btnEdit.isVisible()){
				selectedInstruction.setMessage(txtAreaInstruction.getText());
			}else{
				list.add(new Instruction(list.size() + 1, txtAreaInstruction.getText()));
			}
		}
		
		resetPanel();
	}
	
	private void editInstruction(){
		comboBoxStepNumbers.setSelectedItem(selectedInstruction.getStepNumber());
		txtAreaInstruction.setText(selectedInstruction.getMessage());
		btnAdd.setText("OK");
		btnEdit.setVisible(false);
		btnCancel.setVisible(true);
	}
	
	private void resetPanel(){
		selectedInstruction = null;
		comboBoxStepNumbers.setSelectedIndex(list.size());
		txtAreaInstruction.setText("");
		btnAdd.setText("Add New");
		btnCancel.setVisible(false);
		btnDelete.setVisible(false);
		btnEdit.setVisible(false);
		instructionsList.reload();
	}
	
	private void removeInstruction(){
		list.remove(selectedInstruction);
		resetPanel();
	}
	
	private boolean inputVerified(){
		boolean result = true;
		if(txtAreaInstruction.getText().isEmpty()){
			result = false;
		}
		return result;
	}
}
