package may.bestpoll.entities;

import java.util.List;

// TODO: Check this pattern for votes: http://cookbook.mongodb.org/patterns/votes/

public class Question
{
	private long id;

	private String message;

	private String location; // TODO: improve: store real location like city, country or anywhere

	private List<Option> options;

	private User creator;


	public Question(long id, String message, User creator)
	{
		this.id = id;
		this.message = message;
		this.creator = creator;
	}


	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public List<Option> getOptions()
	{
		return options;
	}

	public void setOptions(List<Option> options)
	{
		this.options = options;
	}

	public User getCreator()
	{
		return creator;
	}

	public void setCreator(User creator)
	{
		this.creator = creator;
	}
}
