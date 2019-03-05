package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Frame extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = 1L;
	private final static int DEFAULT_WIDTH = 900;
	private final static int DEFAULT_HEIGHT = 600;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	static JButton bStart, bFitSin, bShowBM, bExit, bLoadBitmap, bHelp;
	JTextField jtfAmplitude, jtfPhase, jtfPeriod, jtfOffset, jtfNoise, jtfSteps, jtfStart, 
	jtfStop, jtfNumerOfRow;
	JLabel jlAmplitude, jlPhase, jlPeriod, jlOffset, jlNoise, jlSteps, jlStart, jlStop, jlNumerOfRow;
	JPanel jpResultAndChart, jpButtonPanel, jpSettingSimulation, jpMain;
	JTextArea jtaResult;
	
	double start, stop, amplitude, period, phase, offset;
	int steps, noisePercent;
	
	XYSeries xyDataSymulation = new XYSeries("Seria 1");
	static XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
	static JFreeChart lineGraph;
	static ChartPanel chart;
	
	
	
	public Frame()
	{
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setTitle("Aproksynacja sinusa - Kacper Piotrowicz");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setResizable(false);
        setLayout(new BorderLayout());
          
        addSettingSimulationPanel();
        addButtonPanel();
        addChartAndResultPanel();
        
          
	}
	
	
	void addChartAndResultPanel()
	{
        
		jpResultAndChart = new JPanel();
        jpResultAndChart.setLayout(new BorderLayout());
        
        lineGraph = ChartFactory.createXYLineChart(null, null, "Amplituda", xySeriesCollection, 
        	PlotOrientation.VERTICAL, true, true, false); 
        chart = new ChartPanel(lineGraph);
        chart.setPreferredSize(new Dimension(650,300)); 
        jpResultAndChart.add(chart, BorderLayout.EAST);
        
        jtaResult = new JTextArea("");
        jtaResult.setPreferredSize(new Dimension(240, 300));      
        jtaResult.setBorder(new LineBorder(Color.BLACK, 1, true));
        jpResultAndChart.add(jtaResult, BorderLayout.WEST);
        
        this.add(jpResultAndChart, BorderLayout.NORTH);
		
		
	}
	
	void addSettingSimulationPanel()
	{
		jpSettingSimulation = new JPanel();
        jpSettingSimulation.setPreferredSize(new Dimension(440, 300));
        jpSettingSimulation.setLayout(null);
        jpSettingSimulation.setBorder(new TitledBorder("Symulacja"));
        
        
        jlAmplitude = new JLabel("Amplituda:");
        jlAmplitude.setBounds(25, 25, 70, 20);
        jpSettingSimulation.add(jlAmplitude);
        jtfAmplitude = new JTextField("2");
        jtfAmplitude.setBounds(95, 25, 70, 20);
        jpSettingSimulation.add(jtfAmplitude);
        
        jlPeriod = new JLabel("Okres (s):");
        jlPeriod.setBounds(25, 60, 70, 20);
        jpSettingSimulation.add(jlPeriod);
        jtfPeriod = new JTextField("60");
        jtfPeriod.setBounds(95, 60, 70, 20);
        jpSettingSimulation.add(jtfPeriod);
        
        jlPhase = new JLabel("Faza (rad):");
        jlPhase.setBounds(25, 95, 70, 20);
        jpSettingSimulation.add(jlPhase);
        jtfPhase = new JTextField("0.7");
        jtfPhase.setBounds(95, 95, 70, 20);
        jpSettingSimulation.add(jtfPhase);
        
        jlOffset = new JLabel("Offset:");
        jlOffset.setBounds(25, 130, 70, 20);
        jpSettingSimulation.add(jlOffset);
        jtfOffset = new JTextField("0");
        jtfOffset.setBounds(95, 130, 70, 20);
        jpSettingSimulation.add(jtfOffset);
        
        jlNoise = new JLabel("Szumy (%):");
        jlNoise.setBounds(25, 165, 70, 20);
        jpSettingSimulation.add(jlNoise);
        jtfNoise = new JTextField("45");
        jtfNoise.setBounds(95, 165, 70, 20);
        jpSettingSimulation.add(jtfNoise);
        
        
        jlSteps = new JLabel("Liczba kroków:");
        jlSteps.setBounds(195, 25, 95, 20);
        jpSettingSimulation.add(jlSteps);
        jtfSteps = new JTextField("100");
        jtfSteps.setBounds(290, 25, 70, 20);
        jpSettingSimulation.add(jtfSteps);
        
        jlStart = new JLabel("x minimum:");
        jlStart.setBounds(195, 60, 95, 20);
        jpSettingSimulation.add(jlStart);
        jtfStart= new JTextField("0");
        jtfStart.setBounds(290, 60, 70, 20);
        jpSettingSimulation.add(jtfStart);
        
        jlStop = new JLabel("x maximum:");
        jlStop.setBounds(195, 95, 95, 20);
        jpSettingSimulation.add(jlStop);
        jtfStop= new JTextField("255");
        jtfStop.setBounds(290, 95, 70, 20);
        jpSettingSimulation.add(jtfStop);
        
        jlNumerOfRow = new JLabel("Wczytaj wiersz bitmapy (numer):");
        jlNumerOfRow.setBounds(25, 235, 200, 20);
        jpSettingSimulation.add(jlNumerOfRow);
        jtfNumerOfRow = new JTextField("30");
        jtfNumerOfRow.setBounds(225, 235, 70, 20);
        jpSettingSimulation.add(jtfNumerOfRow);
        
        
        this.add(jpSettingSimulation, BorderLayout.WEST);
		
	}

	void addButtonPanel()
	{
		jpButtonPanel = new JPanel();
        jpButtonPanel.setPreferredSize(new Dimension(450, 300));
        jpButtonPanel.setLayout(null);
        
        
        bStart = new JButton("START");
        bStart.setBounds(10, 50, 130, 90);
        jpButtonPanel.add(bStart);
        bStart.addActionListener(this);
        
        
        bLoadBitmap = new JButton("Wczytaj bitmapê");
        bLoadBitmap.setBounds(160, 50, 130, 90);
        jpButtonPanel.add(bLoadBitmap);
        bLoadBitmap.addActionListener(this);
        
        
        bFitSin = new JButton("Dopasuj sinus");
        bFitSin.setBounds(310, 50, 130, 90);
        bFitSin.setEnabled(false);
        jpButtonPanel.add(bFitSin);
        bFitSin.addActionListener(this);
        
        
        
        bShowBM = new JButton("<html>Wyœwietl<br />bitmapê</html>");
        bShowBM.setBounds(10, 160, 130, 90);
        bShowBM.setEnabled(false);
        jpButtonPanel.add(bShowBM);
        bShowBM.addActionListener(this);
        

        bHelp = new JButton("Pomoc");
        bHelp.setBounds(160, 160, 130, 90);
        jpButtonPanel.add(bHelp);
        bHelp.addActionListener(this);
        
        bExit = new JButton("Wyjœcie");
        bExit.setBounds(310, 160, 130, 90);
        jpButtonPanel.add(bExit);
        bExit.addActionListener(this);
               
        this.add(jpButtonPanel, BorderLayout.EAST);
		
	}

	void readValueFromTextFields()
	{
		start = Double.parseDouble(jtfStart.getText());
		stop = Double.parseDouble(jtfStop.getText());
		amplitude = Double.parseDouble(jtfAmplitude.getText());
		period = Double.parseDouble(jtfPeriod.getText());
		phase = Double.parseDouble(jtfPhase.getText());
		steps = Integer.parseInt(jtfSteps.getText());
		noisePercent = Integer.parseInt(jtfNoise.getText());
		offset = Double.parseDouble(jtfOffset.getText());	
		
	}

	void cleanTextsFields()
	{
		jtfAmplitude.setText(null);
		jtfNoise.setText(null);
		jtfOffset.setText(null);
		jtfPeriod.setText(null);
		jtfPhase.setText(null);
		jtfStart.setText(null);
		jtfSteps.setText(null);
		jtfStop.setText(null);	
	}
	
	void getHelp()
	{

		JOptionPane.showMessageDialog(null, "Opis dzia³ania programu dostêpny na stronie: http://kpiotrowicz.pl/projekt1.html", 
				"Pomoc", JOptionPane.INFORMATION_MESSAGE);
		
		int choice = JOptionPane.showConfirmDialog(null, "Czy chcesz przejœæ na stronê: http://kpiotrowicz.pl/projekt1.html", 
				"Otwórz URL", JOptionPane.YES_NO_OPTION);
		
		if( choice == JOptionPane.YES_OPTION) openMyWebsite();

		
	}
	
	void openMyWebsite()
	{
		
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
		{
		    try
			{
				Desktop.getDesktop().browse(new URI("http://kpiotrowicz.pl/projekt1.html"));
			}
		    
		    
		    catch (IOException e)
			{
				e.printStackTrace();
			}
		    
		    
		    catch (URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		Object source = e.getSource();
		
		if(source == bStart)
		{
			xySeriesCollection.removeAllSeries();
			readValueFromTextFields();
			xyDataSymulation = Calculations.GenerateSin("Symulacja", start, stop, steps, amplitude, period, 
					phase, offset, noisePercent);
	        xySeriesCollection.addSeries(xyDataSymulation);
	        
	        bFitSin.setEnabled(true);
	        jtaResult.setText("Wygenerowano przebieg sinus.\n");
	            
		}
		
		
		
		if(source == bFitSin)
		{
			
			
			double coefficients[] = Calculations.FitSinus(xySeriesCollection.getSeries(0));
			
			double periodFit = Calculations.FindPeriod(xySeriesCollection.getSeries(0));
			double offsetFit = coefficients[2];
			double amplitudeFit = Math.sqrt(Math.pow(coefficients[0], 2)+Math.pow(coefficients[1], 2));
			double phaseFit = Math.atan(coefficients[0]/coefficients[1]);
			
			jtaResult.append("\nWyniki dopasowania:");
			
			jtaResult.append("\n\nWspó³czynniki dopasowania:");
			jtaResult.append("\n" + coefficients[0]);
			jtaResult.append("\n" + coefficients[1]);
			jtaResult.append("\n" + coefficients[2]);
			
			jtaResult.append("\n\nOkres: " + df2.format(periodFit));
			jtaResult.append("\nAmplituda: " + df2.format(amplitudeFit));
			jtaResult.append("\nOffset: " + df2.format(offsetFit));
			jtaResult.append("\nFaza(rad): " + df2.format(phaseFit));
			jtaResult.append("\n");
			
			
			double startFit = xySeriesCollection.getSeries(0).getMinX();
			double stopFit = xySeriesCollection.getSeries(0).getMaxX();
			int stepsFit = xySeriesCollection.getSeries(0).getItemCount()*10;
			xySeriesCollection.addSeries(Calculations.GenerateSin("Dopasowanie", startFit, stopFit, stepsFit,
					amplitudeFit, periodFit, phaseFit, offsetFit, 0));		
		}
		
		
		if(source == bLoadBitmap)
		{
			xySeriesCollection.removeAllSeries();
			Bitmap.LoadTheBitmap();
			int numberOfRow = Integer.parseInt(jtfNumerOfRow.getText());
			Bitmap.plot(numberOfRow);
			bShowBM.setEnabled(true);
			bFitSin.setEnabled(true);
			jtaResult.setText("Wyczytano plik z bitmap¹.\n");
		}
		
		if(source == bHelp) getHelp();
		
		if(source == bShowBM) Bitmap.showBitmap();
		
		if(source == bExit) dispose();
		
	}
	
}
