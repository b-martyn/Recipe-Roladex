package recipe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class CategoryList extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	private Set<String> list;
	
	public CategoryList(Set<String> list){
		this.list = list;
		initialize();
	}
	
	private void initialize(){
		for(String category : list){
			JMenuItem menuItem = new JMenuItem(category);
			menuItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					firePropertyChange("categoryselected", true, menuItem.getText());
				}
			});
			add(menuItem);
		}
		addSeparator();
		JMenuItem newCategory = new JMenuItem("New");
		newCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewCategory newCategoryDialog = new NewCategory();
				newCategoryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newCategoryDialog.setVisible(true);
				newCategoryDialog.addPropertyChangeListener("newcategory", new PropertyChangeListener(){
					@Override
					public void propertyChange(PropertyChangeEvent pce){
						firePropertyChange("newcategory", pce.getOldValue(), pce.getNewValue());
					}
				});
			}
		});
		add(newCategory);
	}
}
