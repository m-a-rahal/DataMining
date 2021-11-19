package diagrammes;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import data.Dataset;

public class Diagrammes {
	Dataset dataset;
	XYSeriesCollection data = new XYSeriesCollection();
	
	
	
	public Diagrammes(Dataset dataset) {
		this.dataset = dataset;
	}



	public JFreeChart diagramme_disperssion(int col1, int col2) {
		XYSeries couple = new XYSeries("");
		for (int i = 0; i < dataset.n; i++) {
			couple.add(dataset.data[i][col1], dataset.data[i][col2]);
		}
		data.addSeries(couple);
		JFreeChart scatterplot = ChartFactory.createScatterPlot("diagramme_disperssion", dataset.col_names[col1], dataset.col_names[col2], data);
		//ChartPanel panel = new ChartPanel(scatterplot);
		//JFrame frame = new JFrame();
		//frame.add(panel);
		//frame.setVisible(true);
		return scatterplot;
		
	}
		public JFreeChart histogram(int col) {
			double[] d = new double[dataset.n];
			HistogramDataset histdata = new HistogramDataset();
			histdata.setType(HistogramType.FREQUENCY);
			for (int i = 0; i < dataset.n; i++) {
				d[i]=dataset.data[i][col];
			}
			histdata.addSeries("", d, dataset.n);
			JFreeChart hist = ChartFactory.createHistogram("histogramme", dataset.col_names[col], "frequence", histdata);
			//ChartPanel panel = new ChartPanel(hist);
			//panel.setChart(hist);
			//JFrame frame = new JFrame();
			//frame.add(panel);
			//frame.setVisible(true);
			return hist;
	}
}
