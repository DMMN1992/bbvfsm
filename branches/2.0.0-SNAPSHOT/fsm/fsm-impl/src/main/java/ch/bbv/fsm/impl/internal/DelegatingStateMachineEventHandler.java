package ch.bbv.fsm.impl.internal;

import java.util.List;

import ch.bbv.fsm.events.ExceptionEventArgs;
import ch.bbv.fsm.events.StateMachineEventHandler;
import ch.bbv.fsm.events.TransitionCompletedEventArgs;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.events.TransitionExceptionEventArgs;

/**
 * Delegates events to a list of listeners.
 * 
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class DelegatingStateMachineEventHandler<TState extends Enum<?>, TEvent extends Enum<?>> implements
		StateMachineEventHandler<TState, TEvent> {

	private final List<StateMachineEventHandler<TState, TEvent>> eventHandlers;

	/**
	 * Constructor.
	 * 
	 * @param eventHandlers
	 *            the handlers
	 */
	public DelegatingStateMachineEventHandler(final List<StateMachineEventHandler<TState, TEvent>> eventHandlers) {
		this.eventHandlers = eventHandlers;
	}

	@Override
	public void onExceptionThrown(final ExceptionEventArgs<TState, TEvent> arg) {
		for (final StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onExceptionThrown(arg);
		}
	}

	@Override
	public void onTransitionBegin(final TransitionEventArgs<TState, TEvent> args) {
		for (final StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onTransitionBegin(args);
		}
	}

	@Override
	public void onTransitionCompleted(final TransitionCompletedEventArgs<TState, TEvent> arg) {
		for (final StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onTransitionCompleted(arg);
		}
	}

	@Override
	public void onTransitionDeclined(final TransitionEventArgs<TState, TEvent> arg) {
		for (final StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onTransitionDeclined(arg);
		}
	}

	@Override
	public void onTransitionThrowsException(final TransitionExceptionEventArgs<TState, TEvent> arg) {
		for (final StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onTransitionThrowsException(arg);
		}
	}
}
