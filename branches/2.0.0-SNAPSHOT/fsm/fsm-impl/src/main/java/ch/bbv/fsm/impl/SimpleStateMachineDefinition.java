package ch.bbv.fsm.impl;

import ch.bbv.fsm.StateMachine;

public class SimpleStateMachineDefinition<TState extends Enum<?>, TEvent extends Enum<?>> extends
		AbstractStateMachineDefinition<SimpleStateMachine<TState, TEvent>, TState, TEvent> {

	public SimpleStateMachineDefinition(final String name, final TState initialState) {
		super(name, initialState);
	}

	@Override
	protected SimpleStateMachine<TState, TEvent> createStateMachine(final StateMachine<TState, TEvent> driver) {
		return new SimpleStateMachine(driver);
	}

}
