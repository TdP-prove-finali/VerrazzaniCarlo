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

	public Model() {
		this.dao = new RyderCupDAO();
		this.teamEUR = new ArrayList<>();
		this.teamUSA = new ArrayList<>();
		this.players = new ArrayList<>(dao.getAllPlayers());
		/*this.idMapPlayers = new HashMap<>();
		for(Player p : this.players) {
			this.idMapPlayers.put(p.getPosizioneRanking(), p);
		}*/
	}
	
	public List<Player>loadPlayersEUR(Integer nMinA){
		List<Player>disponibili = new ArrayList<>(dao.getAllPlayersEUR(nMinA));
		return disponibili;
	}
	
	public List<Player>loadPlayersUSA(Integer nMinA){
		List<Player>disponibili = new ArrayList<>(dao.getAllPlayersUSA(nMinA));
		return disponibili;
	}
	public void  calcolaTeamEUR(Integer nMinA) {//alleggerisco la ricorsione inserendo per ciascun team di default i migliori 7 giocatori per ranking: gli altri 5 sono scelti ricorsivamente
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
		ricorsione(parziale, rimanenti, nMinA);
	}
	
	public void  calcolaTeamUSA(Integer nMinA) {//alleggerisco la ricorsione inserendo per ciascun team di default i migliori 7 giocatori per ranking: gli altri 5 sono scelti ricorsivamente
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
		ricorsione(parziale, rimanenti, nMinA);
	}
		
	private void ricorsione(List<Player> parziale, List<Player> rimanenti, Integer nMinA){
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
 				ricorsione(parziale, currentRimanenti, nMinA);
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
	
	
	
}
