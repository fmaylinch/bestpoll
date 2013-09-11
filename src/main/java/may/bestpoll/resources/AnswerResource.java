package may.bestpoll.resources;

import com.google.inject.Inject;
import may.bestpoll.entities.Answer;
import may.bestpoll.entities.Question;
import may.bestpoll.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/answer")
@Produces(MediaType.APPLICATION_JSON)
public class AnswerResource
{
	private static final Logger logger = LoggerFactory.getLogger(AnswerResource.class);

	private QuestionService questionService;

	@Inject
	public AnswerResource(QuestionService questionService)
	{
		this.questionService = questionService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Answer answer)
	{
		questionService.addAnswer(answer);
		return Response.status(Response.Status.OK).entity(answer).build();
	}
}