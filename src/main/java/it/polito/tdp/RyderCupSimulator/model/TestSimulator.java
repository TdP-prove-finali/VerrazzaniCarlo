package it.polito.tdp.RyderCupSimulator.model;

public class TestSimulator {

	public static void main(String[] args) {
		Model m = new Model();
		m.calcolaTeamEUR(56);
		m.calcolaTeamUSA(56);
		m.generaCalendarioDay1();
		m.generaCalendarioDay2();
		m.generaCalendarioDay3();
		Simulator sim = new Simulator(m.getMatchesDay1(), m.getMatchesDay1(), m.getMatchesDay3());
		sim.initialize();
		sim.run();

	}

}
