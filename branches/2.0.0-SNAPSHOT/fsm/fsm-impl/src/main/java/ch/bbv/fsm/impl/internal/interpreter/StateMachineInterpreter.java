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
package ch.bbv.fsm.impl.internal.interpreter;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.events.StateMachineEventHandler;
import ch.bbv.fsm.impl.internal.driver.Notifier;
import ch.bbv.fsm.impl.internal.events.ExceptionEventArgsImpl;
import ch.bbv.fsm.impl.internal.events.TransitionCompletedEventArgsImpl;
import ch.bbv.fsm.impl.internal.events.TransitionEventArgsImpl;
import ch.bbv.fsm.impl.internal.events.TransitionExceptionEventArgsImpl;
import ch.bbv.fsm.impl.internal.state.State;
import ch.bbv.fsm.impl.internal.state.StateContext;
import ch.bbv.fsm.impl.internal.state.StateDictionary;
import ch.bbv.fsm.impl.internal.transition.TransitionContext;
import ch.bbv.fsm.impl.internal.transition.TransitionResult;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * State Machine Implementation.
 * 
 * @author Ueli Kurmann (bbv Software Services AG)
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public class StateMachineInterpreter<TState extends Enum<?>, TEvent extends Enum<?>> implements Notifier<TState, TEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(StateMachineInterpreter.class);

	/**
	 * Name of this state machine used in log messages.
	 */
	private final String name;

	/**
	 * The current state.
	 */
	private State<TState, TEvent> currentState;

	private final Map<State<TState, TEvent>, State<TState, TEvent>> superToSubState = Maps.newHashMap();

	/**
	 * The initial state of the state machine.
	 */
	@SuppressWarnings("unused")
	// Used in reporting
	private TState initialStateId;

	/**
	 * The dictionary of all states.
	 */
	private final StateDictionary<TState, TEvent> states;

	private final List<StateMachineEventHandler<TState, TEvent>> eventHandler;

	/**
	 * Initializes a new instance of the StateMachineImpl<TState,TEvent> class.
	 * 
	 * @param stateMachine
	 *            the state machine
	 * @param name
	 *            The name of this state machine used in log messages.
	 * @param states
	 *            the states
	 */
	public StateMachineInterpreter(final StateMachine<TState, TEvent> stateMachine, final String name,
			final StateDictionary<TState, TEvent> states) {
		this.name = name;
		this.states = states;
		this.eventHandler = Lists.newArrayList();
	}

	/**
	 * Fires the specified event.
	 * 
	 * @param eventId
	 *            the event id.
	 */
	public void fire(final TEvent eventId) {
		this.fire(eventId, null);
	}

	/**
	 * Fires the specified event.
	 * 
	 * @param eventId
	 *            the event id.
	 * @param eventArguments
	 *            the event arguments.
	 */
	public void fire(final TEvent eventId, final Object[] eventArguments) {
		if (LOG.isDebugEnabled()) {
			LOG.info("Fire event {} on state machine {} with current state {} and event arguments {}.",
					new Object[] { eventId, this.getName(), this.getCurrentStateId(), eventArguments });
		}

		final TransitionContext<TState, TEvent> context = new TransitionContext<TState, TEvent>(getCurrentState(), eventId, eventArguments,
				this, this);
		final TransitionResult<TState, TEvent> result = this.currentState.fire(context);

		if (!result.isFired()) {
			LOG.info("No transition possible.");
			this.onTransitionDeclined(context);
			return;
		}

		this.setCurrentState(result.getNewState());

		LOG.debug("State machine {} performed {}.", this, context.getRecords());

		this.onTransitionCompleted(context);
	}

	/**
	 * Returns the current state.
	 * 
	 * @return the current state.
	 */
	private State<TState, TEvent> getCurrentState() {
		return this.currentState;
	}

	/**
	 * Gets the id of the current state.
	 * 
	 * @return The id of the current state.
	 */
	public TState getCurrentStateId() {
		if (this.getCurrentState() != null) {
			return this.getCurrentState().getId();
		} else {
			return null;
		}
	}

	/**
	 * Gets the name of this instance.
	 * 
	 * @return The name of this instance.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Initializes the state machine by setting the specified initial state.
	 * 
	 * @param initialState
	 *            the initial state
	 * @param stateContext
	 *            the state context
	 */
	private void initialize(final State<TState, TEvent> initialState, final StateContext<TState, TEvent> stateContext) {
		if (initialState == null) {
			throw new IllegalArgumentException("initialState; The initial state must not be null.");
		}

		this.initialStateId = initialState.getId();

		final StateMachineInitializer<TState, TEvent> initializer = new StateMachineInitializer<TState, TEvent>(initialState, stateContext);
		this.setCurrentState(initializer.enterInitialState());
	}

	/**
	 * Initializes the state machine by setting the specified initial state.
	 * 
	 * @param initialState
	 *            the initial state.
	 */
	public void initialize(final TState initialState) {
		LOG.info("State machine {} initializes to state {}.", this, initialState);

		final StateContext<TState, TEvent> stateContext = new StateContext<TState, TEvent>(null, this, this);
		this.initialize(this.states.getState(initialState), stateContext);

		LOG.info("State machine {} performed {}.", this, stateContext.getRecords());
	}

	/**
	 * Adds an event handler.
	 * 
	 * @param handler
	 *            the event handler.
	 */
	public void addEventHandler(final StateMachineEventHandler<TState, TEvent> handler) {
		this.eventHandler.add(handler);
	}

	/**
	 * Removes an event handler.
	 * 
	 * @param handler
	 *            the event handle to be removed.
	 */
	public void removeEventHandler(final StateMachineEventHandler<TState, TEvent> handler) {
		this.eventHandler.remove(handler);
	}

	@Override
	public void onExceptionThrown(final StateContext<TState, TEvent> stateContext, final Exception exception) {
		for (final StateMachineEventHandler<TState, TEvent> handler : this.eventHandler) {
			handler.onExceptionThrown(new ExceptionEventArgsImpl<TState, TEvent>(stateContext, exception));
		}
	}

	@Override
	public void onExceptionThrown(final TransitionContext<TState, TEvent> transitionContext, final Exception exception) {
		for (final StateMachineEventHandler<TState, TEvent> handler : this.eventHandler) {
			handler.onTransitionThrowsException(new TransitionExceptionEventArgsImpl<TState, TEvent>(transitionContext, exception));
		}
	}

	@Override
	public void onTransitionBegin(final TransitionContext<TState, TEvent> transitionContext) {
		try {
			for (final StateMachineEventHandler<TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionBegin(new TransitionEventArgsImpl<TState, TEvent>(transitionContext));
			}
		} catch (final Exception e) {
			onExceptionThrown(transitionContext, e);
		}
	}

	/**
	 * Fires a transition completed event.
	 * 
	 * @param transitionContext
	 *            the transition context
	 */
	protected void onTransitionCompleted(final TransitionContext<TState, TEvent> transitionContext) {
		try {
			for (final StateMachineEventHandler<TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionCompleted(new TransitionCompletedEventArgsImpl<TState, TEvent>(this.getCurrentStateId(),
						transitionContext));
			}
		} catch (final Exception e) {
			onExceptionThrown(transitionContext, e);
		}
	}

	/**
	 * Fires the transaction declined event.
	 * 
	 * @param transitionContext
	 *            the transition context.
	 */
	protected void onTransitionDeclined(final TransitionContext<TState, TEvent> transitionContext) {
		try {
			for (final StateMachineEventHandler<TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionDeclined(new TransitionEventArgsImpl<TState, TEvent>(transitionContext));
			}
		} catch (final Exception e) {
			onExceptionThrown(transitionContext, e);
		}

	}

	/**
	 * Returns the report of the execution.
	 * 
	 * @return
	 */
	public String report() {
		// final StateMachineReport<TState, TEvent> report = new
		// StateMachineReport<TState, TEvent>();
		// return report.report(this.toString(), this.states.getStates(),
		// this.initialStateId);
		return "";
	}

	/**
	 * Sets the current state.
	 * 
	 * @param state
	 *            the current state.
	 */
	private void setCurrentState(final State<TState, TEvent> state) {
		LOG.info("State machine {} switched to state {}.", this.getName(), state.getId());
		this.currentState = state;
	}

	/**
	 * Returns the last active substate for the given composite state.
	 * 
	 * @param superState
	 *            the super state
	 */
	public State<TState, TEvent> getLastActiveSubState(final State<TState, TEvent> superState) {
		return superToSubState.get(superState);
	}

	/**
	 * Sets the last active sub state for the given composite state.
	 * 
	 * @param superState
	 *            the super state
	 * @param subState
	 *            the last active sub state
	 */
	public void setLastActiveSubState(final State<TState, TEvent> superState, final State<TState, TEvent> subState) {
		superToSubState.put(superState, subState);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
