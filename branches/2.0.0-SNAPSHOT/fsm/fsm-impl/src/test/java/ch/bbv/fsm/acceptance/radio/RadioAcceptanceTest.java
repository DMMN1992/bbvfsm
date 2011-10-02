package ch.bbv.fsm.acceptance.radio;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import ch.bbv.fsm.acceptance.radio.RadioStateMachineDefinion.Event;

public class RadioAcceptanceTest {

	@Test
	public void radioWhenSimplingTurnOnAndOffThenPlayFM() {
		final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

		final RadioStateMachine radioStateMachine = radioStateMachineDefinion
				.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

		radioStateMachine.start();

		radioStateMachine.fire(Event.TogglePower);
		radioStateMachine.fire(Event.TogglePower);

		radioStateMachine.terminate();

		assertThat(radioStateMachine.consumeLog(),
				is(equalTo("entryOff.exitOff.OffToOn.entryOn.entryFM.exitFM.exitOn.OnToOff.entryOff.exitOff")));
	}

	@Test
	public void radioWhenTuningThenHistoryMustBeRestored() {
		final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

		final RadioStateMachine radioStateMachine = radioStateMachineDefinion
				.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

		radioStateMachine.start();

		radioStateMachine.fire(Event.TogglePower);
		final String turnOnLog = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.ToggleMode);
		final String toggleMode = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.TogglePower);
		final String powerOff = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.TogglePower);
		final String turnOnAgainLog = radioStateMachine.consumeLog();

		assertThat(turnOnLog, is(equalTo("entryOff.exitOff.OffToOn.entryOn.entryFM")));
		assertThat(toggleMode, is(equalTo("exitFM.FMtoAM.entryAM")));
		assertThat(powerOff, is(equalTo("exitAM.exitOn.OnToOff.entryOff")));
		assertThat(turnOnAgainLog, is(equalTo("exitOff.OffToOn.entryOn.entryAM")));
	}

	@Test
	public void radioWhenMaintenanceThenStateMustBeMaintenance() {
		final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

		final RadioStateMachine radioStateMachine = radioStateMachineDefinion
				.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

		radioStateMachine.start();

		radioStateMachine.setMaintenance(true);
		radioStateMachine.fire(Event.TogglePower);
		final String turnOnLog = radioStateMachine.consumeLog();

		assertThat(turnOnLog, is(equalTo("entryOff.exitOff.OffToMaintenance.entryMaintenance")));
	}
}
