package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BoxLayout;
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
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import Classification.Classifieur.Classification;
import Classification.ClassifieurBaysien;
import Classification.Evaluation;
import Classification.Evaluation.Evaluations;
import data.Dataset;
import data.Frequences;
import data.Type;
import diagrammes.Diagrammes;
import input_output_classes.FileManager;
import input_output_classes.URLManager;
import motifs_frequents_et_regles.Apriori;
import motifs_frequents_et_regles.Eclat;
import motifs_frequents_et_regles.Itemsets;
import motifs_frequents_et_regles.Regle;
import motifs_frequents_et_regles.Regle.Regles;

public class Application {
	private Application application;
	public Dataset dataset;
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
	private JComboBox comboBox_attribut1;
	private JComboBox comboBox_attribut2;
	private JComboBox comboBox_type_diagramme;
	private JCheckBox chckbxOutliers_disc;
	private JTextField coeffCorel;
	private JLabel label_info;
	private JLabel label_coef_corr;
	private JTextField nb_intervals;
	private JLabel nbInterLabel;
	private JCheckBox chckbxTrierLesDonns;
	private String[] col_names_with_number;
	private JButton btn_ajouter_ligne;
	private JCheckBox chckbox_apply_before_sort;
	private JTextField textField_Q;
	private JTextField textField_min;
	private JTextField textField_max;
	private JComboBox comboBox_type_discretisation;
	private JComboBox comboBox_type_normalisation;
	private JTextField textField_fichier_datasetdiscret;
	private JTextField textField_min_sup;
	private JTextArea area_res_motifs_freq;
	private JLabel label_tmps_exec_motifs_freq;
	Itemsets motifs_frequents;
	private JTextField textField_confidence;
	private JTextArea area_regles;
	private JLabel label_tmps_exec_regles;
	private JTextField textField_taille_echantillion_testbays;
	private JTextArea area_res_class_bays;
	private JTextArea area_instances_bays;
	private JTable table_mesures_bays;
	private JTable table_mesures_bays__moyennes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Application window = new Application();
					// Maximizer la fenêtre et l'afficher
					window.frame.setExtendedState(window.frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
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
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
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
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_dataset.setVerticalGroup(
			gl_panel_dataset.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_dataset.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);

		table_dataset = add_table_to(panel_2);
		btn_ajouter_ligne = new JButton("ajouter ligne");
		btn_ajouter_ligne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table_dataset.getModel();
				model.addRow(new Double[model.getColumnCount()]);
				int last_row = model.getRowCount()-1;
				// add count for this row
				model.setValueAt(last_row+1, last_row, col_names_with_number.length-1);
				table_dataset.changeSelection(last_row, 0, true, false);
			}
		});

		JButton btn_appliquer_changements = new JButton("appliquer les modifications");
		btn_appliquer_changements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxTrierLesDonns.isSelected()) {
					switch(JOptionPane.showConfirmDialog(frame, "voulez vous vraiment écraser les donnés avec les valeurs triés?")) {
						case 0: // yes
							appliquer_les_changements();
					}
					return;
				}
				appliquer_les_changements();
			}
		});

		JButton btnSupprimerligne = new JButton("supprimer lignes");
		btnSupprimerligne.setToolTipText("supprimer les lignes sélectionnées");
		btnSupprimerligne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table_dataset.getModel();
				int[] rows = table_dataset.getSelectedRows();
				for (int i = 0; i < rows.length; i++) {
					model.removeRow(rows[rows.length-i-1]);
				}
				// update row numbers
				update_row_numbers_on_table(model);
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
			@Override
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
			@Override
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
					TableModel model = table_dataset.getModel();
					int n = model.getRowCount();
					int m = model.getColumnCount()-1;
					FileManager.save_dataset(table_dataset.getModel(),n,m,  dest_file);
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

		chckbxTrierLesDonns = new JCheckBox("trier les donnés");
		chckbxTrierLesDonns.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (chckbxTrierLesDonns.isSelected()) { // apply changes before sorting?
						if (chckbox_apply_before_sort.isSelected()) {
							appliquer_les_changements();
						}
						btnSupprimerligne.setEnabled(false);
						btn_ajouter_ligne.setEnabled(false);
						table_dataset.setEnabled(false);
					} else {
						btnSupprimerligne.setEnabled(true);
						btn_ajouter_ligne.setEnabled(true);
						table_dataset.setEnabled(true);
					}
					load_dataset_on_table();
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		});

		chckbox_apply_before_sort = new JCheckBox("appliquer les changements avant de trier");
		chckbox_apply_before_sort.setSelected(true);

		JButton btnNormaliser = new JButton("Normaliser");
		btnNormaliser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double nouv_max, nouv_min;
				try {
					nouv_min = Double.parseDouble(textField_min.getText());
					nouv_max = Double.parseDouble(textField_max.getText());
				} catch (Exception e2) {
					afficherMessage("La valeur du min ou du max est éronnée");
					return;
				}
				if (nouv_max <= nouv_min) {
					afficherMessage("La valeur du min doit être inférieure strictement à celle du max");
					return;
				}
				for (int i = 0; i < dataset.m-1; i++) {
					if( comboBox_type_normalisation.getSelectedIndex() == 0 ) {
						dataset.normaliser_min_max(i,nouv_max,nouv_min);
					} else {
						//#### norm z score
						System.out.println("z-score non impémenté");
					}
				}
				try {
					load_dataset_on_table();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnNewButton = new JButton("discretiser");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int Q;
				try {
					Q = Integer.parseInt(textField_Q.getText());
				} catch (Exception e2) {
					afficherMessage("La valeur de Q (entier) est éronnée");
					return;
				}
				if (Q <= 0 ) {
					afficherMessage("La valeur de Q doir être positive non nulle");
					return;
				}
				for (int i = 0; i < dataset.m-1; i++) {
					dataset.discretiser_equal_width(i,Q);
				}
				try {
					load_dataset_on_table_disccrete();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		comboBox_type_discretisation = new JComboBox();
		comboBox_type_discretisation.setModel(new DefaultComboBoxModel(new String[] {"Discrétisation en classes d'amplitudes égales", "Discrétisation d'effectifs égaux"}));

		comboBox_type_normalisation = new JComboBox();
		comboBox_type_normalisation.setModel(new DefaultComboBoxModel(new String[] {"normalisation avec Min-Max", "normalisation avec Z-score "}));

		textField_Q = new JTextField();
		textField_Q.setHorizontalAlignment(SwingConstants.CENTER);
		textField_Q.setText("4");
		textField_Q.setToolTipText("le chemin (ou l'url) du dataset ...");
		textField_Q.setColumns(10);

		JLabel lblQ = new JLabel("Q");
		lblQ.setHorizontalAlignment(SwingConstants.CENTER);

		textField_min = new JTextField();
		textField_min.setText("0");
		textField_min.setHorizontalAlignment(SwingConstants.CENTER);
		textField_min.setToolTipText("le chemin (ou l'url) du dataset ...");
		textField_min.setColumns(10);

		textField_max = new JTextField();
		textField_max.setHorizontalAlignment(SwingConstants.CENTER);
		textField_max.setText("1");
		textField_max.setToolTipText("le chemin (ou l'url) du dataset ...");
		textField_max.setColumns(10);

		JLabel lblMax = new JLabel("min");
		lblMax.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblMax_1 = new JLabel("max");
		lblMax_1.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_dest_file, GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSauvegarder, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(btn_ajouter_ligne, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSupprimerligne, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btn_appliquer_changements, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbox_apply_before_sort)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxTrierLesDonns))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(text_dataset_src, GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
									.addGap(22))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(btnNewButton)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBox_type_discretisation, 0, 165, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblQ, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField_Q, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnNormaliser)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBox_type_normalisation, 0, 161, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblMax, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(textField_min, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblMax_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField_max, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(chckbxUrl)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnCharger)))))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
						.addComponent(text_dataset_src, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCharger)
						.addComponent(chckbxUrl))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNewButton)
							.addComponent(comboBox_type_discretisation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(textField_min, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBox_type_normalisation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblQ, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addComponent(textField_Q, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnNormaliser)
							.addComponent(lblMax, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(textField_max, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblMax_1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(btn_ajouter_ligne)
							.addComponent(btnSupprimerligne)
							.addComponent(btn_appliquer_changements))
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(chckbxTrierLesDonns)
							.addComponent(chckbox_apply_before_sort)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSauvegarder)
						.addComponent(lblNewLabel_1)
						.addComponent(textField_dest_file, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
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
			@Override
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

		comboBox_type_diagramme = new JComboBox();
		comboBox_type_diagramme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update_diagramme();
			}
		});
		comboBox_type_diagramme.setModel(new DefaultComboBoxModel(new String[] {"","Histogramme", "Boite à moustache", "Q-Q Plot", "ScatterPlot", "tous les Boites à moustache"}));

		panel_diagrammes = new ChartPanel(null);
		panel_diagrammes.setBorder(new LineBorder(new Color(0, 0, 0)));

		JLabel lblNewLabel_5 = new JLabel("attribut 1");

		comboBox_attribut1 = new JComboBox();
		comboBox_attribut1.setModel(new DefaultComboBoxModel(new String[] {"area", "perimeter", "compactness", "length of kernel", "width of kernel", "asymmetry coefficient", "length of kernel groove", "class"}));
		comboBox_attribut1.setSelectedIndex(0);
		comboBox_attribut1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update_diagramme();
			}
		});


		JLabel lblNewLabel_5_1 = new JLabel("attribut 2");

		comboBox_attribut2 = new JComboBox();
		comboBox_attribut2.setModel(new DefaultComboBoxModel(new String[] {"area", "perimeter", "compactness", "length of kernel", "width of kernel", "asymmetry coefficient", "length of kernel groove", "class"}));
		comboBox_attribut2.setSelectedIndex(1);
		comboBox_attribut2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update_diagramme();
			}
		});

		chckbxOutliers_disc = new JCheckBox("Outliers");
		chckbxOutliers_disc.setToolTipText("");
		chckbxOutliers_disc.setEnabled(false);
		chckbxOutliers_disc.setSelected(true);
		chckbxOutliers_disc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update_diagramme();
			}
		});

		coeffCorel = new JTextField();
		coeffCorel.setEditable(false);
		coeffCorel.setColumns(10);

		label_coef_corr = new JLabel("Coeffitient de correlation");

		label_info = new JLabel(" ");
		label_info.setForeground(Color.RED);

		nb_intervals = new JTextField();
		nb_intervals.setText("20");
		nb_intervals.setColumns(10);
		nb_intervals.setEnabled(false);
		nb_intervals.setVisible(false);
		nb_intervals.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update_diagramme();
			}
		});

		nbInterLabel = new JLabel("nombre d'intervales");
		nbInterLabel.setVisible(false);


		GroupLayout gl_panel_plots = new GroupLayout(panel_plots);
		gl_panel_plots.setHorizontalGroup(
			gl_panel_plots.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_plots.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_plots.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_plots.createSequentialGroup()
							.addGroup(gl_panel_plots.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_plots.createSequentialGroup()
									.addComponent(lblNewLabel_4)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(comboBox_type_diagramme, 0, 281, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_plots.createSequentialGroup()
									.addComponent(label_coef_corr)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(coeffCorel, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(label_info, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_plots.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_plots.createSequentialGroup()
									.addComponent(comboBox_attribut1, 0, 118, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblNewLabel_5_1, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBox_attribut2, 0, 118, Short.MAX_VALUE)
									.addGap(34))
								.addGroup(gl_panel_plots.createSequentialGroup()
									.addComponent(nbInterLabel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(nb_intervals, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(chckbxOutliers_disc))))
						.addComponent(panel_diagrammes, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_plots.setVerticalGroup(
			gl_panel_plots.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_plots.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_plots.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_panel_plots.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel_4)
							.addComponent(comboBox_type_diagramme, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_plots.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel_5)
							.addComponent(comboBox_attribut1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblNewLabel_5_1)
							.addComponent(comboBox_attribut2, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_plots.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxOutliers_disc)
						.addComponent(label_coef_corr)
						.addComponent(label_info)
						.addComponent(coeffCorel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nb_intervals, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nbInterLabel))
					.addGap(6)
					.addComponent(panel_diagrammes, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_plots.setLayout(gl_panel_plots);

		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("Motifs fréquents et règles", null, panel_6, null);

		JPanel panel_7 = new JPanel();

		JPanel panel_8 = new JPanel();

		JLabel lblNewLabel_6_1 = new JLabel("Règles d'association / de corrélation");
		lblNewLabel_6_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_11 = new JLabel("confidence min");

		textField_confidence = new JTextField();
		textField_confidence.setText("90");
		textField_confidence.setHorizontalAlignment(SwingConstants.CENTER);
		textField_confidence.setColumns(10);

		JButton btnNewButton_1 = new JButton("règles de corrélation");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double min_conf;
				try {
					min_conf = Double.parseDouble(textField_confidence.getText());
				} catch (Exception e2) {
					afficherMessage("La valeur de min confidence est éronnée");
					return;
				}
				min_conf /= 100.0;
				if (min_conf < 0 || min_conf > 100) {
					afficherMessage("La valeur du confidence min doit être entre 0 et 100");
					return;
				}
				if (motifs_frequents == null) {
					afficherMessage("Veuillez d'abbord extraire les motifs fréquents");
					return;
				}
				Regles regles_pos, regles_neg;
				try {
					double start = System.currentTimeMillis();
					regles_pos = Regle.regles_correlation(motifs_frequents, min_conf, +1);
					regles_neg = Regle.regles_correlation(motifs_frequents, min_conf, -1);
					label_tmps_exec_regles.setText("temps d'execution = "+(System.currentTimeMillis() - start)+" ms");
					area_regles.setText("règles de corrélation positives:\n"+
										 regles_pos.toString()+
										"\nrègles de corrélation négatives:\n"+
										 regles_neg.toString());

				} catch (Exception e2) {
					afficherMessage("L'extraction de règles de corrélation a échoué !" + e2);
					e2.printStackTrace();
					return;
				}
			}
		});

		JLabel lblNewLabel_12 = new JLabel("%");

		JButton btnNewButton_2 = new JButton("règles d'association");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double min_conf;
				try {
					min_conf = Double.parseDouble(textField_confidence.getText());
				} catch (Exception e2) {
					afficherMessage("La valeur de min confidence est éronnée");
					return;
				}
				min_conf /= 100.0;
				if (min_conf < 0 || min_conf > 100) {
					afficherMessage("La valeur du confidence min doit être entre 0 et 100");
					return;
				}
				if (motifs_frequents == null) {
					afficherMessage("Veuillez d'abbord extraire les motifs fréquents");
					return;
				}
				Regles regles;
				try {
					double start = System.currentTimeMillis();
					regles = Regle.regles_association(motifs_frequents, min_conf);
					label_tmps_exec_regles.setText("temps d'execution = "+(System.currentTimeMillis() - start)+" ms");
					area_regles.setText(regles.toString());

				} catch (Exception e2) {
					afficherMessage("L'extraction de règles d'accosiation a échoué !" + e2);
					e2.printStackTrace();
					return;
				}

			}
		});

		JScrollPane scrollPane_1 = new JScrollPane((Component) null);

		label_tmps_exec_regles = new JLabel("");
		label_tmps_exec_regles.setHorizontalAlignment(SwingConstants.CENTER);
		label_tmps_exec_regles.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel_6_1, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_11)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_confidence, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_12)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(10))
				.addGroup(gl_panel_8.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_tmps_exec_regles, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panel_8.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_8.setVerticalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addComponent(lblNewLabel_6_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_8.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_11)
						.addComponent(textField_confidence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1)
						.addComponent(lblNewLabel_12)
						.addComponent(btnNewButton_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label_tmps_exec_regles, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		area_regles = new JTextArea();
		scrollPane_1.setViewportView(area_regles);
		panel_8.setLayout(gl_panel_8);

		JLabel lblNewLabel_6 = new JLabel("Motifs fréquents");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblNewLabel_7 = new JLabel("dataset des instances");

		textField_fichier_datasetdiscret = new JTextField();
		textField_fichier_datasetdiscret.setText("resources/dataset_discret.txt");
		textField_fichier_datasetdiscret.setColumns(10);

		JButton btn_choisir_ficher_instaces_discret = new JButton("choisir");
		btn_choisir_ficher_instaces_discret.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		JLabel lblNewLabel_8 = new JLabel("algorithme");

		JComboBox comboBox_algorithme = new JComboBox();
		comboBox_algorithme.setModel(new DefaultComboBoxModel(new String[] {"Aprioi", "Eclat"}));

		JLabel lblNewLabel_9 = new JLabel("support minimale");

		textField_min_sup = new JTextField();
		textField_min_sup.setHorizontalAlignment(SwingConstants.CENTER);
		textField_min_sup.setText("20");
		textField_min_sup.setColumns(10);

		JLabel lblNewLabel_10 = new JLabel("%");

		JButton btn_lancer_motif_freq = new JButton("lancer");
		btn_lancer_motif_freq.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double min_sup;
				try {
					min_sup = Double.parseDouble(textField_min_sup.getText());
				} catch (Exception e2) {
					afficherMessage("La valeur de min support est éronnée");
					return;
				}
				min_sup /= 100.0;
				if (min_sup < 0 || min_sup > 100) {
					afficherMessage("La valeur du support min doit être entre 0 et 100");
					return;
				}

				try {
					double start = System.currentTimeMillis();
					if (comboBox_algorithme.getSelectedIndex() == 0) { // apriori
						motifs_frequents = new Apriori(min_sup).run(textField_fichier_datasetdiscret.getText());
					} else {
						motifs_frequents = new Eclat(min_sup).run(textField_fichier_datasetdiscret.getText());
					}
					area_res_motifs_freq.setText(motifs_frequents.toString());
					label_tmps_exec_motifs_freq.setText("temps d'execution = "+(System.currentTimeMillis() - start)+" ms");
				} catch (Exception e2) {
					afficherMessage("L'extraction des motifs fréquents à échoué !" + e2);
					return;
				}
			}
		});
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane((Component) null);

		label_tmps_exec_motifs_freq = new JLabel("");
		label_tmps_exec_motifs_freq.setHorizontalAlignment(SwingConstants.CENTER);
		label_tmps_exec_motifs_freq.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel_6, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_7)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_fichier_datasetdiscret, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btn_choisir_ficher_instaces_discret)
					.addContainerGap())
				.addGroup(gl_panel_7.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_8)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(comboBox_algorithme, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_9)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_min_sup, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_10)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btn_lancer_motif_freq, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_panel_7.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(label_tmps_exec_motifs_freq, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_7)
						.addComponent(textField_fichier_datasetdiscret, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_choisir_ficher_instaces_discret))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_8)
						.addComponent(comboBox_algorithme, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_9)
						.addComponent(textField_min_sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_10)
						.addComponent(btn_lancer_motif_freq))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label_tmps_exec_motifs_freq, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		area_res_motifs_freq = new JTextArea();
		area_res_motifs_freq.setEditable(false);
		scrollPane.setViewportView(area_res_motifs_freq);
		panel_7.setLayout(gl_panel_7);
		panel_6.add(panel_7);
		panel_6.add(panel_8);

		JPanel panel_9 = new JPanel();
		tabbedPane.addTab("Classification", null, panel_9, null);
		panel_9.setLayout(new BoxLayout(panel_9, BoxLayout.X_AXIS));

		JPanel panel_10 = new JPanel();
		panel_9.add(panel_10);

		JLabel lblNewLabel_6_2 = new JLabel("Classification Baysiénne");
		lblNewLabel_6_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_13 = new JLabel("Instances");

		JButton btn_evaluer_instances = new JButton("evaluer instances");
		btn_evaluer_instances.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		JButton btn_tester_dataset = new JButton("lancer le test");
		btn_tester_dataset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int nbr_instances_apprentissge;
				try {
					nbr_instances_apprentissge = Integer.parseInt(textField_taille_echantillion_testbays.getText());
				} catch (Exception e2) {
					afficherMessage("Le nombre d'instances d'appentissage est éronée (doit être un entier)");
					return;
				}
				if (nbr_instances_apprentissge <= 0) {
					afficherMessage("Le nombre d'instances d'appentissage doit être strictement positive");
					return;
				}
				try {
					TableModel model = table_dataset.getModel();
					ClassifieurBaysien classifieur = new ClassifieurBaysien(model, dataset.n, dataset.m, nbr_instances_apprentissge);
					Classification resultats = classifieur.tester(classifieur.instances_de_test(model));
					area_res_class_bays.setText(resultats.toString());
					Evaluations mesures = classifieur.evaluer(resultats);
					update_table_mesures_bays(mesures);
					update_table_mesures_bays_moyennes(mesures);
				} catch (Exception e1) {
					afficherMessage("La classification baysiénne a échoué!");
					e1.printStackTrace();
					return;
				}
			}

			private void update_table_mesures_bays_moyennes(Evaluations mesures) {

				DefaultTableModel model = new DefaultTableModel(new String[] {"Mesure", "Moyenne"}, 4+6); //TP+FN+... //acc+sens+spec+P+R+F
				TableModel data = table_mesures_bays.getModel();
				for (int i = 0; i < data.getRowCount(); i++) {
					double moy = 0;
					for (int j = 1; j < data.getColumnCount(); j++) {
						try {
							moy += Double.parseDouble(data.getValueAt(i,j).toString());
						} catch (Exception e) {
							continue;
						}
					}
					moy /= data.getColumnCount() - 1;
					model.setValueAt(data.getValueAt(i, 0), i, 0); // copier titre
					model.setValueAt(moy, i, 1); // mettre valeure de la moyenne
				}
				table_mesures_bays__moyennes.setModel(model);
			}

			private void update_table_mesures_bays(Evaluations mesures) {
				String [] titres = new String[mesures.size() + 1];
				int i = 0;
				titres[i] = "Mesure";
				for(Evaluation e : mesures) {
					titres[++i] = e.classe;
				}
				DefaultTableModel model = new DefaultTableModel(titres, 4+6); //TP+FN+... //acc+sens+spec+P+R+F
				i = 0;
				model.setValueAt("TP",i,0);      for (int j = 0; j < mesures.size(); j++) model.setValueAt(mesures.get(j).TP,i,j+1); i++;
				model.setValueAt("TN",i,0);      for (int j = 0; j < mesures.size(); j++) model.setValueAt(mesures.get(j).TN,i,j+1); i++;
				model.setValueAt("FP",i,0);      for (int j = 0; j < mesures.size(); j++) model.setValueAt(mesures.get(j).FP,i,j+1); i++;
				model.setValueAt("FN",i,0);      for (int j = 0; j < mesures.size(); j++) model.setValueAt(mesures.get(j).FN,i,j+1); i++;
                model.setValueAt("Accuracy",i,0);      for (int j = 0; j < mesures.size(); j++) model.setValueAt(Dataset.arrondi(mesures.get(j).accuracy()),i,j+1); i++;
                model.setValueAt("Sensitivity",i,0);   for (int j = 0; j < mesures.size(); j++) model.setValueAt(Dataset.arrondi(mesures.get(j).sensitivity()),i,j+1); i++;
                model.setValueAt("Specificity",i,0);   for (int j = 0; j < mesures.size(); j++) model.setValueAt(Dataset.arrondi(mesures.get(j).specificity()),i,j+1); i++;
                model.setValueAt("Precision",i,0);     for (int j = 0; j < mesures.size(); j++) model.setValueAt(Dataset.arrondi(mesures.get(j).precision()),i,j+1); i++;
                model.setValueAt("Rappel",i,0);        for (int j = 0; j < mesures.size(); j++) model.setValueAt(Dataset.arrondi(mesures.get(j).rappel()),i,j+1); i++;
                model.setValueAt("F-score",i,0);       for (int j = 0; j < mesures.size(); j++) model.setValueAt(Dataset.arrondi(mesures.get(j).f_mesure()),i,j+1); i++;
				table_mesures_bays.setModel(model);
			}
		});

		JScrollPane scrollBar = new JScrollPane();

		JScrollPane scrollBar_1 = new JScrollPane();

		JLabel lblNewLabel_14 = new JLabel("nombre instances d'apprentissage");

		textField_taille_echantillion_testbays = new JTextField();
		textField_taille_echantillion_testbays.setText("50");
		textField_taille_echantillion_testbays.setHorizontalAlignment(SwingConstants.CENTER);
		textField_taille_echantillion_testbays.setColumns(10);
		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel_6_2, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_13, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
					.addComponent(btn_evaluer_instances)
					.addContainerGap())
				.addGroup(gl_panel_10.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollBar, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panel_10.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_14)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textField_taille_echantillion_testbays, GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
					.addGap(13)
					.addComponent(btn_tester_dataset, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_panel_10.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollBar_1, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_10.setVerticalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addComponent(lblNewLabel_6_2, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_10.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_13)
						.addComponent(btn_evaluer_instances))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollBar, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_10.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_14)
						.addComponent(btn_tester_dataset)
						.addComponent(textField_taille_echantillion_testbays, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollBar_1, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
					.addContainerGap())
		);

		area_res_class_bays = new JTextArea();
		scrollBar_1.setViewportView(area_res_class_bays);

		area_instances_bays = new JTextArea();
		scrollBar.setViewportView(area_instances_bays);
		panel_10.setLayout(gl_panel_10);

		JPanel panel_11 = new JPanel();
		panel_9.add(panel_11);

		JLabel lblNewLabel_6_2_1 = new JLabel("");
		lblNewLabel_6_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_2_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_15 = new JLabel("Mesures");

		JScrollPane scrollPane_2 = new JScrollPane();

		JLabel lblNewLabel_16 = new JLabel("Mesures moyennes");

		JScrollPane scrollPane_3 = new JScrollPane();
		GroupLayout gl_panel_11 = new GroupLayout(panel_11);
		gl_panel_11.setHorizontalGroup(
			gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_11.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_11.createSequentialGroup()
							.addComponent(lblNewLabel_15)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_6_2_1, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
							.addGap(20))
						.addGroup(gl_panel_11.createSequentialGroup()
							.addComponent(lblNewLabel_16)
							.addContainerGap(355, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_11.createSequentialGroup()
							.addGroup(gl_panel_11.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
								.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
							.addContainerGap())))
		);
		gl_panel_11.setVerticalGroup(
			gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_11.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel_15)
						.addComponent(lblNewLabel_6_2_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_16)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
					.addContainerGap())
		);

		table_mesures_bays__moyennes = new JTable();
		scrollPane_3.setViewportView(table_mesures_bays__moyennes);

		table_mesures_bays = new JTable();
		scrollPane_2.setViewportView(table_mesures_bays);
		panel_11.setLayout(gl_panel_11);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}

	public void update_diagramme() {
		Diagrammes diagrammes = new Diagrammes(dataset, this);
		int attribut1 = comboBox_attribut1.getSelectedIndex();
		int attribut2 = comboBox_attribut2.getSelectedIndex();
		boolean attribut3 = chckbxOutliers_disc.isSelected();
		coeffCorel.setText(""+Dataset.arrondi(dataset.coeffitient_de_correlation(attribut1, attribut2)));
		comboBox_attribut1.setEnabled(true); // by default
		chckbxOutliers_disc.setVisible(false); // by default
		chckbxOutliers_disc.setText("Outliers"); // by default
		chckbxOutliers_disc.setEnabled(false); // by default
		coeffCorel.setVisible(false); // by default
		label_coef_corr.setVisible(false); // by dafault
		nb_intervals.setVisible(false);
		nbInterLabel.setVisible(false);
		switch(comboBox_type_diagramme.getSelectedIndex()) {
			case 1 : // Histogramme
				chckbxOutliers_disc.setVisible(true);
				chckbxOutliers_disc.setEnabled(true);
				chckbxOutliers_disc.setText("discrétisation");
				comboBox_attribut2.setEnabled(false); // disable attribut 2 for histogram
				nb_intervals.setEnabled(true);
				nb_intervals.setVisible(true);
				nbInterLabel.setVisible(true);
				JFreeChart chart = diagrammes.histogram(attribut1, chckbxOutliers_disc.isSelected(),Integer.parseInt(nb_intervals.getText()));
				panel_diagrammes.setChart(chart);
				break;

			case 2 : // boxplot
				chckbxOutliers_disc.setEnabled(true);
				chckbxOutliers_disc.setVisible(true);
				Diagrammes.FORCE_SHOW_OUTLIERS = attribut3; // make this chagable from interface
				comboBox_attribut2.setEnabled(false);
				panel_diagrammes.setChart(diagrammes.boxplot(attribut1));
				break;

			case 3 : // qqplot
				label_coef_corr.setVisible(true);
				coeffCorel.setVisible(true);
				comboBox_attribut2.setEnabled(true); // enable attribut 2
				panel_diagrammes.setChart(diagrammes.qqplot(attribut1,attribut2));
				break;

			case 4 : // scatterplot
				label_coef_corr.setVisible(true);
				coeffCorel.setVisible(true);
				comboBox_attribut2.setEnabled(true); // enable attribut 2
				panel_diagrammes.setChart(diagrammes.diagramme_disperssion(attribut1,attribut2));
				break;

			case 5 : // all box plots
				chckbxOutliers_disc.setEnabled(true);
				chckbxOutliers_disc.setVisible(true);
				Diagrammes.FORCE_SHOW_OUTLIERS = attribut3; // make this chagable from interface
				comboBox_attribut1.setEnabled(false); // disable all, this will show all box plpots
				comboBox_attribut2.setEnabled(false);
				panel_diagrammes.setChart(diagrammes.boxplot());
				break;

			default:
				comboBox_attribut2.setEnabled(true); // enable attribut 2 by default
				panel_diagrammes.setChart(null);
				break;
		}

		update_info_labels();
	}

	private void update_info_labels() {
		// corrélation (positive/négative) [trés] [forte/faible]
		double corr = Double.parseDouble(coeffCorel.getText());
		String[] descriptions = "pas de,moyenne,forte,trés forte".split(",");
		String signe = corr > 0 ? "positive" : "négative";
		String desc = "";
		int i;
		// si c'est le cas de deux attributs
		if (comboBox_attribut1.isEnabled() && comboBox_attribut2.isEnabled() && panel_diagrammes.getChart() != null) {
			double bornes[] = {0.5, 0.75, 0.9, 1.0};
			if (corr < 0) {
				corr = -corr;
			}
			i = 0;
			for (double borne : bornes) {
				if (corr <= borne) {
					desc = descriptions[i];
					break;
				}
				i++;
			}
			label_info.setText(desc+" corrélation " + (desc.equals("pas de") ? "" : signe));
		} else if (comboBox_attribut1.isEnabled() && !comboBox_attribut2.isEnabled()) {
			// si c'est le cas d'un seule attribut
			// données [légèrement/fortement] (symétriques/asymétriques à (droite/gauche))
			double bornes[] = {0.1, 0.25, 0.75, 0.9, 1.0};
			double skewness = dataset.skewness(comboBox_attribut1.getSelectedIndex());
			descriptions = "symétriques,légèrement asymétriques,asymétriques,fortement asymétriques,fortement asymétriques".split(",");
			boolean asymetrique = false;
			String direction = skewness < 0 ? "gauche" : "droite";
			if (skewness < 0) {skewness = -skewness;}
			i = 0;
			for (double borne : bornes) {
				if (skewness <= borne) {
					if (i > 0) asymetrique = true;
					desc = descriptions[i]; break;
				}
				i++;
			}
			label_info.setText("données " + desc + (asymetrique ? " à " + direction : ""));
			coeffCorel.setText("");
		} else {
			label_info.setText(" ");
			coeffCorel.setText("");
		}
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

	private void load_dataset_on_table_disccrete() throws Exception {
		load_dataset_on_table();
		TableModel model = table_dataset.getModel();
		for (int j = 0; j < dataset.m-1; j++) {
			for (int i = 0; i < dataset.n; i++) {
				int k = (int) Math.floor((double) model.getValueAt(i, j));
				model.setValueAt("I"+(j+1)+""+k, i, j);
			}
		}
	}

	public void load_dataset_on_table() throws Exception {
		/** charger la dataset dans la table et afficher les mesures
		 * */

		col_names_with_number = new String[dataset.col_names.length+1]; for (int i = 0; i < dataset.col_names.length; i++) {col_names_with_number[i]=dataset.col_names[i];} col_names_with_number[dataset.col_names.length] = "#";

		// load table in Jtabel
		TableModel tableModel = new DefaultTableModel(col_names_with_number, dataset.n);
		if (chckbxTrierLesDonns.isSelected()) {// afficher les données triées
			for (int j = 0; j < dataset.m; j++) {
				ArrayList<Double> values = dataset.getSortedValues(j);
				while(values.size() < dataset.n) {
					values.add(null);// ajouter null à la fin en cas de valeurs nuls
				}
				for (int i = 0; i < dataset.n; i++) {
					tableModel.setValueAt(values.get(i), i, j);
				}
			}
		} else { // afficher les données originales
			for (int i = 0; i < dataset.n; i++) {
				for (int j = 0; j < dataset.m; j++) {
					tableModel.setValueAt(dataset.data[i][j], i, j);
				}
			}
		}
		// fill number of rows
		update_row_numbers_on_table(tableModel);
		table_dataset.setModel(tableModel);
		// load mesures and description
		updateMesures();
	}

	private void update_row_numbers_on_table(TableModel tableModel) {
		// fill number of rows
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			tableModel.setValueAt(i+1, i, dataset.m);
		}
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
		model_mesures.setValueAt("moyenne",i,0);   for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.moyenne(j)), i, j+1); i++;
		model_mesures.setValueAt("mediane",i,0);   for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.mediane(j)), i, j+1); i++;
		model_mesures.setValueAt("mode",i,0);      for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.mode(j), i, j+1); i++;
		//model_mesures.setValueAt("mode discrèt",i,0); for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.arrondi(dataset.mode_discret(j)), i, j+1); i++;
		model_mesures.setValueAt("max",i,0);       for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.max(j), i, j+1); i++;
		model_mesures.setValueAt("min",i,0);       for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.min(j), i, j+1); i++;
		model_mesures.setValueAt("etendu",i,0);    for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.etendu(j)), i, j+1); i++;
		model_mesures.setValueAt("mi_etendu",i,0); for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.milieu_etendu(j)), i, j+1); i++;
		model_mesures.setValueAt("ecartType",i,0); for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.ecartType(j)), i, j+1); i++;
		model_mesures.setValueAt("variance",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.variance(j)), i, j+1); i++;
		model_mesures.setValueAt("Q1",i,0);        for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.quartile(j,1), i, j+1); i++;
		model_mesures.setValueAt("Q2",i,0);        for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.quartile(j,2), i, j+1); i++;
		model_mesures.setValueAt("Q3",i,0);        for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.quartile(j,3), i, j+1); i++;
		model_mesures.setValueAt("IQR",i,0);       for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.IQR(j)), i, j+1); i++;
		model_mesures.setValueAt("outliers",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(dataset.outliers(j), i, j+1); i++;
		model_mesures.setValueAt("skewness",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.skewness(j)), i, j+1); i++;

		// moyenne tronquée
		position_moy_tronquee = i = 0; // save this position for later
		double q = slider_moy_tronquee.getValue()/100.0;
		model_mesures.setValueAt("moy tronquée",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.moyenne_tronqee(j, q)), i, j+1); i++;
		table_mesures.setModel(model_mesures);
	}

	public void update_moyenne_tronquee() {
		if (position_moy_tronquee == null) {
			return;
		}
		int i = position_moy_tronquee;
		double q = slider_moy_tronquee.getValue()/100.0;
		TableModel model_mesures = table_mesures.getModel();
		model_mesures.setValueAt("moy tronquée",i,0);  for (int j = 0; j < dataset.m; j++) model_mesures.setValueAt(Dataset.arrondi(dataset.moyenne_tronqee(j, q)), i, j+1); i++;
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
			description += "\t"+(i+1)+". " + Dataset.classes[i] + "("+k+") : "+ f.frequence_de(k) +" instances ("+ Dataset.arrondi(100*f.pourcentage(k))+"%)\n";
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
			dataset.m = model.getColumnCount()-1; // -1 to remove last column (used for row count)
			dataset.data = new Double[dataset.n][dataset.m];
			dataset.types = new Type[dataset.m];
			for (i = 0; i < dataset.n; i++) {
				for (j = 0; j < dataset.m; j++) {
					Double x = null;
					Object tmp = table_dataset.getValueAt(i,j);
					String str_value = tmp == null ? "": tmp.toString();
					try {x = Double.parseDouble(str_value);} catch(Exception e) {}
					dataset.types[j] = Type.parse(str_value).combine(dataset.types[j]);
					dataset.data[i][j] = x;
				}
			}
			System.out.println("loaded dataset has "+ dataset.n +" rows");
			// update mesures and other stuff
			updateMesures();
			update_description_text();
			// reload table
			update_row_numbers_on_table(model);
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

	public String get_attribut1() {
		return comboBox_attribut1.getSelectedItem().toString();
	}

	public String get_attribut2() {
		return comboBox_attribut2.getSelectedItem().toString();
	}


	public void afficherMessage(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}
}
