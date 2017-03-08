package org.SE6990.tableEditor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartTableCreation {

	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;

	public StartTableCreation() {
		prepareGUI();
	}

	public static void main(String[] args) {
		StartTableCreation startTableCreation = new StartTableCreation();
		startTableCreation.showGUI();
	}

	private void prepareGUI() {
		mainFrame = new JFrame("Java Swing Examples");
		mainFrame.setSize(400, 400);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		headerLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);

		statusLabel.setSize(350, 100);

		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);
	}

	private void showGUI() {
		headerLabel.setText("Latex Table Creator");

		JLabel rowsLabel = new JLabel("Please enter the number of Rows in the table: ", JLabel.RIGHT);
		JLabel columnsLabel = new JLabel("Please enter the number of Columns in the table: ", JLabel.CENTER);
		final JTextField userRows = new JTextField(6);
		final JTextField userColumns = new JTextField(6);

		JButton loginButton = new JButton("Create Table");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MultiSpanCellTableExample frame = new MultiSpanCellTableExample(Integer.parseInt(userRows.getText()),
						Integer.parseInt(userColumns.getText()));
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
			}
		});

		controlPanel.add(rowsLabel);
		controlPanel.add(userRows);
		controlPanel.add(columnsLabel);
		controlPanel.add(userColumns);
		controlPanel.add(loginButton);
		mainFrame.setVisible(true);
	}
}