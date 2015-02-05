package recipe;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class RecipeComponentList<T extends RecipeComponent> extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected Recipe recipe;
	protected JList<T> list;
	
	public RecipeComponentList(Recipe recipe){
		this.recipe = recipe;
		initialize();
	}
	
	private void initialize(){
		setMinimumSize(new Dimension(0, 100));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
		
		list = new JList<>();
		loadListModel();
		list.setCellRenderer(cellRenderer());
		list.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				firePropertyChange("listSelectionEvent", true, list.getSelectedValue());
			}
		});
		scrollPane.setViewportView(list);
	}
	
	public abstract void loadListModel();
	
	public JList<T> getList(){
		return list;
	}
	
	protected abstract ListCellRenderer<T> cellRenderer();
}
