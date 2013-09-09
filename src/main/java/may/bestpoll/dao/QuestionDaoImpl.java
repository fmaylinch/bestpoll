package may.bestpoll.dao;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import may.bestpoll.entities.Question;
import may.bestpoll.entities.User;
import may.bestpoll.service.SequenceService;
import org.bson.types.ObjectId;

public class QuestionDaoImpl implements QuestionDao
{
	private static final String COLLECTION_NAME = "questions";

	private final DBCollection questions;
	private final SequenceService sequenceService;

	@Inject
	public QuestionDaoImpl(DB db, SequenceService sequenceService)
	{
		this.questions = db.getCollection(COLLECTION_NAME);
		this.sequenceService = sequenceService;
	}

	@Override
	public void create(Question question)
	{
		question.setId(sequenceService.getCounter(COLLECTION_NAME));

		DBObject questionObj = new BasicDBObject();

		questionObj.put("_id", question.getId());
		questionObj.put("user", question.getCreator().getId());
		questionObj.put("loc", question.getLocation());
		questionObj.put("msg", question.getMessage());

		questions.save(questionObj);
	}

	@Override
	public Question findById(long id)
	{
		DBObject objectId = new BasicDBObject("_id", id);

		DBObject questionObj = questions.findOne(objectId);

		ObjectId userId = (ObjectId) questionObj.get("user");

		// TODO: How to handle different ways of getting data?
		// Should the service/dao provide different methods for that?
		// The data retrieved depends on what we need it for (for the view? for a service?)

		User user = null; // TODO: Use UserDao to get User?

		Question question = new Question(
				(Long) questionObj.get("_id"),
				(String) questionObj.get("msg"),
				user);

		return question;
	}
}
