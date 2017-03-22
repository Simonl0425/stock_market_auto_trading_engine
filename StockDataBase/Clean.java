import java.nio.file.Paths;
public class Clean
{
    public static void main(String args[])
    {
        try{System.out.println(HTMLCleaner.clean(Paths.get(args[0])));}
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
