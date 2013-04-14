package may.bestpoll;

import com.google.code.morphia.Morphia;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.yammer.dropwizard.config.Configuration;
import may.bestpoll.dao.Dao;
import may.bestpoll.entities.Poll;
import may.bestpoll.entities.Sequence;
import may.bestpoll.entities.User;
import may.bestpoll.service.*;
import org.bson.types.ObjectId;

public class ApplicationModule extends AbstractModule
{
    @Override
    protected void configure()
    {
		bind(new TypeLiteral<Dao<Sequence, ObjectId>>(){});
		bind(SequenceService.class).to(SequenceServiceImpl.class);

		bind(new TypeLiteral<Dao<User, ObjectId>>(){});
		bind(UserService.class).to(UserServiceImpl.class);

		bind(new TypeLiteral<Dao<Poll, ObjectId>>(){});
		bind(PollService.class).to(PollServiceImpl.class);
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
    public Mongo provideMongo(ApplicationConfiguration configuration) throws Exception
    {
        MongoURI mongoURI = new MongoURI(configuration.getDatabase().getUri());
        DB db = mongoURI.connectDB();
        if (mongoURI.getUsername() != null)
        {
            db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
        }
        return  db.getMongo();

    }

    @Provides
    public Morphia provideMorphia()
    {
        Morphia morphia = new Morphia();
        morphia.mapPackage("may.bestpoll.entities");
        return morphia;
    }

}
