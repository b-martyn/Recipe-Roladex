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
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

public class RecipeViewer extends JFrame implements ActionListener, WindowListener, PropertyChangeListener, ChangeListener{
	private static final long serialVersionUID = 1L;

	private RecipeManager recipeManager;
	
	private JTabbedPane tabbedPane;
	private JMenuBar menu;
	private JMenuItem mnuFileEdit;
	private JMenuItem mnuFileDelete;
	private JMenuItem mnuFilePrint;
	
	public RecipeViewer(RecipeManager recipeManager) {
		this.recipeManager = recipeManager;
		initialize();
	}
	
	private void initialize(){
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(100, 100, 850, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		
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
		if(recipeManager.getRecipes().isEmpty()){
			WelcomeDialog welcome = new WelcomeDialog();
			welcome.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			welcome.setAlwaysOnTop(true);
			welcome.setVisible(true);
			welcome.requestFocusInWindow();
		}else{
			reloadViewer();
		}
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		add(tabbedPane, gbc_tabbedPane);
	}
	
	public RecipeManager getRecipes(){
		return recipeManager;
	}
	
	private CategoryViewer getSelectedCategoryViewer(){
		if(tabbedPane.getTabCount() > 0 && tabbedPane.getSelectedIndex() != -1){
			return (CategoryViewer)tabbedPane.getComponent(tabbedPane.getSelectedIndex());
		}else{
			return null;
		}
	}
	
	private void reloadViewer(){
		tabbedPane.removeAll();
		for(String category : recipeManager.getRecipes().keySet()){
			CategoryViewer categoryPanel = new CategoryViewer(category, recipeManager.getRecipes().get(category));
			categoryPanel.addPropertyChangeListener(this);
			tabbedPane.addTab(category, categoryPanel);
		}
		reloadTabs();
	}
	
	private void moveRecipe(Recipe recipe, String newCategory){
		recipe.setCategory(newCategory);
		reloadViewer();
	}
	
	private void newRecipe(){
		RecipeEditor dialog = new RecipeEditor(recipeManager.getRecipes().keySet());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(this);
		dialog.setVisible(true);
	}
	
	private void editRecipe (){
		if(getSelectedCategoryViewer().getList().getSelected() != null){
			RecipeEditor dialog = new RecipeEditor(recipeManager.getRecipes().keySet(), getSelectedCategoryViewer().getList().getSelected());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.addWindowListener(this);
			dialog.setVisible(true);
		}
	}
	
	private void deleteRecipe(){
		if(getSelectedCategoryViewer().getList().getSelected() != null){
			recipeManager.deleteRecipe(getSelectedCategoryViewer().getList().getSelected(), getSelectedCategoryViewer().getCategory());
		}
		reloadViewer();
	}
	
	private void printRecipe(){
		if(getSelectedCategoryViewer().getList().getSelected() != null){
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(createRecipe(15, 15));
			//job.setPrintable(getSelectedCategoryViewer().getList().getSelected());
			if(job.printDialog()){
				try {
					job.print();
				} catch (PrinterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
				mnuFilePrint.setEnabled(true);
			}else{
				mnuFileEdit.setEnabled(false);
				mnuFileDelete.setEnabled(false);
				mnuFilePrint.setEnabled(true);
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
				recipeManager.save();
				break;
			case "mnuFilePrint":
				printRecipe();
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
				if(((RecipeEditor)windowEvent.getSource()).isNewRecipe()){
					recipeManager.newRecipe(((RecipeEditor)windowEvent.getSource()).getRecipe());
				}
				reloadViewer();
			}
		}else if(windowEvent.getSource() instanceof NewStringInput){
			if(!((NewStringInput)windowEvent.getSource()).isCancelled()){
				moveRecipe(getSelectedCategoryViewer().getList().getSelected(), ((NewStringInput)windowEvent.getSource()).getText());
			}
		}else if(windowEvent.getSource() instanceof RecipeViewer){
			recipeManager.save();
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
			if(((JTabbedPane)changeEvent.getSource()).getTabCount() > 0){
				checkSelectedRecipe();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void printRecipes(){
		for(String category : recipeManager.getRecipes().keySet()){
			System.out.printf("Category:%s | RecipeManager:%d\n", category, recipeManager.getRecipes().get(category).size());
		}
	}
	
	//@SuppressWarnings("unused")
	private Recipe createRecipe(int numIngredients, int numInstructions){
		Recipe recipe = new Recipe();
		recipe.setName("Test Recipe");
		recipe.setCategory("Test Category");
		Collection<RecipeIngredient> ingredients = new ArrayList<>();
		Collection<Instruction> instructions = new ArrayList<>();
		for(int i = 0; i < numIngredients; i++){
			
			if(i == 10 || i == 20){
				Ingredient ingredient = new Ingredient("This is a longer than normal ingredient that will take up more space");
				Measurement measurement = new Measurement(i, "Type");
				ingredients.add(new RecipeIngredient(ingredient, measurement));
				continue;
			}
			
			Ingredient ingredient = new Ingredient("Ingredient " + i);
			Measurement measurement = new Measurement(i, "Type");
			ingredients.add(new RecipeIngredient(ingredient, measurement));
		}
		recipe.setIngredients(ingredients);
		for(int i = 0; i < numInstructions; i++){
			if(i == 10 || i == 14){
				instructions.add(new Instruction(i, "This is a long instruction that I am testing to see what happens when the length of an instruction lasts longer than the width of the graphics panel that it is being drawn on to.  This will help in the formatting of printing pages of recipes and does stuff and things and places.  In a galaxy far far away..."));
			}else{
				instructions.add(new Instruction(i, "Instruction " + i));
			}
		}
		recipe.setInstructions(instructions);
		
		return recipe;
	}
}
