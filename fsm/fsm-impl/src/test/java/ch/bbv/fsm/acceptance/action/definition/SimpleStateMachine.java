/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Mario Martinez
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
 *     bbv Software Services AG (http://www.bbv.ch), Mario Martinez
 *******************************************************************************/

/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Mario Martinez
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
 *     bbv Software Services AG (http://www.bbv.ch), Mario Martinez
 *******************************************************************************/
package ch.bbv.fsm.acceptance.action.definition;

import java.util.LinkedList;
import java.util.List;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.acceptance.action.definition.SimpleStateMachineDefinition.Event;
import ch.bbv.fsm.acceptance.action.definition.SimpleStateMachineDefinition.State;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.impl.AbstractStateMachine;

public class SimpleStateMachine
		extends
		AbstractStateMachine<SimpleStateMachine, SimpleStateMachineDefinition.State, SimpleStateMachineDefinition.Event> {

	private final List<Action<SimpleStateMachine, State, Event>> callingActions = new LinkedList<Action<SimpleStateMachine, State, Event>>();

	private String log = "";

	public SimpleStateMachine() {
		super(null);
	}

	public SimpleStateMachine(final StateMachine<State, Event> driver) {
		super(driver);
	}

	public void addCallingAction(
			final Action<SimpleStateMachine, State, Event> callingAction) {
		callingActions.add(callingAction);
	}

	public List<Action<SimpleStateMachine, State, Event>> getCallingActions() {
		return callingActions;
	}

	public String consumeLog() {
		return log;
	}

	public void log(final String msg) {
		this.log = msg;
	}

}