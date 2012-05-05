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
package ch.bbv.fsm.impl.internal.statemachine.transition;

import java.util.List;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.internal.statemachine.state.State;
import ch.bbv.fsm.impl.internal.statemachine.state.StateImpl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Mapping between a state and its transitions.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class TransitionDictionaryImpl<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements TransitionDictionary<TStateMachine, TState, TEvent> {

	/**
	 * The state this transitions belong to.
	 */
	private final State<TStateMachine, TState, TEvent> state;

	private final Multimap<TEvent, Transition<TStateMachine, TState, TEvent>> transitions;

	/**
	 * Creates a new instance.
	 * 
	 * @param state
	 *            the state this transitions belong to.
	 */
	public TransitionDictionaryImpl(final StateImpl<TStateMachine, TState, TEvent> state) {
		this.state = state;
		this.transitions = HashMultimap.create();
	}

	@Override
	public void add(final TEvent eventId, final Transition<TStateMachine, TState, TEvent> transition) {
		transition.setSource(this.state);
		this.transitions.put(eventId, transition);
	}

	@Override
	public List<TransitionInfo<TStateMachine, TState, TEvent>> getTransitions() {
		final List<TransitionInfo<TStateMachine, TState, TEvent>> list = Lists.newArrayList();
		for (final TEvent eventId : this.transitions.keySet()) {
			this.getTransitionsOfEvent(eventId, list);
		}

		return list;
	}

	@Override
	public List<Transition<TStateMachine, TState, TEvent>> getTransitions(final TEvent eventId) {
		return ImmutableList.copyOf(this.transitions.get(eventId));
	}

	/**
	 * Returns all transitions of an event.
	 * 
	 * @param eventId
	 *            the event id
	 * @param list
	 *            the list of transitions
	 */
	private void getTransitionsOfEvent(final TEvent eventId, final List<TransitionInfo<TStateMachine, TState, TEvent>> list) {
		for (final Transition<TStateMachine, TState, TEvent> transition : this.transitions.get(eventId)) {
			list.add(new TransitionInfo<TStateMachine, TState, TEvent>(eventId, transition.getSource(), transition.getTarget(), transition
					.getGuard() != null, transition.getActions().size()));
		}
	}
}