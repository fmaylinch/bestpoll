package may.bestpoll.service;

import may.bestpoll.entities.Poll;
import org.bson.types.ObjectId;

import java.util.Collection;

public interface PollService
{
	int MAX_RESULT_SIZE = 100;

	/**
	 * Creates a poll and returns its id.
	 */
	ObjectId create(Poll poll);

	/**
	 * Returns the poll with the given id, or null if it doesn't exist.
	 */
	Poll findById(String id);

	/**
	 * Returns the latest polls (sorted by date, descending).
	 *
	 * @param offset number of polls to skip
	 * @param limit number of polls to return (can't be greater than {@link #MAX_RESULT_SIZE}).
	 */
	Collection<Poll> findLatest(int offset, int limit);
}
