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
package ch.bbv.fsm.impl.internal;

import ch.bbv.fsm.Action;
import ch.bbv.fsm.Function;
import ch.bbv.fsm.dsl.EntryActionSyntax;
import ch.bbv.fsm.dsl.EventActionSyntax;
import ch.bbv.fsm.dsl.EventSyntax;
import ch.bbv.fsm.dsl.ExecuteSyntax;
import ch.bbv.fsm.dsl.ExitActionSyntax;
import ch.bbv.fsm.dsl.GotoSyntax;
import ch.bbv.fsm.impl.internal.aop.MethodCallImpl;
import ch.bbv.fsm.impl.internal.state.State;
import ch.bbv.fsm.impl.internal.state.StateDictionary;
import ch.bbv.fsm.impl.internal.transition.Transition;
import ch.bbv.fsm.impl.internal.transition.TransitionImpl;

/**
 * State Builder.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG).
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public class StateBuilder<TState extends Enum<?>, TEvent extends Enum<?>>
		implements EntryActionSyntax<TState, TEvent>,
		EventActionSyntax<TState, TEvent>, ExecuteSyntax<TState, TEvent>,
		GotoSyntax<TState, TEvent> {

	private final State<TState, TEvent> state;
	private final StateDictionary<TState, TEvent> stateDictionary;
	private Transition<TState, TEvent> currentTransition;

	/**
	 * Creates a new instance.
	 * 
	 * @param state
	 *            the state
	 * @param stateDictionary
	 *            the state dictionary
	 */
	public StateBuilder(final State<TState, TEvent> state,
			final StateDictionary<TState, TEvent> stateDictionary) {
		this.state = state;
		this.stateDictionary = stateDictionary;
	}

	@Override
	public ExecuteSyntax<TState, TEvent> execute(
			final Action<TState, TEvent> action) {
		this.currentTransition.getActions().add(action);
		return this;
	}

	@Override
	public ExecuteSyntax<TState, TEvent> execute(final Object methodCall) {
		this.currentTransition.getActions().add(
				new MethodCallAction<TState, TEvent>(MethodCallImpl.pop()));
		return this;
	}

	@Override
	public ExitActionSyntax<TState, TEvent> executeOnEntry(
			final Action<TState, TEvent> action) {
		this.state.setEntryAction(new ActionHolderNoParameter<TState, TEvent>(
				action));
		return this;
	}

	@Override
	public <T> ExitActionSyntax<TState, TEvent> executeOnEntry(
			final Action<TState, TEvent> action, final T parameter) {
		this.state.setEntryAction(new ActionHolderParameter<TState, TEvent, T>(
				action, parameter));
		return this;
	}

	@Override
	public ExitActionSyntax<TState, TEvent> executeOnEntry(final Object action) {
		this.state.setEntryAction(new ActionHolderMethodCall<TState, TEvent>(
				MethodCallImpl.pop()));
		return this;
	}

	@Override
	public ExitActionSyntax<TState, TEvent> executeOnExit(
			final Action<TState, TEvent> action) {
		this.state.setExitAction(new ActionHolderNoParameter<TState, TEvent>(
				action));
		return this;
	}

	@Override
	public <T> EventSyntax<TState, TEvent> executeOnExit(
			final Action<TState, TEvent> action, final T parameter) {
		this.state.setExitAction(new ActionHolderParameter<TState, TEvent, T>(
				action, parameter));
		return this;
	}

	@Override
	public ExitActionSyntax<TState, TEvent> executeOnExit(final Object action) {
		this.state.setExitAction(new ActionHolderMethodCall<TState, TEvent>(
				MethodCallImpl.pop()));
		return this;
	}

	@Override
	public ExecuteSyntax<TState, TEvent> goTo(final TState target) {
		this.currentTransition.setTarget(this.stateDictionary.getState(target));
		return this;
	}

	@Override
	public EventActionSyntax<TState, TEvent> on(final TEvent eventId) {
		this.currentTransition = new TransitionImpl<TState, TEvent>();
		this.state.getTransitions().add(eventId, this.currentTransition);
		return this;
	}

	@Override
	public EventSyntax<TState, TEvent> onlyIf(final boolean guard) {
		this.currentTransition.setGuard(new MethodCallFunction<TState, TEvent>(
				MethodCallImpl.pop()));
		return this;

	}

	@Override
	public EventSyntax<TState, TEvent> onlyIf(
			final Function<TState, TEvent, Object[], Boolean> guard) {
		this.currentTransition.setGuard(guard);
		return this;
	}
}
