package may.bestpoll.service;

import may.bestpoll.entities.Answer;
import may.bestpoll.entities.Question;

import java.util.Collection;

public interface QuestionService
{
	int MAX_RESULT_SIZE = 100;

	/**
	 * Creates a question.
	 *
	 * @param question  must come with message, [location], creator.id. After the call it will have id.
	 */
	void create(Question question);

	/**
	 * Creates an answer.
	 *
	 * @param answer  must come with creator.id, question.id, text (must not already exist in question), [url].
	 */
	void addAnswer(Answer answer);

	/**
	 * Returns latest questions
	 *
	 * @param offset  0 to start from the latest question
	 * @param limit   maximum number of questions to retrieve (1 <= limit <= {@link #MAX_RESULT_SIZE})
	 */
	Collection<Question> findLatest(int offset, int limit);
}
