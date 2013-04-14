package may.bestpoll.service;

import com.google.inject.Inject;
import may.bestpoll.dao.Dao;
import may.bestpoll.entities.User;
import org.bson.types.ObjectId;

public class UserServiceImpl implements UserService
{
    private final Dao<User, ObjectId> userDAO;

    @Inject
    public UserServiceImpl(Dao<User, ObjectId> userDAO)
    {
        this.userDAO = userDAO;
	}

    @Override
    public void create(User user)
    {
		final User existingUser = userDAO.findOne("_id", user.getUsername());

		if (existingUser != null) // TODO: Which kind of exception here?
			throw new IllegalArgumentException("User already exists: " + user.getUsername());

		userDAO.save(user);
    }

	@Override
    public User findByUsername(String username)
    {
        return userDAO.findOne("_id", username);
    }
}
