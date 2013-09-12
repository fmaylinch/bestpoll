package may.bestpoll.exception;

/**
 * Generic exception for the application
 */
public class FindTheBestException extends RuntimeException
{
	public FindTheBestException()
	{
	}

	public FindTheBestException(String message)
	{
		super(message);
	}

	public FindTheBestException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FindTheBestException(Throwable cause)
	{
		super(cause);
	}

	public FindTheBestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
