import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
public class HTMLCleaner
{

    private static PrintWriter logWriter;
    private static BufferedReader fileReader;


    public static String clean(Path path, Path logPath) throws IOException, IllegalArgumentException
    {
        String log = "";

        BufferedWriter bw = new BufferedWriter(new FileWriter(logPath.toString(), true));
        logWriter = new PrintWriter(bw);
        fileReader = Files.newBufferedReader(path,StandardCharsets.UTF_8);
        log += "\tCleaning " + path.toString() + "\n";
        long startTime = System.currentTimeMillis();

        StringBuilder content = new StringBuilder();
        String line = "";
        while((line = fileReader.readLine()) != null)
        {content.append(line);}


        content = new StringBuilder(clean(content.toString()));
        long endTime = System.currentTimeMillis();
        log += "\t\tCleaning took: " + (endTime - startTime)/1000.0 + " seconds\n";

        logWriter.write(log);
        logWriter.flush();
        fileReader.close();

        return content.toString();
    }








    public static String clean(String html)
    {
    	html = stripComments(html);
    	html = stripElement(html, "head");
		html = stripElement(html, "style");
		html = stripElement(html, "script");
    	html = stripTags(html);
    	html = stripEntities(html);
		html = stripSpaces(html);
        return html;
    }
    public static String stripEntities(String html)
	{return html.replaceAll("&[^\\s]+?;", " ");}

	public static String stripComments(String html)
	{return html.replaceAll("(?s)<!-.+?->", " ");}

	public static String stripTags(String html)
	{return html.replaceAll("<[^>]+?>", " ");}

	public static String stripElement(String html, String name)
	{return html.replaceAll("(?s)(?i)<"+name+".+?</"+name+".+?>", " ");}

	public static String stripSpaces(String html)
	{return html.replaceAll("\\p{Space}+", " ");}

}
