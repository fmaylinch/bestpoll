package may.bestpoll.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import may.bestpoll.entities.Question;
import may.bestpoll.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/question")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
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
		return Response.status(Response.Status.OK).entity(question).build();
	}

	@GET
	public Collection<Question> findLatest(
			@QueryParam("offset") @DefaultValue("0") int offset,
			@QueryParam("limit") @DefaultValue("10") int limit)
	{
		return questionService.findLatest(offset,  limit);
	}
}