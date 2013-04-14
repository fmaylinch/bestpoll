package may.bestpoll;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class DatabaseConfiguration extends Configuration
{
    @NotEmpty
    @JsonProperty
    private String uri;

    public String getUri()
    {
        return uri;
    }
}
