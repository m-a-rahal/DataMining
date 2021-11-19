package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartPanel;

import data.Dataset;
import data.Frequences;
import data.Type;
import diagrammes.Diagrammes;
import input_sources.FileManager;
import input_sources.URLManager;

public class Application {
	private Application application;
	private Dataset dataset;
	private JFrame frame;
	private JTable table_dataset;
	private JTextField text_dataset_src;
	private JTable table_mesures;
	private ChartPanel panel_diagrammes;
	private JTextArea textArea_description;
	private JSlider slider_moy_tronquee;
	private JLabel label_pourcentage_moy_tronquee;
	private Integer position_moy_tronquee = null;
	private String dest_file = null;
	private JTextField textField_dest_file;
	private JTable table_attributs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					// Maximizer la fenêtre et l'afficher
					window.frame.setExtendedState(window.frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
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
		application = this; // self reference, needed later
		frame = new JFrame();
		frame.setBounds(100, 100, 830, 482);
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
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.GRAY));
		
		JPanel panel_2 = new JPanel();
		GroupLayout gl_panel_dataset = new GroupLayout(panel_dataset);
		gl_panel_dataset.setHorizontalGroup(
			gl_panel_dataset.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_dataset.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_dataset.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_dataset.setVerticalGroup(
			gl_panel_dataset.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_dataset.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);
		
		table_dataset = add_table_to(panel_2);
		JButton btnNewButton = new JButton("ajouter ligne");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table_dataset.getModel();
				model.addRow(new Double[model.getColumnCount()]);
				table_dataset.changeSelection(model.getRowCount()-1, 0, true, false);
			}
		});
		
		JButton btn_appliquer_changements = new JButton("appliquer les modifications");
		btn_appliquer_changements.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				appliquer_les_changements();
			}
		});
		
		JButton btnSupprimerligne = new JButton("supprimer lignes");
		btnSupprimerligne.setToolTipText("supprimer les lignes sélectionnées");
		btnSupprimerligne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table_dataset.getModel();
				int[] rows = table_dataset.getSelectedRows();
				for (int i = 0; i < rows.length; i++) {
					model.removeRow(rows[rows.length-i-1]);
				}
			}
		});
		
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
		            try {
		            	text_dataset_src.setText(FileManager.ChooseFileWindow(application));
		            	dataset = FileManager.extract_dataset(text_dataset_src.getText());
		            	text_dataset_src.getText();
		            } catch (NullPointerException e1) {
		            	e1.printStackTrace();
		            } catch (FileNotFoundException e1) {e1.printStackTrace();}
				}else {
					try {
						dataset = URLManager.extract_dataset(text_dataset_src.getText());
					} catch (IOException e1) {
						afficherMessage("l'URL que vous avez introduit est éronné");
					}
				}
				try {
					load_dataset_on_table();
					update_description_text();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			
		});
		
		JButton btnSauvegarder = new JButton("sauvegarder");
		btnSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(JOptionPane.showConfirmDialog(frame, "voulez vous appliquer les changements avant de sauvegarder le dataset?")) {
					case 0: // yes
						appliquer_les_changements();
						break;
					case 1: // no
						break;
					case 2: // cancel
						return;
				}
				dest_file = textField_dest_file.getText();
				if (dest_file == null || dest_file.equals("")) {
					dest_file = FileManager.SaveFileWindow();
					textField_dest_file.setText(dest_file);
				}
				try {
					FileManager.save_dataset(dataset, dest_file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					afficherMessage("Erreure lors de la sauvegarde!\n" + e.toString());
					return;
				}
				afficherMessage("dataset sauvegardé sous \""+ dest_file+"\"");
			}

		});
		
		JLabel lblNewLabel_1 = new JLabel("Destination");
		
		textField_dest_file = new JTextField();
		textField_dest_file.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(text_dataset_src, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(btnNewButton)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSupprimerligne)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btn_appliquer_changements)
									.addGap(79)))
							.addComponent(chckbxUrl)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnCharger))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_dest_file, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSauvegarder, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
						.addComponent(text_dataset_src, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCharger)
						.addComponent(chckbxUrl))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnSupprimerligne)
						.addComponent(btn_appliquer_changements))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSauvegarder)
						.addComponent(lblNewLabel_1)
						.addComponent(textField_dest_file, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		panel_dataset.setLayout(gl_panel_dataset);
		
		JPanel panel_desc_mesures = new JPanel();
		tabbedPane.addTab("Description et mesures", null, panel_desc_mesures, null);
		
		JLabel lblNewLabel_2 = new JLabel("Description du dataset");
		
		textArea_description = new JTextArea();
		
		JLabel lblNewLabel_2_1 = new JLabel("Mesures");
		JPanel panel_3 = new JPanel();
		table_mesures = add_table_to(panel_3);
		
		JLabel lblNewLabel_3 = new JLabel("Pourcentage à trouquer");
		label_pourcentage_moy_tronquee = new JLabel("10%");
		
		slider_moy_tronquee = new JSlider();
		slider_moy_tronquee.setValue(10);
		slider_moy_tronquee.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int seuil = slider_moy_tronquee.getValue();
				label_pourcentage_moy_tronquee.setText(seuil+"%");
				update_moyenne_tronquee();
			}
		});
		slider_moy_tronquee.setMinorTickSpacing(1);
		slider_moy_tronquee.setMaximum(49);
		
		JPanel panel_4 = new JPanel();
		textArea_description = add_textArea_to(panel_4);
		
		JPanel panel_5 = new JPanel();
		table_attributs = add_table_to(panel_5);
		
		GroupLayout gl_panel_desc_mesures = new GroupLayout(panel_desc_mesures);
		gl_panel_desc_mesures.setHorizontalGroup(
			gl_panel_desc_mesures.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_desc_mesures.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_desc_mesures.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
						.addGroup(gl_panel_desc_mesures.createSequentialGroup()
							.addGroup(gl_panel_desc_mesures.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
								.addComponent(slider_moy_tronquee, GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
								.addGroup(gl_panel_desc_mesures.createSequentialGroup()
									.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGap(18)
							.addComponent(label_pourcentage_moy_tronquee, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblNewLabel_2_1, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_desc_mesures.createSequentialGroup()
							.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel_desc_mesures.setVerticalGroup(
			gl_panel_desc_mesures.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_desc_mesures.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_desc_mesures.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_2_1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_desc_mesures.createParallelGroup(Alignment.LEADING)
						.addComponent(label_pourcentage_moy_tronquee, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(slider_moy_tronquee, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(21))
		);
		panel_desc_mesures.setLayout(gl_panel_desc_mesures);
		
		JPanel panel_plots = new JPanel();
		tabbedPane.addTab("Diagrammes", null, panel_plots, null);
		
		JLabel lblNewLabel_4 = new JLabel("Type du diagramme");
		
		JComboBox comboBox_type_diagramme = new JComboBox();
		comboBox_type_diagramme.setModel(new DefaultComboBoxModel(new String[] {"Histogramme", "Boite à moustache", "Q-Q Plot", "ScatterPlot"}));
		
		panel_diagrammes = new ChartPanel(null);
		panel_diagrammes.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JComboBox comboBox_col1 = new JComboBox();
		
		JComboBox comboBox_col2 = new JComboBox();
		comboBox_col1.setModel(new DefaultComboBoxModel(new String[] {"area", "perimeter", "compactness", "length of kernel","width of kernel","asymmetry coefficient","length of kernel groove","class"}));
		comboBox_col2.setModel(new DefaultComboBoxModel(new String[] {"area", "perimeter", "compactness", "length of kernel","width of kernel","asymmetry coefficient","length of kernel groove","class"}));
		comboBox_col2.disable();
		JLabel lblNewLabel_5 = new JLabel("Col 1 : ");
		
		JLabel lblNewLabel_5_1 = new JLabel("Col 2 : ");
		
		JButton btnNewButton_afficher = new JButton("Afficher");
		btnNewButton_afficher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Diagrammes diagrammes = new Diagrammes(dataset);
				switch(comboBox_type_diagramme.getSelectedIndex()) {
					case 0 : // Histogramme
						panel_diagrammes.setChart(diagrammes.histogram(comboBox_col1.getSelectedIndex()));
						break;
					case 1 : // Histogramme
						panel_diagrammes.setChart(null);
						break;
					case 2 : // Histogramme
						panel_diagrammes.setChart(null);
						break;
					case 3 : // scatterplot
						panel_diagrammes.setChart(diagrammes.diagramme_disperssion(comboBox_col1.getSelectedIndex(),comboBox_col2.getSelectedIndex()));
						break;
					default:
						panel_diagrammes.setChart(null);
						break;
				}
			}
		});
		comboBox_type_diagramme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(comboBox_type_diagramme.getSelectedIndex()) {
					case 0 : // Histogramme
						panel_diagrammes.setChart(null);
						comboBox_col2.disable();
						break;
					case 1 : // boxplot
						panel_diagrammes.setChart(null);
						comboBox_col2.disable();
						break;
					case 2 : // qqplot
						panel_diagrammes.setChart(null);
						comboBox_col2.enable();
						break;
					case 3 : // scatterplot
						panel_diagrammes.setChart(null);
						comboBox_col2.enable();
						break;
					default:
						
						break;
				}
			}
		});
		GroupLayout gl_panel_plots = new GroupLayout(panel_plots);
		gl_panel_plots.setHorizontalGroup(
			gl_panel_plots.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_plots.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_plots.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_diagrammes, GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
						.addGroup(gl_panel_plots.createSequentialGroup()
							.addComponent(lblNewLabel_4)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(comboBox_type_diagramme, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel_5)
							.addGap(10)
							.addComponent(comboBox_col1, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_5_1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBox_col2, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnNewButton_afficher, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel_plots.setVerticalGroup(
			gl_panel_plots.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_plots.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_plots.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(comboBox_type_diagramme, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_5)
						.addComponent(lblNewLabel_5_1)
						.addComponent(comboBox_col2, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox_col1, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_afficher, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_diagrammes, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_plots.setLayout(gl_panel_plots);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	private JTable add_table_to(JPanel panel) {
		panel.setLayout(new BorderLayout(0,0));
		JTable table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		panel.add(table, BorderLayout.CENTER);
		panel.add(new JScrollPane(table));
		return table;
	}
	
	private JTextArea add_textArea_to(JPanel panel) {
		panel.setLayout(new BorderLayout(0,0));
		JTextArea area = new JTextArea();
		area.setEditable(false);
		panel.add(area, BorderLayout.CENTER);
		panel.add(new JScrollPane(area));
		return area;
	}

	public void load_dataset_on_table() throws Exception {
		/** charger la dataset dans la table et afficher les mesures
		 * */
		// load table in Jtabel
		TableModel tableModel = new DefaultTableModel(dataset.col_names, dataset.n);
		for (int i = 0; i < dataset.n; i++) {
			for (int j = 0; j < dataset.m; j++) {
				tableModel.setValueAt(dataset.data[i][j], i, j);
			}
		}
		table_dataset.setModel(tableModel);
		
		// load mesures and description
		updateMesures();
	}
	
	private void updateMesures() throws Exception {
		String names [] = new String[dataset.m+1];
		for (int i = 1; i <= dataset.m; i++) {
			names[i] = dataset.col_names[i-1];
		}
		names[0] = "Mesures";
		final int NOMBRE_DE_MESURES = 18; // 17 est le nombre de mesures a afficher ! il faut le mettre a jour
		TableModel model_mesures = new DefaultTableModel(names, NOMBRE_DE_MESURES); 
		int i = 1;
		model_mesures.setValueAt("moyenne",i,0);   for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.moyenne(j)), i, j+1); i++;
		model_mesures.setValueAt("mediane",i,0);   for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.mediane(j)), i, j+1); i++;
		model_mesures.setValueAt("mode",i,0);      for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.mode(j), i, j+1); i++;
		//model_mesures.setValueAt("mode discrèt",i,0); for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.mode_discret(j)), i, j+1); i++;
		model_mesures.setValueAt("max",i,0);       for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.max(j), i, j+1); i++;
		model_mesures.setValueAt("min",i,0);       for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.min(j), i, j+1); i++;
		model_mesures.setValueAt("etendu",i,0);    for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.etendu(j)), i, j+1); i++;
		model_mesures.setValueAt("mi_etendu",i,0); for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.milieu_etendu(j)), i, j+1); i++;
		model_mesures.setValueAt("ecartType",i,0); for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.ecartType(j)), i, j+1); i++;
		model_mesures.setValueAt("variance",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.variance(j)), i, j+1); i++;
		model_mesures.setValueAt("Q1",i,0);        for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.quartile(j,1), i, j+1); i++;
		model_mesures.setValueAt("Q2",i,0);        for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.quartile(j,2), i, j+1); i++;
		model_mesures.setValueAt("Q3",i,0);        for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.quartile(j,3), i, j+1); i++;
		model_mesures.setValueAt("IQR",i,0);       for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.IQR(j)), i, j+1); i++;
		model_mesures.setValueAt("outliers",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.outliers(j), i, j+1); i++;
		model_mesures.setValueAt("skewness",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.skewness(j)), i, j+1); i++;

		// moyenne tronquée
		position_moy_tronquee = i = 0; // save this position for later
		double q = slider_moy_tronquee.getValue()/100.0;
		model_mesures.setValueAt("moy tronquée",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.moyenne_tronqee(j, q)), i, j+1); i++;
		table_mesures.setModel(model_mesures);
	}

	public void update_moyenne_tronquee() {
		if (position_moy_tronquee == null) {
			return;
		}
		int i = position_moy_tronquee;
		double q = slider_moy_tronquee.getValue()/100.0;
		TableModel model_mesures = table_mesures.getModel();
		model_mesures.setValueAt("moy tronquée",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.moyenne_tronqee(j, q)), i, j+1); i++;
		table_mesures.setModel(model_mesures);
	}
	
	public void update_description_text() {
		String description = "dataset : \"" + getFilename() + "\" :\n";
		description += "- nombre d'instances = " + dataset.n + "\n";
		description += "- nombre d'attributs = " + dataset.m + "\n";
		description += "- nombre de classes  = " + dataset.getClassCount() + "\n";
		description += "- classes :\n";
		
		Frequences f = dataset.frequences_de(dataset.m-1); // frequences du dernier attribut
		for (int i = 0; i < Dataset.classes.length; i++) {
			double k = i+1;
			description += "\t"+(i+1)+". " + Dataset.classes[i] + "("+k+") : "+ f.frequence_de(k) +" instances ("+ dataset.arrondi(100*f.pourcentage(k))+"%)\n";
		}
		remplir_table_attributs();
		textArea_description.setText(description);
	}
	
	private void remplir_table_attributs() {
		int i;
		DefaultTableModel model = new DefaultTableModel(" ,attribut,type,nombre de valeurs manquantes, valeurs possibles".split(","), dataset.m);
		for (i = 0; i < dataset.m; i++) { // all except last attribut
			int j = 0;
			model.setValueAt(i+1, i, j++);
			model.setValueAt(dataset.col_names[i], i, j++);
			if (i<dataset.m-1) 
			model.setValueAt("continu | numérique | quantitatif", i, j++);
			else 
			model.setValueAt("discret | numérique | quantitatif", i, j++);
			model.setValueAt(dataset.nombre_de_cases_vides(i), i, j++);
			model.setValueAt("[ "+dataset.min(i)+" , "+dataset.max(i)+" ]", i, j++);
		}
		table_attributs.setModel(model);
	}

	private void appliquer_les_changements() {
		int i = 0,j = 0;
		table_dataset.clearSelection(); // must use this to avoid some errors
		try {
			// recreate dataset from table
			DefaultTableModel model = (DefaultTableModel) table_dataset.getModel();
			dataset.n = model.getRowCount();
			dataset.m = model.getColumnCount();
			dataset.data = new Double[dataset.n][dataset.m];
			dataset.types = new Type[dataset.m];
			for (i = 0; i < dataset.n; i++) {
				for (j = 0; j < dataset.m; j++) {
					Double x = null;
					Object tmp = table_dataset.getValueAt(i,j);
					String str_value = tmp == null ? "": tmp.toString();
					try {x = Double.parseDouble(str_value);} catch(Exception e) {};
					dataset.types[j] = Type.parse(str_value).combine(dataset.types[j]);
					dataset.data[i][j] = x;
				}
			}
			System.out.println("loaded dataset has "+ dataset.n +" rows");
			// update mesures and other stuff
			updateMesures();
			update_description_text();
		} catch (Exception e1) {e1.printStackTrace();
		afficherMessage("veillez corriger la valeure dans dans la position ("+i+", "+j+").\n(ou vérifier que vous avez déselectionné la case que vous êtes en train de remplir pour valider l'entrée)");
		table_dataset.changeSelection(i, j, true, false);
		Object old_value = table_dataset.getValueAt(i, j);
		String value = old_value == null ? "*" : old_value + "*";
		table_dataset.setValueAt(value, i, j);
		System.out.println(i + " "+ j);}
	}
	
	// extra methods
	private String getFilename() {
		try {
			return Paths.get(text_dataset_src.getText()).getFileName().toString();
		} catch (Exception e) {
			return text_dataset_src.getText();
		}
		
	}
	
	public void afficherMessage(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}
}
