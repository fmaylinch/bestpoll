package may.bestpoll.service;

import com.google.inject.Inject;

public class SequenceServiceImpl implements SequenceService
{
	@Inject
	public SequenceServiceImpl()
	{
		// TODO
	}

	@Override
	public long getCounter(String name)
	{
/*
		TODO

		Query<Sequence> query = sequenceDAO.createQuery().field("_id").equal(name);

		UpdateOperations<Sequence> update = sequenceDAO.createUpdateOperations().inc("counter");
		Sequence sequence = sequenceDAO.findAndModify(query, update, false, true);

		return sequence.getCounter();
*/
		return 0;
	}

}
