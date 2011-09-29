package ch.bbv.fsm.impl;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.events.StateMachineEventHandler;

/**
 * Base class for finite state machine implementations.
 * 
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public class AbstractStateMachine<TState extends Enum<?>, TEvent extends Enum<?>> implements StateMachine<TState, TEvent> {

	private final StateMachine<TState, TEvent> driver;

	/**
	 * Create a state machine.
	 * 
	 * @param driver
	 *            the executor of the state event machine.
	 */
	protected AbstractStateMachine(final StateMachine<TState, TEvent> driver) {
		this.driver = driver;
	}

	@Override
	public final void fire(final TEvent eventId, final Object... eventArguments) {
		driver.fire(eventId, eventArguments);
	}

	@Override
	public final void firePriority(final TEvent eventId, final Object... eventArguments) {
		driver.fire(eventId, eventArguments);
	}

	@Override
	public final boolean isExecuting() {
		return driver.isExecuting();
	}

	@Override
	public final boolean isRunning() {
		return driver.isRunning();
	}

	@Override
	public final int numberOfQueuedEvents() {
		return driver.numberOfQueuedEvents();
	}

	@Override
	public final void start() {
		driver.start();
	}

	@Override
	public final void stop() {
		driver.stop();
	}

	@Override
	public final TState getCurrentState() {
		return driver.getCurrentState();
	}

	@Override
	public void addEventHandler(final StateMachineEventHandler<TState, TEvent> handler) {
		driver.addEventHandler(handler);
	}

	@Override
	public void removeEventHandler(final StateMachineEventHandler<TState, TEvent> handler) {
		driver.removeEventHandler(handler);
	}
}