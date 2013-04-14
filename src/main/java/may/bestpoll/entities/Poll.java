package may.bestpoll.entities;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import may.bestpoll.entities.base.Identity;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Collection;
import java.util.Set;

@Entity(value = "polls", noClassnameStored = true)
public class Poll extends Identity
{
	@NotEmpty
	private String message;

//	@NotEmpty // TODO: with @NotEmpty there is a validation exception when calling PollResource.create
	@Reference
	private User creator;

	/**
	 * Used this approach: http://cookbook.mongodb.org/patterns/votes/
	 */
	@Reference(lazy = true)
	private Collection<User> voters;

	private Set<String> tags;

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
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
