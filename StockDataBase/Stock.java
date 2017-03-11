import java.nio.file.Path;
import java.util.ArrayList;
public class Stock implements Comparable<Stock>
{
    private Path dailyPriceCSV;
    private final String NAME;
    private final String ALIAS;
    private String earningDate;
    private String EPS_text;
    private EPS eps;
    private String range52;
    private Double year_High;
    private Double year_Low;
    private Double year_Average;
    private Double monthAverage;
    private Double weekAverage;
    private Double dualWeekAverage;

    public Stock(String alias, String name, String earningDate, String range52, String EPS_text, Path csvPath)
    {
        ALIAS = alias;
        NAME = name;
        this.earningDate = earningDate;
        this.range52 = range52;
        this.EPS_text = EPS_text;
        this.dailyPriceCSV = csvPath;
        setEPS();
    }

    @Override
    public String toString()
    {
        String output = "";
        output += "=============================================\n" + NAME + "|" + ALIAS + "\n";
        output += "ED: " + earningDate + "\n";
        output += "Range: " + range52 + "\n";
        output += "EPS: " + EPS_text + "\n";
        output += "CSV point at: " + dailyPriceCSV.toString();
        output += "\n============================================\n";
        return output;
    }

    public int compareTo(Stock stock)
    {
        return ALIAS.compareTo(stock.ALIAS);
    }

    private void setEPS()
    {
        ArrayList<Double> doubles = new ArrayList<>();
        String split[]  = EPS_text.split(",");
        for(String s: split)
        {
            if(s.equals("N/A"))
            {
                doubles.add(null);
            }else{
                doubles.add(Double.parseDouble(s));
            }
        }
        eps = new EPS(doubles);
    }

}
