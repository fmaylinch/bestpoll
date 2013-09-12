package may.bestpoll.service;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import may.bestpoll.entities.Answer;
import may.bestpoll.entities.Question;
import may.bestpoll.exception.FindTheBestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static may.bestpoll.util.MongoUtil.get;
import static may.bestpoll.util.MongoOperation.PUSH;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class QuestionServiceImpl implements QuestionService
{
	private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

	private static final String COLLECTION_NAME = "questions";

	private static final String FIELD_ID = "_id";
	private static final String FIELD_LOCATION = "loc";
	private static final String FIELD_MESSAGE = "msg";
	private static final String FIELD_TEXT = "text";
	private static final String FIELD_URL = "url";
	private static final String FIELD_CREATOR = "user";
	private static final String FIELD_VOTES = "votes";
	private static final String FIELD_ANSWERS = "ans";

	private final DBCollection questions;
	private final SequenceService sequenceService;

	@Inject
	public QuestionServiceImpl(DB db, SequenceService sequenceService)
	{
		this.questions = db.getCollection(COLLECTION_NAME);
		this.sequenceService = sequenceService;
	}

	@Override
	public void create(Question question)
	{
		question.setId(sequenceService.getCounter(COLLECTION_NAME));

		DBObject questionObj = new BasicDBObject()
				.append(FIELD_ID, question.getId())
				.append(FIELD_CREATOR, question.getCreator().getId())
				.append(FIELD_LOCATION, question.getLocation())
				.append(FIELD_MESSAGE, question.getMessage());

		questions.save(questionObj);
	}

	@Override
	public void addAnswer(Answer answer)
	{
		final BasicDBObject questionId = new BasicDBObject(FIELD_ID, answer.getQuestion().getId());
		DBObject questionObj = questions.findOne(questionId);

		// Check question exists
		if (questionObj == null)
			throw new FindTheBestException("Question " + answer.getQuestion().getId() + " not found"); // TODO: improve

		List<DBObject> answerObjs = get(questionObj, FIELD_ANSWERS);
		if (answerObjs == null)
			answerObjs = new ArrayList<DBObject>();

		// Check answer doesn't exist
		for (DBObject answerObj : answerObjs)
			if (answer.getText().equals(answerObj.get(FIELD_TEXT)))
				throw new FindTheBestException("Question " + answer.getQuestion().getId() + " already contains answer: " + answer.getText()); // TODO: Improve

		final DBObject newAnswerObj = new BasicDBObject()
				.append(FIELD_TEXT, answer.getText())
				.append(FIELD_CREATOR, answer.getCreator().getId())
				.append(FIELD_VOTES, 0);
		if (isNotEmpty(answer.getUrl()))
			newAnswerObj.put(FIELD_URL, answer.getUrl());

		questions.update(questionId, new BasicDBObject(PUSH.op, new BasicDBObject(FIELD_ANSWERS, newAnswerObj)));
	}

	@Override
	public Collection<Question> findLatest(int offset, int limit)
	{
		return null;  // TODO: how to store and retrieve question data (related to: how keep track of votes?)
	}
}
