package recipe;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow extends JFrame implements ActionListener, WindowListener, ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private RecipeManager recipeManager;
	private RecipeViewer recipeViewer;
	
	private JMenuItem mnuFileEdit;
	private JMenuItem mnuFileDelete;
	private JMenuItem mnuFilePrint;
	
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow(new RecipeManager(new RecipesDAOFilesImpl()));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainWindow(RecipeManager recipeManager){
		this.recipeManager = recipeManager;
		initialize();
	}
	
	private void initialize(){
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(100, 100, 850, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		initMenu();
	}
	
	private void initMenu(){
		{
			JMenuBar menu = new JMenuBar();
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
		recipeViewer = new RecipeViewer(recipeManager.getRecipes());
		recipeViewer.addChangeListener(this);
		setComponent(recipeViewer);
		
		System.out.println(recipeManager.getRecipes().size());
	}
	
	private void setComponent(Component component){
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		add(component, gbc_tabbedPane);
	}
	
	private void newRecipe(){
		RecipeEditor dialog = new RecipeEditor(recipeManager.getRecipes().keySet());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(this);
		dialog.setVisible(true);
	}
	
	private void editRecipe(){
		RecipeEditor dialog = new RecipeEditor(recipeManager.getRecipes().keySet(), recipeViewer.getSelectedRecipe());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(this);
		dialog.setVisible(true);
	}
	
	private void deleteRecipe(){
		recipeManager.deleteRecipe(recipeViewer.getSelectedRecipe(), recipeViewer.getSelectedRecipe().getCategory());
		recipeViewer.reloadPanes();
	}
	
	private void printRecipe(){
		Thread printingThread = new Thread(){
			@Override
			public void run(){
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(recipeViewer.getSelectedRecipe());
				if(job.printDialog()){
					try {
						job.print();
					} catch (PrinterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		printingThread.start();
	}
	
	private void toggleRecipeControls(){
		if(recipeViewer.getSelectedRecipe() != null){
			mnuFileEdit.setEnabled(true);
			mnuFileDelete.setEnabled(true);
			mnuFilePrint.setEnabled(true);
		}else{
			mnuFileEdit.setEnabled(false);
			mnuFileDelete.setEnabled(false);
			mnuFilePrint.setEnabled(false);
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
			default:
				System.out.println("Missing Action Command: " + actionEvent.getActionCommand());
				break;
		}
	}

	@Override
	public void windowActivated(WindowEvent windowEvent) {
		//Do Nothing
	}

	@Override
	public void windowClosed(WindowEvent windowEvent) {
		if(windowEvent.getSource() instanceof RecipeEditor){
			RecipeEditor recipeEditor = (RecipeEditor)windowEvent.getSource();
			if(recipeEditor.getRecipe() != null && !recipeEditor.isCancelled()){
				if(recipeEditor.isNewRecipe()){
					recipeManager.newRecipe(recipeEditor.getRecipe());
				}
				recipeViewer.reloadPanes();
			}
		}else if(windowEvent.getSource() instanceof MainWindow){
			recipeManager.save();
		}
	}

	@Override
	public void windowClosing(WindowEvent windowEvent) {
		//Do Nothing
	}

	@Override
	public void windowDeactivated(WindowEvent windowEvent) {
		//Do Nothing
	}

	@Override
	public void windowDeiconified(WindowEvent windowEvent) {
		//Do Nothing
	}

	@Override
	public void windowIconified(WindowEvent windowEvent) {
		//Do Nothing
	}

	@Override
	public void windowOpened(WindowEvent windowEvent) {
		//Do Nothing
	}
	
	@Override
	public void stateChanged(ChangeEvent changeEvent) {
		toggleRecipeControls();
	}
}