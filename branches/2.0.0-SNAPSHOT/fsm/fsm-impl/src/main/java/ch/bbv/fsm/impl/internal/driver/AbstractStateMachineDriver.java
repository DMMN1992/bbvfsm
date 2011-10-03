package ch.bbv.fsm.impl.internal.driver;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.internal.statemachine.StateMachineInterpreter;
import ch.bbv.fsm.impl.internal.statemachine.state.StateDictionary;

/**
 * Base implementation for all state machine drivers.
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the enumeration type of the states.
 * @param <TEvent>
 *            the enumeration type of the events.
 */
abstract class AbstractStateMachineDriver<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachine<TState, TEvent> {

	private RunningState runningState = RunningState.Created;

	/**
	 * The internal state machine.
	 */
	private StateMachineInterpreter<TStateMachine, TState, TEvent> stateMachineInterpreter;

	public AbstractStateMachineDriver() {
	}

	/**
	 * Initializes the state machine.
	 * 
	 * @param stateMachine
	 *            the custom state machine
	 * @param name
	 *            the name of the state machine used in the logs.
	 * @param states
	 *            the states
	 */
	public void initialize(final TStateMachine stateMachine, final String name,
			final StateDictionary<TStateMachine, TState, TEvent> states, final TState initialState) {
		this.stateMachineInterpreter = new StateMachineInterpreter<TStateMachine, TState, TEvent>(stateMachine, name, states, initialState);
	}

	@Override
	public RunningState getRunningState() {
		return runningState;
	}

	@Override
	public void start() {
		if (RunningState.Created != getRunningState()) {
			throw new IllegalStateException("Starting the statemachine is not allowed in this state. State is " + getRunningState().name());
		}
		runningState = RunningState.Running;
		stateMachineInterpreter.initialize();
	}

	@Override
	public void terminate() {
		stateMachineInterpreter.terminate();
		runningState = RunningState.Terminated;
	}

	@Override
	public TState getCurrentState() {
		return stateMachineInterpreter.getCurrentStateId();
	}

	/**
	 * Fires the event on the state machine.
	 * 
	 * @param e
	 *            the event to be fired on the state machine.
	 */
	protected void fireEventOnStateMachine(final EventInformation<TEvent> e) {
		stateMachineInterpreter.fire(e.getEventId(), e.getEventArguments());
	}

}
