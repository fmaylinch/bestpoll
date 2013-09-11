package may.bestpoll.service;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import may.bestpoll.entities.User;
import org.bson.types.ObjectId;

import static may.bestpoll.util.MongoUtil.getString;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class UserServiceImpl implements UserService
{
	private static final String COLLECTION_NAME = "users";

	private static final String FIELD_FACEBOOK_ID = "fbId";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_ID = "_id";

	private final DBCollection users;

	@Inject
    public UserServiceImpl(DB db)
    {
		this.users = db.getCollection(COLLECTION_NAME);
	}

    @Override
    public void assureUser(User user)
    {
		// TODO: Only using Facebook by now
		DBObject userObj = findUserObjByFacebookId(user.getFacebookId());

		if (userObj == null)
			userObj = createNewUser(user);
		else
			updateUserObjIfNecessary(user, userObj);

		user.setId((ObjectId) userObj.get(FIELD_ID));
    }

	private DBObject findUserObjByFacebookId(String facebookId)
	{
		return users.findOne(new BasicDBObject(FIELD_FACEBOOK_ID, facebookId));
	}

	/** Persists user and returns the userObj created in DB */
	private DBObject createNewUser(User user)
	{
		DBObject userObj;
		final DBObject userObjNew = new BasicDBObject()
			.append(FIELD_FACEBOOK_ID, user.getFacebookId())
			.append(FIELD_NAME, user.getName());
		users.save(userObjNew);
		userObj = userObjNew;
		return userObj;
	}

	/** Updates userObj if user has different values. It only updates name for now. */
	private void updateUserObjIfNecessary(User user, DBObject userObj)
	{
		final String name = getString(userObj, FIELD_NAME);
		if (isEmpty(name) || !name.equals(user.getName()))
		{
			userObj.put(FIELD_NAME, user.getName());
			users.save(userObj);
		}
	}
}
