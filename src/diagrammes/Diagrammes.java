package diagrammes;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import application.Application;
import data.Dataset;

public class Diagrammes {
	Dataset dataset;
	XYSeriesCollection data = new XYSeriesCollection();
	private Application app;
	
	
	
	public Diagrammes(Dataset dataset, Application application) {
		this.dataset = dataset;
		this.app = application;
	}



	public JFreeChart diagramme_disperssion(int col1, int col2) {
		XYSeries couple = new XYSeries("");
		for (int i = 0; i < dataset.n; i++) {
			couple.add(dataset.data[i][col1], dataset.data[i][col2]);
		}
		data.addSeries(couple);

		String x = app.get_attribut1(),
			   y = app.get_attribut2();
		JFreeChart scatterplot = ChartFactory.createScatterPlot("Scatter Plot", x, y, data);
		//ChartPanel panel = new ChartPanel(scatterplot);
		//JFrame frame = new JFrame();
		//frame.add(panel);
		//frame.setVisible(true);
		return scatterplot;
		
	}
	
	public JFreeChart qqplot(int col1, int col2) {
		XYSeries couple = new XYSeries("");
		ArrayList<Double> X = app.dataset.getSortedValues(col1);
		ArrayList<Double> Y = app.dataset.getSortedValues(col2);
		for (int i = 0; i < dataset.n; i++) {
			couple.add(X.get(i), Y.get(i));
		}
		data.addSeries(couple);

		String x = app.get_attribut1(),
			   y = app.get_attribut2();
		JFreeChart plot = ChartFactory.createScatterPlot("Q-Q Plot", x, y, data);
		//ChartPanel panel = new ChartPanel(scatterplot);
		//JFrame frame = new JFrame();
		//frame.add(panel);
		//frame.setVisible(true);
		return plot;
		
	}
	
	public JFreeChart boxplot(int col) {
		DefaultBoxAndWhiskerCategoryDataset boxPlot_dataset = new DefaultBoxAndWhiskerCategoryDataset();
		fillBoxPlot_dataset(boxPlot_dataset, col);
		
		final CategoryAxis xAxis = new CategoryAxis("Type");
        final NumberAxis yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
        renderer.setDefaultToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        renderer.setDefaultPaint(new Color(153,0,0));
        final CategoryPlot plot = new CategoryPlot(boxPlot_dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(new Color(224,224,224));

        final JFreeChart chart = new JFreeChart(
            "Box Plot",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        chart.setBackgroundPaint(new Color(255,255,255));
        return chart;
	}
	
	
	private void fillBoxPlot_dataset(DefaultBoxAndWhiskerCategoryDataset boxPlot_dataset, int col) {
		ArrayList<Double> values = dataset.getSortedValues(col);
		boxPlot_dataset.add(values, app.get_attribut1(), values.get(0));
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
