import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CSVhandler
{
	private static Logger log = LogManager.getLogger();

	public static void write(Path path, StockSet set) throws IOException
	{
		log.info("Writing stockSet " + set.hashCode() + " to " + path.toString());
		BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		writer.write("Code,Name,Earnings Data,52 week Range,Est_Q1,Est_Q2,Est_Q3,Est_Q4,Act_Q1,Act_Q2,Act_Q3,Act_Q4,CSV File Path\n");
		Iterator<Stock> iterator = set.getIterator();
		while (iterator.hasNext())
		{
			Stock temp = iterator.next();
			String line = "";
			line += temp.getALIAS() + ",";
			line += temp.getNAME().replaceAll(",", " ") + ",";
			if (temp.getEarningDate() != null)
			{
				line += temp.getEarningDate().toString().replaceAll(",", " ") + ",";
			} else
			{
				line += ",";
			}
			if (temp.getRange52() != null)
			{
				line += temp.getRange52().toString().replaceAll(",", " ") + ",";
			} else
			{
				line += ",";
			}
			line += temp.getEPS_text() + ",";
			if (temp.getDailyPriceCSV() != null)
			{
				line += temp.getDailyPriceCSV().toString().replaceAll(",", " ") + ",";
			} else
			{
				line += ",";
			}
			writer.write(line + "\n");
		}
		writer.flush();
	}

	/**
	 * This method is used to retrieve compiled data from a given csv file.
	 * Gather data from csv file and use the data to construct a Stock Object,
	 * please reference the csv file and the Stock Constructor for detail
	 * formatting.
	 *
	 * Construct a StockSet and use StockSet.add(Stock) method to add individual
	 * Stock to the StockSet and return. Refer StockSet class.
	 *
	 * @param path the path of the data.csv
	 * @return a StockSet that contains all Stock objects built from csv.
	 */
	public static StockSet read(Path path)
	{
		// TODO
		return null;
	}

}
