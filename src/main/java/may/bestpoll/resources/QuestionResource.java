package may.bestpoll.resources;

import com.google.inject.Inject;
import may.bestpoll.entities.Question;
import may.bestpoll.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Collection;

@Path("/poll")
@Produces(MediaType.APPLICATION_JSON)
public class QuestionResource
{
	private static final Logger logger = LoggerFactory.getLogger(QuestionResource.class);

	private QuestionService questionService;

	@Inject
	public QuestionResource(QuestionService questionService)
	{
		this.questionService = questionService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Question question)
	{
		questionService.create(question);
		return Response.created(UriBuilder.fromResource(QuestionResource.class).build(question.getId())).build();
	}

	@GET
	@Path("/{id}")
	public Question findById(@PathParam("id") long id)
	{
		Question question = questionService.findById(id);

		if (question == null)
		{
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		return question;
	}

	@GET
	public Collection<Question> findLatest(
			@QueryParam("offset") @DefaultValue("0") int offset,
			@QueryParam("limit") @DefaultValue("10") int limit)
	{
		return questionService.findLatest(offset,  limit);
	}
}