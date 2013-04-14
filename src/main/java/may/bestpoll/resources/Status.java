package may.bestpoll.resources;

public class Status
{
	private boolean ok;

	private String message;

	public Status(boolean ok, String message)
	{
		this.ok = ok;
		this.message = message;
	}

	public boolean isOk()
	{
		return ok;
	}

	public String getMessage()
	{
		return message;
	}
}
