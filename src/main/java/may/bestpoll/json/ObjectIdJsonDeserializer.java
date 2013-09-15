package may.bestpoll.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdJsonDeserializer extends JsonDeserializer<ObjectId>
{
	@Override
	public ObjectId deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException
	{
		if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
		{
			String objectIdStr = parser.getValueAsString();
			return new ObjectId(objectIdStr);
		}

		throw context.mappingException("Expected String");
	}
}
