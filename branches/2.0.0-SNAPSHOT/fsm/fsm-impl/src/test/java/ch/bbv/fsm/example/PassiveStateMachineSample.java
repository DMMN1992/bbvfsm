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
package ch.bbv.fsm.example;

import org.junit.Test;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.StateMachineDefinition;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.guard.Function;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

/**
 * Sample showing usage of state machine.
 */
public class PassiveStateMachineSample {
	/**
	 * Announces the floor.
	 */
	private final Action<States, Events> announceFloor = new Action<States, Events>() {
		@Override
		public void execute(final StateMachine<States, Events> stateMachine, final Object... arguments) {
			System.out.println("announceFloor: 1");
		}
	};

	/**
	 * Announces that the elevator is overloaded.
	 */
	private final Action<States, Events> announceOverload = new Action<States, Events>() {
		@Override
		public void execute(final StateMachine<States, Events> stateMachine, final Object... arguments) {
			System.out.println("announceOverload...");
		};
	};

	/**
	 * Checks whether the elevator is overloaded.
	 */
	private final Function<States, Events, Object[], Boolean> checkOverload = new Function<States, Events, Object[], Boolean>() {
		@Override
		public Boolean execute(final StateMachine<States, Events> stateMachine, final Object[] arguments) {
			return true;
		};
	};

	/**
	 * Unit test showing a sample of the state machine usage.
	 */
	@Test
	public void sample() {

		final StateMachineDefinition<States, Events> elevator = new AbstractStateMachineDefinition<States, Events>("Elevator");

		elevator.defineHierarchyOn(States.Healthy, States.OnFloor, HistoryType.DEEP, States.OnFloor, States.Moving);
		elevator.defineHierarchyOn(States.Moving, States.MovingUp, HistoryType.SHALLOW, States.MovingUp, States.MovingDown);
		elevator.defineHierarchyOn(States.OnFloor, States.DoorClosed, HistoryType.NONE, States.DoorClosed, States.DoorOpen);

		elevator.in(States.Healthy).on(Events.ErrorOccured).goTo(States.Error);

		elevator.in(States.Error).on(Events.Reset).goTo(States.Healthy);

		elevator.in(States.OnFloor).executeOnEntry(this.announceFloor).on(Events.CloseDoor).goTo(States.DoorClosed).on(Events.OpenDoor)
				.goTo(States.DoorOpen).on(Events.GoUp).goTo(States.MovingUp).onlyIf(this.checkOverload).on(Events.GoUp)
				.execute(this.announceOverload).on(Events.GoDown).goTo(States.MovingDown).onlyIf(this.checkOverload).on(Events.GoUp)
				.execute(this.announceOverload);

		elevator.in(States.Moving).on(Events.Stop).goTo(States.OnFloor);

		final StateMachine<States, Events> testee = elevator.createPassiveStateMachine("sample", States.OnFloor);

		testee.fire(Events.ErrorOccured);
		testee.fire(Events.Reset);

		testee.start();

		testee.fire(Events.OpenDoor);
		testee.fire(Events.CloseDoor);
		testee.fire(Events.GoUp);
		testee.fire(Events.Stop);
		testee.fire(Events.OpenDoor);

		testee.stop();

		System.out.println("log messages:");

		System.out.println("report:");
		System.out.println(elevator.report());
	}

}
