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
package ch.bbv.fsm.impl.internal.events;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.events.ContextEventArgs;
import ch.bbv.fsm.impl.internal.state.StateContext;

public class ContextEventArgsImpl<TState extends Enum<?>, TEvent extends Enum<?>>
		implements ContextEventArgs<TState, TEvent> {

	/**
	 * The context.
	 */
	private final StateContext<TState, TEvent> stateContext;

	/**
	 * Initializes a new instance.
	 * 
	 * @param stateContext
	 *            the state context.
	 */
	public ContextEventArgsImpl(final StateContext<TState, TEvent> stateContext) {
		this.stateContext = stateContext;
	}

	/**
	 * Returns the state context.
	 * 
	 * @return the state context.
	 */
	public StateContext<TState, TEvent> getStateContext() {
		return this.stateContext;
	}

	@Override
	public StateMachine<TState, TEvent> getSource() {
		return this.stateContext.getStateMachine();
	}
}