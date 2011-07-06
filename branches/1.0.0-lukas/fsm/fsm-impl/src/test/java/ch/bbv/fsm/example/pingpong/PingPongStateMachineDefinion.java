package ch.bbv.fsm.example.pingpong;

import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

enum State {
	Ping, Pong
}

enum Event {
	Hit, Out, Go
}

public class PingPongStateMachineDefinion extends
		AbstractStateMachineDefinition<PingPongStateMachine, State, Event> {

	public PingPongStateMachineDefinion() {
		super(PingPongStateMachine.class);
	}

	public void setup() {
		PingPongStateMachine pingPongStateMachine = getPrototype();

		fromStart().on(Event.Go).goTo(State.Pong);

		in(State.Ping).on(Event.Hit).goTo(State.Pong);
		in(State.Ping).executeOnEntry(pingPongStateMachine.sayPing());
		in(State.Ping).on(Event.Out).terminate();

		in(State.Pong).on(Event.Hit).goTo(State.Ping);
		in(State.Pong).executeOnEntry(pingPongStateMachine.sayPong());
		in(State.Pong).on(Event.Out).terminate();
	}
}
