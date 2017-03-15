import java.util.*;


public class ArgumentMap {


	private HashMap<String,String> map;

	public ArgumentMap()
	{
		map = new HashMap<>();
	}

	public ArgumentMap(String[] args)
	{
		this();
		parse(args);
	}


	public int numFlags()
	{
		return map.size();
	}

	public boolean hasFlag(String flag)
	{
		return map.containsKey(flag);
	}

	public boolean hasValue(String flag)
	{
		return !(map.get(flag) == null);
	}

	public void parse(String[] args)
	{
		for(int i = 0; i < args.length;i++)
		{
			String input = args[i];
			if(isFlag(input))
			{
				map.put(input, null);
			}else if(isValue(input) && i != 0 && isFlag(args[i-1])){
				map.put(args[i-1], input);
			}
		}
	}

	private boolean isFlag(String input)
	{
		if(input == null)
			return false;
		else
			return (input.startsWith("-")) && (input.length() >= 2) && (input.indexOf(" ") == -1);
	}

	private boolean isValue(String input)
	{
		if(input == null)
			return false;
		else
			return (!input.startsWith("-")) && (input.length() >= 1) && !(input.startsWith(" ")) && !input.contains("\t");
	}

	public String getValue(String flag)
	{
		return map.get(flag);
	}


	public String toString()
	{
		String output = "";
		for(String key: map.keySet())
		{
			output += "Flag: " + key + "  Value: " + map.get(key) + "\n";
		}
		return output;
	}
}
