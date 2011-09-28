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
package ch.bbv.fsm.dsl;

import ch.bbv.fsm.Action;

/**
 * Possibilities to execute an action.
 * 
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public interface ExecuteSyntax<TState extends Enum<?>, TEvent extends Enum<?>> extends GuardSyntax<TState, TEvent> {

	/**
	 * Defines the actions to execute on a transition.
	 * 
	 * @param action
	 *            The actions.
	 * @return Guard syntax.
	 */
	ExecuteSyntax<TState, TEvent> execute(Action<TState, TEvent> action);

	/**
	 * Defines the actions to execute on a transition.
	 * 
	 * @param methodCall
	 *            The actions.
	 * @return Guard syntax.
	 */
	ExecuteSyntax<TState, TEvent> execute(Object methodCall);
}
