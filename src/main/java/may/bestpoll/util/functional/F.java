package may.bestpoll.util.functional;

/** Represents a function that gets a {@link Parameter} and returns a {@link Result} */
public interface F<Parameter,Result>
{
	Result f(Parameter p);
}
