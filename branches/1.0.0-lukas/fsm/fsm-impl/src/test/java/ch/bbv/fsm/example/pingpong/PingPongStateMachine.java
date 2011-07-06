package ch.bbv.fsm.example.pingpong;

import ch.bbv.fsm.impl.AbstractStateMachine;

public class PingPongStateMachine extends AbstractStateMachine<State, Event> {

	private String player1;
	private String player2;

	public Void sayPing() {
		System.out.println(player1 + " ping");
		return null;
	}

	public Void sayPong() {
		System.out.println(player2 + " pong");
		return null;
	}

	public void setPlayer1(final String player1) {
		this.player1 = player1;
	}

	public void setPlayer2(final String player2) {
		this.player2 = player2;
	}

}
