package may.bestpoll.resources;

import com.google.inject.Inject;
import may.bestpoll.entities.User;
import may.bestpoll.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource
{
	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

	private UserService userService;

	@Inject
	public UserResource(UserService userService)
	{
		this.userService = userService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user)
	{
		userService.assureUser(user);
		return Response.status(Response.Status.OK).entity(user).build();
	}
}