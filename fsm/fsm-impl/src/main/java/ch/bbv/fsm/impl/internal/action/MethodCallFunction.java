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
package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.guard.Function;
import ch.bbv.fsm.impl.internal.aop.MethodCall;

/**
 * Implementation of a function that wraps a MethodCall.
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class MethodCallFunction<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements Function<TStateMachine, TState, TEvent, Object[], Boolean> {

	private final MethodCall<TStateMachine> methodCall;

	/**
	 * Creates a new instance.
	 * 
	 * @param methodCall
	 *            the methodCall instance
	 */
	public MethodCallFunction(final MethodCall<TStateMachine> methodCall) {
		this.methodCall = methodCall;
	}

	@Override
	public Boolean execute(final TStateMachine stateMachine, final Object[] args) {
		try {
			return (Boolean) this.methodCall.execute(stateMachine, args);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Guard: " + methodCall;
	}
}
