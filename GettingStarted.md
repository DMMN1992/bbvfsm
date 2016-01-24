# Getting Started #
bbvfsm is a hierarchical, enum-based state machine. This mini tutorial explains some of the features of the state machine.

## Download ##
Download the current release and add the jar file to the classpath.

Or using maven:

**Parent group-id:** ch.bbv.fsm
  * Artifact Id: fsm
  * Artifact Id: fsm-api
  * Artifact Id: fsm-impl


## Example ##
We will explain the main features of the state machine by a tuner.

![http://bbvfsm.googlecode.com/svn/wiki/tuner.png](http://bbvfsm.googlecode.com/svn/wiki/tuner.png)

### Definition of the State-Machine ###
The snipped below shows the definition of the state-machine. This example uses a special syntax to describe the methods to be invoked on certain events. Java does not support function parameters. Therefore we did some magic in the background to provide an easy to use declaration of the state-statemachine.

If you don't like this syntax, you can:

  * use anonymous classes and implement the Action interface (version 1.x).
  * Reference an "action class" that implements Action  (since version 2.0). [example](http://code.google.com/p/bbvfsm/wiki/ActionDefinition)

```


public class RadioStateMachineDefinion extends
AbstractStateMachineDefinition<RadioStateMachine,
RadioStateMachineDefinion.State, RadioStateMachineDefinion.Event> {

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

private final HistoryType historyTypeForOn;
private final HistoryType historyTypeForAM;

public RadioStateMachineDefinion() {
this(HistoryType.DEEP, HistoryType.NONE);
}

public RadioStateMachineDefinion(final HistoryType historyTypeForOn, final HistoryType historyTypeForAM) {
super(State.Off);
this.historyTypeForOn = historyTypeForOn;
this.historyTypeForAM = historyTypeForAM;
define();
}

@Override
protected RadioStateMachine createStateMachine(final StateMachine<State, Event> driver) {
return new RadioStateMachine(driver);
}

private void define() {
final RadioStateMachine radioStateMachinePrototype = getPrototype();
in(State.Off).on(Event.TogglePower).goTo(State.On)
.execute(radioStateMachinePrototype.logTransitionFromOffToOn())
.onlyIf(radioStateMachinePrototype.isUserMode());

in(State.Off).executeOnEntry(radioStateMachinePrototype.logOffEntry());
in(State.Off).executeOnExit(radioStateMachinePrototype.logOffExit());

in(State.On).on(Event.TogglePower).goTo(State.Off)
.execute(radioStateMachinePrototype.logTransitionFromOnToOff());
in(State.On).executeOnEntry(radioStateMachinePrototype.logOnEntry());
in(State.On).executeOnExit(radioStateMachinePrototype.logOnExit());

in(State.Off).on(Event.TogglePower).goTo(State.Maintenance)
.execute(radioStateMachinePrototype.logTransitionOffToMaintenance())
.onlyIf(radioStateMachinePrototype.isMaintenanceMode());

in(State.Maintenance).on(Event.TogglePower).goTo(State.Off)
.execute(radioStateMachinePrototype.logTransitionFromMaintenanceToOff());

in(State.Maintenance).executeOnEntry(radioStateMachinePrototype.logMaintenanceEntry());
in(State.Maintenance).executeOnExit(radioStateMachinePrototype.logMaintenanceExit());

defineOn(radioStateMachinePrototype);
}

private void defineOn(final RadioStateMachine radioStateMachinePrototype) {
defineHierarchyOn(State.On, State.FM, historyTypeForOn, State.FM, State.AM);

in(State.FM).on(Event.ToggleMode).goTo(State.AM)
.execute(radioStateMachinePrototype.logTransitionFromFMToAM());
in(State.FM).executeOnEntry(radioStateMachinePrototype.logFMEntry());
in(State.FM).executeOnExit(radioStateMachinePrototype.logFMExit());

in(State.AM).on(Event.ToggleMode).goTo(State.FM)
.execute(radioStateMachinePrototype.logTransitionFromAMToFM());
in(State.AM).executeOnEntry(radioStateMachinePrototype.logAMEntry());
in(State.AM).executeOnExit(radioStateMachinePrototype.logAMExit());

defineAM(radioStateMachinePrototype);
}

private void defineAM(final RadioStateMachine radioStateMachinePrototype) {
defineHierarchyOn(State.AM, State.Play, historyTypeForAM, State.Play, State.AutoTune);
in(State.Play).on(Event.StationLost).goTo(State.AutoTune)
.execute(radioStateMachinePrototype.logTransitionFromPlayToAutoTune());
in(State.Play).executeOnEntry(radioStateMachinePrototype.logPlayEntry());
in(State.Play).executeOnExit(radioStateMachinePrototype.logPlayExit());
in(State.AutoTune).on(Event.StationFound).goTo(State.Play)
.execute(radioStateMachinePrototype.logTransitionFromAutoTuneToPlay());
in(State.AutoTune).executeOnEntry(radioStateMachinePrototype.logAutoTuneEntry());
in(State.AutoTune).executeOnExit(radioStateMachinePrototype.logAutoTuneExit());
}
}
```

## State Machine Implementation ##
```

public class RadioStateMachine extends AbstractStateMachine<RadioStateMachine, State, Event> {

private StringBuilder log = new StringBuilder();
private boolean maintenance = false;

public RadioStateMachine(final StateMachine<State, Event> driver) {
super(driver);
}

public RadioStateMachine() {
super(null);
}

public boolean isUserMode() {
return !maintenance;
}

public boolean isMaintenanceMode() {
return maintenance;
}

public Void logOffEntry() {
addOptionalDot();
log.append("entryOff");
return null;
}

public Void logOnEntry() {
addOptionalDot();
log.append("entryOn");
return null;
}

public Void logFMEntry() {
addOptionalDot();
log.append("entryFM");
return null;
}

public Void logTransitionFromOffToOn() {
addOptionalDot();
log.append("OffToOn");
return null;
}

public Void logOffExit() {
addOptionalDot();
log.append("exitOff");
return null;
}

public Void logTransitionFromOnToOff() {
addOptionalDot();
log.append("OnToOff");
return null;
}

public Void logOnExit() {
addOptionalDot();
log.append("exitOn");
return null;
}

public Void logFMExit() {
addOptionalDot();
log.append("exitFM");
return null;
}

public Void logTransitionFromFMToAM() {
addOptionalDot();
log.append("FMtoAM");
return null;
}

public Void logTransitionFromAMToFM() {
addOptionalDot();
log.append("AMtoFM");
return null;
}

public Void logAMEntry() {
addOptionalDot();
log.append("entryAM");
return null;
}

public Void logAMExit() {
addOptionalDot();
log.append("exitAM");
return null;
}

public Void logTransitionOffToMaintenance() {
addOptionalDot();
log.append("OffToMaintenance");
return null;
}

public Void logTransitionFromMaintenanceToOff() {
addOptionalDot();
log.append("MaintenanceToOff");
return null;
}

public Void logMaintenanceEntry() {
addOptionalDot();
log.append("entryMaintenance");
return null;
}

public Void logMaintenanceExit() {
addOptionalDot();
log.append("exitMaintenance");
return null;
}

public Void logTransitionFromPlayToAutoTune() {
addOptionalDot();
log.append("PlayToAutoTune");
return null;
}

public Void logPlayEntry() {
addOptionalDot();
log.append("entryPlay");
return null;
}

public Void logPlayExit() {
addOptionalDot();
log.append("exitPlay");
return null;
}

public Void logTransitionFromAutoTuneToPlay() {
addOptionalDot();
log.append("AutoTuneToPlay");
return null;
}

public Void logAutoTuneEntry() {
addOptionalDot();
log.append("entryAutoTune");
return null;
}

public Void logAutoTuneExit() {
addOptionalDot();
log.append("exitAutoTune");
return null;
}

public String consumeLog() {
final String result = log.toString();
log = new StringBuilder();
return result;
}

private void addOptionalDot() {
if (log.length() > 0) {
log.append('.');
}
}

public void setMaintenance(final boolean maintenance) {
this.maintenance = maintenance;
}
}

```

### Some Test-Cases ###
```

public class RadioAcceptanceTest {

@Test
public void radioWhenSimplingTurnOnAndOffThenPlayFM() {
final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

final RadioStateMachine radioStateMachine = radioStateMachineDefinion.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

radioStateMachine.start();

radioStateMachine.fire(Event.TogglePower);
radioStateMachine.fire(Event.TogglePower);

radioStateMachine.terminate();

assertThat(radioStateMachine.consumeLog(), is(equalTo("entryOff.exitOff.OffToOn.entryOn.entryFM.exitFM.exitOn.OnToOff.entryOff.exitOff")));
}

@Test
public void radioWhenSetAMAutoTuningAndHistoryNoneThenPlayMustBeRestored() {
final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

final RadioStateMachine radioStateMachine = radioStateMachineDefinion.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

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

final RadioStateMachine radioStateMachine = radioStateMachineDefinion.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

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

final RadioStateMachine radioStateMachine = radioStateMachineDefinion.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

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

final RadioStateMachine radioStateMachine = radioStateMachineDefinion.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

radioStateMachine.start();

radioStateMachine.setMaintenance(true);
radioStateMachine.fire(Event.TogglePower);
final String turnOnLog = radioStateMachine.consumeLog();

assertThat(turnOnLog, is(equalTo("entryOff.exitOff.OffToMaintenance.entryMaintenance")));
}

@Test
public void parallelUsageMustNotInfluenceEachOther() {
final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

final RadioStateMachine radioStateMachine1 = radioStateMachineDefinion.createPassiveStateMachine("fsm1");
final RadioStateMachine radioStateMachine2 = radioStateMachineDefinion.createPassiveStateMachine("fsm2");

radioStateMachine1.start();
radioStateMachine2.start();

radioStateMachine1.setMaintenance(true);
radioStateMachine1.fire(Event.TogglePower);
final String turnOnLog1 = radioStateMachine1.consumeLog();

radioStateMachine2.fire(Event.TogglePower);
final String turnOnLog2 = radioStateMachine2.consumeLog();

assertThat(turnOnLog1, is(equalTo("entryOff.exitOff.OffToMaintenance.entryMaintenance")));
assertThat(turnOnLog2, is(equalTo("entryOff.exitOff.OffToOn.entryOn.entryFM")));
}

}

```