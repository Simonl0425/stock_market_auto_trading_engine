import java.util.ArrayList;
import java.nio.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class repetitionTester
{
    public static void main(String args[])
    {
        args[0] = "-p";
        args[1] = "../test/Nasdaq20";
        args[2] = "-l";
        args[3] = "../log.txt";
        System.out.println("With logging: ");
        int it = 50;
        long sum = 0;
        for(int i = 0; i < it;i++)
        {
            System.out.println("On iteration " + i);
            StockBuilder.build(args);
        }
        double logTime = (sum/1000.0/it);
        System.out.println("\nAvg logging time: " + logTime);

        args[0] = "-p";
        args[1] = "../test/Nasdaq20";
        args[2] = "";
        args[3] = "";
        System.out.println("Without logging: ");
        sum = 0;
        for(int i = 0; i < it;i++)
        {
            System.out.println("On iteration " + i);
            StockBuilder.build(args);
        }

        System.out.println("\nAvg logging time: " + logTime);
        System.out.println("\nAvg no logging time: " + sum/1000.0/it);


    }
}
