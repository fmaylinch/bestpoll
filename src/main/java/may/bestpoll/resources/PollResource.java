package may.bestpoll.resources;

import com.google.inject.Inject;
import may.bestpoll.entities.Poll;
import may.bestpoll.entities.User;
import may.bestpoll.service.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Collection;

@Path("/poll")
@Produces(MediaType.APPLICATION_JSON)
public class PollResource
{
	private static final Logger logger = LoggerFactory.getLogger(PollResource.class);

	private PollService pollService;

	@Inject
	public PollResource(PollService pollService)
	{
		this.pollService = pollService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@Valid Poll poll)
	{
		pollService.create(poll);
		return Response.created(UriBuilder.fromResource(PollResource.class).build(poll.getId())).build();
	}

	@GET
	@Path("/{id}")
	public Poll findById(@PathParam("id") String id)
	{
		Poll poll = pollService.findById(id);

		if (poll == null)
		{
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		return poll;
	}

	@GET
	public Collection<Poll> findLatest(
			@QueryParam("offset") @DefaultValue("0") int offset,
			@QueryParam("limit") @DefaultValue("10") int limit)
	{
		return pollService.findLatest(offset,  limit);
	}
}