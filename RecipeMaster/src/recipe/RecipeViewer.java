package recipe;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.EventObject;

public class RecipeViewer extends JPanel implements ActionListener, PopupMenuListener, WindowListener{
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	private Recipes recipes;
	
	public RecipeViewer(Recipes recipes) {
		this.recipes = recipes;
		initialize();
	}
	
	private void initialize(){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		MainMenu controlPanel = new MainMenu();
		controlPanel.addActionListener(this);
		GridBagConstraints gbc_contolPanel = new GridBagConstraints();
		gbc_contolPanel.insets = new Insets(0, 0, 5, 0);
		gbc_contolPanel.fill = GridBagConstraints.BOTH;
		gbc_contolPanel.gridx = 0;
		gbc_contolPanel.gridy = 0;
		add(controlPanel, gbc_contolPanel);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		add(tabbedPane, gbc_tabbedPane);
		
		if(recipes.getRecipes().isEmpty()){
			tabbedPane.addTab("Welcome", new WelcomeScreen());
		}else{
			for(String category : recipes.getRecipes().keySet()){
				CategoryViewer categoryPanel = new CategoryViewer(category, recipes.getRecipes().get(category));
				tabbedPane.addTab(category, categoryPanel);
			}
		}
	}
	
	public CategoryViewer getSelectedCategoryViewer(){
		if(tabbedPane.getComponent(tabbedPane.getSelectedIndex()) instanceof CategoryViewer){
			return (CategoryViewer)tabbedPane.getComponent(tabbedPane.getSelectedIndex());
		}else{
			return null;
		}
	}
	
	public JTabbedPane getTabbedPane(){
		return tabbedPane;
	}
	
	public void verifyRecipe(Recipe recipe){
		if(recipes.getRecipes().keySet().size() != tabbedPane.getTabCount()){
			CategoryLoop:
			for(String category : recipes.getRecipes().keySet()){
				for(int i = 0; i < tabbedPane.getTabCount() - 1; i++){
					if(tabbedPane.getTitleAt(i).equalsIgnoreCase(category)){
						continue CategoryLoop;
					}
				}
				tabbedPane.add(category, new CategoryViewer(category, recipes.getRecipes().get(category)));
			}
		}else if(!recipes.getRecipes().get(recipe.getCategory()).contains(recipe)){
			recipes.getRecipes().get(recipe.getCategory()).add(recipe);
		}
		
		reloadTabs();
	}
	
	public void moveRecipe(Recipe recipe, String newCategory){
		recipe.setCategory(newCategory);
		verifyRecipe(recipe);
	}
	
	private void moveRecipe(Component initiator){
		if(getSelectedCategoryViewer().getList().getSelected() != null){
			RecipeCategoryEditor categoryEditor = new RecipeCategoryEditor(getSelectedCategoryViewer().getList().getSelected(), recipes.getRecipes().keySet());
			categoryEditor.show(initiator, 0, initiator.getHeight());
			categoryEditor.addActionListener(this);
		}
	}
	
	private void newRecipe(){
		RecipeEditor dialog = new RecipeEditor(recipes.getRecipes().keySet());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(this);
		dialog.setVisible(true);
	}
	
	private void editRecipe (){
		if(getSelectedCategoryViewer().getList().getSelected() != null){
			RecipeEditor dialog = new RecipeEditor(recipes.getRecipes().keySet(), getSelectedCategoryViewer().getList().getSelected());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.addWindowListener(this);
			dialog.setVisible(true);
		}
	}
	
	private void deleteRecipe(){
		if(getSelectedCategoryViewer().getList().getSelected() != null){
			recipes.deleteRecipe(getSelectedCategoryViewer().getList().getSelected(), getSelectedCategoryViewer().getCategory());
			reloadTabs();
		}
	}
	
	public void reloadTabs(){
		for(Component component : tabbedPane.getComponents()){
			if(component instanceof CategoryViewer){
				((CategoryViewer)component).getList().reload();
			}
		}
	}
	
	private void newCategory(){
		NewCategory newCategoryDialog = new NewCategory();
		newCategoryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		newCategoryDialog.addWindowListener(this);
		newCategoryDialog.setVisible(true);
	}
	
	private void handleEvent(EventObject eventObject){
		if(eventObject instanceof PopupMenuEvent){
			if(eventObject.getSource() instanceof RecipeCategoryEditor){
				verifyRecipe(getSelectedCategoryViewer().getList().getSelected());
			}
		}else if(eventObject instanceof WindowEvent){
			if(eventObject.getSource() instanceof RecipeEditor){
				if(((RecipeEditor)eventObject.getSource()).getRecipe() != null && !((RecipeEditor)eventObject.getSource()).isCancelled()){
					verifyRecipe(((RecipeEditor)eventObject.getSource()).getRecipe());
				}
			}else if(eventObject.getSource() instanceof NewCategory){
				moveRecipe(getSelectedCategoryViewer().getList().getSelected(), ((NewCategory)eventObject.getSource()).getText());
			}
		}else if(eventObject instanceof ActionEvent){
			ActionEvent actionEvent = (ActionEvent)eventObject;
			switch(actionEvent.getActionCommand()){
				case "mnuNew":
					newRecipe();
					break;
				case "mnuEdit":
					editRecipe();
					break;
				case "mnuDelete":
					deleteRecipe();
					break;
				case "mnuMove":
					moveRecipe((Component)actionEvent.getSource());
					break;
				case "categoryselected":
					moveRecipe(getSelectedCategoryViewer().getList().getSelected(), ((JMenuItem)actionEvent.getSource()).getText());
					break;
				case "newcategoryselected":
					newCategory();
					break;
				default:
					System.out.println(actionEvent.getSource());
					break;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		handleEvent(actionEvent);
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
		//Do Nothing
		//handleEvent(popupMenuEvent);
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
		handleEvent(popupMenuEvent);
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
		//Do Nothing
		//handleEvent(popupMenuEvent);
	}

	@Override
	public void windowActivated(WindowEvent windowEvent) {
		//Do Nothing
		//handleEvent(windowEvent);
	}

	@Override
	public void windowClosed(WindowEvent windowEvent) {
		handleEvent(windowEvent);
	}

	@Override
	public void windowClosing(WindowEvent windowEvent) {
		//Do Nothing
		//handleEvent(windowEvent);
	}

	@Override
	public void windowDeactivated(WindowEvent windowEvent) {
		//Do Nothing
		//handleEvent(windowEvent);
	}

	@Override
	public void windowDeiconified(WindowEvent windowEvent) {
		//Do Nothing
		//handleEvent(windowEvent);
	}

	@Override
	public void windowIconified(WindowEvent windowEvent) {
		//Do Nothing
		//handleEvent(windowEvent);
	}

	@Override
	public void windowOpened(WindowEvent windowEvent) {
		//Do Nothing
		//handleEvent(windowEvent);
	}
	
	@SuppressWarnings("unused")
	private void printRecipes(){
		for(String category : recipes.getRecipes().keySet()){
			System.out.printf("Category:%s | Recipes:%d\n", category, recipes.getRecipes().get(category).size());
		}
	}
}
