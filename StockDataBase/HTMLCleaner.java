import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class HTMLCleaner
{
    private static Path path;
    private static Path logPath;
    private static String content = "";
    private static String log;

    private static BufferedWriter logWriter;
    private static BufferedReader fileReader;

    public static String clean(Path path, Path logPath) throws IOException, IllegalArgumentException
    {
        if(Files.isDirectory(path) || Files.isHidden(path) || !Files.isReadable(path) || !Files.isWritable(logPath))
        {throw new IllegalArgumentException();}

        log = "";
        logWriter = Files.newBufferedWriter(logPath,StandardCharsets.UTF_8);
        fileReader = Files.newBufferedReader(path,StandardCharsets.UTF_8);
        log += "Cleaning " + path.toString() + "\n";


        String line = "";
        while((line = fileReader.readLine()) != null)
        {content+=line;}

        long startTime = System.currentTimeMillis();
        content = clean(content);
        long endTime = System.currentTimeMillis();
        log += "\tCleaning took: " + (endTime - startTime)/1000.0 + " seconds\n";

        logWriter.append(log);
        logWriter.flush();

        return content;
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
	{return html.replaceAll("(?s)<!-.*?->", " ");}

	public static String stripTags(String html)
	{return html.replaceAll("<[^>]*?>", " ");}

	public static String stripElement(String html, String name)
	{return html.replaceAll("(?s)(?i)<"+name+".*?</"+name+".*?>", " ");}

	public static String stripSpaces(String html)
	{return html.replaceAll("\\p{Space}+", " ");}

}
