package recipe;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RecipeIngredientList extends JPanel implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	
	private Collection<RecipeIngredient> ingredientList;
	private JList<RecipeIngredient> list;
	
	private RecipeIngredient selectedIngredient = null;
	
	public RecipeIngredientList(Collection<RecipeIngredient> ingredientList) {
		this.ingredientList = ingredientList;
		initialize();
	}
	
	private void initialize(){
		GridBagLayout gbl_ingredientListPanel = new GridBagLayout();
		gbl_ingredientListPanel.columnWidths = new int[]{0, 0};
		gbl_ingredientListPanel.rowHeights = new int[]{0, 0, 0};
		gbl_ingredientListPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_ingredientListPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gbl_ingredientListPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
		
		list = new JList<>(ingredientList.toArray(new RecipeIngredient[ingredientList.size()]));
		list.setCellRenderer(new IngredientListRenderer());
		list.addListSelectionListener(this);
		scrollPane.setViewportView(list);
	}
	
	public void reload(){
		list.setListData(ingredientList.toArray(new RecipeIngredient[ingredientList.size()]));
	}
	
	public void setSelectedIngredient(RecipeIngredient value){
		if(ingredientList.contains(value)){
			list.setSelectedValue(value, true);
		}else{
			throw new IllegalArgumentException("value not found:\n" + value);
		}
	}
	
	public RecipeIngredient getSelectedIngredient(){
		return selectedIngredient;
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(list.getSelectedValue() != null){
			selectedIngredient = list.getSelectedValue();
		}
	}
	
	public static class IngredientListRenderer implements ListCellRenderer<RecipeIngredient> {
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends RecipeIngredient> jList, RecipeIngredient value, int index,	boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(jList, value, index, isSelected, cellHasFocus);
			
			if(renderer != null){
				renderer.setText(value.getMeasurement().getAmount() + " " + value.getMeasurement().getType() + " " + value.getIngredient().getName());
			}
			
			return renderer;
		}
	}
}
