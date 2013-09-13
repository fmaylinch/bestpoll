package may.bestpoll.util.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utilities of "functional programming". There are some libraries out there that offer this,
 * like <a href="http://functionaljava.org/">Functional Java</a>.
 */
public class FunctionalUtils
{
	/**
	 * Converts a Collection of A to a List of B
	 * by applying the function `func` to each element of `collection`.
	 */
	// TODO: Use Google Guava Luke...
	public static <A,B> List<B> map (Collection<? extends A> collection, F<A,B> func)
	{
		List<B> result = new ArrayList<B>();
		for (A a : collection)
		{
			result.add(func.f(a));
		}
		return result;
	}
}
