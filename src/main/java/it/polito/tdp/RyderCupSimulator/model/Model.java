package it.polito.tdp.RyderCupSimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.RyderCupSimulator.dao.RyderCupDAO;

public class Model {
	
	private RyderCupDAO dao;
	private List<Player>players;
	private Map<Integer,Player>idMapPlayers;
	private List<Player>teamEUR;//questo sarà il team Europeo selezionato
	private List<Player>teamUSA;//questo sarà il team USA selezionato
	private List<Player>listaUSA;//sono tutti i players USA
	private List<Player>listaEUR;//sono tutti i playrs EUR
	private double salarioMaggiore;
	private double mediaTeamTOP;
	private List<MatchDoppio>matchesDay1;
	private ArrayList<MatchSingolo> matchesDay3;

	public Model() {
		this.dao = new RyderCupDAO();
		this.teamEUR = new ArrayList<>();
		this.teamUSA = new ArrayList<>();
		//this.players = new ArrayList<>(dao.getAllPlayers());
		/*this.idMapPlayers = new HashMap<>();
		for(Player p : this.players) {
			this.idMapPlayers.put(p.getPosizioneRanking(), p);
		}*/
	}
	
	public List<Player>loadPlayersEUR(Integer nMinA){//prendo solo players con media diversa da zero(senno è un casino)
		//a sto punto mi conviene aggiungere un database nella tabella con gli incassi degli europei(molti hanno 0 $ in PGA)
		//https://golftoday.co.uk/european-tour-career-money-list/    questo è il db da aggiungere
		
		List<Player>disponibili = new ArrayList<>(dao.getAllPlayersEUR(nMinA));
		List<Player>disponibili2 = new ArrayList<>(disponibili);

		for(Player p : disponibili2) {
			if(p.getMediaScore() <= 1.0) {
				disponibili.remove(p);
			}
		}
		return disponibili;
	}
	
	public List<Player>loadPlayersUSA(Integer nMinA){
		List<Player>disponibili = new ArrayList<>(dao.getAllPlayersUSA(nMinA));
		List<Player>disponibili2 = new ArrayList<>(disponibili);
		for(Player p : disponibili2) {
			if(p.getMediaScore() <= 0.0) {
				disponibili.remove(p);
			}
		}
		return disponibili;
	}
	public List<Player> calcolaTeamEUR(Integer nMinA) {//alleggerisco la ricorsione inserendo per ciascun team di default i migliori 7 giocatori per ranking: gli altri 5 sono scelti ricorsivamente
		this.salarioMaggiore = 0.0;
		this.mediaTeamTOP = 0.0;
		List<Player> rimanenti = new ArrayList<>(this.loadPlayersEUR(nMinA));
		List<Player> parziale = new ArrayList<>();
		parziale.add(rimanenti.get(0));
		parziale.add(rimanenti.get(1));
		parziale.add(rimanenti.get(2));
		parziale.add(rimanenti.get(3));
		parziale.add(rimanenti.get(4));
		parziale.add(rimanenti.get(5));
		parziale.add(rimanenti.get(6));
		rimanenti.remove(parziale.get(0));
		rimanenti.remove(parziale.get(1));
		rimanenti.remove(parziale.get(2));
		rimanenti.remove(parziale.get(3));
		rimanenti.remove(parziale.get(4));
		rimanenti.remove(parziale.get(5));
		rimanenti.remove(parziale.get(6));
		ricorsioneEUR(parziale, rimanenti, nMinA);//devo far si che questo metodo ritorni la lista di giocatori eu
		return this.teamEUR;
	}
	
	public List<Player> calcolaTeamUSA(Integer nMinA) {//alleggerisco la ricorsione inserendo per ciascun team di default i migliori 7 giocatori per ranking: gli altri 5 sono scelti ricorsivamente
		this.salarioMaggiore = 0.0;
		this.mediaTeamTOP = 0.0;
		List<Player> rimanenti = new ArrayList<>(this.loadPlayersUSA(nMinA));
		List<Player> parziale = new ArrayList<>();
		parziale.add(rimanenti.get(0));
		parziale.add(rimanenti.get(1));
		parziale.add(rimanenti.get(2));
		parziale.add(rimanenti.get(3));
		parziale.add(rimanenti.get(4));
		parziale.add(rimanenti.get(5));
		parziale.add(rimanenti.get(6));
		rimanenti.remove(parziale.get(0));
		rimanenti.remove(parziale.get(1));
		rimanenti.remove(parziale.get(2));
		rimanenti.remove(parziale.get(3));
		rimanenti.remove(parziale.get(4));
		rimanenti.remove(parziale.get(5));
		rimanenti.remove(parziale.get(6));
		ricorsioneUSA(parziale, rimanenti, nMinA);//devo far si che questo metodo ritorni la lista di giocatori USA
		return this.teamUSA;
	}
		
