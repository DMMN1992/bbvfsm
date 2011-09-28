package ch.bbv.fsm.impl.internal;

import java.util.List;

import ch.bbv.fsm.events.ExceptionEventArgs;
import ch.bbv.fsm.events.StateMachineEventHandler;
import ch.bbv.fsm.events.TransitionCompletedEventArgs;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.events.TransitionExceptionEventArgs;

public class DelegatingStateMachineEventHandler<TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachineEventHandler<TState, TEvent> {

	private final List<StateMachineEventHandler<TState, TEvent>> eventHandlers;

	public DelegatingStateMachineEventHandler(
			List<StateMachineEventHandler<TState, TEvent>> eventHandler) {
		this.eventHandlers = eventHandler;
	}

	@Override
	public void onExceptionThrown(ExceptionEventArgs<TState, TEvent> arg) {
		for (StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onExceptionThrown(arg);
		}
	}

	@Override
	public void onTransitionBegin(TransitionEventArgs<TState, TEvent> args) {
		for (StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onTransitionBegin(args);
		}
	}

	@Override
	public void onTransitionCompleted(
			TransitionCompletedEventArgs<TState, TEvent> arg) {
		for (StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onTransitionCompleted(arg);
		}
	}

	@Override
	public void onTransitionDeclined(TransitionEventArgs<TState, TEvent> arg) {
		for (StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onTransitionDeclined(arg);
		}
	}

	@Override
	public void onTransitionThrowsException(
			TransitionExceptionEventArgs<TState, TEvent> arg) {
		for (StateMachineEventHandler<TState, TEvent> handler : eventHandlers) {
			handler.onTransitionThrowsException(arg);
		}
	}
}
