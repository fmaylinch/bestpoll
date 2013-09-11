package may.bestpoll.util;

import com.mongodb.DBObject;

import java.util.List;

public class MongoUtil
{
	/** Adds an element to an array */
	public static final String OP_PUSH = "$push";

	@SuppressWarnings("unchecked")
	public static <E> List<E> getList(DBObject obj, String field)
	{
		return (List<E>) obj.get(field);
	}

	@SuppressWarnings("unchecked")
	public static String getString(DBObject obj, String field)
	{
		return (String) obj.get(field);
	}
}
