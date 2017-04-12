import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EPS
{
	private Logger log = LogManager.getLogger();
	public boolean isValid = true;
	private Double Q1_ACTUAL;
	private Double Q2_ACTUAL;
	private Double Q3_ACTUAL;
	private Double Q4_ACTUAL;

	private Double Q1_EST;
	private Double Q2_EST;
	private Double Q3_EST;
	private Double Q4_EST;

	private Double Q1_DIFF;
	private Double Q2_DIFF;
	private Double Q3_DIFF;
	private Double Q4_DIFF;

	private Double Q1_GROWTH;
	private Double Q2_GROWTH;
	private Double Q3_GROWTH;
	private Double Q4_GROWTH;

	public EPS(ArrayList<Double> input)
	{
		if (input.contains(null))
		{
			isValid = false;
			return;
		}
		DecimalFormat doublesFormatter = new DecimalFormat("##.00");
		Q1_EST = input.get(0);
		Q2_EST = input.get(1);
		Q3_EST = input.get(2);
		Q4_EST = input.get(3);
		Q1_ACTUAL = input.get(4);
		Q2_ACTUAL = input.get(5);
		Q3_ACTUAL = input.get(6);
		Q4_ACTUAL = input.get(7);
		Q1_DIFF = Double.parseDouble(doublesFormatter.format(Q1_ACTUAL - Q1_EST));
		Q2_DIFF = Double.parseDouble(doublesFormatter.format(Q2_ACTUAL - Q2_EST));
		Q3_DIFF = Double.parseDouble(doublesFormatter.format(Q3_ACTUAL - Q3_EST));
		Q4_DIFF = Double.parseDouble(doublesFormatter.format(Q4_ACTUAL - Q4_EST));
		Q1_GROWTH = Double.parseDouble(doublesFormatter.format(0.00));
		Q2_GROWTH = Double.parseDouble(doublesFormatter.format(Q2_ACTUAL - Q1_ACTUAL));
		Q3_GROWTH = Double.parseDouble(doublesFormatter.format(Q3_ACTUAL - Q2_ACTUAL));
		Q4_GROWTH = Double.parseDouble(doublesFormatter.format(Q4_ACTUAL - Q3_ACTUAL));
		
		log.info("EPS " + this.hashCode() + " constructed, valid: " + isValid);
	}

	public Double getEst(int Q)
	{
		switch (Q)
		{
			case 0:
				return Q1_EST;
			case 1:
				return Q2_EST;
			case 2:
				return Q3_EST;
			case 3:
				return Q4_EST;
			default:
				return null;
		}
	}

	public Double getDiff(int Q)
	{
		switch (Q)
		{
			case 0:
				return Q1_DIFF;
			case 1:
				return Q2_DIFF;
			case 2:
				return Q3_DIFF;
			case 3:
				return Q4_DIFF;
			default:
				return null;
		}
	}

	public Double getGrowth(int Q)
	{
		switch (Q)
		{
			case 0:
				return 0.0;
			case 1:
				return Q2_GROWTH;
			case 2:
				return Q3_GROWTH;
			case 3:
				return Q4_GROWTH;
			default:
				return null;
		}
	}

	public Double getActual(int Q)
	{
		switch (Q)
		{
			case 0:
				return Q1_ACTUAL;
			case 1:
				return Q2_ACTUAL;
			case 2:
				return Q3_ACTUAL;
			case 3:
				return Q4_ACTUAL;
			default:
				return null;
		}
	}

	public Double[] getGrowthAll()
	{
		Double[] output = new Double[4];
		for (int i = 0; i < 4; i++)
		{
			output[i] = getGrowth(i);
		}
		return output;
	}

	public Double[] getDiffAll()
	{
		Double[] output = new Double[4];
		for (int i = 0; i < 4; i++)
		{
			output[i] = getDiff(i);
		}
		return output;
	}

	public Double[] getEstAll()
	{
		Double[] output = new Double[4];
		for (int i = 0; i < 4; i++)
		{
			output[i] = getEst(i);
		}
		return output;
	}

	public Double[] getActualAll()
	{
		Double[] output = new Double[4];
		for (int i = 0; i < 4; i++)
		{
			output[i] = getActual(i);
		}
		return output;
	}

	@Override
	public String toString()
	{
		String output = "";
		output += "EPS Object at: " + this.hashCode() + "\n";
		output += "\t Estimates:  " + Q1_EST + "," + Q2_EST + "," + Q3_EST + "," + Q4_EST + "\n";
		output += "\t Actual:     " + Q1_ACTUAL + "," + Q2_ACTUAL + "," + Q3_ACTUAL + "," + Q4_ACTUAL + "\n";
		output += "\t Difference: " + Q1_DIFF + "," + Q2_DIFF + "," + Q3_DIFF + "," + Q4_DIFF + "\n";
		output += "\t Growth:     " + Q1_GROWTH + "," + Q2_GROWTH + "," + Q3_GROWTH + "," + Q4_GROWTH + "\n";
		return output;
	}

	public String getOriginal()
	{
		return Q1_EST + "," + Q2_EST + "," + Q3_EST + "," + Q4_EST + "," + Q1_ACTUAL + "," + Q2_ACTUAL + "," + Q3_ACTUAL + "," + Q4_ACTUAL + ",";
	}

}
