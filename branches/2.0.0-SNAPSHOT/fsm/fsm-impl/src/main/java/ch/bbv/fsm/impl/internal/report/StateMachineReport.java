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
package ch.bbv.fsm.impl.internal.report;

import java.util.List;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.internal.statemachine.state.State;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionInfo;

/**
 * State Machine Report.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class StateMachineReport<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {
	private static final String INDENTATION = "    ";
	private static final String NEWLINE = System.getProperty("line.separator");

	/**
	 * Returns a string that describes this state machine.
	 * 
	 * @param name
	 *            the name of the state machine
	 * @param states
	 *            the list of states.
	 * @param initialStateId
	 *            the initial state
	 * @return description of the state machine.
	 */
	public String report(final String name, final List<State<TStateMachine, TState, TEvent>> states, final TState initialStateId) {
		final StringBuilder report = new StringBuilder();

		final String indentation = INDENTATION;

		report.append(String.format("%s: initial state = %s%s", name, initialStateId.toString(), NEWLINE));

		// write states
		for (final State<TStateMachine, TState, TEvent> state : states) {
			if (state.getSuperState() == null) {
				this.reportState(state, report, indentation);
			}
		}

		return report.toString();
	}

	/**
	 * Creates the part of the report for the specified state.
	 * 
	 * @param state
	 *            the state
	 * @param report
	 *            the report to add to.
	 * @param indentation
	 *            the current indentation level.
	 */
	private void reportState(final State<TStateMachine, TState, TEvent> state, final StringBuilder report, final String indentation) {
		this.reportStateNameInitialStateHistoryTypeEntryAndExitAction(report, indentation, state);

		final String nextIndentation = indentation + INDENTATION;

		for (final TransitionInfo<TStateMachine, TState, TEvent> transition : state.getTransitions().getTransitions()) {
			this.reportTransition(report, nextIndentation, transition);
		}

		for (final State<TStateMachine, TState, TEvent> subState : state.getSubStates()) {
			this.reportState(subState, report, nextIndentation);
		}
	}

	/**
	 * Reports the state name, initial state, history type, entry and exit action.
	 * 
	 * @param report
	 *            the report.
	 * @param indentation
	 *            the indentation.
	 * @param state
	 *            the state
	 */
	private void reportStateNameInitialStateHistoryTypeEntryAndExitAction(final StringBuilder report, final String indentation,
			final State<TStateMachine, TState, TEvent> state) {
		report.append(String.format("%s %s: initial state = %s history type = %s %s", indentation, state,
				state.getInitialState() != null ? state.getInitialState().toString() : "None", state.getHistoryType(), NEWLINE));
		final String newIndentation = indentation + INDENTATION;
		report.append(String.format("%s entry action: %s %s", newIndentation, state.getEntryAction() != null, NEWLINE));
		report.append(String.format("%s exit action: %s %s", newIndentation, state.getExitAction() != null, NEWLINE));
	}

	/**
	 * Reports the transition.
	 * 
	 * @param report
	 *            the report.
	 * @param indentation
	 *            the indentation.
	 * @param transition
	 *            the transition.
	 */
	private void reportTransition(final StringBuilder report, final String indentation, final TransitionInfo<TStateMachine, TState, TEvent> transition) {
		report.append(String.format("%s%s -> %s actions: %s guard:%s%s", indentation, transition.getEventId(),
				transition.getTarget() != null ? transition.getTarget().toString() : "internal", transition.getActions(),
				transition.hasGuard(), NEWLINE));
	}
}
