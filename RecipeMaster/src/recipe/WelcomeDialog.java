package recipe;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class WelcomeDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	
	public WelcomeDialog(){
		initialize();
	}
	
	public void initialize() {
		setBounds(100, 100, 976, 513);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel welcomeScreen = new WelcomeScreen();
			GridBagConstraints gbc_welcomeScreen = new GridBagConstraints();
			gbc_welcomeScreen.fill = GridBagConstraints.BOTH;
			gbc_welcomeScreen.gridx = 0;
			gbc_welcomeScreen.gridy = 0;
			contentPanel.add(welcomeScreen, gbc_welcomeScreen);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnDone = new JButton("Done");
				btnDone.setActionCommand("btnDone");
				btnDone.addActionListener(this);
				buttonPane.add(btnDone);
				getRootPane().setDefaultButton(btnDone);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch(actionEvent.getActionCommand()){
			case "btnDone":
				dispose();
				break;
			default:
				System.out.println("Missing Action Command: " + actionEvent.getActionCommand());
				break;
		}
	}

}
