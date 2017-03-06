import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
public class StockBuilder
{
    private static Path root;
    private static Path log;

    public static void main(String args[])
    {
        if(args.length != 2)
        {
            System.err.println("Usage: java StockBuilder <directory> <log.txt path>");
            System.exit(1);
        }

        root = Paths.get(args[0]);
        log = Paths.get(args[1]);

        try
        {
            HashSet<HashSet<Path>> structure = Traverser.get(root,log);


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
                        String words[] = HTMLCleaner.clean(stockFile, log).split("\\s");
                        if(stockFile.getFileName().toString().toLowerCase().startsWith("analysts"))
                        {
                            alias = words[0];
                            int c = 8;
                            while(!words[c].toLowerCase().equals("stock"))
                            {name += words + " "; c++;}
                            for(int i = 10;i < words.length;i++)
                            {
                                if(words[i].equals("Est.") || words[i].equals("Actual"))
                                {
                                    EPS_text += words[i+1] + "," + words[i+2] + "," + words[i+3] + "," + words[i+4] + ",";
                                }
                            }
                        }else if(stockFile.getFileName().toString().toLowerCase().startsWith("summary"))
                        {
                            for(int i = 0; i < words.length;i++)
                            {
                                if(words[i].equals("Date") && words[i-1].equals("Earnings"))
                                {
                                    earningDate = words[i+1]+words[i+2]+words[i+3]+words[i+4]+words[i+5]+words[i+6]+words[i+7];
                                }
                                if(words[i].equals("Range") && words[i-1].equals("Week") && words[i-2].equals("52"))
                                {
                                    range52 = words[i] + words[i+1] + words[i+2];
                                }
                            }
                        }
                    }else if(stockFile.toString().toLowerCase().endsWith(".csv"))
                    {
                        csvPath = Paths.get(stockFile.toString());
                    }else{

                    }
                }
            }



        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
