package may.bestpoll.entities.base;

import com.google.code.morphia.annotations.PrePersist;

import java.util.Date;

public class DatedObject
{
	private Date date;

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	@PrePersist
	void prePersist()
	{
		date = new Date();
	}
}
