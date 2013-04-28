package may.bestpoll.service;

import com.google.inject.Inject;
import may.bestpoll.dao.Dao;
import may.bestpoll.entities.Poll;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class PollServiceImpl implements PollService
{
	private static final Logger logger = LoggerFactory.getLogger(PollServiceImpl.class);

	private final Dao<Poll, ObjectId> pollDao;

	@Inject
	public PollServiceImpl(Dao<Poll, ObjectId> pollDao)
	{
		this.pollDao = pollDao;
	}

	@Override
	public String create(Poll poll)
	{
		// TODO: Avoid duplicates
		poll.setPermalink(formatForUrl(poll.getMessage()));

		pollDao.save(poll);
		return poll.getId().toString();
	}

	private String formatForUrl(String text)
	{
		return text.toLowerCase().replaceAll("[^ a-z0-9]", "").replace(' ', '-');
	}

	@Override
	public Poll findById(String id)
	{
		return pollDao.findOne("_id", new ObjectId(id));
	}

	@Override
	public Collection<Poll> findLatest(int offset, int limit)
	{
		if (limit > MAX_RESULT_SIZE)
			throw new IllegalArgumentException("limit can't be greater than " + MAX_RESULT_SIZE);

		return pollDao.createQuery().order("-date").offset(offset).limit(limit).asList();
	}
}
