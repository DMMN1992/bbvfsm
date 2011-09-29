package ch.bbv.fsm.acceptance.pingpong;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

public class PingPongStateMachineDefinion extends
		AbstractStateMachineDefinition<PingPongStateMachine, PingPongStateMachineDefinion.State, PingPongStateMachineDefinion.Event> {

	public enum State {
		Ping, Pong, Finished
	}

	public enum Event {
		Hit, Out, Go
	}

	public PingPongStateMachineDefinion() {
		super(State.Ping);
	}

	@Override
	protected PingPongStateMachine createStateMachine(final StateMachine<State, Event> driver) {
		return new PingPongStateMachine(driver);
	}

	@Override
	protected void define() {
		final PingPongStateMachine pingPongStateMachine = getPrototype();

		in(State.Ping).on(Event.Hit).goTo(State.Pong);
		in(State.Ping).executeOnEntry(pingPongStateMachine.sayPing());
		in(State.Ping).on(Event.Out).goTo(State.Finished).execute(pingPongStateMachine.sayTerminated());

		in(State.Pong).on(Event.Hit).goTo(State.Ping);
		in(State.Pong).executeOnEntry(pingPongStateMachine.sayPong());
		in(State.Pong).on(Event.Out).goTo(State.Finished).execute(pingPongStateMachine.sayTerminated());
	}
}
