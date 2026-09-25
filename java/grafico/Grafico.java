package grafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import frontEnd.Selecionador;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Grafico extends JFrame {
	private ArrayList<String> listaDasVariaveisDosGraficos = new ArrayList<String>();
	private int valor;

	public int getValor() {
		return valor;
	}

	private void setValor(int valor) {
		this.valor = valor;
	}

	public Grafico(ArrayList<String> listaDasVariaveisDosGraficos) {
		this.listaDasVariaveisDosGraficos = listaDasVariaveisDosGraficos;

		CategoryDataset dataset = createDataset();
		JFreeChart chart = createChart();
		ChartPanel chartPanel = new ChartPanel(chart);

		chartPanel.setPreferredSize(new Dimension(1024, 460));
		setContentPane(chartPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setBounds(500, 500, 850, 800);
		setLocationRelativeTo(null);
		setTitle("Resultados");
	}

	private CategoryDataset createDataset() {
		int tamanhoDasVariaveis = listaDasVariaveisDosGraficos.size() / 2;
		String[] series = new String[tamanhoDasVariaveis];
		String[] category = new String[tamanhoDasVariaveis];
		int[] valor = new int[tamanhoDasVariaveis];
		int var = 0;
		// linhas
		for (int i = 0; i < listaDasVariaveisDosGraficos.size(); i++) {
			if (i % 2 == 0) {
				series[var] = listaDasVariaveisDosGraficos.get(i);
				category[var] = listaDasVariaveisDosGraficos.get(i);
			}
			if (i % 2 == 1) {
				valor[var] = Integer.parseInt(listaDasVariaveisDosGraficos.get(i));
				var++;
			}

			
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		setValor(0);

		// adicionando valores
		for (int i = 0; i < series.length; i++) {
			dataset.addValue(valor[i], category[i], series[i]);
		}
		return dataset;

	}

	public JFreeChart createChart() {
		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart("Resultados", // chart title
				"", // domain axis label
				"Value", // range axis label
				createDataset(), // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
		);

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.gray);

		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.getAutoRangeMinimumSize();
		rangeAxis.setVisible(true);
		plot.setOutlineVisible(false);

		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(true);
		renderer.setGradientPaintTransformer(null);
		renderer.setShadowVisible(true);
		//renderer.setItemMargin(-10);

		Random rand = new Random();

		Paint[] colors = {

				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),
				new Color((float) (rand.nextFloat() / 2f + 0.3), (float) (rand.nextFloat() / 2f + 0.3),
						(float) (rand.nextFloat() / 2f + 0.3)),
				new Color((float) (rand.nextFloat() / 2f + 0.2), (float) (rand.nextFloat() / 2f + 0.2),
						(float) (rand.nextFloat() / 2f + 0.2)),

		};

		for (int i = 0; i < 4; i++) {
			renderer.setSeriesPaint(i, colors[i % colors.length]);
		}

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/6)

		);

		return chart;

	}

}