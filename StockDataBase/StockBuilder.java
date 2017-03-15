import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class StockBuilder
{
    private static Path root;
    private static Path logPath;
    private static boolean DEBUG = false;
    private static PrintWriter logWriter;


    public static void main(String args[])
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

        try
        {
            HashSet<HashSet<Path>> structure = Traverser.get(root);

            long start = System.currentTimeMillis();

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


                for(Path stockFile: stockFolder)
                {
                    if(stockFile.toString().toLowerCase().endsWith(".html"))
                    {
                        String words[] = HTMLCleaner.clean(stockFile).split("\\p{Space}");
                        if(stockFile.getFileName().toString().toLowerCase().startsWith("analysts"))
                        {
                            alias = words[1];
                            alias = alias.trim();

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
                            int c = 5;
                            while(!words[c].equals("-") && c < words.length-1)
                            {name += words[c] + " "; c++;}
                            name = name.trim();


                            for(int i = 50; i < Math.min(300,words.length);i++)
                            {
                                if(words[i].equals("Date") && words[i-1].equals("Earnings"))
                                {
                                    earningDate = words[i+1]+words[i+2]+words[i+3]+words[i+4]+words[i+5]+words[i+6]+words[i+7];
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

            long end = System.currentTimeMillis();
            System.out.println("done");
            System.out.println(set);
            System.out.println("Process took: " + (end - start)/1000.0 + "seconds");
            System.out.println("Per stock average: " + (end - start)/1000.0/structure.size() + "seconds");

            if(logWriter != null)
            {
                logWriter.close();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
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
