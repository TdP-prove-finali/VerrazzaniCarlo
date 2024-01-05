
package it.polito.tdp.RyderCupSimulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.RyderCupSimulator.dao.RyderCupDAO;

public class Model {
	
	private RyderCupDAO dao;
	private List<Player>players;
	private Map<String,Player>idMapPlayers;
	private List<Player>teamEUR;//questo sarà il team Europeo selezionato
	private List<Player>teamUSA;//questo sarà il team USA selezionato
	private List<Player>listaUSA;//sono tutti i players USA
	private List<Player>listaEUR;//sono tutti i playrs EUR
	private double salarioMaggiore;
	private List<MatchDoppio>matchesDay1;
	private List<MatchDoppio> matchesDay2;
	private List<MatchSingolo> matchesDay3;
	private List<MatchDoppio>risultatiDay1;
	private List<MatchDoppio>risultatiDay2;
	private List<MatchSingolo>risultatiDay3;

	public Model() {
		this.dao = new RyderCupDAO();
		this.teamEUR = new ArrayList<>();
		this.teamUSA = new ArrayList<>();
		this.players = new ArrayList<>(dao.getAllPlayers());
		this.idMapPlayers = new HashMap<>();
		
		for(Player p : this.players) {
			String fullName = p.getNome()+p.getCognome();
			this.idMapPlayers.put(fullName, p);
		}
	}
	
