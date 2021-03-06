package may.bestpoll;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import may.bestpoll.exception.FindTheBestExceptionMapper;
import may.bestpoll.json.ObjectIdJsonDeserializer;
import may.bestpoll.json.ObjectIdJsonSerializer;
import org.bson.types.ObjectId;

public class ApplicationService extends Service<ApplicationConfiguration>
{
    public static void main(String[] args) throws Exception
	{
		new ApplicationService().run(args);
	}

	@Override
	public void initialize(Bootstrap<ApplicationConfiguration> bootstrap)
	{
		bootstrap.setName("bestpoll");

		bootstrap.addBundle(new AssetsBundle("/pages/", "/"));

		bootstrap.addBundle(GuiceBundle.newBuilder()
			.addModule(new ApplicationModule())
			.enableAutoConfig(getClass().getPackage().getName())
			.build()
		);

		bootstrap.getObjectMapperFactory().registerModule(createJacksonModule());
	}

	@Override
	public void run(ApplicationConfiguration configuration, Environment environment) throws Exception
	{
		environment.addProvider(new FindTheBestExceptionMapper());
	}

	/** Jackson module with custom de/serializers */
	private SimpleModule createJacksonModule()
	{
		final SimpleModule module = new SimpleModule();
		module.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
		module.addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
		return module;
	}
}
