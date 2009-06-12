package temp;

public class Temp
{
	private static long count = 0;
    private long number;
    
    public Temp()
    {
        super();
        
        number = count++;
    }

    public String toString()
    {
        return "t" + number;
    }
}