package recipe;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RecipeViewer extends JFrame implements ActionListener, WindowListener, PropertyChangeListener, ChangeListener{
	private static final long serialVersionUID = 1L;

	private Recipes recipes;
	
	private JTabbedPane tabbedPane;
	private JMenuBar menu;
	private JMenuItem mnuFileEdit;
	private JMenuItem mnuFileDelete;
	private JMenuItem mnuFilePrint;
	
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
		{
			menu = new JMenuBar();
			setJMenuBar(menu);
			
			JMenu mnuFile = new JMenu("File");
			menu.add(mnuFile);
			
			JMenuItem mnuFileNew = new JMenuItem("New");
			mnuFileNew.setActionCommand("mnuFileNew");
			mnuFileNew.addActionListener(this);
			mnuFile.add(mnuFileNew);
			
			mnuFileEdit = new JMenuItem("Edit");
			mnuFileEdit.setActionCommand("mnuFileEdit");
			mnuFileEdit.addActionListener(this);
			mnuFileEdit.setEnabled(false);
			mnuFile.add(mnuFileEdit);
			
			mnuFileDelete = new JMenuItem("Delete");
			mnuFileDelete.setActionCommand("mnuFileDelete");
			mnuFileDelete.addActionListener(this);
			mnuFileDelete.setEnabled(false);
			mnuFile.add(mnuFileDelete);
			
			JMenuItem mnuFileSave = new JMenuItem("Save");
			mnuFileSave.setActionCommand("mnuFileSave");
			mnuFileSave.addActionListener(this);
			mnuFile.add(mnuFileSave);
			
			mnuFilePrint = new JMenuItem("Print");
			mnuFilePrint.setActionCommand("mnuFilePrint");
			mnuFilePrint.addActionListener(this);
			mnuFilePrint.setEnabled(false);
			mnuFile.add(mnuFilePrint);
			
			JSeparator separator = new JSeparator();
			mnuFile.add(separator);
			
			JMenuItem mnuFileExit = new JMenuItem("Exit");
			mnuFileExit.setActionCommand("mnuFileExit");
			mnuFileExit.addActionListener(this);
			mnuFile.add(mnuFileExit);
		}
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(this);
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
				categoryPanel.addPropertyChangeListener(this);
				tabbedPane.addTab(category, categoryPanel);
			}
		}
	}
	
	public Recipes getRecipes(){
		return recipes;
	}
	
	public void addActionListener(ActionListener actionListener){
		for(int i = 0; i < menu.getMenuCount(); i++){
			//System.out.println(menu.getMenu(i));
			for(int j = 0; j < menu.getMenu(i).getItemCount(); j++){
				if(menu.getMenu(i).getItem(j) instanceof JMenuItem){
					menu.getMenu(i).getItem(j).addActionListener(actionListener);
				}
			}
		}
	}
	
	private CategoryViewer getSelectedCategoryViewer(){
		if(tabbedPane.getComponent(tabbedPane.getSelectedIndex()) instanceof CategoryViewer){
			return (CategoryViewer)tabbedPane.getComponent(tabbedPane.getSelectedIndex());
		}else{
			return null;
		}
	}
	
	private void verifyRecipe(Recipe recipe){
		if(recipes.getRecipes().keySet().size() != tabbedPane.getTabCount()){
			CategoryLoop:
			for(String category : recipes.getRecipes().keySet()){
				for(int i = 0; i < tabbedPane.getTabCount() - 1; i++){
					if(tabbedPane.getTitleAt(i).equals(category)){
						continue CategoryLoop;
					}
				}
				CategoryViewer newViewer = new CategoryViewer(category, recipes.getRecipes().get(category));
				newViewer.addPropertyChangeListener(this);
				tabbedPane.add(category, newViewer);
			}
		}else if(!recipes.getRecipes().get(recipe.getCategory()).contains(recipe)){
			recipes.getRecipes().get(recipe.getCategory()).add(recipe);
		}
		
		reloadTabs();
	}
	
	private void moveRecipe(Recipe recipe, String newCategory){
		recipe.setCategory(newCategory);
		verifyRecipe(recipe);
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
	
	private void reloadTabs(){
		for(Component component : tabbedPane.getComponents()){
			if(component instanceof CategoryViewer){
				((CategoryViewer)component).getList().reload();
			}
		}
	}
	
	private void newCategory(){
		NewStringInput newCategoryDialog = new NewStringInput("New Category");
		newCategoryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		newCategoryDialog.addWindowListener(this);
		newCategoryDialog.setVisible(true);
	}
	
	private void checkSelectedRecipe(){
		if(getSelectedCategoryViewer() != null){
			if(getSelectedCategoryViewer().getList().getSelected() != null){
				mnuFileEdit.setEnabled(true);
				mnuFileDelete.setEnabled(true);
			}else{
				mnuFileEdit.setEnabled(false);
				mnuFileDelete.setEnabled(false);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch(actionEvent.getActionCommand()){
			case "mnuFileNew":
				newRecipe();
				break;
			case "mnuFileEdit":
				editRecipe();
				break;
			case "mnuFileDelete":
				deleteRecipe();
				break;
			case "mnuFileSave":
				//Do Nothing
				break;
			case "mnuFileExit":
				dispose();
				break;
			case "categoryselected":
				moveRecipe(getSelectedCategoryViewer().getList().getSelected(), ((JMenuItem)actionEvent.getSource()).getText());
				break;
			case "newcategoryselected":
				newCategory();
				break;
			default:
				System.out.println("Missing Action Command: " + actionEvent.getActionCommand());
				break;
		}
	}
	
	@Override
	public void windowActivated(WindowEvent windowEvent) {
		//Do Nothing
		//handleEvent(windowEvent);
	}

	@Override
	public void windowClosed(WindowEvent windowEvent) {
		if(windowEvent.getSource() instanceof RecipeEditor){
			if(((RecipeEditor)windowEvent.getSource()).getRecipe() != null && !((RecipeEditor)windowEvent.getSource()).isCancelled()){
				verifyRecipe(((RecipeEditor)windowEvent.getSource()).getRecipe());
			}
		}else if(windowEvent.getSource() instanceof NewStringInput){
			if(!((NewStringInput)windowEvent.getSource()).isCancelled()){
				moveRecipe(getSelectedCategoryViewer().getList().getSelected(), ((NewStringInput)windowEvent.getSource()).getText());
			}
		}
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

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		switch(propertyChangeEvent.getPropertyName()){
			case "selectedRecipeChange":
				checkSelectedRecipe();
				break;
			case "ancestor":
				//Do Nothing
				break;
			default:
				System.out.println("Missing Property Change Event: " + propertyChangeEvent.getPropertyName());
				break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent changeEvent) {
		if(changeEvent.getSource() instanceof JTabbedPane){
			checkSelectedRecipe();
		}
	}
	
	@SuppressWarnings("unused")
	private void printRecipes(){
		for(String category : recipes.getRecipes().keySet()){
			System.out.printf("Category:%s | Recipes:%d\n", category, recipes.getRecipes().get(category).size());
		}
	}
}
