/**
 * 
 */
package flatten;

/**
 * @author IBM_ADMIN
 * @param <T>
 *
 */
public class MyFunction<T,R> implements Function<T,T>{

	@Override
	public T apply(T p) {
		return p;
	}

}