	private void ricorsioneUSA(List<Player> parziale, List<Player> rimanenti, Integer nMinA){
		// Condizione Terminale
		if (parziale.size() == 12) {
			//calcolo media e totIncassi
			double salario = getSalarioTeam(parziale);
			//double media = this.calcolaMediaTeam(parziale);
			if (salario > this.salarioMaggiore /*&& media < this.mediaTeamTOP*/) {
				this.salarioMaggiore = salario;
				// this.mediaTeamTOP = media;
				this.teamUSA = new ArrayList<Player>(parziale);
			}
				return;
		}		
       	for (Player p : rimanenti) {
 			List<Player> currentRimanenti = new ArrayList<>(rimanenti);
 				parziale.add(p);
 				currentRimanenti.remove(p);
 				ricorsioneUSA(parziale, currentRimanenti, nMinA);
 				parziale.remove(parziale.size()-1);
 		}
	}
	
	private void ricorsioneEUR(List<Player> parziale, List<Player> rimanenti, Integer nMinA){
		// Condizione Terminale
		if (parziale.size() == 12) {
			//calcolo media e totIncassi
			double salario = getSalarioTeam(parziale);
			//double media = this.calcolaMediaTeam(parziale);
			if (salario > this.salarioMaggiore /*&& media < this.mediaTeamTOP*/) {
				this.salarioMaggiore = salario;
				// this.mediaTeamTOP = media;
				this.teamEUR = new ArrayList<Player>(parziale);
			}
				return;
		}		
       	for (Player p : rimanenti) {
 			List<Player> currentRimanenti = new ArrayList<>(rimanenti);
 				parziale.add(p);
 				currentRimanenti.remove(p);
 				ricorsioneEUR(parziale, currentRimanenti, nMinA);
 				parziale.remove(parziale.size()-1);
 		}
	}
	private double getSalarioTeam(List<Player> team) {
		double result = 0.0;
		for (Player p : team) {
			result += p.getTotaleIncassiAnno();
		}
		return result;
	}
	
	private double calcolaMediaTeam(List<Player> team) {
		double sum = 0.0;
		double result = 0.0;
		for (Player p : team) {
			sum += p.getMediaScore();
		}
		result = sum/team.size();
		return result;
	}
	
