package may.bestpoll.service;

import may.bestpoll.entities.User;
import org.bson.types.ObjectId;

public interface UserService
{
	/**
	 * The user must have a unique username
	 */
    void create(User user);

    User findByUsername(String username);
}
