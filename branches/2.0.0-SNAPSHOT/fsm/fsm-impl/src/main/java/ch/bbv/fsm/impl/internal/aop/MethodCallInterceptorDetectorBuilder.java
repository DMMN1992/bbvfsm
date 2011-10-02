package ch.bbv.fsm.impl.internal.aop;

import net.sf.cglib.proxy.Enhancer;

/**
 * Defines a mock for detecting method calls.
 */
public final class MethodCallInterceptorDetectorBuilder {

	private MethodCallInterceptorDetectorBuilder() {
		// Tool class
	}

	/**
	 * Intercepts a type to simulate delegates.
	 * 
	 * @param <T>
	 *            the type
	 * @param type
	 *            the type to call
	 */
	@SuppressWarnings("unchecked")
	public static <T> T build(final Class<T> type) {
		final MethodCallInterceptor interceptor = new MethodCallInterceptor();
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setCallback(interceptor);
		final T instance = (T) enhancer.create();
		return instance;
	}
}
