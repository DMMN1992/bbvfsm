package ch.bbv.fsm.impl.internal.aop;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 * 
 */
public class ClassMethodCallInterceptor<TObject> implements
		MethodInterceptor {

	@Override
	public Object intercept(final Object object, final Method method,
			final Object[] args, final MethodProxy methodProxy)
			throws Throwable {
		if (!method.isAccessible()) {
			method.setAccessible(true);
		}
		final MethodCall<TObject> methodCall = new ClassMethodCallImpl<TObject>(
				method, args);
		CallInterceptorBuilder.push(methodCall);
		return null;
	}
}
