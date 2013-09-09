package may.bestpoll.service;

import may.bestpoll.entities.Question;

import java.util.Collection;

public interface QuestionService
{
	int MAX_RESULT_SIZE = 100;

	/**
	 * Creates a question and returns its id.
	 */
	long create(Question question);

	/**
	 * Returns the question with the given id, or null if it doesn't exist.
	 */
	Question findById(long id);

	/**
	 * Returns the latest questions (sorted by date, descending).
	 *
	 * @param offset number of questions to skip
	 * @param limit number of questions to return (can't be greater than {@link #MAX_RESULT_SIZE}).
	 */
	Collection<Question> findLatest(int offset, int limit);
}
