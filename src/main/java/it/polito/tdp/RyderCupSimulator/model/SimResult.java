package it.polito.tdp.RyderCupSimulator.model;

import java.util.ArrayList;
import java.util.List;

public class SimResult {
	private double punteggioEUR;
	private double punteggioUSA;
	private List<MatchDoppio>risultatiDay1;
	private List<MatchDoppio>risultatiDay2;
	private List<MatchSingolo>risultatiDay3;
	public SimResult(double punteggioEUR, double punteggioUSA, List<MatchDoppio> risultatiDay1,
			List<MatchDoppio> risultatiDay2, List<MatchSingolo> risultatiDay3) {
		super();
		this.punteggioEUR = punteggioEUR;
		this.punteggioUSA = punteggioUSA;
		this.risultatiDay1 = risultatiDay1;
		this.risultatiDay2 = risultatiDay2;
		this.risultatiDay3 = risultatiDay3;
	}
	public double getPunteggioEUR() {
		return punteggioEUR;
	}
	public void setPunteggioEUR(double punteggioEUR) {
		this.punteggioEUR = punteggioEUR;
	}
	public double getPunteggioUSA() {
		return punteggioUSA;
	}
	public void setPunteggioUSA(double punteggioUSA) {
		this.punteggioUSA = punteggioUSA;
	}
	public List<MatchDoppio> getRisultatiDay1() {
		return risultatiDay1;
	}
	public void setRisultatiDay1(List<MatchDoppio> risultatiDay1) {
		this.risultatiDay1 = risultatiDay1;
	}
	public List<MatchDoppio> getRisultatiDay2() {
		return risultatiDay2;
	}
	public void setRisultatiDay2(List<MatchDoppio> risultatiDay2) {
		this.risultatiDay2 = risultatiDay2;
	}
	public List<MatchSingolo> getRisultatiDay3() {
		return risultatiDay3;
	}
	public void setRisultatiDay3(List<MatchSingolo> risultatiDay3) {
		this.risultatiDay3 = risultatiDay3;
	}
	
	
}

