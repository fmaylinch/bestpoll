package may.bestpoll.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;

public class FindTheBestExceptionMapper implements ExceptionMapper<FindTheBestException>
{
	@Override
	public Response toResponse(FindTheBestException e)
	{
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("exception", e.getClass().getName());
		map.put("message", e.getMessage());

		try
		{
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(objectMapper.writeValueAsString(map))
					.type(MediaType.APPLICATION_JSON)
					.build();
		}
		catch (JsonProcessingException jpe) // Could happen?
		{
			throw new RuntimeException("Could create message for exception: " + e, jpe);
		}
	}
}