	public void generaCalendarioDay1e2() {
		this.matchesDay1 = new ArrayList<>();
		Player Player0EUR = this.teamEUR.get(0);
		Player Player1EUR = this.teamEUR.get(1);
		Player Player2EUR = this.teamEUR.get(2);
		Player Player3EUR = this.teamEUR.get(3);
		Player Player4EUR = this.teamEUR.get(4);
		Player Player5EUR = this.teamEUR.get(5);
		Player Player6EUR = this.teamEUR.get(6);
		Player Player7EUR = this.teamEUR.get(7);
		Player Player8EUR = this.teamEUR.get(8);
		Player Player9EUR = this.teamEUR.get(9);
		Player Player10EUR = this.teamEUR.get(10);
		Player Player11EUR = this.teamEUR.get(11);
		
		Player Player0USA = this.teamUSA.get(0);
		Player Player1USA = this.teamUSA.get(1);
		Player Player2USA = this.teamUSA.get(2);
		Player Player3USA = this.teamUSA.get(3);
		Player Player4USA = this.teamUSA.get(4);
		Player Player5USA = this.teamUSA.get(5);
		Player Player6USA = this.teamUSA.get(6);
		Player Player7USA = this.teamUSA.get(7);
		Player Player8USA = this.teamUSA.get(8);
		Player Player9USA = this.teamUSA.get(9);
		Player Player10USA = this.teamUSA.get(10);
		Player Player11USA = this.teamUSA.get(11);
		
		MatchDoppio m0 = new MatchDoppio(Player0EUR, Player11EUR, Player0USA, Player11USA, 0.0, 0.0, 0);
		MatchDoppio m1 = new MatchDoppio(Player1EUR, Player10EUR, Player1USA, Player10USA, 0.0, 0.0, 0);
		MatchDoppio m2 = new MatchDoppio(Player2EUR, Player9EUR, Player2USA, Player9USA, 0.0, 0.0, 0);
		MatchDoppio m3 = new MatchDoppio(Player3EUR, Player8EUR, Player3USA, Player8USA, 0.0, 0.0, 0);
		MatchDoppio m4 = new MatchDoppio(Player4EUR, Player7EUR, Player4USA, Player7USA, 0.0, 0.0, 0);
		MatchDoppio m5 = new MatchDoppio(Player5EUR, Player6EUR, Player5USA, Player6USA, 0.0, 0.0, 0);
		MatchDoppio m6 = new MatchDoppio(Player0EUR, Player3EUR, Player0USA, Player3USA, 0.0, 0.0, 0);
		MatchDoppio m7 = new MatchDoppio(Player1EUR, Player2EUR, Player1USA, Player2USA, 0.0, 0.0, 0);
		matchesDay1.add(m0);
		matchesDay1.add(m1);
		matchesDay1.add(m2);
		matchesDay1.add(m3);
		matchesDay1.add(m4);
		matchesDay1.add(m5);
		matchesDay1.add(m6);
		matchesDay1.add(m7);
	}
	public void generaCalendarioDay3() {
		this.matchesDay3 = new ArrayList<>();
		Player Player0EUR = this.teamEUR.get(0);
		Player Player1EUR = this.teamEUR.get(1);
		Player Player2EUR = this.teamEUR.get(2);
		Player Player3EUR = this.teamEUR.get(3);
		Player Player4EUR = this.teamEUR.get(4);
		Player Player5EUR = this.teamEUR.get(5);
		Player Player6EUR = this.teamEUR.get(6);
		Player Player7EUR = this.teamEUR.get(7);
		Player Player8EUR = this.teamEUR.get(8);
		Player Player9EUR = this.teamEUR.get(9);
		Player Player10EUR = this.teamEUR.get(10);
		Player Player11EUR = this.teamEUR.get(11);
		
		Player Player0USA = this.teamUSA.get(0);
		Player Player1USA = this.teamUSA.get(1);
		Player Player2USA = this.teamUSA.get(2);
		Player Player3USA = this.teamUSA.get(3);
		Player Player4USA = this.teamUSA.get(4);
		Player Player5USA = this.teamUSA.get(5);
		Player Player6USA = this.teamUSA.get(6);
		Player Player7USA = this.teamUSA.get(7);
		Player Player8USA = this.teamUSA.get(8);
		Player Player9USA = this.teamUSA.get(9);
		Player Player10USA = this.teamUSA.get(10);
		Player Player11USA = this.teamUSA.get(11);
		
		
		MatchSingolo ms0 = new MatchSingolo(Player0EUR, Player0USA, 0.0, 0.0, 0);
		MatchSingolo ms1 = new MatchSingolo(Player1EUR, Player1USA, 0.0, 0.0, 0);
		MatchSingolo ms2 = new MatchSingolo(Player2EUR, Player2USA, 0.0, 0.0, 0);
		MatchSingolo ms3 = new MatchSingolo(Player3EUR, Player3USA, 0.0, 0.0, 0);
		MatchSingolo ms4 = new MatchSingolo(Player4EUR, Player4USA, 0.0, 0.0, 0);
		MatchSingolo ms5 = new MatchSingolo(Player5EUR, Player5USA, 0.0, 0.0, 0);
		MatchSingolo ms6 = new MatchSingolo(Player6EUR, Player6USA, 0.0, 0.0, 0);
		MatchSingolo ms7 = new MatchSingolo(Player7EUR, Player7USA, 0.0, 0.0, 0);
		MatchSingolo ms8 = new MatchSingolo(Player8EUR, Player8USA, 0.0, 0.0, 0);
		MatchSingolo ms9 = new MatchSingolo(Player9EUR, Player9USA, 0.0, 0.0, 0);
		MatchSingolo ms10 = new MatchSingolo(Player10EUR, Player10USA, 0.0, 0.0, 0);
		MatchSingolo ms11 = new MatchSingolo(Player11EUR, Player11USA, 0.0, 0.0, 0);
		matchesDay3.add(ms0);
		matchesDay3.add(ms1);
		matchesDay3.add(ms2);
		matchesDay3.add(ms3);
		matchesDay3.add(ms4);
		matchesDay3.add(ms5);
		matchesDay3.add(ms6);
		matchesDay3.add(ms7);
		matchesDay3.add(ms8);
		matchesDay3.add(ms9);
		matchesDay3.add(ms10);
		matchesDay3.add(ms11);
		
	}

	public RyderCupDAO getDao() {
		return dao;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Map<Integer, Player> getIdMapPlayers() {
		return idMapPlayers;
	}

	public List<Player> getTeamEUR() {
		return teamEUR;
	}

	public List<Player> getTeamUSA() {
		return teamUSA;
	}

	public List<MatchDoppio> getMatchesDay1() {
		return matchesDay1;
	}

	public ArrayList<MatchSingolo> getMatchesDay3() {
		return matchesDay3;
	}
	
	
	
}
