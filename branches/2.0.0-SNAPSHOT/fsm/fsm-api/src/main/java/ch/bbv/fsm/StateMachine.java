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
 *     bbv Software Services AG (http://www.bbv.ch)
 *******************************************************************************/
package ch.bbv.fsm;

import ch.bbv.fsm.events.StateMachineEventHandler;

/**
 * Interface for all finite state machines.
 * 
 * @param <TState>
 *            the state enumeration
 * @param <TEvent>
 *            the event enumeration
 */
public interface StateMachine<TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * Fires the specified event.
	 * 
	 * @param eventId
	 *            the event
	 * @param eventArguments
	 *            the event arguments
	 */
	void fire(TEvent eventId, Object... eventArguments);

	/**
	 * Fires the specified priority event. The event will be handled before any already queued event.
	 * 
	 * @param eventId
	 *            the event.
	 * @param eventArguments
	 *            the event arguments.
	 */
	void firePriority(TEvent eventId, Object... eventArguments);

	/**
	 * Returns true if the state machine is executing events.
	 * 
	 * @return true if the state machine is executing events.
	 */
	boolean isExecuting();

	/**
	 * Gets a value indicating whether this instance is running. The state machine is running if if was started and not yet stopped.
	 * 
	 * @return true if this instance is running.
	 */
	boolean isRunning();

	/**
	 * Returns the number of queued events.
	 * 
	 * @return the number of queued events.
	 */
	int numberOfQueuedEvents();

	/**
	 * Starts the state machine. Events will be processed. If the state machine is not started then the events will be queued until the
	 * state machine is started. Already queued events are processed.
	 */
	void start();

	/**
	 * Stops the state machine. Events will be queued until the state machine is started.
	 */
	void stop();

	/**
	 * Returns the current state.
	 */
	TState getCurrentState();

	/**
	 * Adds an event handler.
	 * 
	 * @param handler
	 *            the event handler
	 */
	void addEventHandler(StateMachineEventHandler<StateMachine<TState, TEvent>, TState, TEvent> handler);

	/**
	 * Removes the given event handler.
	 * 
	 * @param handler
	 *            the event handler to be removed.
	 */
	void removeEventHandler(StateMachineEventHandler<StateMachine<TState, TEvent>, TState, TEvent> handler);

}
