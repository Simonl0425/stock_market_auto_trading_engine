import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class StockBuilder
{
    private static Path root;
    private static Path logPath;

    public static void main(String args[])
    {
        if(args.length != 2)
        {
            System.err.println("Usage: java StockBuilder <directory> <log.txt path>");
            System.exit(1);
        }
        long start = System.currentTimeMillis();
        root = Paths.get(args[0]);
        logPath = Paths.get(args[1]);
        StockSet set = new StockSet();

        try
        {
            HashSet<HashSet<Path>> structure = Traverser.get(root,logPath);

            BufferedWriter bw = new BufferedWriter(new FileWriter(logPath.toString(), true));
            PrintWriter logWriter = new PrintWriter(bw);


            for(HashSet<Path> stockFolder: structure)
            {
                String name = "";
                String alias = "";
                String earningDate = "";
                String EPS_text = "";
                String range52 = "";
                Path csvPath = null;


                for(Path stockFile: stockFolder)
                {
                    if(stockFile.toString().toLowerCase().endsWith(".html"))
                    {
                        String words[] = HTMLCleaner.clean(stockFile, logPath).split("\\p{Space}");
                        if(stockFile.getFileName().toString().toLowerCase().startsWith("analysts"))
                        {
                            alias = words[1];
                            alias = alias.trim();
                            int c = 8;
                            while(!words[c].equals("Stock"))
                            {name += words[c] + " "; c++;}
                            name = name.trim();
                            for(int i = 10;i < words.length;i++)
                            {
                                if(words[i].equals("Est.") || words[i].equals("Actual"))
                                {
                                    EPS_text += words[i+1] + "," + words[i+2] + "," + words[i+3] + "," + words[i+4] + ",";
                                    if(EPS_text.split(",").length == 8){break;}
                                }
                            }
                            if(EPS_text.split(",").length == 8)
                            {
                                EPS_text = EPS_text.substring(0,EPS_text.length() - 1);
                            }else{
                                logWriter.write("Illegal EPS detected, data member count: " + EPS_text.split(",").length + "\n");
                            }
                        }else if(stockFile.getFileName().toString().toLowerCase().startsWith("summary"))
                        {
                            for(int i = 50; i < 300;i++)
                            {
                                if(words[i].equals("Date") && words[i-1].equals("Earnings"))
                                {
                                    earningDate = words[i+1]+words[i+2]+words[i+3]+words[i+4]+words[i+5]+words[i+6]+words[i+7];
                                }
                                if(words[i].equals("Range") && words[i-1].equals("Week") && words[i-2].equals("52"))
                                {
                                    range52 = words[i+1] + " " + words[i+2] + " " + words[i+3];
                                }
                            }
                        }
                    }else if(stockFile.toString().toLowerCase().endsWith(".csv"))
                    {
                        logWriter.write("\tCSV captured\n");
                        csvPath = Paths.get(stockFile.toString());
                    }else{
                        logWriter.write("===>Unknown file detected at" + stockFile.toString() + "\n");
                    }
                }

                if(alias != "" && name != "" && earningDate != "" && EPS_text != "" && range52 != "" && csvPath != null)
                {
                    Stock temp = new Stock(alias,name,earningDate,range52,EPS_text,csvPath);
                    logWriter.write("Data Collected, constructing Stock object " + temp.hashCode() + "\n");
                    set.add(temp);
                }else{
                    logWriter.write("===============!Infomation did not collect fully!=================\n");
                }
                logWriter.flush();
            }


            logWriter.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Process took: " + (end - start)/1000.0 + "seconds");

    }


}