	public List<Player>loadPlayersEUR(Integer nMinA){//prendo solo players con media diversa da zero(senno è un casino)
		//a sto punto mi conviene aggiungere un database nella tabella con gli incassi degli europei(molti hanno 0 $ in PGA)
		
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
	public List<Player> calcolaTeamEUR(Integer nMinA) {//alleggerisco la ricorsione inserendo per ciascun team di default i migliori 5 giocatori per ranking e 4 a caso tra i migliori 20(esclusi i primi 5): gli altri 3 sono scelti ricorsivamente
		this.salarioMaggiore = 0.0;
		List<Player> rimanenti = new ArrayList<>(this.loadPlayersEUR(nMinA));
		List<Player> parziale = new ArrayList<>();
		parziale.add(rimanenti.get(0));//essendo parziale ordinato per ranking: prendo i primi 5.
		parziale.add(rimanenti.get(1));//prendo inoltre 4 player a caso tra i migliori 20 EUR.
		parziale.add(rimanenti.get(2));
		parziale.add(rimanenti.get(3));
		parziale.add(rimanenti.get(4));
		parziale.add(rimanenti.get(5));
		//parziale.add(rimanenti.get(6));
		rimanenti.remove(parziale.get(0));
		rimanenti.remove(parziale.get(1));
		rimanenti.remove(parziale.get(2));
		rimanenti.remove(parziale.get(3));
		rimanenti.remove(parziale.get(4));
		rimanenti.remove(parziale.get(5));
		//rimanenti.remove(parziale.get(6));
		
		ricorsioneEURSalario(parziale, rimanenti, nMinA);
		List<Player>last3Players = new ArrayList<>(calcolaTrePlayerPerMediaEUR(parziale, rimanenti, nMinA));
		this.teamEUR.addAll(last3Players);
		return this.teamEUR;
	}
	
	public List<Player> calcolaTeamUSA(Integer nMinA) {//alleggerisco la ricorsione inserendo per ciascun team di default i migliori 7 giocatori per ranking: gli altri 5 sono scelti ricorsivamente
		this.salarioMaggiore = 0.0;
		List<Player> rimanenti = new ArrayList<>(this.loadPlayersUSA(nMinA));
		List<Player> parziale = new ArrayList<>();
		parziale.add(rimanenti.get(0));
		parziale.add(rimanenti.get(1));
		parziale.add(rimanenti.get(2));
		parziale.add(rimanenti.get(3));
		parziale.add(rimanenti.get(4));
		parziale.add(rimanenti.get(5));
		//parziale.add(rimanenti.get(6));
		rimanenti.remove(parziale.get(0));
		rimanenti.remove(parziale.get(1));
		rimanenti.remove(parziale.get(2));
		rimanenti.remove(parziale.get(3));
		rimanenti.remove(parziale.get(4));
		rimanenti.remove(parziale.get(5));
		//rimanenti.remove(parziale.get(6));
		
		ricorsioneUSASalario(parziale, rimanenti, nMinA);
		List<Player>last3Players = new ArrayList<>(calcolaTrePlayerPerMediaUSA(parziale, rimanenti, nMinA));
		this.teamUSA.addAll(last3Players);
		return this.teamUSA;
	}
		
	private void ricorsioneUSASalario(List<Player> parziale, List<Player> rimanenti, Integer nMinA){
		// Condizione Terminale
		if (parziale.size() == 9) {
			//calcolo totIncassi
			double salario = getSalarioTeam(parziale);
			if (salario > this.salarioMaggiore) {
				this.salarioMaggiore = salario;
				this.teamUSA = new ArrayList<Player>(parziale);
			}
				return;
		}		
       	for (Player p : rimanenti) {
 			List<Player> currentRimanenti = new ArrayList<>(rimanenti);
 				parziale.add(p);
 				currentRimanenti.remove(p);
 				ricorsioneUSASalario(parziale, currentRimanenti, nMinA);
 				parziale.remove(parziale.size()-1);
 		}
	}
	
	private List<Player> calcolaTrePlayerPerMediaUSA(List<Player>parziale, List<Player> rimanenti, Integer nMinA){
		List<Player>ultimiPlayers = new ArrayList<>();
		rimanenti.removeAll(teamUSA);
		List<Player>rimanentiOrdinati = new ArrayList<>(rimanenti);
		Collections.sort(rimanentiOrdinati);
		Integer i = 0;
		while(ultimiPlayers.size()<3){
			if(!teamUSA.contains(rimanentiOrdinati.get(i))) {
				ultimiPlayers.add(rimanentiOrdinati.get(i));
				i++;
			}
		}
		return ultimiPlayers;
	}
	
	private void ricorsioneEURSalario(List<Player> parziale, List<Player> rimanenti, Integer nMinA){
		// Condizione Terminale
		if (parziale.size() == 9) {
			//calcolo media e totIncassi
			double salario = getSalarioTeam(parziale);
			if (salario > this.salarioMaggiore) {
				this.salarioMaggiore = salario;
				this.teamEUR = new ArrayList<Player>(parziale);
			}
				return;
		}		
       	for (Player p : rimanenti) {
 			List<Player> currentRimanenti = new ArrayList<>(rimanenti);
 				parziale.add(p);
 				currentRimanenti.remove(p);
 				ricorsioneEURSalario(parziale, currentRimanenti, nMinA);
 				parziale.remove(parziale.size()-1);
 		}
	}
	
	private List<Player> calcolaTrePlayerPerMediaEUR(List<Player>parziale, List<Player> rimanenti, Integer nMinA){
		List<Player>ultimiPlayers = new ArrayList<>();
		rimanenti.removeAll(teamEUR);
		List<Player>rimanentiOrdinati = new ArrayList<>(rimanenti);
		Collections.sort(rimanentiOrdinati);
		Integer i = 0;
		while(ultimiPlayers.size()<3){
			if(!teamEUR.contains(rimanentiOrdinati.get(i))) {
				ultimiPlayers.add(rimanentiOrdinati.get(i));
				i++;
			}
		}
		return ultimiPlayers;
	}
	
	
	
	private double getSalarioTeam(List<Player> team) {
		double result = 0.0;
		for (Player p : team) {
			result += p.getTotaleIncassiAnno();
		}
		return result;
	}
	
	/*private double calcolaMediaTeam(List<Player> team) {
		double sum = 0.0;
		double result = 0.0;
		for (Player p : team) {
			sum += p.getMediaScore();
		}
		result = sum/team.size();
		return result;
	}*/
	
	public void generaCalendarioDay1() {//metto: 1+12, 2+11, ... e infine i migliori 2 per squadra giocano 2 matches nel day1
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
	
	public void generaCalendarioDay2() {//qui devo cambiare ordine dei matches: quelli commentati sono = a quelli del day1
		//quelli sotto sono accoppiamenti randomici
		this.matchesDay2 = new ArrayList<>();
		List<Player>squadraEUR = new ArrayList<>(this.teamEUR);
		List<Player>squadraUSA = new ArrayList<>(this.teamUSA);
		List<Player>squadraEUR2 = new ArrayList<>(this.teamEUR);
		List<Player>squadraUSA2 = new ArrayList<>(this.teamUSA);
		while(!squadraEUR.isEmpty()) {//genero la prima serie di accoppiamenti(matches: 1-->6)
			
			Integer n0 = (int) (Math.random()*squadraEUR.size());
			Player p0EU = squadraEUR.get(n0);
			squadraEUR.remove(p0EU);
			Integer n1 = (int) (Math.random()*squadraEUR.size());
			Player p1EU = squadraEUR.get(n1);
			squadraEUR.remove(p1EU);
			
			Integer nu0 = (int) (Math.random()*squadraUSA.size());
			Player p0US = squadraUSA.get(nu0);
			squadraUSA.remove(p0US);
			Integer nu1 = (int) (Math.random()*squadraUSA.size());
			Player p1US = squadraUSA.get(nu1);
			squadraUSA.remove(p1US);
			MatchDoppio m = new MatchDoppio(p0EU, p1EU, p0US, p1US, 0.0, 0.0, 0);
			matchesDay2.add(m);
		}
		for(int i = 1; i<=2; i++){//genero gli ultimi due matches anche qui generando coppie casuali
			Integer n0 = (int) (Math.random()*squadraEUR2.size());
			Player p0EU = squadraEUR2.get(n0);
			squadraEUR2.remove(p0EU);
			Integer n1 = (int) (Math.random()*squadraEUR2.size());
			Player p1EU = squadraEUR2.get(n1);
			squadraEUR2.remove(p1EU);
			
			Integer nu0 = (int) (Math.random()*squadraUSA2.size());
			Player p0US = squadraUSA2.get(nu0);
			squadraUSA2.remove(p0US);
			Integer nu1 = (int) (Math.random()*squadraUSA2.size());
			Player p1US = squadraUSA2.get(nu1);
			squadraUSA2.remove(p1US);
			MatchDoppio m = new MatchDoppio(p0EU, p1EU, p0US, p1US, 0.0, 0.0, 0);
			matchesDay2.add(m);
		}			
	}
	
	public void generaCalendarioDay3() {
		this.matchesDay3 = new ArrayList<>();
		List<Player>squadraEUR = new ArrayList<>(this.teamEUR);
		List<Player>squadraUSA = new ArrayList<>(this.teamUSA);
		while(!squadraEUR.isEmpty()) {
			
			Integer n0 = (int) (Math.random()*squadraEUR.size());
			Player p0EU = squadraEUR.get(n0);
			squadraEUR.remove(p0EU);
			
			Integer nu0 = (int) (Math.random()*squadraUSA.size());
			Player p0US = squadraUSA.get(nu0);
			squadraUSA.remove(p0US);
			
			MatchSingolo m = new MatchSingolo(p0EU, p0US, 0.0, 0.0, 0);
			matchesDay3.add(m);
		}
		
	}
	
	public String satsPlayer(String player) {
		Player p = this.idMapPlayers.get(player);
		String risultati = player + "'s results:\n";
		List<MatchSingolo>singoli = new ArrayList<>(risultatiDay3);
		List<MatchDoppio>doppi1 = new ArrayList<>(risultatiDay1);
		List<MatchDoppio>doppi2 = new ArrayList<>(risultatiDay2);
		List<MatchDoppio>doppi = new ArrayList<>();
		doppi.addAll(doppi1);
		doppi.addAll(doppi2);
		
		for(MatchSingolo x : singoli) {
			if(x.toString().contains(player) /*x.getPlayerEUR().equals(p) || x.getPlayerUSA().equals(p)*/) {
				String risMatch = "";
	    		if(x.getPunteggio()<0) {
	    			Integer delta = -x.getPunteggio();
	    			risMatch += delta+" UP (EU)";
	    		}
	    		if(x.getPunteggio()==0) {
	    			risMatch += " EVEN";
	    		}
	    		if(x.getPunteggio()>0) {
	    			risMatch += x.getPunteggio()+" DOWN (EU)";
	    		}
				risultati += x.toStringSenzaACapo()+" "+risMatch+"\n";
			}
		}
		for(MatchDoppio x : doppi) {
			if(x.toString().contains(player)) {
				String risMatch = "";
	    		if(x.getRisultatoMatch()<0) {
	    			Integer delta = -x.getRisultatoMatch();
	    			risMatch += delta+" UP (EU)";
	    		}
	    		if(x.getRisultatoMatch()==0) {
	    			risMatch += "EVEN";
	    		}
	    		if(x.getRisultatoMatch()>0) {
	    			risMatch += x.getRisultatoMatch()+" DOWN (EU)";
	    		}
	    		risultati +=  x.toStringSenzaACapo()+" "+risMatch+"\n";
			}
		}
		return risultati;
	}
	
	public SimResult simula(List<MatchDoppio>matchesDay1, List<MatchDoppio>matchesDay2, List<MatchSingolo>matchesDay3) {
		Simulator sim = new Simulator(matchesDay1, matchesDay2, matchesDay3);
		sim.initialize();
		sim.run();
		SimResult res = sim.getRisultato();
		this.risultatiDay1 = new ArrayList<>(res.getRisultatiDay1());
		this.risultatiDay2 = new ArrayList<>(res.getRisultatiDay2());
		this.risultatiDay3 = new ArrayList<>(res.getRisultatiDay3());
		return res;
	}

	public RyderCupDAO getDao() {
		return dao;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Map<String, Player> getIdMapPlayers() {
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

	public List<MatchDoppio> getMatchesDay2() {
		return matchesDay2;
	}

	public List<MatchSingolo> getMatchesDay3() {
		return matchesDay3;
	}

	public List<MatchDoppio> getRisultatiDay1() {
		return risultatiDay1;
	}

	public List<MatchDoppio> getRisultatiDay2() {
		return risultatiDay2;
	}

	public List<MatchSingolo> getRisultatiDay3() {
		return risultatiDay3;
	}
	
	
	
}
