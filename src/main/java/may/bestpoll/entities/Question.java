package may.bestpoll.entities;

import java.util.Date;
import java.util.List;

// TODO: Check this pattern for votes: http://cookbook.mongodb.org/patterns/votes/

public class Question
{
	private int id;

	private String message;

	private String location; // TODO: improve: store real location like city, country or anywhere

	private Date created;

	private List<Answer> answers;

	private User creator;


	public Question()
	{
	}

	public Question(int id)
	{
		this.id = id;
	}


	public int getId()
	{
		return id;
	}

	public void setId(int id)
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

	public Date getCreated()
	{
		return created;
	}

	public void setCreated(Date created)
	{
		this.created = created;
	}

	public List<Answer> getAnswers()
	{
		return answers;
	}

	public void setAnswers(List<Answer> answers)
	{
		this.answers = answers;
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
