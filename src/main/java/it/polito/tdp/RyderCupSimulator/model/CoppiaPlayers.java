package it.polito.tdp.RyderCupSimulator.model;

public class CoppiaPlayers {

	private Player player1;
	private Player player2;
	private String squadra;
	public CoppiaPlayers(Player player1, Player player2, String squadra) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.squadra = squadra;
	}
	public Player getPlayer1() {
		return player1;
	}
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}
	public Player getPlayer2() {
		return player2;
	}
	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	public String getSquadra() {
		return squadra;
	}
	public void setSquadra(String squadra) {
		this.squadra = squadra;
	}
	
	
}
