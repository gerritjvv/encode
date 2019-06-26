package encode;

/**
 * A function interface that throws exceptions.
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface IFn<T, R> {
    R apply(T t) throws Exception;
}
