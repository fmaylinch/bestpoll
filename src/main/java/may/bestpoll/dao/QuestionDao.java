package may.bestpoll.dao;

import may.bestpoll.entities.Question;

public interface QuestionDao
{
	void create(Question question);

	Question findById(long id);
}
