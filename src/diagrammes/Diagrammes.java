package diagrammes;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
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
	public static boolean FORCE_SHOW_OUTLIERS = true;
	
	
	
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
		DefaultBoxAndWhiskerCategoryDataset box_dataset = new DefaultBoxAndWhiskerCategoryDataset();

		BoxAndWhiskerItem data_item = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(dataset.getValues(col));
		box_dataset.add(data_item, dataset.col_names[col], dataset.col_names[col]);
		
		return make_boxplot(box_dataset, dataset.min(col), dataset.max(col));
	}
	
	public JFreeChart boxplot() {
		DefaultBoxAndWhiskerCategoryDataset box_dataset = new DefaultBoxAndWhiskerCategoryDataset();
		double min = dataset.min(0), max = dataset.max(0);
		for (int i = 1; i < dataset.m; i++) {
			BoxAndWhiskerItem data_item = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(dataset.getValues(i));
			box_dataset.add(data_item, dataset.col_names[i], "");
			double local_max = dataset.min(i); 
			double local_min = dataset.max(i);
			if (max < local_max) max = local_max;
			if (min > local_min) min = local_min;
			
		}
		return make_boxplot(box_dataset, min, max);
	}
	
	private JFreeChart make_boxplot(DefaultBoxAndWhiskerCategoryDataset box_dataset, double min, double max) {
		JFreeChart box_plot = ChartFactory.createBoxAndWhiskerChart("Title", "", "",box_dataset, true);
		CategoryPlot plot = (CategoryPlot) box_plot.getPlot();
		// régler le problème d'outliers qui ne s'affichent pas------
		
		if (FORCE_SHOW_OUTLIERS ) {
			ValueAxis range = plot.getRangeAxis();
			double etendu = max - min;
			range.setRange(min - etendu / 10, max + etendu / 10);
		}
		// -----------------------------------------------------------
		ExtendedBoxAndWhiskerRenderer renderer = new ExtendedBoxAndWhiskerRenderer();
		renderer.setFillBox(true);
		renderer.setUseOutlinePaintForWhiskers(true);   
		renderer.setMeanVisible(false);
		plot.setRenderer(renderer);
		return box_plot;
	}
	
	
	/*private JFreeChart make_boxplot2(DefaultBoxAndWhiskerCategoryDataset box_dataset) {
		JFreeChart box_plot = ChartFactory.createBoxAndWhiskerChart("Title", "attribute name", "attribute values",box_dataset, true);
		CategoryPlot plot = (CategoryPlot) box_plot.getPlot();
		
		ExtendedBoxAndWhiskerRenderer renderer = new ExtendedBoxAndWhiskerRenderer();
		renderer.setFillBox(true);
		renderer.setUseOutlinePaintForWhiskers(true);   
		renderer.setMeanVisible(false);
		plot.setRenderer(new BoxAndWhiskerRenderer() {
			private static final long serialVersionUID = 1L;

			public void drawVerticalItem(Graphics2D g2) {
		        // existing code that calls the methods below
		    }

		    private void drawEllipse(Point2D point, double oRadius, Graphics2D g2) {
		        Paint temp = g2.getPaint();
		        g2.setColor(Color.black);
		        Ellipse2D dot = new Ellipse2D.Double(point.getX() + oRadius / 2,
		                point.getY(), oRadius, oRadius);
		        g2.draw(dot);
		        g2.setPaint(temp);
		    }

		    private void drawHighFarOut(double aRadius, Graphics2D g2, double xx,
		            double m) {
		        Paint temp = g2.getPaint();
		        g2.setColor(Color.black);
		        double side = aRadius * 2;
		        g2.draw(new Line2D.Double(xx - side, m + side, xx + side, m + side));
		        g2.draw(new Line2D.Double(xx - side, m + side, xx, m));
		        g2.draw(new Line2D.Double(xx + side, m + side, xx, m));
		        g2.setPaint(temp);
		    }
		});
		return box_plot;
	}*/

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
