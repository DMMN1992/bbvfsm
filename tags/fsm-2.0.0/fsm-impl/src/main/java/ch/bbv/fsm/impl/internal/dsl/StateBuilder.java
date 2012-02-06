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
package ch.bbv.fsm.impl.internal.dsl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.dsl.EntryActionSyntax;
import ch.bbv.fsm.dsl.EventActionSyntax;
import ch.bbv.fsm.dsl.EventSyntax;
import ch.bbv.fsm.dsl.ExecuteSyntax;
import ch.bbv.fsm.dsl.ExitActionSyntax;
import ch.bbv.fsm.dsl.GotoSyntax;
import ch.bbv.fsm.dsl.IllegalActionClassDefinitionException;
import ch.bbv.fsm.guard.Function;
import ch.bbv.fsm.impl.internal.action.ActionHolderMethodCall;
import ch.bbv.fsm.impl.internal.action.ActionHolderNoParameter;
import ch.bbv.fsm.impl.internal.action.ActionHolderParameter;
import ch.bbv.fsm.impl.internal.action.ActionClassCallAction;
import ch.bbv.fsm.impl.internal.action.MethodCallAction;
import ch.bbv.fsm.impl.internal.action.MethodCallFunction;
import ch.bbv.fsm.impl.internal.aop.CallInterceptorBuilder;
import ch.bbv.fsm.impl.internal.aop.MethodCall;
import ch.bbv.fsm.impl.internal.statemachine.state.State;
import ch.bbv.fsm.impl.internal.statemachine.state.StateDictionary;
import ch.bbv.fsm.impl.internal.statemachine.transition.Transition;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionImpl;

