package may.bestpoll.service;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import may.bestpoll.entities.User;
import org.bson.types.ObjectId;

public class UserServiceImpl implements UserService
{
	private static final String COLLECTION_NAME = "users";

	private final DBCollection users;

	@Inject
    public UserServiceImpl(DB db)
    {
		this.users = db.getCollection(COLLECTION_NAME);
	}

    @Override
    public User assureUser(User user)
    {
		// TODO: Only using Facebook by now
		DBObject userObjectToFind = new BasicDBObject("fbId", user.getFacebookId());
		DBObject userObjectFound = users.findOne(userObjectToFind);

		// If user not found, add name and save
		if (userObjectFound == null)
		{
			userObjectToFind.put("name", user.getName());
			users.save(userObjectToFind);
			userObjectFound = userObjectToFind;
		}

		user.setId((ObjectId) userObjectFound.get("_id"));
		return user;
    }
}
