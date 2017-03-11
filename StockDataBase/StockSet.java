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

    public int size()
    {
        return stocks.size();
    }

    public String toString()
    {
        String output = "";
        output += "Set at location" + this.hashCode() +  " contains " + size() + " stocks\n" ;
        for(Stock s: stocks)
        {
            output += s.toString() + "\n";
        }
        return output;
    }
}
