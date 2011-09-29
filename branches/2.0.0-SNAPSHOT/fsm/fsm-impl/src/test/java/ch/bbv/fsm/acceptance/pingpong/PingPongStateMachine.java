package ch.bbv.fsm.acceptance.pingpong;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.acceptance.pingpong.PingPongStateMachineDefinion.Event;
import ch.bbv.fsm.acceptance.pingpong.PingPongStateMachineDefinion.State;
import ch.bbv.fsm.impl.AbstractStateMachine;

public class PingPongStateMachine extends AbstractStateMachine<State, Event> {

	private String player1;
	private String player2;

	private final StringBuilder log = new StringBuilder();

	public PingPongStateMachine(final StateMachine<State, Event> driver) {
		super(driver);
	}

	public PingPongStateMachine() {
		super(null);
	}

	public Void sayPing() {
		log.append(player1 + " ping\n");
		return null;
	}

	public Void sayPong() {
		log.append(player2 + " pong\n");
		return null;
	}

	public void setPlayer1(final String player1) {
		this.player1 = player1;
	}

	public void setPlayer2(final String player2) {
		this.player2 = player2;
	}

	public Void sayStarted() {
		log.append("Started\n");
		return null;
	}

	public Void sayTerminated() {
		log.append("Terminated\n");
		return null;
	}

	public String getLog() {
		return log.toString();
	}

	public boolean checkTermination() {
		return false;
	}
}