/**
 * State Builder.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG).
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public class StateBuilder<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements EntryActionSyntax<TStateMachine, TState, TEvent>,
		EventActionSyntax<TStateMachine, TState, TEvent>,
		ExecuteSyntax<TStateMachine, TState, TEvent>,
		GotoSyntax<TStateMachine, TState, TEvent> {

	private static final Logger LOG = LoggerFactory
			.getLogger(StateBuilder.class);

	private final State<TStateMachine, TState, TEvent> state;
	private final StateDictionary<TStateMachine, TState, TEvent> stateDictionary;
	private Transition<TStateMachine, TState, TEvent> currentTransition;

	/**
	 * Creates a new instance.
	 * 
	 * @param state
	 *            the state
	 * @param stateDictionary
	 *            the state dictionary
	 */
	public StateBuilder(final State<TStateMachine, TState, TEvent> state,
			final StateDictionary<TStateMachine, TState, TEvent> stateDictionary) {
		this.state = state;
		this.stateDictionary = stateDictionary;
	}

	@Override
	public ExecuteSyntax<TStateMachine, TState, TEvent> execute(
			final Object methodCall) {
		final MethodCall<TStateMachine> call = CallInterceptorBuilder.pop();
		LOG.debug(currentTransition.toString() + " use action " + call);
		this.currentTransition.getActions().add(
				new MethodCallAction<TStateMachine, TState, TEvent>(call));
		return this;
	}

	@Override
	public ExecuteSyntax<TStateMachine, TState, TEvent> execute(
			final Class<? extends Action<TStateMachine, TState, TEvent>> embeddedAction) {

		checkAction(embeddedAction);

		this.currentTransition.getActions().add(
				new ActionClassCallAction<TStateMachine, TState, TEvent>(
						embeddedAction));
		return this;
	}

	@Override
	public ExitActionSyntax<TStateMachine, TState, TEvent> executeOnEntry(
			final Class<? extends Action<TStateMachine, TState, TEvent>> action) {

		checkAction(action);

		this.state
				.setEntryAction(new ActionHolderNoParameter<TStateMachine, TState, TEvent>(
						new ActionClassCallAction<TStateMachine, TState, TEvent>(
								action)));

		return this;
	}

	@Override
	public <T> ExitActionSyntax<TStateMachine, TState, TEvent> executeOnEntry(
			final Class<? extends Action<TStateMachine, TState, TEvent>> actionClass,
			final T parameter) {

		checkAction(actionClass);

		this.state
				.setEntryAction(new ActionHolderParameter<TStateMachine, TState, TEvent, T>(
						new ActionClassCallAction<TStateMachine, TState, TEvent>(
								actionClass), parameter));
		return this;
	}

	@Override
	public ExitActionSyntax<TStateMachine, TState, TEvent> executeOnEntry(
			final Void action) {
		MethodCall<TStateMachine> call = CallInterceptorBuilder.pop();
		this.state
				.setEntryAction(new ActionHolderMethodCall<TStateMachine, TState, TEvent>(
						call));
		return this;
	}

	@Override
	public ExitActionSyntax<TStateMachine, TState, TEvent> executeOnExit(
			final Object action) {
		MethodCall<TStateMachine> call = CallInterceptorBuilder.pop();
		this.state
				.setExitAction(new ActionHolderMethodCall<TStateMachine, TState, TEvent>(
						call));
		return this;
	}

	@Override
	public ExitActionSyntax<TStateMachine, TState, TEvent> executeOnExit(
			final Class<? extends Action<TStateMachine, TState, TEvent>> actionClass) {

		checkAction(actionClass);

		this.state
				.setExitAction(new ActionHolderNoParameter<TStateMachine, TState, TEvent>(
						new ActionClassCallAction<TStateMachine, TState, TEvent>(
								actionClass)));

		return this;
	}

	@Override
	public <T> EventSyntax<TStateMachine, TState, TEvent> executeOnExit(
			final Class<? extends Action<TStateMachine, TState, TEvent>> actionClass,
			final T parameter) {

		checkAction(actionClass);

		this.state
				.setExitAction(new ActionHolderParameter<TStateMachine, TState, TEvent, T>(
						new ActionClassCallAction<TStateMachine, TState, TEvent>(
								actionClass), parameter));
		return this;
	}

	@Override
	public ExecuteSyntax<TStateMachine, TState, TEvent> goTo(final TState target) {
		this.currentTransition.setTarget(this.stateDictionary.getState(target));
		return this;
	}

	@Override
	public EventActionSyntax<TStateMachine, TState, TEvent> on(
			final TEvent eventId) {
		this.currentTransition = new TransitionImpl<TStateMachine, TState, TEvent>();
		this.state.getTransitions().add(eventId, this.currentTransition);
		return this;
	}

	@Override
	public EventSyntax<TStateMachine, TState, TEvent> onlyIf(final boolean guard) {
		final MethodCall<TStateMachine> call = CallInterceptorBuilder.pop();
		LOG.debug(currentTransition.toString() + " use guard " + call);
		this.currentTransition
				.setGuard(new MethodCallFunction<TStateMachine, TState, TEvent>(
						call));
		return this;

	}

	@Override
	public EventSyntax<TStateMachine, TState, TEvent> onlyIf(
			final Function<TStateMachine, TState, TEvent, Object[], Boolean> guard) {
		this.currentTransition.setGuard(guard);
		return this;
	}

	/**
	 * @param action
	 *            the action class
	 * @throws IllegalActionClassDefinitionException
	 *             if the action definition is not valid
	 */
	private void checkAction(
			final Class<? extends Action<TStateMachine, TState, TEvent>> action) {

		if (!isValidActionDefinition(action)) {
			throw new IllegalActionClassDefinitionException(
					"Action class "
							+ action.getName()
							+ " must be either static or not have a declaring class. The default constructor must also be accesible.",
					null);
		}
	}

	/**
	 * @param action
	 * @return
	 */
	private boolean isValidActionDefinition(
			final Class<? extends Action<TStateMachine, TState, TEvent>> action) {
		try {

			return isRegularOrStaticClass(action)
					&& hasDefaultConstructor(action) && isInstanciable(action);

		} catch (Exception e) {
			// In case of no default constructor.
			return false;
		}
	}

	/**
	 * @param action
	 * @return
	 */
	private boolean isRegularOrStaticClass(
			final Class<? extends Action<TStateMachine, TState, TEvent>> action) {

		return Modifier.isStatic(action.getModifiers())
				|| action.getDeclaringClass() == null;
	}

	/**
	 * @param action
	 * @return
	 * @throws NoSuchMethodException
	 */
	private boolean hasDefaultConstructor(
			final Class<? extends Action<TStateMachine, TState, TEvent>> action)
			throws NoSuchMethodException {

		return action.getConstructor(new Class[0]) != null;
	}

	/**
	 * @param action
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private boolean isInstanciable(
			final Class<? extends Action<TStateMachine, TState, TEvent>> action)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		return action.getConstructor(new Class[0]).newInstance() != null;
	}
}
