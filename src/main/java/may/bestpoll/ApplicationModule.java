package may.bestpoll;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.yammer.dropwizard.config.Configuration;
import may.bestpoll.service.*;

import java.net.UnknownHostException;

public class ApplicationModule extends AbstractModule
{
    @Override
    protected void configure()
    {
		bind(SequenceService.class).to(SequenceServiceImpl.class);

		bind(UserService.class).to(UserServiceImpl.class);

		bind(QuestionService.class).to(QuestionServiceImpl.class);
    }

    @Provides
    public ApplicationConfiguration configuration(Configuration configuration)
    {
        return (ApplicationConfiguration) configuration;
    }

    @Provides
    @Named("sample")
    public String provideTemplate(ApplicationConfiguration configuration)
    {
        return configuration.getSample();
    }

    @Provides
    @Named("databaseName")
    public String provideDatabaseName(ApplicationConfiguration configuration)
    {
        return new MongoURI(configuration.getDatabase().getUri()).getDatabase();
    }

	@Provides
	@Inject
	public DB provideDB(ApplicationConfiguration configuration) throws UnknownHostException
	{
		MongoURI mongoURI = new MongoURI(configuration.getDatabase().getUri());
		DB db = mongoURI.connectDB();
		if (mongoURI.getUsername() != null)
		{
			db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
		}
		return db;
	}

	@Provides
	@Inject
    public Mongo provideMongo(DB db)
    {
        return  db.getMongo();
    }
}
