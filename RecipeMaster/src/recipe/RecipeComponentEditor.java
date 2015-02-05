package recipe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class RecipeComponentEditor<T extends RecipeComponent> extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected Recipe recipe;
	protected RecipeComponentList<T> componentList;
	protected JPanel componentPanel;
	protected JPanel controlPanel;
	
	public RecipeComponentEditor(Recipe recipe){
		this.recipe = recipe;
		initialize();
	}
	
	private void initialize(){
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[]{0, 0};
		gbl.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl.rowHeights = new int[]{0, 0, 0, 0};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl);
		
		GridBagConstraints gbc_recipeComponentList = new GridBagConstraints();
		gbc_recipeComponentList.insets = new Insets(0, 0, 0, 0);
		gbc_recipeComponentList.fill = GridBagConstraints.BOTH;
		gbc_recipeComponentList.gridx = 0;
		gbc_recipeComponentList.gridy = 0;
		add(componentList = componentList(), gbc_recipeComponentList);

		GridBagConstraints gbc_componentPanel = new GridBagConstraints();
		gbc_componentPanel.insets = new Insets(0, 0, 0, 0);
		gbc_componentPanel.fill = GridBagConstraints.BOTH;
		gbc_componentPanel.gridx = 0;
		gbc_componentPanel.gridy = 1;
		add(componentInput(), gbc_componentPanel);
		
		GridBagConstraints gbc_controlPanel = new GridBagConstraints();
		gbc_controlPanel.insets = new Insets(0, 0, 0, 0);
		gbc_controlPanel.fill = GridBagConstraints.BOTH;
		gbc_controlPanel.gridx = 0;
		gbc_controlPanel.gridy = 2;
		add(controls(), gbc_controlPanel);
	}
	
	public RecipeComponentList<T> getRecipeComponentList(){
		return componentList;
	}
	
	public JPanel getInputPanel(){
		return componentPanel;
	}
	
	public JPanel getControlPanel(){
		return controlPanel;
	}
	
	protected abstract RecipeComponentList<T> componentList();
	
	protected abstract JPanel componentInput();
	
	private JPanel controls(){
		controlPanel = new JPanel();
		GridBagLayout gbl_controlPanel = new GridBagLayout();
		gbl_controlPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_controlPanel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_controlPanel.rowHeights = new int[]{0, 0};
		gbl_controlPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		controlPanel.setLayout(gbl_controlPanel);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ControlActionListener());
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 0;
		controlPanel.add(btnAdd, gbc_btnAdd);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ControlActionListener());
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 0, 5);
		gbc_btnEdit.gridx = 1;
		gbc_btnEdit.gridy = 0;
		controlPanel.add(btnEdit, gbc_btnEdit);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ControlActionListener());
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
		gbc_btnDelete.gridx = 2;
		gbc_btnDelete.gridy = 0;
		controlPanel.add(btnDelete, gbc_btnDelete);
		
		return controlPanel;
	}
	
	private class ControlActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			firePropertyChange(((JButton)e.getSource()).getText(), true, e.getSource());
		}
	}
}
