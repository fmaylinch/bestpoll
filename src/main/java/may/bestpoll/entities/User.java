package may.bestpoll.entities;


import org.bson.types.ObjectId;

public class User
{
	private ObjectId id;

    private String facebookId;

    private String googleId;

	private String name;


	public ObjectId getId()
	{
		return id;
	}

	public void setId(ObjectId id)
	{
		this.id = id;
	}

	public String getFacebookId()
    {
        return facebookId;
    }

    public void setFacebookId(String facebookId)
    {
        this.facebookId = facebookId;
    }

	public String getGoogleId()
	{
		return googleId;
	}

	public void setGoogleId(String googleId)
	{
		this.googleId = googleId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}