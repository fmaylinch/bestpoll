package may.bestpoll.entities;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import may.bestpoll.entities.base.DatedObject;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Collection;
import java.util.Set;

@Entity(value = "polls", noClassnameStored = true)
public class Poll extends DatedObject
{
	@Id
	private ObjectId id;

	@NotEmpty
	private String message;

	private String permalink;

	//	@NotEmpty // TODO: with @NotEmpty there is a validation exception when calling PollResource.create
	@Reference
	private User creator;

	/**
	 * Used this approach: http://cookbook.mongodb.org/patterns/votes/
	 */
	@Reference(lazy = true)
	private Collection<User> voters;

	private Set<String> tags;


	public ObjectId getId()
	{
		return id;
	}

	public void setId(ObjectId id)
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

	public String getPermalink()
	{
		return permalink;
	}

	public void setPermalink(String permalink)
	{
		this.permalink = permalink;
	}

	public User getCreator()
	{
		return creator;
	}

	public void setCreator(User creator)
	{
		this.creator = creator;
	}

	public Collection<User> getVoters()
	{
		return voters;
	}

	public void setVoters(Collection<User> voters)
	{
		this.voters = voters;
	}

	public Set<String> getTags()
	{
		return tags;
	}

	public void setTags(Set<String> tags)
	{
		this.tags = tags;
	}
}
