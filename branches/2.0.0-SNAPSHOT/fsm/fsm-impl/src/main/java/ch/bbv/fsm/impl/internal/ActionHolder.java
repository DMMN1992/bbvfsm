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

import ch.bbv.fsm.StateMachine;

/**
 * Action Holder. A wrapper class for different action types. It allows to execute all actions with the same method call.
 * 
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public interface ActionHolder<TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * Executes the wrapped action.
	 * 
	 * @param stateMachine
	 *            the calling state machine
	 */
	void execute(StateMachine<TState, TEvent> stateMachine);
}
