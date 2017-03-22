import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.omg.CORBA.SystemException;
import java.text.DecimalFormat;
public class StockBuilder
{
    private static Path root;
    private static Path logPath;
    private static boolean DEBUG = false;
    private static PrintWriter logWriter;
    private static DecimalFormat doublesFormatter = new DecimalFormat("0.000000");


    public static StockSet build(String args[])
    {
        ArgumentMap arguments = new ArgumentMap();
        arguments.parse(args);

        if(arguments.hasFlag("-d")){DEBUG = true;}
        if(arguments.hasFlag("-l") && arguments.hasValue("-l"))
        {
            logPath = Paths.get(arguments.getValue("-l"));
        }
        if(arguments.hasFlag("-p") && arguments.hasValue("-p"))
        {
            root = Paths.get(arguments.getValue("-p"));
        }else{
            System.out.println("\nUsage: java StockBuilder <flag> <value>\n\t-p <path> input path\n\t-l <path> enable logging with path\n\t-d        enable debugging mode\n");
            System.exit(1);
        }

        StockSet set = new StockSet();
        long start = System.currentTimeMillis();
        HashSet<HashSet<Path>> structure = null;
        long cleanTime = 0;
        try
        {
            structure = Traverser.get(root);
            if(logPath != null)
            {
                BufferedWriter bw = new BufferedWriter(new FileWriter(logPath.toString(), true));
                logWriter = new PrintWriter(bw);
            }

            int cnt = 0;
            for(HashSet<Path> stockFolder: structure)
            {
                for(int i = 0; i < String.valueOf(cnt).length()+9;i++){System.out.print("\b");}
                System.out.print("Working: "+cnt++);
                String name = "";
                String alias = "";
                String earningDate = "";
                String EPS_text = "";
                String range52 = "";
                Path csvPath = null;

                stock:
                for(Path stockFile: stockFolder)
                {
                    //System.out.println(stockFile);
                    if(stockFile.toString().toLowerCase().endsWith(".html"))
                    {
                        long cleanStart = System.currentTimeMillis();
                        String text = HTMLCleaner.clean(stockFile);
                        long cleanEnd = System.currentTimeMillis();
                        cleanTime += (cleanEnd- cleanStart);
                        if(text.contains("Did you mean"))
                        {
                            set.add(new Stock("NULL","NULL",null,null,null,null));
                            break stock;
                        }
                        String words[] = text.split("\\p{Space}");
                        if(stockFile.getFileName().toString().toLowerCase().startsWith("analysts"))
                        {
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
                                log("==========!!!Illegal EPS detected, illegal data member count: " + EPS_text.split(",").length + "\n");
                            }
                        }else if(stockFile.getFileName().toString().toLowerCase().startsWith("summary"))
                        {
                            alias = words[1];
                            alias = alias.trim();
                            int c = 5;
                            while(!words[c].equals("-") && c < words.length-1)
                            {name += words[c] + " "; c++;}
                            name = name.trim();


                            for(int i = 50; i < Math.min(300,words.length);i++)
                            {
                                if(words[i].equals("Date") && words[i-1].equals("Earnings") && !words[i+1].equals("N/A"))
                                {
                                    int k = i+1;
                                    while(!words[k].equals("Dividend") && (k-i)<7 && k< words.length)
                                    {
                                        earningDate += words[k] + " ";k++;
                                    }
                                }else if(words[i].equals("Range") && words[i-1].equals("Week") && words[i-2].equals("52"))
                                {
                                    range52 = words[i+1] + " " + words[i+2] + " " + words[i+3];
                                }
                            }
                        }
                    }else if(stockFile.toString().toLowerCase().endsWith(".csv"))
                    {
                        log("     CSV captured\n");
                        csvPath = Paths.get(stockFile.toString());
                    }else{
                        log("======>Unknown file detected at" + stockFile.toString() + "\n");
                    }
                }
                log("Data captrured, attempting to create stock object...");
                set.add(new Stock(alias,name,earningDate,range52,EPS_text,csvPath));
            }


            if(logWriter != null)
            {
                logWriter.close();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("done");
        System.out.println(set);
        System.out.println("Process took: " + doublesFormatter.format((end - start)/1000.0) + " seconds");
        System.out.println("Total Cleaning time: " + doublesFormatter.format(cleanTime/1000.0) + " seconds");
        System.out.println("Per stock total average: " + doublesFormatter.format((end - start)/1000.0/structure.size()) + " seconds");
        System.out.println("Per stock cleaning time average: " + doublesFormatter.format(cleanTime/1000.0/structure.size()) + " seconds");
        System.out.println("Per stock calclulation time average: "+ doublesFormatter.format(((end - start)/1000.0/structure.size())-(cleanTime/1000.0/structure.size()))+ " seconds");
        System.out.println(Stock.cnttt);
        return set;
    }


    public static void log(String input)
    {
        if(logWriter != null)
        {
            input = "["+ new SimpleDateFormat("HH:mm:ss:SSS").format(Calendar.getInstance().getTime()).toString() + "]" + input;
            logWriter.write(input);
        }
    }

}
