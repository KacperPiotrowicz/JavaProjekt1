package main;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;
import org.apache.log4j.Logger;
import org.jfree.data.xy.XYSeries;
//import javax.swing.JOptionPane;

public class Calculations
{
	
	static Logger logger = Logger.getLogger(Calculations.class);
	
	
	public static XYSeries GenerateSin(String title, double start, double stop, int steps, double amplitude, double period, 
			double phase, double offset, int noisePercent)
	{
		
		XYSeries sinSeries= new XYSeries(title);
		double noiseMax = amplitude *  (float)noisePercent/100;
		double noise;
		double x, dx, y;
		Random generator = new Random();
		
		for(int n = 1; n <= steps; n++)
		{
			noise = generator.nextDouble() * noiseMax;
			if(generator.nextBoolean()) noise *= (-1);
			
			dx = n*(stop - start)/steps;
			x = start + dx;
			y = amplitude * Math.sin((x * 2 * Math.PI / period) + phase) + offset + noise;
			sinSeries.add(x, y);
		}
		
		return sinSeries;
			
	}
	
	
	public static double Pearson(double A[], double B[])
	{
		
		if( A.length != B.length)
			{
				logger.error("Funkcja Person: ró¿na d³ugoœæ tablic");
				//JOptionPane.showMessageDialog(null, "Funkcja Person: ró¿na d³ugoœæ tablic.", "B³¹d", JOptionPane.ERROR_MESSAGE);
				return 2;
			}
		
		
		double result;
		
		double aAverage = SumDoubleArray(A) / A.length;
		double bAverage = SumDoubleArray(B) / B.length;
		
		double counter[] = new double[A.length];
		double denominator1[] = new double[A.length];
		double denominator2[] = new double[A.length];
		
		for(int i = 0; i < A.length; i++)
		{
			counter[i] =  (A[i] - aAverage)*(B[i] - bAverage);
			denominator1[i] = Math.pow(A[i] - aAverage, 2);
			denominator2[i] = Math.pow(B[i] - bAverage, 2);
		}
		
		result = SumDoubleArray(counter) / Math.sqrt(SumDoubleArray(denominator1)*SumDoubleArray(denominator2));
		return result;
	}
	
	
	static double SumDoubleArray(double x[])
	{
		return DoubleStream.of(x).sum();
	}

	
	public static double FindPeriod(XYSeries inSeries)
	{
		XYSeries PearsonSeries = new XYSeries("Sygna³ odszumiony");
		
		double arraySeries[][] = inSeries.toArray();
		double arrayX[] = Arrays.copyOf(arraySeries[0], arraySeries[0].length);
		double arrayY[] = Arrays.copyOf(arraySeries[1], arraySeries[1].length);
		
		int comparativeRange = (int) (0.3 * arrayX.length);	
		int numbersOfShift = arrayX.length - comparativeRange + 1; // +1, bo zaczynamy od 0
		
		double comparativeArrayY[] = Arrays.copyOf(arrayY, comparativeRange);
		double shiftedArrayY[] = new double[comparativeRange];
		double pearsonArray[] = new double[numbersOfShift];
		
		for (int n = 0; n < numbersOfShift; n++)
		{
			shiftedArrayY = Arrays.copyOfRange(arrayY, n, comparativeRange + n);
			pearsonArray[n] = Pearson(comparativeArrayY, shiftedArrayY);
			PearsonSeries.add(arrayX[n], pearsonArray[n]);
		}
		
	
		// Wyznaczanie okresu na podstawie zbocza opadaj¹cego:
		
		double xLocationOfMaximum[] = new double[0];
		double y1, y2;
		
		for (int n = 0; n < pearsonArray.length - 1; n++)
		{
			y1 = PearsonSeries.getY(n).doubleValue();
			y2 = PearsonSeries.getY(n+1).doubleValue();
			
			if( y2 < y1 && y1 * y2 <= 0)
			{
				xLocationOfMaximum = Arrays.copyOf(xLocationOfMaximum, xLocationOfMaximum.length + 1);
				xLocationOfMaximum[xLocationOfMaximum.length - 1] = PearsonSeries.getX(n).doubleValue();
			}
			
		}
		
		
		double averangePeriod = 0;
		
		for (int n = 0; n < xLocationOfMaximum.length - 1; n++)
		{
			averangePeriod += (xLocationOfMaximum[n+1] - xLocationOfMaximum[n]);
		}
		
		averangePeriod = ( averangePeriod / (xLocationOfMaximum.length - 1) );
		return averangePeriod;
		
	}

	
	static boolean FillVector(double vector[], double value)

