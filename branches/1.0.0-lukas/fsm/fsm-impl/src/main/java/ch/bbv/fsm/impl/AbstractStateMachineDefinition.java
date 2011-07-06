package ch.bbv.fsm.impl;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.StateMachineDefinition;
import ch.bbv.fsm.dsl.EntryActionSyntax;
import ch.bbv.fsm.events.StateMachineEventHandler;

public class AbstractStateMachineDefinition<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachineDefinition<TState, TEvent> {

	public AbstractStateMachineDefinition(final Class<?> class1) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public EntryActionSyntax<TState, TEvent> in(final TState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void defineHierarchyOn(final TState superStateId,
			final TState initialSubStateId, final HistoryType historyType,
			final TState... subStateIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
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

	@Override
	public TStateMachine createActiveStateMachine(final String name,
			final TState initialState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TStateMachine createPassiveStateMachine(final String name,
			final TState initialState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String report() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StateMachine<TState, TEvent> createActiveStateMachine(
			final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StateMachine<TState, TEvent> createPassiveStateMachine(
			final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	protected TStateMachine getPrototype() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryActionSyntax<TState, TEvent> fromStart() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented");
	}

	public TStateMachine createPassiveStateMachine() {
		return null;
	}
}
