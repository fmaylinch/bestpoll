package may.bestpoll.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource
{
	private static final Logger logger = LoggerFactory.getLogger(StatusResource.class);

	@GET
	public Status getStatus()
	{
		return new Status(true, "API is up!");
	}
}