package ch.bbv.fsm.example.pingpong;

import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

public class PingPongSample {

	public static void main(final String[] args) {
		AbstractStateMachineDefinition<PingPongStateMachine, State, Event> pingPongDefinition = new PingPongStateMachineDefinion();

		PingPongStateMachine pingPongStateMachine1 = pingPongDefinition
				.createPassiveStateMachine();
		pingPongStateMachine1.setPlayer1("Joe");
		pingPongStateMachine1.setPlayer2("John");

		PingPongStateMachine pingPongStateMachine2 = pingPongDefinition
				.createPassiveStateMachine();
		pingPongStateMachine2.setPlayer1("Michael");
		pingPongStateMachine2.setPlayer2("Lucius");

		pingPongStateMachine1.start();
		pingPongStateMachine2.start();

		pingPongStateMachine1.fire(Event.Hit);
		pingPongStateMachine2.fire(Event.Hit);

		pingPongStateMachine1.stop();
		pingPongStateMachine2.stop();

	}
}
