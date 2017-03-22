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

public class StockDB
{
    public static void main(String args[])
    {
        StockSet set;
        ArgumentMap arg = new ArgumentMap(args);
        int mode = 0;
        if(arg.hasFlag("-b") && arg.hasValue("-b"))
        {
            if(arg.isValid("-l"))
            {
                set = StockBuilder.build(Paths.get(arg.getValue("-b")),Paths.get(arg.getValue("-l")));
            }else{
                set = StockBuilder.build(Paths.get(arg.getValue("-b")));
            }
        }else if(arg.hasFlag("-load"))
        {

        }
    }
}
