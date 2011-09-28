/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Ueli Kurmann
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * Contributors:
 *     bbv Software Services AG (http://www.bbv.ch), Ueli Kurmann
 *******************************************************************************/
package ch.bbv.fsm.impl.internal;

import java.lang.reflect.InvocationTargetException;

import ch.bbv.fsm.Action;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.dsl.MethodCall;

/**
 * Calls a method.
 * 
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class MethodCallAction<TState extends Enum<?>, TEvent extends Enum<?>>
		implements Action<TState, TEvent> {

	private final MethodCall methodCall;

	/**
	 * Constructor.
	 * 
	 * @param methodCall
	 *            the method to call
	 */
	public MethodCallAction(final MethodCall methodCall) {
		this.methodCall = methodCall;
	}

	@Override
	public void execute(final StateMachine<TState, TEvent> stateMachine,
			final Object... arguments) {
		try {
			this.methodCall.getMethod().invoke(this.methodCall.getOwner(),
					arguments);
		} catch (final IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (final InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
