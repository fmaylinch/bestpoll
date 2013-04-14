package may.bestpoll.service;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.inject.Inject;
import may.bestpoll.dao.Dao;
import may.bestpoll.entities.Sequence;
import org.bson.types.ObjectId;

public class SequenceServiceImpl implements SequenceService
{
	private final Dao<Sequence, ObjectId> sequenceDAO;

	@Inject
	public SequenceServiceImpl(Dao<Sequence, ObjectId> sequenceDAO)
	{
		this.sequenceDAO = sequenceDAO;
	}

	@Override
	public long getCounter(String name)
	{
		Query<Sequence> query = sequenceDAO.createQuery().field("_id").equal(name);

		UpdateOperations<Sequence> update = sequenceDAO.createUpdateOperations().inc("counter");
		Sequence sequence = sequenceDAO.findAndModify(query, update, false, true);

		return sequence.getCounter();
	}

}
