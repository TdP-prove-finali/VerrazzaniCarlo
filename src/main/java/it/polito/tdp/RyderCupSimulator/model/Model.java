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
	
	public List<Player>loadPlayersEUR(Integer nMinA, Integer rankMax){//prendo solo players con media diversa da zero(senno è un casino)
		//a sto punto mi conviene aggiungere un database nella tabella con gli incassi degli europei(molti hanno 0 $ in PGA)
		
		List<Player>disponibili = new ArrayList<>(dao.getAllPlayersEUR(nMinA, rankMax));
		List<Player>disponibili2 = new ArrayList<>(disponibili);

		for(Player p : disponibili2) {
			if(p.getMediaScore() <= 1.0) {
				disponibili.remove(p);
			}
		}
		return disponibili;
	}
	
	public List<Player>loadPlayersUSA(Integer nMinA, Integer rankMax){
		List<Player>disponibili = new ArrayList<>(dao.getAllPlayersUSA(nMinA, rankMax));
		List<Player>disponibili2 = new ArrayList<>(disponibili);
		for(Player p : disponibili2) {
			if(p.getMediaScore() <= 0.0) {
				disponibili.remove(p);
			}
		}
		return disponibili;
	}
	public List<Player> calcolaTeamEUR(Integer nMinA, Integer rankMax) {//alleggerisco la ricorsione inserendo per ciascun team di default i migliori 5 giocatori per ranking e 4 a caso tra i migliori 20(esclusi i primi 5): gli altri 3 sono scelti ricorsivamente
		this.salarioMaggiore = 0.0;
		List<Player> rimanenti = new ArrayList<>(this.loadPlayersEUR(nMinA, rankMax));
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
	
	public List<Player> calcolaTeamUSA(Integer nMinA, Integer rankMax) {//alleggerisco la ricorsione inserendo per ciascun team di default i migliori 7 giocatori per ranking: gli altri 5 sono scelti ricorsivamente
		this.salarioMaggiore = 0.0;
		List<Player> rimanenti = new ArrayList<>(this.loadPlayersUSA(nMinA, rankMax));
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
	
	public void generaCalendarioDay2() {//qui creo accoppiamenti in base ai risultati del day1: se una coppia ha vinto nei primi 6 matches del day 1 la ripropongo per il day 2, se ha perso: la cambio
		this.matchesDay2 = new ArrayList<>();
		List<CoppiaPlayers>vincentiEUR = new ArrayList<>();
		List<CoppiaPlayers>vincentiUSA = new ArrayList<>();
		List<Player>disponibiliEUR = new ArrayList<>();
		List<Player>disponibiliUSA = new ArrayList<>();
		List<Player>disponibiliEUR2 = new ArrayList<>();
		List<Player>disponibiliUSA2 = new ArrayList<>();

		List<CoppiaPlayers>coppieEUR = new ArrayList<>();
		List<CoppiaPlayers>coppieUSA = new ArrayList<>();
		
		for(Player p : this.teamEUR) {
			disponibiliEUR.add(p);
			disponibiliEUR2.add(p);
		}
		for(Player p : this.teamUSA) {
			disponibiliUSA.add(p);
			disponibiliUSA2.add(p);
		}
		//conto solo i primi sei match del day 1
		List<MatchDoppio>matchesSelezionati = new ArrayList<>();
		for(int i = 0; i<6; i++) {
			matchesSelezionati.add(this.risultatiDay1.get(i));
		}
		for(MatchDoppio x : matchesSelezionati) {
			//mantengo traccia delle coppie vincenti
			//le coppie che hanno vinto il 1 giorno le mantengo
			if(x.getRisultatoMatch() < 0) {
				Player p1 = x.getPlayer1EUR();
				Player p2 = x.getPlayer2EUR();
				CoppiaPlayers c = new CoppiaPlayers(p1, p2, "eur");
				vincentiEUR.add(c);
				coppieEUR.add(c);
				disponibiliEUR.remove(p1);
				disponibiliEUR.remove(p2);
			}
			if(x.getRisultatoMatch()>0) {
				Player p1 = x.getPlayer1USA();
				Player p2 = x.getPlayer2USA();
				CoppiaPlayers c = new CoppiaPlayers(p1, p2, "usa");
				vincentiUSA.add(c);
				coppieUSA.add(c);
				disponibiliUSA.remove(p1);
				disponibiliUSA.remove(p2);
			}
		}
			//per gli altri giocatori: cambio coppie in modo casuale
			while(!disponibiliEUR.isEmpty()) {//cosi ho creato 6 coppie per l'Europa
				Integer n0 = (int) (Math.random()*disponibiliEUR.size());
				Player p0EU = disponibiliEUR.get(n0);
				disponibiliEUR.remove(p0EU);
				Integer n1 = (int) (Math.random()*disponibiliEUR.size());
				Player p1EU = disponibiliEUR.get(n1);
				disponibiliEUR.remove(p1EU);
				CoppiaPlayers c = new CoppiaPlayers(p0EU, p1EU, "eur");
				coppieEUR.add(c);
			}
			while(!disponibiliUSA.isEmpty()) {//cosi ho creato 6 coppie per l'America
				Integer nu0 = (int) (Math.random()*disponibiliUSA.size());
				Player p0US = disponibiliUSA.get(nu0);
				disponibiliUSA.remove(p0US);
				Integer nu1 = (int) (Math.random()*disponibiliUSA.size());
				Player p1US = disponibiliUSA.get(nu1);
				disponibiliUSA.remove(p1US);
				CoppiaPlayers c = new CoppiaPlayers(p0US, p1US, "usa");
				coppieUSA.add(c);
		    }
			
			for(int i = 0; i<6; i++) {//creo i primi 6 matches
				Player p1EUR = coppieEUR.get(i).getPlayer1();
				Player p2EUR = coppieEUR.get(i).getPlayer2();
				Player p1USA = coppieUSA.get(i).getPlayer1();
				Player p2USA = coppieUSA.get(i).getPlayer2();
				MatchDoppio m = new MatchDoppio(p1EUR, p2EUR, p1USA, p2USA, 0.0, 0.0, 0);
				matchesDay2.add(m);
				
			}
			//ora devo creare ancora 2 coppie per ciascuna squadra(infatti 4 giocatori per squadra giocano 2 matches in 1 giorno: scelgo i giocatori casualmente tenendo fuori quelli che hanno già giocato 2 partite al day1
			disponibiliEUR2.remove(0);
			disponibiliEUR2.remove(1);
			disponibiliEUR2.remove(2);
			disponibiliEUR2.remove(3);
			disponibiliUSA2.remove(0);
			disponibiliUSA2.remove(1);
			disponibiliUSA2.remove(2);
			disponibiliUSA2.remove(3);
			for(int i = 0; i<2;  i++) {
				Integer n0 = (int) (Math.random()*disponibiliEUR2.size());
				Player p0EU = disponibiliEUR2.get(n0);
				disponibiliEUR2.remove(p0EU);
				Integer n1 = (int) (Math.random()*disponibiliEUR2.size());
				Player p1EU = disponibiliEUR2.get(n1);
				disponibiliEUR2.remove(p1EU);
			
				Integer nu0 = (int) (Math.random()*disponibiliUSA2.size());
				Player p0US = disponibiliUSA2.get(nu0);
				disponibiliUSA2.remove(p0US);
				Integer nu1 = (int) (Math.random()*disponibiliUSA2.size());
				Player p1US = disponibiliUSA2.get(nu1);
				disponibiliUSA2.remove(p1US);
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
	
	/*public SimResult simula(List<MatchDoppio>matchesDay1, List<MatchDoppio>matchesDay2, List<MatchSingolo>matchesDay3) {//----metodo originale
		Simulator sim = new Simulator(matchesDay1, matchesDay2, matchesDay3);
		sim.initialize();
		sim.run();
		SimResult res = sim.getRisultato();
		this.risultatiDay1 = new ArrayList<>(res.getRisultatiDay1());
		this.risultatiDay2 = new ArrayList<>(res.getRisultatiDay2());
		this.risultatiDay3 = new ArrayList<>(res.getRisultatiDay3());
		return res;
	}*/
	
	public SimResult simulaDay1(List<MatchDoppio>matchesDay1) {
		SimulatorDay1 sim = new SimulatorDay1(matchesDay1);
		sim.initialize();
		sim.run();
		SimResult res = sim.getRisultato();
		this.risultatiDay1 = new ArrayList<>(res.getRisultatiDay1());
		return res;
	}
	
	public SimResult simulaDay2(List<MatchDoppio>matchesDay2) {
		SimulatorDay2 sim = new SimulatorDay2(matchesDay2);
		sim.initialize();
		sim.run();
		SimResult res = sim.getRisultato();
		this.risultatiDay2 = new ArrayList<>(res.getRisultatiDay2());
		return res;
	}
	
	public SimResult simulaDay3(List<MatchSingolo>matchesDay3) {
		SimulatorDay3 sim = new SimulatorDay3(matchesDay3);
		sim.initialize();
		sim.run();
		SimResult res = sim.getRisultato();
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

