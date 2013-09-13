package may.bestpoll.service;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import may.bestpoll.entities.Answer;
import may.bestpoll.entities.Question;
import may.bestpoll.entities.User;
import may.bestpoll.exception.FindTheBestException;
import may.bestpoll.util.functional.F;
import may.bestpoll.util.functional.FunctionalUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class QuestionServiceImpl implements QuestionService
{
	private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

	// TODO: Mix of questions and answers, how to separate?

	private static final String QUESTIONS_COLLECTION_NAME = "questions";
	private static final String ANSWERS_COLLECTION_NAME = "answers";

	private static final String FIELD_ID = "_id";
	private static final String FIELD_CREATOR = "user";
	private static final String FIELD_LOCATION = "loc";
	private static final String FIELD_MESSAGE = "msg";
	private static final String FIELD_CREATED = "date";
	private static final String FIELD_QUESTION_ID = "q_id";
	private static final String FIELD_TEXT = "text";
	private static final String FIELD_URL = "url";
	private static final String FIELD_VOTES = "votes";
	private static final String FIELD_UP_VOTES = "up";
	private static final String FIELD_DOWN_VOTES = "down";

	private final DBCollection questionsCol;
	private final DBCollection answersCol;
	private final SequenceService sequenceService;


	@Inject
	public QuestionServiceImpl(DB db, SequenceService sequenceService)
	{
		this.questionsCol = db.getCollection(QUESTIONS_COLLECTION_NAME);
		this.answersCol = db.getCollection(ANSWERS_COLLECTION_NAME);
		this.sequenceService = sequenceService;
	}

	@Override
	public void create(Question question)
	{
		question.setId(sequenceService.getCounter(QUESTIONS_COLLECTION_NAME));

		final DBObject questionObj = new BasicDBObject()
				.append(FIELD_ID, question.getId())
				.append(FIELD_CREATOR, question.getCreator().getId())
				.append(FIELD_LOCATION, question.getLocation())
				.append(FIELD_MESSAGE, question.getMessage())
				.append(FIELD_CREATED, new Date());

		questionsCol.insert(questionObj);
	}

	@Override
	public void addAnswer(Answer answer)
	{
		// Check question exists
		if (findQuestionObj(answer.getQuestion().getId()) == null)
			throw new FindTheBestException("Question " + answer.getQuestion().getId() + " not found"); // TODO: improve

		// Check answer doesn't exist
		if (findAnswerObj(answer.getQuestion().getId(), answer.getText()) != null)
			throw new FindTheBestException("Question " + answer.getQuestion().getId() + " already contains answer: " + answer.getText()); // TODO: Improve

		answer.setId(sequenceService.getCounter(ANSWERS_COLLECTION_NAME));

		final DBObject newAnswerObj = new BasicDBObject()
				.append(FIELD_ID, answer.getId())
				.append(FIELD_QUESTION_ID, answer.getQuestion().getId())
				.append(FIELD_TEXT, answer.getText())
				.append(FIELD_CREATOR, answer.getCreator().getId())
				.append(FIELD_VOTES, 0);
		if (isNotEmpty(answer.getUrl()))
			newAnswerObj.put(FIELD_URL, answer.getUrl());

		answersCol.insert(newAnswerObj);
	}

	@Override
	public Collection<Question> findLatest(int offset, int limit)
	{
		DBObject orderByCreatedDesc = new BasicDBObject(FIELD_CREATED, -1);

		List<DBObject> questionObjs = questionsCol.find().sort(orderByCreatedDesc).skip(offset).limit(limit).toArray();

		List<Question> questions = FunctionalUtils.map(questionObjs, questionMongo2Java);

		fillAnswers(questions);

		return questions;

		// Use a date instead of an offset? It's faster. But I can only do that if sorted by date.
		// Not, for example, if sorted by total votes, etc.

		// I think it will be better to separate the questions and answers collections
		// When querying questions a get a bunch (say 10 or 20) and then do an $in to get their answers.
		// Using the 3-query solution I'd have to query the answers 3 times.

		// To add up-votes ('up' field) to an answer (answers are in the 'ans' field):
		//   If we don't know the answer (although better find it by id, not name)
		//     db.questions.update({_id:QUESTION_ID, "ans.id":ANSWER_ID}, {"$push":{"ans.$.up":USER_ID}})
		//   If we know the position of the answer (can we retrieve it when querying?):
		//     db.questions.update({_id:QUESTION_ID}, {"$push":{"ans.ANSWER_POSITION.up":id1}})
		//
		// User $pull instead of $push to remove vote

		// Get answers I voted up:
		//   db.questions.aggregate([ {$unwind:"$ans"}, {$match: {"ans.up":id1}} ])
		// Get answers I voted down:
		//   db.questions.aggregate([ {$unwind:"$ans"}, {$match: {"ans.down":id1}} ])
		// Get answers I didn't vote:
		//   db.questions.aggregate([ {$unwind:"$ans"}, {$match: {"ans.up":{$ne:id1}, "ans.down":{$ne:id1}}} ])
	}

	private void fillAnswers(List<Question> questions)
	{
		for (Question question : questions)
		{
			List<Answer> answers = new ArrayList<Answer>();

			// TODO: query answers collection
			Random random = new Random();
			for (int i = 1; i <= 5; i++)
			{
				Answer dummyAnswer = new Answer();
				dummyAnswer.setPoints(random.nextInt(100));
				dummyAnswer.setText("Dummy answer " + i);
				if (random.nextBoolean())
					dummyAnswer.setUrl("http://javadeveloping.wordpress.com");
				answers.add(dummyAnswer);
			}

			question.setAnswers(answers);
		}
	}

	private Object findQuestionObj(long questionId)
	{
		final BasicDBObject query = new BasicDBObject(FIELD_ID, questionId);
		return questionsCol.findOne(query);
	}

	private Object findAnswerObj(long questionId, String answerText)
	{
		final BasicDBObject query = new BasicDBObject()
				.append(FIELD_QUESTION_ID, questionId)
				.append(FIELD_TEXT, answerText);
		return answersCol.findOne(query);
	}


	private static final F<DBObject,Question> questionMongo2Java = new F<DBObject, Question>()
	{
		@Override
		public Question f(DBObject obj)
		{
			Question result = new Question();
			result.setId((Integer) obj.get(FIELD_ID));
			result.setCreated((Date) obj.get(FIELD_CREATED));
			result.setCreator(new User((ObjectId) obj.get(FIELD_CREATOR))); // TODO: Store user name also in questions collection?
			result.setMessage((String) obj.get(FIELD_MESSAGE));
			result.setLocation((String) obj.get(FIELD_LOCATION));
			return result;
		}
	};
}
