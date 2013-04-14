package may.bestpoll.resources;

import com.google.inject.Inject;
import may.bestpoll.entities.User;
import may.bestpoll.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
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
	public Response create(@Valid User user)
	{
		userService.create(user);
		return Response.status(Response.Status.CREATED).entity(user.getUsername()).build();
	}

	@GET
	@Path("/{username}")
	public User findByUsername(@PathParam("username") String username)
	{
		User user = userService.findByUsername(username);

		if (user == null)
		{
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		return user;
	}
}