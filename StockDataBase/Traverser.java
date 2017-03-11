import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.nio.file.DirectoryStream;
import java.io.File;
public class Traverser
{
    private static Path root;
    private static Path log;

    private static PrintWriter logWriter;

    public static HashSet<HashSet<Path>> get(Path root, Path logPath) throws IOException
    {
        String log = "";
        HashSet<HashSet<Path>> structure = new HashSet<>();
        BufferedWriter bw = new BufferedWriter(new FileWriter(logPath.toString(), true));
        logWriter = new PrintWriter(bw);

        long startTime = System.currentTimeMillis();


        DirectoryStream<Path> stream_outer = Files.newDirectoryStream(root);
        int size = new File(root.toString()).listFiles().length;
        log += "Traversing through " + root + "("+ size + ")...";
        for(Path po: stream_outer)
        {
            DirectoryStream<Path> stream_inner = Files.newDirectoryStream(po);
            HashSet<Path> innerStructure = new HashSet<>();
            for(Path pi: stream_inner)
            {
                innerStructure.add(pi);
            }
            stream_inner.close();
            structure.add(innerStructure);
        }
        stream_outer.close();

        long endTime = System.currentTimeMillis();
        log += "Done, process took " + (endTime - startTime)/1000.0 + "seconds \n";
        logWriter.write(log);
        logWriter.flush();

        return structure;
    }


}
