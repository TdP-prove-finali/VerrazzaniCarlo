package it.polito.tdp.RyderCupSimulator.model;

public class Player implements Comparable<Player>{
	private String nome;
	private String cognome;
	private String nazione;
	private Integer appearances;
	private Integer posizioneRanking;
	private Integer totaleIncassiAnno;
	private Double mediaScore;
	
	public Player(String nome, String cognome, String nazione, Integer appearances, Integer posizioneRanking,
			Integer totaleIncassiAnno, Double mediaScore) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.nazione = nazione;
		this.appearances = appearances;
		this.posizioneRanking = posizioneRanking;
		this.totaleIncassiAnno = totaleIncassiAnno;
		this.mediaScore = mediaScore;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	public Integer getAppearances() {
		return appearances;
	}

	public void setAppearances(Integer appearances) {
		this.appearances = appearances;
	}

	public Integer getPosizioneRanking() {
		return posizioneRanking;
	}

	public void setPosizioneRanking(Integer posizioneRanking) {
		this.posizioneRanking = posizioneRanking;
	}

	public Integer getTotaleIncassiAnno() {
		return totaleIncassiAnno;
	}

	public void setTotaleIncassiAnno(Integer totaleIncassiAnno) {
		this.totaleIncassiAnno = totaleIncassiAnno;
	}

	public Double getMediaScore() {
		return mediaScore;
	}

	public void setMediaScore(Double mediaScore) {
		this.mediaScore = mediaScore;
	}

	@Override
	public String toString() {
		return nome + " " + cognome + " " + posizioneRanking + " (tot.Incassi= " +totaleIncassiAnno+") " + " (numApparizioni= " + this.appearances+ ") " +" [mediaScore= "+ this.mediaScore+ "]\n";
	}

	@Override
	public int compareTo(Player o) {
		// TODO Auto-generated method stub
		return (int) (this.mediaScore-o.mediaScore);
	}
	
	


}
