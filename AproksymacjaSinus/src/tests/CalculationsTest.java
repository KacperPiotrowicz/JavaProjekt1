package tests;

import static org.junit.Assert.*;

import org.jfree.data.xy.XYSeries;
import org.junit.Test;

import main.Calculations;

public class CalculationsTest
{
	double[] x = {1, 2.21, 3, 0, 14};
	double[] y = {3.14, 0.21, -3.5, 2, 1};
	double[][] matrix = new double[3][3];
	
	void fillMatrix()
	{
		matrix[0][0] = 3.4;
		matrix[1][0] = 7.1;
		matrix[2][0] = -3;
		
		matrix[0][1] = 2;
		matrix[1][1] = 2;
		matrix[2][1] = 0;
		
		matrix[0][2] = 5;
		matrix[1][2] = 1;
		matrix[2][2] = 2.5;
	}
	

	@Test
	public void testPearson()
	{
		double actual = Calculations.Pearson(x, y);
		double expected = -0.077;
		String a = String.format("%.3f", actual);
		String e = String.format("%.3f", expected);
		
		assertEquals(a, e);	
	}
	
	@Test
	public void testPearsonSameVaules()
	{
		double actual = Calculations.Pearson(x, x);
		double expected = 1;
		String a = String.format("%.3f", actual);
		String e = String.format("%.3f", expected);
		
		assertEquals(a, e);	
	}
	
	@Test
	public void testPearsonOppositeValues()
	{
		double[] minusx = {-1, -2.21, -3, -0, -14};
		
		double actual = Calculations.Pearson(x, minusx);
		double expected = -1;
		String a = String.format("%.3f", actual);
		String e = String.format("%.3f", expected);
		
		assertEquals(a, e);	
	}

	
	@Test
	public void testFindPeriod()
	{
		double period = 18;
		double acceptableRange = 0.95;
		
		XYSeries xy = Calculations.GenerateSin("", -20, 120, 600, 1, period, 0, 0, 0);
		double fitPeriod = Calculations.FindPeriod(xy);
		
		assertTrue(fitPeriod >= period*acceptableRange && 
				fitPeriod <= (2-acceptableRange)*period);
	}
	
	
	@Test
	public void testDotProduct()
	{
		double expected = 7.1041;
		double actual = Calculations.DotProduct(x, y);
		String a = String.format("%.3f", actual);
		String e = String.format("%.3f", expected);
		
		assertEquals(a, e);
		
	}
	
	@Test
	public void testDotProductEmpty()
	{
		double[] x1 = new double[0];
		double[] y1 = new double[0];
		
		double actual = Calculations.DotProduct(x1, y1);
		double expected = 0;
		
		String a = String.format("%.3f", actual);
		String e = String.format("%.3f", expected);
		
		assertEquals(a, e);
		
	}
	
	

	@Test
	public void testMatrixMinnor()
	{

		fillMatrix();
		matrix = Calculations.MatrixMinnor(matrix, 0, 0);
		matrix = Calculations.MatrixMinnor(matrix, 0, 1);
		assertTrue(matrix[0][0] == 0);
		
	}
	
	

	@Test
	public void testMatrixDet()
	{
		fillMatrix();
		double expected = 5.5;
		double actual = Calculations.MatrixDet(matrix);
		
		String a = String.format("%.2f", actual);
		String e = String.format("%.2f", expected);
		
		assertEquals(a, e);
		
	}

	@Test
	public void testMatrixOneElement()
	{
		double[][] matrix2 = new double[1][1];
		matrix2[0][0] = 5;
		
		assertTrue(Calculations.MatrixDet(matrix2) == 5);
		
	}
	
	@Test
	public void testMatrixNonSquare()
	{
		double[][] matrix3 = new double[1][2];
		matrix3[0][0] = 3.4;
		matrix3[0][1] = 2;
		
		assertTrue(Calculations.MatrixDet(matrix3) == 0);
		
	}
	
	@Test
	public void testMatrixEmpty()
	{
		double[][] matrix4 = new double[0][0];
		assertTrue(Calculations.MatrixDet(matrix4) == 0);
		
	}
	
	
	@Test
	public void testInverseMatrix()
	{
		fillMatrix();
		double[][] expectedInvMatrix = new double[3][3];
		
		expectedInvMatrix[0][0] = 0.91;
		expectedInvMatrix[1][0] = -3.77;
		expectedInvMatrix[2][0] = 1.09;
		
		expectedInvMatrix[0][1] = -0.91;
		expectedInvMatrix[1][1] = 4.27;
		expectedInvMatrix[2][1] = -1.09;
		
		expectedInvMatrix[0][2] = -1.45;
		expectedInvMatrix[1][2] = 5.84;
		expectedInvMatrix[2][2] = -1.35;
		
		double[][] actualInvMatrix = Calculations.InverseMatrix(matrix);
		
		boolean isOkey = true;
		String a, e;
		
		for(int n = 0; n < 3; n++)
		{
			for(int m = 0; m < 3; m++)
			{
				a = String.format("%.2f",actualInvMatrix[n][m]);
				e = String.format("%.2f",expectedInvMatrix[n][m]);
				if (! a.equals(e)) isOkey = false;	
			}
		}
		
		assertTrue(isOkey);
	}
	
	
	
}
