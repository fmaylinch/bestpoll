package may.bestpoll.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdJsonSerializer extends JsonSerializer<ObjectId>
{
	@Override
	public void serialize(ObjectId objectId, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException
	{
		String objectIdStr = objectId.toString();
		generator.writeString(objectIdStr);
	}
}
