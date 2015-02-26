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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RecipeInstructionEditor extends JPanel implements ListSelectionListener, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private List<Instruction> instructionList = new ArrayList<>();
	private Instruction selectedInstruction = null;
	
	private JTextArea txtAreaInstruction;
	private JList<Instruction> list;
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
			instructionList.add(instruction.clone());
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
			JPanel instructionListPanel = new JPanel();
			GridBagLayout gbl_instructionListPanel = new GridBagLayout();
			gbl_instructionListPanel.columnWidths = new int[]{0, 0};
			gbl_instructionListPanel.rowHeights = new int[]{0, 0, 0};
			gbl_instructionListPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_instructionListPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			instructionListPanel.setLayout(gbl_instructionListPanel);
			
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 1;
			instructionListPanel.add(scrollPane, gbc_scrollPane);
			
			list = new JList<>(instructionList.toArray(new Instruction[instructionList.size()]));
			list.setCellRenderer(new InstructionListRenderer());
			list.addListSelectionListener(this);
			scrollPane.setViewportView(list);
			
			GridBagConstraints gbc_instructionListPanel = new GridBagConstraints();
			gbc_instructionListPanel.insets = new Insets(0, 0, 0, 0);
			gbc_instructionListPanel.fill = GridBagConstraints.BOTH;
			gbc_instructionListPanel.gridx = 0;
			gbc_instructionListPanel.gridy = 0;
			add(instructionListPanel, gbc_instructionListPanel);
		}
		{
			JPanel inputPanel = new JPanel();
			GridBagLayout gbl_inputPanel = new GridBagLayout();
			gbl_inputPanel.columnWidths = new int[]{0, 0};
			gbl_inputPanel.rowHeights = new int[]{0, 0};
			gbl_inputPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_inputPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			inputPanel.setLayout(gbl_inputPanel);
			
			txtAreaInstruction = new JTextArea();
			txtAreaInstruction.setLineWrap(true);
			txtAreaInstruction.setBorder(new LineBorder(new Color(0, 0, 0)));
			GridBagConstraints gbc_textAreaInstruction = new GridBagConstraints();
			gbc_textAreaInstruction.insets = new Insets(0, 0, 5, 0);
			gbc_textAreaInstruction.fill = GridBagConstraints.BOTH;
			gbc_textAreaInstruction.gridx = 0;
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
		return instructionList;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(ListSelectionEvent listSelectionEvent) {
		if(list.getSelectedValue() != null){
			selectedInstruction = (Instruction)((JList<Instruction>)listSelectionEvent.getSource()).getSelectedValue();
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
				instructionList.add(new Instruction(instructionList.size() + 1, txtAreaInstruction.getText()));
			}
		}
		
		resetPanel();
	}
	
	private void editInstruction(){
		txtAreaInstruction.setText(selectedInstruction.getMessage());
		btnAdd.setText("OK");
		btnEdit.setVisible(false);
		btnCancel.setVisible(true);
	}
	
	private void resetPanel(){
		selectedInstruction = null;
		txtAreaInstruction.setText("");
		btnAdd.setText("Add New");
		list.clearSelection();
		btnCancel.setVisible(false);
		btnDelete.setVisible(false);
		btnEdit.setVisible(false);
		list.setListData(instructionList.toArray(new Instruction[instructionList.size()]));
	}
	
	private void removeInstruction(){
		instructionList.remove(selectedInstruction);
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
