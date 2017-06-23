
public class Flipped 
{
	
	boolean isFlipped;
	public Flipped()
	{
		isFlipped = false;
	}
	public void flip()
	{
		isFlipped = !isFlipped;
	}
	public boolean getFlip()
	{
		return isFlipped;
	}
	public boolean equals(Flipped other)
	{
		if(other.getFlip()==getFlip())
			return true;
		return false;
	}
}
