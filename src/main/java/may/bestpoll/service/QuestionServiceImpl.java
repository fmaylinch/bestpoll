package may.bestpoll.service;

import com.google.inject.Inject;
import com.mongodb.*;
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
	private static final String FIELD_POINTS = "pnt";
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
		question.setCreated(new Date());

		final DBObject questionObj = new BasicDBObject()
				.append(FIELD_ID, question.getId())
				.append(FIELD_CREATOR, question.getCreator().getId())
				.append(FIELD_LOCATION, question.getLocation())
				.append(FIELD_MESSAGE, question.getMessage())
				.append(FIELD_CREATED, question.getCreated());

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
				.append(FIELD_POINTS, 0);
		if (isNotEmpty(answer.getUrl()))
			newAnswerObj.put(FIELD_URL, answer.getUrl());

		answersCol.insert(newAnswerObj);
	}

	@Override
	public Collection<Question> findLatest(int offset, int limit)
	{
		DBObject orderByCreatedDesc = new BasicDBObject(FIELD_CREATED, -1);

		final List<DBObject> questionObjs = questionsCol.find().sort(orderByCreatedDesc).skip(offset).limit(limit).toArray();
		final List<Question> questions = FunctionalUtils.map(questionObjs, questionMongo2Java);
		fillAnswers(questions);

		return questions;
	}

	/** Fills question with its answers */
	private void fillAnswers(List<Question> questions)
	{
		for (Question question : questions)
		{
			final List<DBObject> answerObjs = findAnswerObjs(question.getId()).toArray();
			final List<Answer> answers = FunctionalUtils.map(answerObjs, answerMongo2Java);
			question.setAnswers(answers);
		}
	}

	private DBCursor findAnswerObjs(int questionId)
	{
		return answersCol.find(new BasicDBObject(FIELD_QUESTION_ID, questionId));
	}

	private Object findQuestionObj(int questionId)
	{
		return questionsCol.findOne(new BasicDBObject(FIELD_ID, questionId));
	}

	private Object findAnswerObj(long questionId, String answerText)
	{
		return answersCol.findOne(new BasicDBObject()
				.append(FIELD_QUESTION_ID, questionId)
				.append(FIELD_TEXT, answerText));
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

	private static final F<DBObject,Answer> answerMongo2Java = new F<DBObject, Answer>()
	{
		@Override
		public Answer f(DBObject obj)
		{
			Answer result = new Answer();
			result.setId((Integer) obj.get(FIELD_ID));
			result.setQuestion(new Question((Integer) obj.get(FIELD_QUESTION_ID)));
			result.setCreator(new User((ObjectId) obj.get(FIELD_CREATOR))); // TODO: Store user name also in answers collection?
			result.setText((String) obj.get(FIELD_TEXT));
			result.setUrl((String) obj.get(FIELD_URL));
			result.setPoints((Integer) obj.get(FIELD_POINTS));
			result.setYourVote(0); // TODO: We need the user to calculate this value
			return result;
		}
	};
}
