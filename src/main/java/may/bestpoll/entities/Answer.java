package may.bestpoll.entities;

public class Answer
{
	private String text;

	private String url;

	private User creator;

	private Question question;

	private int votes;

	private int yourVote;


	public Answer()
	{
	}


	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public User getCreator()
	{
		return creator;
	}

	public void setCreator(User creator)
	{
		this.creator = creator;
	}

	public Question getQuestion()
	{
		return question;
	}

	public void setQuestion(Question question)
	{
		this.question = question;
	}

	public int getVotes()
	{
		return votes;
	}

	public void setVotes(int votes)
	{
		this.votes = votes;
	}

	public int getYourVote()
	{
		return yourVote;
	}

	public void setYourVote(int yourVote)
	{
		this.yourVote = yourVote;
	}
}
