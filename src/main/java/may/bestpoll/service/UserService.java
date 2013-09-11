package may.bestpoll.service;

import may.bestpoll.entities.User;
import org.bson.types.ObjectId;

public interface UserService
{
	/**
	 * Assures that user is present in the DB. After the call, it will have the id.
	 *
	 * @param user  must have facebookId and name (TODO: later we will support googleId)
	 */
    void assureUser(User user);
}
