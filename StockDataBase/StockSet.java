import java.io.*;
import java.nio.*;
import java.nio.file.Path;
import java.util.*;

public class StockSet
{
    private HashSet<Stock> stocks;

    public StockSet()
    {
        stocks = new HashSet<>();
    }

    public boolean add(Stock stock)
    {
        return stocks.add(stock);
    }

    public Iterator<Stock> getIterator()
    {
        return stocks.iterator();
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
        int pathCnt = 0;
        int edCnt = 0;
        int rangeCnt = 0;
        int epsCnt = 0;


        for(Stock s: stocks)
        {
            if(s == null){System.out.println("Invalid Stock");continue;}
            output += s.toString() + "\n";
            if(!s.isValid())invalidCnt++;

            if(s.FAILS.contains("PATH"))pathCnt++;
            if(s.FAILS.contains("ED"))edCnt++;
            if(s.FAILS.contains("RANGE"))rangeCnt++;
            if(s.FAILS.contains("EPS"))epsCnt++;
        }

        output += "\nTotal of " + size() + " stocks, " + invalidCnt + " are invalid, " + pathCnt + " are caused by bad csv Path, " + epsCnt + " are caused by bad EPS capture.";
        return output;
    }
}
