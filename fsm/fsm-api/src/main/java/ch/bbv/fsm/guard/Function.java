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
package ch.bbv.fsm.guard;

import ch.bbv.fsm.StateMachine;

/**
 * Defines a function with one parameter.
 * 
 * @author Ueli Kurmann (bbv Software Services AG)
 * @param <ParameterType>
 *            parameter type
 * @param <ReturnType>
 *            return type
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the state enumeration
 * @param <TEvent>
 *            the event enumeration
 */
public interface Function<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>, ParameterType, ReturnType> {

	/**
	 * Executes the function.
	 * 
	 * @param stateMachine
	 *            the calling state machine
	 * @param parameter
	 *            the parameter of the function.
	 * @return the return value of the function.
	 */
	ReturnType execute(TStateMachine stateMachine, ParameterType parameter);

}
