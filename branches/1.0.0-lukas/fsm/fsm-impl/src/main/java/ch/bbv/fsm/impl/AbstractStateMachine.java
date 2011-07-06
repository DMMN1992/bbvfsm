package ch.bbv.fsm.impl;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.events.StateMachineEventHandler;

public class AbstractStateMachine<TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachine<TState, TEvent> {

	@Override
	public void fire(final TEvent eventId, final Object... eventArguments) {
		// TODO Auto-generated method stub

	}

	@Override
	public void firePriority(final TEvent eventId,
			final Object... eventArguments) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isExecuting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int numberOfQueuedEvents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public TState getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEventHandler(
			final StateMachineEventHandler<TState, TEvent> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEventHandler(
			final StateMachineEventHandler<TState, TEvent> handler) {
		// TODO Auto-generated method stub

	}

}
