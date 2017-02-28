import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.ArrayList;
public class HTMLFilter
{
    private static Path sourcePath;
    private static Path logPath;

    private static String log = "";

    public static String cleanAndFilterInfo(String args[])
    {

        String output = "";
        if(args.length!=2)
            System.out.println("Usage: java HTMLCleaner <Directory of HTMLs> <Output directory>");
        sourcePath = Paths.get(args[0]);
        logPath = Paths.get(args[1]);


        try (
        		DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath);
        		BufferedWriter writer = Files.newBufferedWriter(logPath, StandardCharsets.UTF_8);
        )
        {
            for (Path HTML: stream)
            {
                if(Files.isDirectory(HTML))
                {System.out.println("CheCK");continue;}


            	log += "Filtering " + HTML.toString() + "\n";
                System.out.println("Filtering " + HTML.toString());
            	try(BufferedReader reader = Files.newBufferedReader(HTML, StandardCharsets.UTF_8))
            	{
                    String content = "";
                    String line = "";
                    while((line = reader.readLine()) != null)
                    	content+=line;
                    String EPS = "";
                    String Share = "";
                    String[] words = clean(content).split(" ");
                    log += "\tClean words count: " + words.length + "\n";
                     System.out.println(words.length);
                     for(int i = 0; i <words.length;i++)
                     {
                         if(words[i].equals("Est.") || words[i].equals("Actual"))
                         {
                             System.out.println("Found");
                             output += words[i+1]+","+words[i+2]+","+words[i+3]+","+words[i+4] + ",";
                         }
                     }
                    log += "\tEPS: " + EPS + "\n";
                    log += "\tShares: " + Share + "\n";
            	}
            }
            writer.write(log);
        } catch (IOException | DirectoryIteratorException x)
        {
            x.printStackTrace();
        }

        return output;
    }


    public static String clean(String html)
    {
    	long startTime = System.currentTimeMillis();
    	html = stripComments(html);
    	html = stripElement(html, "head");
		html = stripElement(html, "style");
		html = stripElement(html, "script");
    	html = stripTags(html);
    	html = stripEntities(html);
		html = stripSpaces(html);
		long stopTime = System.currentTimeMillis();
		log += "\tCleaning time took: " + (stopTime - startTime)/1000.0 + " seconds.\n";
		return html;
    }
    public static String stripEntities(String html)
	{return html.replaceAll("&[^\\s]+?;", " ");}

	public static String stripComments(String html)
	{return html.replaceAll("(?s)<!-.*?->", " ");}

	public static String stripTags(String html)
	{return html.replaceAll("<[^>]*?>", " ");}

	public static String stripElement(String html, String name)
	{return html.replaceAll("(?s)(?i)<"+name+".*?</"+name+".*?>", " ");}

	public static String stripSpaces(String html)
	{return html.replaceAll("\\p{Space}+", " ");}

}
