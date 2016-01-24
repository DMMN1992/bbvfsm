## Example ##
We show now an alternative way for defining actions, using a simplified version of the Radio tuner. **[tuner example](http://code.google.com/p/bbvfsm/wiki/GettingStarted)**

## When should I use this action definition approach? ##

This approach is less transparent for the user that the preferred method (via AbstractStateMachineDefinition.getPrototype(), which uses net.sf.cglib).
We offer this alternative approach in case for example that net.sf.cglib can't be used.

## State Machine definition ##

```


public class RadioStateMachineDefinion
extends
AbstractStateMachineDefinition<RadioStateMachine, RadioStateMachineDefinion.State, RadioStateMachineDefinion.Event> {

public enum State {
Off, // Radio is turned off
On, // Radio is turned on
}

public enum Event {
TogglePower, // Switch on an off
}

public RadioStateMachineDefinion() {
this(HistoryType.DEEP, HistoryType.NONE);
}

public RadioStateMachineDefinion(final HistoryType historyTypeForOn,
final HistoryType historyTypeForAM) {
super(State.Off);
define();
}

@Override
protected RadioStateMachine createStateMachine(
final StateMachine<State, Event> driver) {
return new RadioStateMachine(driver);
}

private void define() {

in(State.Off)
.on(Event.TogglePower)
.goTo(State.On)
.execute(
RadioStateMachineActions.LogTransitionFromOffToOn.class);
in(State.Off)
.executeOnEntry(RadioStateMachineActions.LogOffEntry.class);
in(State.Off).executeOnExit(RadioStateMachineActions.LogOffExit.class);

in(State.On)
.on(Event.TogglePower)
.goTo(State.Off)
.execute(
RadioStateMachineActions.LogTransitionFromOnToOff.class);
in(State.On).executeOnEntry(RadioStateMachineActions.LogOnEntry.class);
in(State.On).executeOnExit(RadioStateMachineActions.LogOnExit.class);

}
}

```

## State Machine Implementation ##

```

public class RadioStateMachine extends
AbstractStateMachine<RadioStateMachine, State, Event> {

private StringBuilder log = new StringBuilder();

public RadioStateMachine(final StateMachine<State, Event> driver) {
super(driver);
}

public RadioStateMachine() {
super(null);
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

public Void logTransitionFromAMToFM() {
addOptionalDot();
log.append("AMtoFM");
return null;
}

public String consumeLog() {
final String result = log.toString();
log = new StringBuilder();
return result;
}

void addOptionalDot() {
if (log.length() > 0) {
log.append('.');
}
}

}
```

## Action Definitions ##


```

public class RadioStateMachineActions {

public static class LogOffEntry implements
Action<RadioStateMachine, State, Event> {

@Override
public void execute(final RadioStateMachine stateMachine,
final Object... arguments) {

stateMachine.logOffEntry();
}

}

public static class LogOffExit implements
Action<RadioStateMachine, State, Event> {

@Override
public void execute(final RadioStateMachine stateMachine,
final Object... arguments) {

stateMachine.logOffExit();
}

}

public static class LogOnEntry implements
Action<RadioStateMachine, State, Event> {

@Override
public void execute(final RadioStateMachine stateMachine,
final Object... arguments) {

stateMachine.logOnEntry();
}

}

public static class LogOnExit implements
Action<RadioStateMachine, State, Event> {

@Override
public void execute(final RadioStateMachine stateMachine,
final Object... arguments) {

stateMachine.logOnExit();
}

}

public static class LogTransitionFromOffToOn implements
Action<RadioStateMachine, State, Event> {

@Override
public void execute(final RadioStateMachine stateMachine,
final Object... arguments) {

stateMachine.logTransitionFromOffToOn();
}

}

public static class LogTransitionFromOnToOff implements
Action<RadioStateMachine, State, Event> {

@Override
public void execute(final RadioStateMachine stateMachine,
final Object... arguments) {

stateMachine.logTransitionFromOnToOff();
}

}

}

```

## Test Case ##
```

public class RadioAcceptanceTest {

@Test
public void radioWhenSimplingTurnOnAndOff() {
final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

final RadioStateMachine radioStateMachine = radioStateMachineDefinion
.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

radioStateMachine.start();

radioStateMachine.fire(Event.TogglePower);
radioStateMachine.fire(Event.TogglePower);

radioStateMachine.terminate();

assertThat(
radioStateMachine.consumeLog(),
is(equalTo("entryOff.exitOff.OffToOn.entryOn.exitOn.OnToOff.entryOff.exitOff")));
}

}

```