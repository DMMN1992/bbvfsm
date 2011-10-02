package ch.bbv.fsm.acceptance.radio;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

public class RadioStateMachineDefinion extends
		AbstractStateMachineDefinition<RadioStateMachine, RadioStateMachineDefinion.State, RadioStateMachineDefinion.Event> {

	public enum State {
		Off, // Radio is turned off
		On, // Radio is turned on
		Maintenance, // Selfcheck radio
		// 'On' sub-states
		FM, // Playing on FM
		AM, // Playing on AM
		// 'AM' sub-states
		Play, // Play the current frequency
		AutoTune, // Search for station

	}

	public enum Event {
		TogglePower, // Switch on an off
		ToggleMode, // Switch between AM and FM
		StationLost, // Not station on this AM frequency
		StationFound // Found a station's frequency
	}

	public RadioStateMachineDefinion() {
		super(State.Off);
	}

	@Override
	protected RadioStateMachine createStateMachine(final StateMachine<State, Event> driver) {
		return new RadioStateMachine(driver);
	}

	@Override
	protected void define() {
		final RadioStateMachine radioStateMachinePrototype = getPrototype();

		in(State.Off).on(Event.TogglePower).goTo(State.On).execute(radioStateMachinePrototype.logTransitionFromOffToOn())
				.onlyIf(radioStateMachinePrototype.isUserMode());
		in(State.Off).executeOnEntry(radioStateMachinePrototype.logOffEntry());
		in(State.Off).executeOnExit(radioStateMachinePrototype.logOffExit());

		in(State.On).on(Event.TogglePower).goTo(State.Off).execute(radioStateMachinePrototype.logTransitionFromOnToOff());
		in(State.On).executeOnEntry(radioStateMachinePrototype.logOnEntry());
		in(State.On).executeOnExit(radioStateMachinePrototype.logOnExit());

		in(State.Off).on(Event.TogglePower).goTo(State.Maintenance).execute(radioStateMachinePrototype.logTransitionOffToMaintenance())
				.onlyIf(radioStateMachinePrototype.isMaintenanceMode());

		in(State.Maintenance).on(Event.TogglePower).goTo(State.Off).execute(radioStateMachinePrototype.logTransitionFromMaintenanceToOff());
		in(State.Maintenance).executeOnEntry(radioStateMachinePrototype.logMaintenanceEntry());
		in(State.Maintenance).executeOnExit(radioStateMachinePrototype.logMaintenanceExit());

		defineOn(radioStateMachinePrototype);
	}

	private void defineOn(final RadioStateMachine radioStateMachinePrototype) {
		defineHierarchyOn(State.On, State.FM, HistoryType.DEEP, State.FM, State.AM);

		in(State.FM).on(Event.ToggleMode).goTo(State.AM).execute(radioStateMachinePrototype.logTransitionFromFMToAM());
		in(State.FM).executeOnEntry(radioStateMachinePrototype.logFMEntry());
		in(State.FM).executeOnExit(radioStateMachinePrototype.logFMExit());

		in(State.AM).on(Event.ToggleMode).goTo(State.FM).execute(radioStateMachinePrototype.logTransitionFromAMToFM());
		in(State.AM).executeOnEntry(radioStateMachinePrototype.logAMEntry());
		in(State.AM).executeOnExit(radioStateMachinePrototype.logAMExit());

		defineAm(radioStateMachinePrototype);
	}

	private void defineAm(final RadioStateMachine radioStateMachinePrototype) {
		defineHierarchyOn(State.AM, State.Play, HistoryType.NONE, State.Play, State.AutoTune);

		in(State.Play).on(Event.StationLost).goTo(State.AutoTune);
		in(State.AutoTune).on(Event.StationFound).goTo(State.Play);
	}
}
