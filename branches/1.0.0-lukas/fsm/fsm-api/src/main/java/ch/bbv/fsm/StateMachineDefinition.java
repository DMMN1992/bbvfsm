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
package ch.bbv.fsm;

import ch.bbv.fsm.dsl.EntryActionSyntax;
import ch.bbv.fsm.events.StateMachineEventHandler;

/**
 * Defines the interface of a state machine.
 * 
 * @author Ueli Kurmann (bbv Software Services AG)
 * @param <TState>
 *            the enumeration type of the states.
 * @param <TEvent>
 *            the enumeration type of the events.
 */
public interface StateMachineDefinition<TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * @param state
	 * @return
	 */
	EntryActionSyntax<TState, TEvent> in(TState state);

	EntryActionSyntax<TState, TEvent> fromStart();

	/**
	 * Defines a state hierarchy.
	 * 
	 * @param superStateId
	 *            the super state id.
	 * @param initialSubStateId
	 *            the initial sub state id.
	 * @param historyType
	 *            type of history.
	 * @param subStateIds
	 *            the sub state id's.
	 */
	void defineHierarchyOn(TState superStateId, TState initialSubStateId,
			HistoryType historyType, TState... subStateIds);

	/**
	 * Returns the name of this state machine.
	 */
	String getName();

	/**
	 * Adds an event handler
	 * 
	 * @param handler
	 *            the event handler
	 */
	void addEventHandler(StateMachineEventHandler<TState, TEvent> handler);

	/**
	 * Removes the given event handler.
	 * 
	 * @param handler
	 *            the event handler to be removed.
	 */
	void removeEventHandler(StateMachineEventHandler<TState, TEvent> handler);

	/**
	 * Creates an active state-machine from this definition.
	 * 
	 * @param initialState
	 *            The state to which the state machine is initialized.
	 */
	StateMachine<TState, TEvent> createActiveStateMachine(String name,
			TState initialState);

	/**
	 * Creates an passive state-machine from this definition.
	 * 
	 * @param initialState
	 *            The state to which the state machine is initialized.
	 */
	StateMachine<TState, TEvent> createPassiveStateMachine(String name,
			TState initialState);

	/**
	 * Returns a report of this state machine with all states and transitions.
	 */
	String report();

	StateMachine<TState, TEvent> createActiveStateMachine(String name);

	StateMachine<TState, TEvent> createPassiveStateMachine(String name);

}