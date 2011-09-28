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

import ch.bbv.fsm.Action;
import ch.bbv.fsm.StateMachine;

/**
 * Wraps an action without parameters.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class ActionHolderNoParameter<TState extends Enum<?>, TEvent extends Enum<?>>
		implements ActionHolder<TState, TEvent> {

	/**
	 * the wrapped action.
	 */
	private final Action<TState, TEvent> action;

	/**
	 * Initializes a new instance.
	 * 
	 * @param action
	 *            the action to wrap.
	 */
	public ActionHolderNoParameter(final Action<TState, TEvent> action) {
		this.action = action;
	}

	@Override
	public void execute(StateMachine<TState, TEvent> stateMachine) {
		this.action.execute(stateMachine);
	}
}