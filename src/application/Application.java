package application;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Application {

	private JFrame frame;
	private JTable table;
	private JTextField text_dataset_src;
	private JTextField text_selected_data;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 629, 456);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
		);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
		);
		
		JPanel panel_dataset = new JPanel();
		tabbedPane.addTab("Dataset", null, panel_dataset, null);
		
		table = new JTable();
		
		JLabel lblNewLabel = new JLabel("Dataset");
		
		text_dataset_src = new JTextField();
		text_dataset_src.setToolTipText("le chemin (ou l'url) du dataset ...");
		text_dataset_src.setColumns(10);
		
		JCheckBox chckbxUrl = new JCheckBox("url");
		chckbxUrl.setToolTipText("spécifier si vous voulez récupérer votre dataset depuis un url sur internet ou localement avec son chemin d'emplacement");
		
		JButton btnCharger = new JButton("charger");
		btnCharger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! chckbxUrl.isSelected()) {
					JFileChooser fileChooser = new JFileChooser();
		            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		            fileChooser.setAcceptAllFileFilterUsed(false);
		            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("ficher texte (.txt)", new String[] { "txt" }));
		            fileChooser.showOpenDialog(null);
		            try {
		            	text_dataset_src.setText(fileChooser.getSelectedFile().getAbsolutePath());
		              
		            } catch (NullPointerException nullPointerException) {}
				}else {
					// when url is checked
				}
		
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.GRAY));
		GroupLayout gl_panel_dataset = new GroupLayout(panel_dataset);
		gl_panel_dataset.setHorizontalGroup(
			gl_panel_dataset.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_dataset.createSequentialGroup()
					.addGroup(gl_panel_dataset.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_dataset.createSequentialGroup()
							.addGap(7)
							.addComponent(table, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE))
						.addGroup(gl_panel_dataset.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel)
							.addGap(6)
							.addComponent(text_dataset_src, GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxUrl)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCharger)
							.addGap(81))
						.addGroup(gl_panel_dataset.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel_dataset.setVerticalGroup(
			gl_panel_dataset.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_dataset.createSequentialGroup()
					.addGap(8)
					.addComponent(table, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_dataset.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_dataset.createParallelGroup(Alignment.BASELINE)
							.addComponent(text_dataset_src, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addComponent(chckbxUrl))
						.addComponent(btnCharger))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 56, Short.MAX_VALUE)
					.addGap(10))
		);
		
		JButton btnNewButton = new JButton("ajouter ligne");
		
		JButton btnNewButton_1 = new JButton("appliquer les modifications");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("Ligne séléctionnée");
		
		text_selected_data = new JTextField();
		text_selected_data.setToolTipText("les donnés ici sont celles de la ligne séléctionnée séparés par des virgules");
		text_selected_data.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(lblNewLabel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(text_selected_data, GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
						.addComponent(text_selected_data, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		panel_dataset.setLayout(gl_panel_dataset);
		
		JPanel panel_desc = new JPanel();
		tabbedPane.addTab("Description", null, panel_desc, null);
		
		JPanel panel_plots = new JPanel();
		tabbedPane.addTab("Graphiques", null, panel_plots, null);
		
		JPanel panel_mesures = new JPanel();
		tabbedPane.addTab("Mesures", null, panel_mesures, null);
		GroupLayout gl_panel_mesures = new GroupLayout(panel_mesures);
		gl_panel_mesures.setHorizontalGroup(
			gl_panel_mesures.createParallelGroup(Alignment.LEADING)
				.addGap(0, 429, Short.MAX_VALUE)
		);
		gl_panel_mesures.setVerticalGroup(
			gl_panel_mesures.createParallelGroup(Alignment.LEADING)
				.addGap(0, 233, Short.MAX_VALUE)
		);
		panel_mesures.setLayout(gl_panel_mesures);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}
}
