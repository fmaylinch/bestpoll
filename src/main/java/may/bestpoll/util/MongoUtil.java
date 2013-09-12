package may.bestpoll.util;

import com.mongodb.DBObject;

import java.util.List;

public class MongoUtil
{
	/**
	 * Gets the value of a field and casts it, just to avoid the ugly casts.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(DBObject obj, String field)
	{
		return (T) obj.get(field);
	}
}
