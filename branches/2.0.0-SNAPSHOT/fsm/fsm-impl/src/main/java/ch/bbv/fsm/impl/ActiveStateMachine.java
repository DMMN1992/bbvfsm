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
package ch.bbv.fsm.impl;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.events.StateMachineEventHandler;
import ch.bbv.fsm.impl.internal.EventInformation;
import ch.bbv.fsm.impl.internal.StateMachineImpl;
import ch.bbv.fsm.impl.internal.state.StateDictionary;

/**
 * An active state machine. This state machine reacts to events on a separate
 * worker thread.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * @param <TState>
 *            the type of the states. (Enum)
 * @param <TEvent>
 *            the type of the events. (Enum)
 */
public class ActiveStateMachine<TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachine<TState, TEvent> {

	private static final int WAIT_FOR_TERMINATION_MS = 10000;

	/**
	 * The internal state machine.
	 */
	private final StateMachineImpl<TState, TEvent> stateMachine;

	/**
	 * List of all queued events.
	 */
	private final BlockingDeque<EventInformation<TEvent>> events;

	private ExecutorService executorService;

	/**
	 * Whether this state machine is executing an event. Allows that events can
	 * be added while executing.
	 */
	private volatile boolean executing;

	/**
	 * Indicates if the state machine is running.
	 */
	private volatile boolean isRunning;

	private final Runnable worker = new Runnable() {
		@Override
		public void run() {
			ActiveStateMachine.this.execute();
		}
	};

	/**
	 * Initializes the state machine.
	 * 
	 * @param name
	 *            the name of the state machine used in the logs.
	 * @param states
	 *            the states
	 */
	public ActiveStateMachine(final String name,
			final StateDictionary<TState, TEvent> states) {
		this.stateMachine = new StateMachineImpl<TState, TEvent>(this, name,
				states);
		this.events = new LinkedBlockingDeque<EventInformation<TEvent>>();
		this.executorService = Executors.newFixedThreadPool(1);
	}

	/**
	 * Executes all queued events.
	 */
	private void execute() {
		if (this.executing || !this.isRunning) {
			return;
		}

		while (this.isRunning) {
			try {
				/**
				 * FIXME kuu
				 * <p>
				 * this implementation is not thread safe. this.executing should
				 * be true before fetching the next event. after switching the
				 * lines, the machine is "always" executing and it could not be
				 * interrupted.
				 */
				final EventInformation<TEvent> eventToProcess = this
						.getNextEventToProcess();
				this.executing = true;
				if (eventToProcess != null) {
					this.fireEventOnStateMachine(eventToProcess);
				}
			} finally {
				this.executing = false;
			}
		}

	}

	@Override
	public void fire(final TEvent eventId, final Object... eventArguments) {
		this.events.addLast(new EventInformation<TEvent>(eventId,
				eventArguments));

	}

	/**
	 * Fires the event on the state machine.
	 * 
	 * @param e
	 *            the event to be fired on the state machine.
	 */
	private void fireEventOnStateMachine(final EventInformation<TEvent> e) {
		this.stateMachine.fire(e.getEventId(), e.getEventArguments());
	}

	@Override
	public void firePriority(final TEvent eventId,
			final Object... eventArguments) {
		this.events.addFirst(new EventInformation<TEvent>(eventId,
				eventArguments));

	}

	/**
	 * Gets the next event to process for the queue.
	 * 
	 * @return The next queued event.
	 */
	private EventInformation<TEvent> getNextEventToProcess() {
		try {
			final EventInformation<TEvent> e = this.events.pollFirst(10,
					TimeUnit.MILLISECONDS);
			return e;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Initializes the state machine.
	 * 
	 * @param initialState
	 *            the initial state to use
	 */
	public void initialize(final TState initialState) {
		this.stateMachine.initialize(initialState);
	}

	@Override
	public boolean isExecuting() {
		return this.executing;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	@Override
	public int numberOfQueuedEvents() {
		return this.events.size();
	}

	@Override
	public void start() {
		this.isRunning = true;
		this.executorService = Executors.newFixedThreadPool(1);
		this.executorService.execute(this.worker);
	}

	/**
	 * This method blocks until the termination of the worker thread. After a
	 * timeout of 10 seconds the thread is interrupted.
	 * 
	 * @see ch.bbv.fsm.StateMachineDefinition#stop()
	 */
	@Override
	public void stop() {
		this.isRunning = false;
		this.executorService.shutdown();
		try {
			this.executorService.awaitTermination(WAIT_FOR_TERMINATION_MS, TimeUnit.MILLISECONDS);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public TState getCurrentState() {
		return stateMachine.getCurrentStateId();
	}

	@Override
	public void addEventHandler(
			final StateMachineEventHandler<TState, TEvent> handler) {
		stateMachine.addEventHandler(handler);
	}

	@Override
	public void removeEventHandler(
			final StateMachineEventHandler<TState, TEvent> handler) {
		stateMachine.removeEventHandler(handler);
	}

}
