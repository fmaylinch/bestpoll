package may.bestpoll.service;

import com.google.inject.Inject;
import com.mongodb.Mongo;
import com.yammer.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoManaged implements Managed
{
	private static final Logger logger = LoggerFactory.getLogger(MongoManaged.class);

    private final Mongo mongo;

    @Inject
    public MongoManaged(Mongo mongo)
    {
        this.mongo = mongo;
    }

    @Override
    public void start() throws Exception
    {
		logger.info("Mongo started");
    }

    @Override
    public void stop() throws Exception
    {
		logger.info("Closing Mongo");
        mongo.close();
    }
}
