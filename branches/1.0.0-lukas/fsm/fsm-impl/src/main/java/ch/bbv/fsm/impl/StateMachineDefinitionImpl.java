package ch.bbv.fsm.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.StateMachineDefinition;
import ch.bbv.fsm.dsl.EntryActionSyntax;
import ch.bbv.fsm.events.StateMachineEventHandler;
import ch.bbv.fsm.impl.internal.DelegatingStateMachineEventHandler;
import ch.bbv.fsm.impl.internal.Notifier;
import ch.bbv.fsm.impl.internal.StateBuilder;
import ch.bbv.fsm.impl.internal.StateMachineImpl;
import ch.bbv.fsm.impl.internal.events.ExceptionEventArgsImpl;
import ch.bbv.fsm.impl.internal.events.TransitionEventArgsImpl;
import ch.bbv.fsm.impl.internal.events.TransitionExceptionEventArgsImpl;
import ch.bbv.fsm.impl.internal.state.State;
import ch.bbv.fsm.impl.internal.state.StateContext;
import ch.bbv.fsm.impl.internal.state.StateDictionary;
import ch.bbv.fsm.impl.internal.transition.TransitionContext;

import com.google.common.collect.Lists;

public class StateMachineDefinitionImpl<TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachineDefinition<TState, TEvent>,
		Notifier<TState, TEvent> {

	private static Logger LOG = LoggerFactory.getLogger(StateMachineImpl.class);

	/**
	 * The dictionary of all states.
	 */
	private StateDictionary<TState, TEvent> states;

	/**
	 * Name of this state machine used in log messages.
	 */
	private String name;

	/**
	 * List of Listeners informed by the {link
	 */
	private final List<StateMachineEventHandler<TState, TEvent>> eventHandler;

	/**
	 * Initializes the passive state machine.
	 */
	public StateMachineDefinitionImpl() {
		this(StateMachineDefinitionImpl.class.getSimpleName());
	}

	/**
	 * Initializes the state machine.
	 * 
	 * @param name
	 *            the name of the state machine used in the logs.
	 */
	public StateMachineDefinitionImpl(final String name) {
		this.name = name;
		this.states = new StateDictionary<TState, TEvent>();
		this.eventHandler = Lists.newArrayList();
	}

	@Override
	public void defineHierarchyOn(final TState superStateId,
			final TState initialSubStateId, final HistoryType historyType,
			final TState... subStateIds) {
		final State<TState, TEvent> superState = this.states
				.getState(superStateId);
		superState.setHistoryType(historyType);

		for (final TState subStateId : subStateIds) {
			final State<TState, TEvent> subState = this.states
					.getState(subStateId);
			subState.setSuperState(superState);
			superState.addSubState(subState);
		}

		superState.setInitialState(this.states.getState(initialSubStateId));
	}

	@Override
	public EntryActionSyntax<TState, TEvent> in(final TState state) {
		final State<TState, TEvent> newState = this.states.getState(state);
		return new StateBuilder<TState, TEvent>(newState, this.states);
	}

	@Override
	public void addEventHandler(
			final StateMachineEventHandler<TState, TEvent> handler) {
		this.eventHandler.add(handler);
	}

	@Override
	public void removeEventHandler(
			final StateMachineEventHandler<TState, TEvent> handler) {
		this.eventHandler.remove(handler);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public StateMachine<TState, TEvent> createActiveStateMachine(String name,
			TState initialState) {
		ActiveStateMachine<TState, TEvent> activeStateMachine = new ActiveStateMachine<TState, TEvent>(
				name, states);
		activeStateMachine
				.addEventHandler(new DelegatingStateMachineEventHandler<TState, TEvent>(
						eventHandler));
		activeStateMachine.initialize(initialState);
		return activeStateMachine;

	}

	@Override
	public StateMachine<TState, TEvent> createActiveStateMachine(String name) {
		ActiveStateMachine<TState, TEvent> activeStateMachine = new ActiveStateMachine<TState, TEvent>(
				name, states);
		activeStateMachine
				.addEventHandler(new DelegatingStateMachineEventHandler<TState, TEvent>(
						eventHandler));
		return activeStateMachine;
	}

	@Override
	public StateMachine<TState, TEvent> createPassiveStateMachine(String name,
			TState initialState) {
		PassiveStateMachine<TState, TEvent> passiveStateMachine = new PassiveStateMachine<TState, TEvent>(
				name, states);
		passiveStateMachine
				.addEventHandler(new DelegatingStateMachineEventHandler<TState, TEvent>(
						eventHandler));
		passiveStateMachine.initialize(initialState);
		return passiveStateMachine;

	}

	@Override
	public StateMachine<TState, TEvent> createPassiveStateMachine(String name) {
		PassiveStateMachine<TState, TEvent> passiveStateMachine = new PassiveStateMachine<TState, TEvent>(
				name, states);
		passiveStateMachine
				.addEventHandler(new DelegatingStateMachineEventHandler<TState, TEvent>(
						eventHandler));
		return passiveStateMachine;
	}

	@Override
	public String report() {
		// TODO Implement
		return "";
	}

	@Override
	public void onExceptionThrown(
			final StateContext<TState, TEvent> stateContext,
			final Exception exception) {
		try {
			for (final StateMachineEventHandler<TState, TEvent> handler : this.eventHandler) {
				handler.onExceptionThrown(new ExceptionEventArgsImpl<TState, TEvent>(
						stateContext, exception));
			}
		} catch (final Exception e) {
			LOG.error("Exception during event handler.", e);
		}

	}

	@Override
	public void onExceptionThrown(
			final TransitionContext<TState, TEvent> transitionContext,
			final Exception exception) {
		try {
			for (final StateMachineEventHandler<TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionThrowsException(new TransitionExceptionEventArgsImpl<TState, TEvent>(
						transitionContext, exception));
			}
		} catch (final Exception e) {
			LOG.error("Exception during event handler.", e);
		}

	}

	@Override
	public void onTransitionBegin(
			final TransitionContext<TState, TEvent> transitionContext) {
		try {
			for (final StateMachineEventHandler<TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionBegin(new TransitionEventArgsImpl<TState, TEvent>(
						transitionContext));
			}
		} catch (final Exception e) {
			onExceptionThrown(transitionContext, e);
		}
	}

}
