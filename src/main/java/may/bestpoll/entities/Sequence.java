package may.bestpoll.entities;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity(value="sequences", noClassnameStored=true)
public class Sequence
{
	@Id
	private String name;

	private Long counter;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Long getCounter()
	{
		return counter;
	}

	public void setCounter(Long counter)
	{
		this.counter = counter;
	}
}
