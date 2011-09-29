package ch.bbv.fsm.acceptance.pingpong;

import org.junit.Test;

import ch.bbv.fsm.acceptance.pingpong.PingPongStateMachineDefinion.Event;

public class PingPongAcceptanceTest {

	@Test
	public void basicUsage() {
		final PingPongStateMachineDefinion pingPongDefinition = new PingPongStateMachineDefinion();

		final PingPongStateMachine pingPongStateMachine = pingPongDefinition.createPassiveStateMachine("SimpleTest");
		pingPongStateMachine.setPlayer1("Joe");
		pingPongStateMachine.setPlayer2("John");

		pingPongStateMachine.start();

		pingPongStateMachine.fire(Event.Hit);
		pingPongStateMachine.fire(Event.Hit);

		pingPongStateMachine.fire(Event.Out);

		pingPongStateMachine.stop();

		System.out.println(pingPongStateMachine.getLog());
	}
}
