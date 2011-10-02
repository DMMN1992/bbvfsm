package ch.bbv.fsm.acceptance.radio;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import ch.bbv.fsm.HistoryType;
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
	public void radioWhenSetAMAutoTuningAndHistoryNoneThenPlayMustBeRestored() {
		final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

		final RadioStateMachine radioStateMachine = radioStateMachineDefinion
				.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

		radioStateMachine.start();

		radioStateMachine.fire(Event.TogglePower);
		final String turnOnLog = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.ToggleMode);
		final String toggleMode = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.StationLost);
		final String autoTuning = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.TogglePower);
		final String powerOff = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.TogglePower);
		final String turnOnAgainLog = radioStateMachine.consumeLog();

		assertThat(turnOnLog, is(equalTo("entryOff.exitOff.OffToOn.entryOn.entryFM")));
		assertThat(toggleMode, is(equalTo("exitFM.FMtoAM.entryAM.entryPlay")));
		assertThat(autoTuning, is(equalTo("exitPlay.PlayToAutoTune.entryAutoTune")));
		assertThat(powerOff, is(equalTo("exitAutoTune.exitAM.exitOn.OnToOff.entryOff")));
		assertThat(turnOnAgainLog, is(equalTo("exitOff.OffToOn.entryOn.entryAM.entryPlay")));
	}

	@Test
	public void radioWhenSetAMAutoTuningAndHistoryShallowOnAMThenAutoTuneMustBeRestored() {
		final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion(HistoryType.DEEP, HistoryType.SHALLOW);

		final RadioStateMachine radioStateMachine = radioStateMachineDefinion
				.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

		radioStateMachine.start();

		radioStateMachine.fire(Event.TogglePower);
		final String turnOnLog = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.ToggleMode);
		final String toggleMode = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.StationLost);
		final String autoTuning = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.TogglePower);
		final String powerOff = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.TogglePower);
		final String turnOnAgainLog = radioStateMachine.consumeLog();

		assertThat(turnOnLog, is(equalTo("entryOff.exitOff.OffToOn.entryOn.entryFM")));
		assertThat(toggleMode, is(equalTo("exitFM.FMtoAM.entryAM.entryPlay")));
		assertThat(autoTuning, is(equalTo("exitPlay.PlayToAutoTune.entryAutoTune")));
		assertThat(powerOff, is(equalTo("exitAutoTune.exitAM.exitOn.OnToOff.entryOff")));
		assertThat(turnOnAgainLog, is(equalTo("exitOff.OffToOn.entryOn.entryAM.entryAutoTune")));
	}

	@Test
	public void radioWhenSetAMAutoTuningAndHistoryShallowOnOnThenAutoTuneMustBeRestored() {
		final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion(HistoryType.SHALLOW, HistoryType.SHALLOW);

		final RadioStateMachine radioStateMachine = radioStateMachineDefinion
				.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

		radioStateMachine.start();

		radioStateMachine.fire(Event.TogglePower);
		final String turnOnLog = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.ToggleMode);
		final String toggleMode = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.StationLost);
		final String autoTuning = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.TogglePower);
		final String powerOff = radioStateMachine.consumeLog();

		radioStateMachine.fire(Event.TogglePower);
		final String turnOnAgainLog = radioStateMachine.consumeLog();

		assertThat(turnOnLog, is(equalTo("entryOff.exitOff.OffToOn.entryOn.entryFM")));
		assertThat(toggleMode, is(equalTo("exitFM.FMtoAM.entryAM.entryPlay")));
		assertThat(autoTuning, is(equalTo("exitPlay.PlayToAutoTune.entryAutoTune")));
		assertThat(powerOff, is(equalTo("exitAutoTune.exitAM.exitOn.OnToOff.entryOff")));
		assertThat(turnOnAgainLog, is(equalTo("exitOff.OffToOn.entryOn.entryAM.entryPlay")));
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
