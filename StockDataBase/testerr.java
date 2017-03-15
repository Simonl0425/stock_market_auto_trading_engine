import java.util.ArrayList;
import java.nio.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class testerr
{
    public static void main(String args[])
    {
        Path path = Paths.get(args[0]);

        try{System.out.println(HTMLCleaner.clean(path));}
        catch(Exception e)
        {e.printStackTrace();}

    }
}
