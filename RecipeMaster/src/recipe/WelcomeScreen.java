package recipe;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.JTextArea;

public class WelcomeScreen extends JPanel {
	private static final long serialVersionUID = 1L;

	public WelcomeScreen() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblHeader = new JLabel("Recipe Roladex");
		lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 22));
		GridBagConstraints gbc_lblHeader = new GridBagConstraints();
		gbc_lblHeader.insets = new Insets(0, 0, 5, 0);
		gbc_lblHeader.gridx = 0;
		gbc_lblHeader.gridy = 0;
		add(lblHeader, gbc_lblHeader);
		
		JTextArea txtBody = new JTextArea();
		txtBody.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		txtBody.setLineWrap(true);
		txtBody.setEditable(false);
		txtBody.setText("Welcome to the recipe roladex.\r\n\r\nTo start click the new button up above.  This will open a new window that will ask you to provide the name of the recipe you wish to enter along with a way to enter in a list of ingredients needed and instructions for the recipe.  Once you confirm a new tab will open up in this window so you can view your input.\r\n\r\nIf you need to make any changes, simply click on the recipe you wish to change and click the edit button above.  To delete follow these same steps but click the delete button.");
		GridBagConstraints gbc_txtBody = new GridBagConstraints();
		gbc_txtBody.fill = GridBagConstraints.BOTH;
		gbc_txtBody.gridx = 0;
		gbc_txtBody.gridy = 1;
		add(txtBody, gbc_txtBody);
		
	}

}
