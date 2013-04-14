package may.bestpoll.service;

/**
 * Manages counters
 */
public interface SequenceService
{
	/**
	 * Returns a named counter after incrementing it.
	 * It will be created (with initial value of 1) if it doesn't exist.
	 */
	long getCounter(String name);
}
