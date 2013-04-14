package may.bestpoll;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ApplicationConfiguration extends Configuration
{
	@Valid
	@NotNull
	@JsonProperty
	private DatabaseConfiguration database = new DatabaseConfiguration();

	@NotEmpty
	@JsonProperty
	private String sample;

	public DatabaseConfiguration getDatabase()
	{
		return database;
	}

	public String getSample()
	{
		return sample;
	}
}
