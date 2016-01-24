The bbvfsm provides a mechanism for storing and reloading the internal state of a state machine. With this mechanism the state machine can be passivated and later on activated again.

The following sample shows how the state machine is passivated.

```

final StateMachineMemento<State, Event> memento = new RadioStateMachineMemento();
radioStateMachine.passivate(memento);
saveToStore(memento);
```

The following memento class is used:

```

public class RadioStateMachineMemento extends StateMachineMemento<RadioStateMachineDefinion.State, RadioStateMachineDefinion.Event> {}
```

Implementors may add additional fields to this class if the want store other state.

The following sample shows how the state machine is reactivated.

```

final StateMachineMemento<State, Event> memento = loadFromStorage();
radioStateMachine.activate(memento);
```