package it.polito.tdp.RyderCupSimulator.model;

public class MatchSingolo {
	private Player playerEUR;
	private Player playerUSA;
	private Double scorePlayerEUR;
	private Double scorePlayerUSA;
	private Integer punteggio;
	
	public MatchSingolo(Player playerEUR, Player playerUSA, Double scorePlayerEUR, Double scorePlayerUSA,
			Integer punteggio) {
		super();
		this.playerEUR = playerEUR;
		this.playerUSA = playerUSA;
		this.scorePlayerEUR = scorePlayerEUR;
		this.scorePlayerUSA = scorePlayerUSA;
		this.punteggio = punteggio;
	}

	public Player getPlayerEUR() {
		return playerEUR;
	}

	public void setPlayerEUR(Player playerEUR) {
		this.playerEUR = playerEUR;
	}

	public Player getPlayerUSA() {
		return playerUSA;
	}

	public void setPlayerUSA(Player playerUSA) {
		this.playerUSA = playerUSA;
	}

	public Double getScorePlayerEUR() {
		return scorePlayerEUR;
	}

	public void setScorePlayerEUR(Double scorePlayerEUR) {
		this.scorePlayerEUR = scorePlayerEUR;
	}

	public Double getScorePlayerUSA() {
		return scorePlayerUSA;
	}

	public void setScorePlayerUSA(Double scorePlayerUSA) {
		this.scorePlayerUSA = scorePlayerUSA;
	}

	public Integer getPunteggio() {
		return punteggio;
	}

	public void setPunteggio(Integer punteggio) {
		this.punteggio = punteggio;
	}

	@Override
	public String toString() {
		return playerEUR.getNome()+playerEUR.getCognome()+" vs "+playerUSA.getNome()+playerUSA.getCognome()+"\n";
	}
	
	public String toStringSenzaACapo() {
		return playerEUR.getNome()+playerEUR.getCognome()+" vs "+playerUSA.getNome()+playerUSA.getCognome();
	}
}
