package recipe;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class InstructionList extends RecipeComponentList<Instruction> {
	private static final long serialVersionUID = 1L;

	public InstructionList(Recipe recipe) {
		super(recipe);
	}

	@Override
	public void loadListModel() {
		DefaultListModel<Instruction> instructionModel = new DefaultListModel<Instruction>();
		if(recipe.getIngredients() != null){
			List<Instruction> copy = new ArrayList<Instruction>(recipe.getInstructions());
			Collections.sort(copy);
			for(Instruction instruction : copy){
				instructionModel.addElement(instruction);
			}
		}
		list.setModel(instructionModel);
	}

	@Override
	protected ListCellRenderer<Instruction> cellRenderer() {
		return new InstructionListRenderer();
	}
	
	private class InstructionListRenderer implements ListCellRenderer<Instruction> {
		
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
}
