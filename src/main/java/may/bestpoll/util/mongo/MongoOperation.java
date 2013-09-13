package may.bestpoll.util.mongo;

/**
 * MongoDB operations.
 *
 * The list is incomplete; we will be adding the ops we use.
 */
public enum MongoOperation
{
	/** Adds an element to an array */
	PUSH,

	/** Increments the specified amount */
	INC;

	/** Operator keyword used in MongoDB calls */
	public final String op;

	private MongoOperation()
	{
		this.op = "$" + this.name().toLowerCase(); // Usual form of MongoDB operators
	}

	MongoOperation(String op)
	{
		this.op = op;
	}
}