	{
		for(int n = 0; n < vector.length; n++)
		{
			vector[n] = value;
		}
		
		return true;
	}
	
	
	static double[] VectorSin(double arguments[], double period, double phase)
	{
		
		double result[] = new double[arguments.length];
		
		for(int n = 0; n < arguments.length; n++)
		{
			result[n] = Math.sin((2 * Math.PI / period) * arguments[n] + phase);
		}
		
		return result;
	}
	
	
	static double[] VectorCos(double arguments[], double period, double phase)
	{
		
		double result[] = new double[arguments.length];
		
		for(int n = 0; n < arguments.length; n++)
		{
			result[n] = Math.cos((2 * Math.PI / period) * arguments[n] + phase);
		}
		
		return result;
	}
	
	
	static double[] PrepareSinCosVector(double inX[], double period, double phase, int n)
	{
		
		double result[] = new double[inX.length];
		
		if (n == 0) FillVector(result, 1);
		if (n == 1) result = VectorSin(inX, period, phase);
		if (n == 2) result = VectorCos(inX, period, phase);
		
		return result;
	}

	
	public static double DotProduct(double A[], double B[])
	{
		
		if(A.length != B.length)
		{
			logger.error("Funkcja DotProduct: ró¿na d³ugoœæ wektorów");
			//JOptionPane.showMessageDialog(null, "Funkcja DotProduct: ró¿na d³ugoœæ wektorów.", "B³¹d", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
		
		double result = 0;
		for(int n =0; n < A.length; n++)
		{
			result += (A[n] * B[n]);
		}
		
		return result;
		
	}

	
	static double[] PowerVector(double A[], int index)
	{
		
		double result[] = new double[A.length];
		
		for(int n = 0; n < A.length; n++)
		{
			result[n] = Math.pow(A[n], index);
		}
		
		return result;
	}


	public static double[][] MatrixMinnor(double matrix[][], int numberR, int numberC)
	{
		
		int R = matrix.length;
		int C = matrix[0].length;
		
		double minnor[][] = new double[R-1][C-1];
		
		int jumpR, jumpC;
		for(int n = 0; n < R-1; n++)
		{
			if(n >= numberR) jumpR = 1;
			else jumpR = 0;
			
			for(int i = 0; i < C-1; i++)
			{
				if(i >= numberC) jumpC = 1;
				else jumpC = 0;
				
				minnor[n][i] = matrix[n + jumpR][i + jumpC];
			}
			
		}
		
		
		return minnor;
	}


	public static double MatrixDet(double A[][])
	{
		
		if( A.length == 0)
		{
			logger.error("Funkcja MartixDet: pusta macierz");
			//JOptionPane.showMessageDialog(null, "Funkcja MartixDet: pusta macierz.", "B³¹d", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
		
		if( A.length != A[0].length)
		{
			logger.error("Funkcja MartixDet: niekwadratowa macierz");
			//JOptionPane.showMessageDialog(null, "Funkcja MartixDet: niekwadratowa macierz.", "B³¹d", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
		
		
		
		
		if(A.length == 1) return A[0][0];
		if(A.length == 2) return ( A[0][0]*A[1][1] - A[0][1]*A[1][0] );
		
		double result = 0;
		
		for(int j = 0; j < A.length; j++)
		{
			result += ( A[0][j] * Math.pow(-1, j+2) * MatrixDet(MatrixMinnor(A, 0, j)) );
			
		}
	
		return result;
		
	}
	
	
	public static double[][] InverseMatrix(double A[][])
	{
		
		double det = MatrixDet(A);
		
		int R = A.length;
		int C = A[0].length;
		
		double minnor[][] = new double[R-1][C-1];
		double temp[][] = new double[R][C];
		double result[][] = new double[R][C];
		
	
		for(int n = 0; n < C; n++)
		{
			for(int i = 0; i < R; i++)
			{
				minnor = MatrixMinnor(A, n, i);
				temp[n][i] = Math.pow(-1, n+i+2) * MatrixDet(minnor);
				result[i][n] = temp[n][i] * (1/det);
			}
		}
		
		return result;
		
	}
	
	
	public static double[] FitSinus(XYSeries inSeries)
	{
		
		double inArraySeries[][] = inSeries.toArray();
		double inArrayX[] = Arrays.copyOf(inArraySeries[0], inArraySeries[0].length);
		double inArrayY[] = Arrays.copyOf(inArraySeries[1], inArraySeries[1].length);
		
		double Y[] = new double[3];
		double matrixX[][] = new double[3][3];
		
		double fi1[] = new double[inArrayX.length];
		double fi2[] = new double[inArrayX.length];	
		double period = FindPeriod(inSeries);
			
		for (int i = 0; i < 3; i++)
		{
			fi1 = PrepareSinCosVector(inArrayX, period, 0, i);
			
			for(int ii = 0; ii < 3; ii++)
			{
				fi2 = PrepareSinCosVector(inArrayX, period, 0, ii);
				
				matrixX[i][ii] = DotProduct(fi1, fi2);
			}
		}
		
		
		FillVector(fi1, 1);
		Y[0] = DotProduct(fi1, inArrayY);
		fi1 = VectorSin(inArrayX, period, 0);
	    Y[1] = DotProduct(fi1, inArrayY);
		fi1 = VectorCos(inArrayX, period, 0);
	    Y[2] = DotProduct(fi1, inArrayY);
	    
	    
	    double X_inverse[][] = new double[3][3];
	    X_inverse = InverseMatrix(matrixX);
	    
	    
	    double fittingCoefficients[] = new double[3];
	    FillVector(fittingCoefficients, 0);
		
	    for(int n = 0; n < 3; n++)
	    {
	    	for(int i = 0; i < 3; i++)
	    	{
	    		fittingCoefficients[n] += (X_inverse[2 - n][i] * Y[i]);
	    	}
	    }
		
	    
		return fittingCoefficients;
		
	}
	
	

	
	
}
