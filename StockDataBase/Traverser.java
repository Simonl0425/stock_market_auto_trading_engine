import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Traverser
{
	private static Logger log = LogManager.getLogger();

	public static HashSet<HashSet<Path>> get(Path root) throws IOException
	{
		HashSet<HashSet<Path>> structure = new HashSet<>();

		long startTime = System.currentTimeMillis();

		DirectoryStream<Path> stream_outer = Files.newDirectoryStream(root);
		int size = new File(root.toString()).listFiles().length;
		log.info("Traversing through " + root + "(" + size + ")...");
		for (Path po: stream_outer)
		{
			DirectoryStream<Path> stream_inner = Files.newDirectoryStream(po);
			HashSet<Path> innerStructure = new HashSet<>();
			for (Path pi: stream_inner)
			{
				innerStructure.add(pi);
			}
			stream_inner.close();
			structure.add(innerStructure);
		}
		stream_outer.close();

		long endTime = System.currentTimeMillis();
		log.info("Done, process took " + (endTime - startTime) / 1000.0 + "seconds \n");

		return structure;
	}

}
