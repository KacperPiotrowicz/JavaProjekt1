package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import org.jfree.data.xy.XYSeries;


public class Bitmap
{
	static Logger logger = Logger.getLogger(Bitmap.class);
	
	public static File bm;
	public static BufferedImage img;
	public static XYSeries bmSeries;
	
	
	public static void LoadTheBitmap()
	{
		JFileChooser fc = new JFileChooser();
		
		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			bm = fc.getSelectedFile();
			try
			{
				img = ImageIO.read(bm);
			}
			catch (IOException e1)
			{
				logger.error("Funkcja LoadTheBitmap: Problem z wczytaniem bitmapy");
				JOptionPane.showMessageDialog(null, "Funkcja LoadTheBitmap: Problem z wczytaniem bitmapy", "B³¹d", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}
	
	
	public static int getPixelValue(int x, int y)
	{
		Color pix = new Color(img.getRGB(x, y));
		int result = (pix.getBlue() + pix.getGreen() + pix.getRed()) / 3;
		return result;
	}
	
	
	public static void plot(int height)
	{
	
		bmSeries= new XYSeries("Bitmapa");
		
		for(int n = 0; n < img.getWidth(); n++)
		{
			bmSeries.add(n, getPixelValue(n, height));
		}
		
		Frame.xySeriesCollection.addSeries(bmSeries);	
	}
	
	
	public static void showBitmap()
	{
		JFrame showBM = new JFrame();
		
		showBM.setSize(648, 484);
		showBM.setTitle("Wczytana bitmapa");
		showBM.setLocationByPlatform(true);
		showBM.getContentPane().add(new JLabel(new ImageIcon(Bitmap.img)));
		showBM.setVisible(true);
	}

	
}
