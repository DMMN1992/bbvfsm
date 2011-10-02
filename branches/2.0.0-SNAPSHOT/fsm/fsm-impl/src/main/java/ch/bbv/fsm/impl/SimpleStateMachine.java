package ch.bbv.fsm.impl;

import ch.bbv.fsm.StateMachine;

public class SimpleStateMachine<TState extends Enum<?>, TEvent extends Enum<?>> extends
		AbstractStateMachine<SimpleStateMachine<TState, TEvent>, TState, TEvent> {

	protected SimpleStateMachine(final StateMachine<TState, TEvent> driver) {
		super(driver);
	}

}
