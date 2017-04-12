import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StockBuilder
{
	private static Logger log = LogManager.getLogger();
	private static Path root;
	private static DecimalFormat doublesFormatter = new DecimalFormat("0.000000");

	public static StockSet build(Path rootPath)
	{
		return build(rootPath, null);
	}

	public static StockSet build(Path rootPath, Path logPath)
	{

		StockBuilder.root = rootPath;

		StockSet set = new StockSet();
		long start = System.currentTimeMillis();
		HashSet<HashSet<Path>> structure = null;
		long cleanTime = 0;
		try
		{
			structure = Traverser.get(root);

			int cnt = 0;
			for (HashSet<Path> stockFolder: structure)
			{
				String name = "";
				String alias = "";
				String earningDate = "";
				String EPS_text = "";
				String range52 = "";
				Path csvPath = null;

				stock: for (Path stockFile: stockFolder)
				{
					// System.out.println(stockFile);
					if (stockFile.toString().toLowerCase().endsWith(".html"))
					{
						long cleanStart = System.currentTimeMillis();
						String text = HTMLCleaner.clean(stockFile);
						long cleanEnd = System.currentTimeMillis();
						cleanTime += (cleanEnd - cleanStart);
						if (text.contains("Did you mean"))
						{
							set.add(new Stock("NULL", "NULL", null, null, null, null));
							break stock;
						}
						String words[] = text.split("\\p{Space}");
						if (stockFile.getFileName().toString().toLowerCase().startsWith("analysts"))
						{
							for (int i = 10; i < words.length; i++)
							{
								if (words[i].equals("Est.") || words[i].equals("Actual"))
								{
									EPS_text += words[i + 1] + "," + words[i + 2] + "," + words[i + 3] + "," + words[i + 4] + ",";
									if (EPS_text.split(",").length == 8)
									{
										break;
									}
								}
							}
							if (EPS_text.split(",").length == 8)
							{
								EPS_text = EPS_text.substring(0, EPS_text.length() - 1);
							} else
							{
								log.warn("==========!!!Illegal EPS detected, illegal data member count: " + EPS_text.split(",").length);
							}
						} else if (stockFile.getFileName().toString().toLowerCase().startsWith("summary"))
						{
							alias = words[1];
							alias = alias.trim();
							int c = 5;
							while (!words[c].equals("-") && c < words.length - 1)
							{
								name += words[c] + " ";
								c++;
							}
							name = name.trim();

							for (int i = 50; i < Math.min(300, words.length); i++)
							{
								if (words[i].equals("Date") && words[i - 1].equals("Earnings") && !words[i + 1].equals("N/A"))
								{
									int k = i + 1;
									while (!words[k].equals("Dividend") && (k - i) < 7 && k < words.length)
									{
										earningDate += words[k] + " ";
										k++;
									}
								} else if (words[i].equals("Range") && words[i - 1].equals("Week") && words[i - 2].equals("52"))
								{
									range52 = words[i + 1] + " " + words[i + 2] + " " + words[i + 3];
								}
							}
						}
					} else if (stockFile.toString().toLowerCase().endsWith(".csv"))
					{
						log.info("CSV captured");
						csvPath = Paths.get(stockFile.toString());
					} else
					{
						log.warn("======>Unknown file detected at" + stockFile.toString());
					}
				}
				log.info("Data captrured, attempting to create stock object...");
				set.add(new Stock(alias, name, earningDate, range52, EPS_text, csvPath));
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("done");
		System.out.println(set);
		System.out.println("Process took: " + doublesFormatter.format((end - start) / 1000.0) + " seconds");
		System.out.println("Total Cleaning time: " + doublesFormatter.format(cleanTime / 1000.0) + " seconds");
		System.out.println("Per stock total average: " + doublesFormatter.format((end - start) / 1000.0 / structure.size()) + " seconds");
		System.out.println("Per stock cleaning time average: " + doublesFormatter.format(cleanTime / 1000.0 / structure.size()) + " seconds");
		System.out.println("Per stock calclulation time average: " + doublesFormatter.format(((end - start) / 1000.0 / structure.size()) - (cleanTime / 1000.0 / structure.size())) + " seconds");
		System.out.println(Stock.cnttt);
		return set;
	}

}
