package may.bestpoll.service;

import com.google.inject.Inject;
import may.bestpoll.dao.QuestionDao;
import may.bestpoll.entities.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class QuestionServiceImpl implements QuestionService
{
	private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

	private final QuestionDao questionDao;

	@Inject
	public QuestionServiceImpl(QuestionDao questionDao)
	{
		this.questionDao = questionDao;
	}

	@Override
	public long create(Question question)
	{
		questionDao.create(question);

		return question.getId();
	}

	@Override
	public Question findById(long id)
	{
		return questionDao.findById(id);
	}

	@Override
	public Collection<Question> findLatest(int offset, int limit)
	{
		if (limit > MAX_RESULT_SIZE)
			throw new IllegalArgumentException("limit can't be greater than " + MAX_RESULT_SIZE);

		// TODO
//		return pollDao.createQuery().order("-date").offset(offset).limit(limit).asList();

		return null;
	}
}
