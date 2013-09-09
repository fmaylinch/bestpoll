package may.bestpoll.service;

import may.bestpoll.entities.User;
import org.bson.types.ObjectId;

public interface UserService
{
	/**
	 * Gets or creates a user.
	 *
	 * @param user  must come with facebookId and name (TODO: later we will support googleId)
	 *
	 * @return complete user with id
	 */
    User assureUser(User user);
}
