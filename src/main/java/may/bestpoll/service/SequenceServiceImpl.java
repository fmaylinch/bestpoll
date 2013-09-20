package may.bestpoll.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import static may.bestpoll.util.mongo.MongoUtil.get;
import static may.bestpoll.util.mongo.MongoOperation.INC;

@Singleton
public class SequenceServiceImpl implements SequenceService
{
	private static final String COLLECTION_NAME = "sequences";

	private static final String FIELD_ID = "_id";
	private static final String FIELD_COUNTER = "counter";

	private final DBCollection sequences;

	@Inject
	public SequenceServiceImpl(DB db)
	{
		this.sequences = db.getCollection(COLLECTION_NAME);
	}

	@Override
	public int getCounter(String name)
	{
		final BasicDBObject query = new BasicDBObject(FIELD_ID, name);
		final BasicDBObject update = new BasicDBObject(INC.op, new BasicDBObject(FIELD_COUNTER, 1));

		final DBObject counter = sequences.findAndModify(query, null, null, false, update, true, true);

		return get(counter, FIELD_COUNTER);
	}

}
