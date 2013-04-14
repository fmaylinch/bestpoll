package may.bestpoll.health;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.yammer.metrics.core.HealthCheck;

public class TemplateHealthCheck extends HealthCheck
{
    private final String sample;

    @Inject
    public TemplateHealthCheck( @Named("sample") String sample)
    {
        super("sample");
        this.sample = sample;
    }

    @Override
    protected HealthCheck.Result check() throws Exception
    {
        if (!"Sample property".equals(sample))
        {
            return HealthCheck.Result.unhealthy("Sample property not found or has an unexpected value: " + sample);
        }

        return HealthCheck.Result.healthy();
    }

}
