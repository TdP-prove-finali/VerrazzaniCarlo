package it.polito.tdp.RyderCupSimulator.model;

public class MatchDoppio {
	private Player player1EUR;
	private Player player2EUR;
	private Player player1USA;
	private Player player2USA;
	private Double scoreEUR;
	private Double scoreUSA;
	private Integer risultatoMatch;	

	public MatchDoppio(Player player1eur, Player player2eur, Player player1usa, Player player2usa, Double scoreEUR,
			Double scoreUSA, Integer risultatoMatch) {
		super();
		player1EUR = player1eur;
		player2EUR = player2eur;
		player1USA = player1usa;
		player2USA = player2usa;
		this.scoreEUR = scoreEUR;
		this.scoreUSA = scoreUSA;
		this.risultatoMatch = risultatoMatch;
	}

	public Player getPlayer1EUR() {
		return player1EUR;
	}



	public void setPlayer1EUR(Player player1eur) {
		player1EUR = player1eur;
	}



	public Player getPlayer2EUR() {
		return player2EUR;
	}



	public void setPlayer2EUR(Player player2eur) {
		player2EUR = player2eur;
	}



	public Player getPlayer1USA() {
		return player1USA;
	}



	public void setPlayer1USA(Player player1usa) {
		player1USA = player1usa;
	}



	public Player getPlayer2USA() {
		return player2USA;
	}



	public void setPlayer2USA(Player player2usa) {
		player2USA = player2usa;
	}



	public Double getScoreEUR() {
		return scoreEUR;
	}



	public void setScoreEUR(Double scoreEUR) {
		this.scoreEUR = scoreEUR;
	}



	public Double getScoreUSA() {
		return scoreUSA;
	}



	public void setScoreUSA(Double scoreUSA) {
		this.scoreUSA = scoreUSA;
	}



	public Integer getRisultatoMatch() {
		return risultatoMatch;
	}



	public void setRisultatoMatch(Integer risultatoMatch) {
		this.risultatoMatch = risultatoMatch;
	}



	@Override
	public String toString() {
		return "("+this.player1EUR.getNome()+this.player1EUR.getCognome()+"-"+this.player2EUR.getNome()+this.player2EUR.getCognome()+") vs "+"("+this.player1USA.getNome()+this.player1USA.getCognome()+"-"+this.player2USA.getNome()+this.player2USA.getCognome()+")\n";
	}
	
	public String toStringSenzaACapo() {
		return "("+this.player1EUR.getNome()+this.player1EUR.getCognome()+"-"+this.player2EUR.getNome()+this.player2EUR.getCognome()+") vs "+"("+this.player1USA.getNome()+this.player1USA.getCognome()+"-"+this.player2USA.getNome()+this.player2USA.getCognome()+")";
	}
	

}
