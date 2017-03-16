import java.io.*;
import java.nio.*;
import java.nio.file.Path;
import java.util.*;

public class StockSet
{
    private TreeSet<Stock> stocks;

    public StockSet()
    {
        stocks = new TreeSet<>();
    }

    public boolean add(Stock stock)
    {
        return stocks.add(stock);
    }

    public int size()
    {
        return stocks.size();
    }

    public String toString()
    {
        String output = "";
        output += "\nSet at location" + this.hashCode() +  " contains " + size() + " stocks\n\n" ;
        int invalidCnt = 0;
        for(Stock s: stocks)
        {
            if(s == null){System.out.println("Invalid Stock");continue;}
            output += s.toString() + "\n";
            if(!s.isValid())invalidCnt++;
        }



        output += "\nTotal of " + size() + " stocks, " + invalidCnt + " are invalid.";
        return output;


    }
}
