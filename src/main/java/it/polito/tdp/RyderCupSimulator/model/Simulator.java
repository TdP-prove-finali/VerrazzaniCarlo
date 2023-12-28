package it.polito.tdp.RyderCupSimulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.RyderCupSimulator.dao.RyderCupDAO;
import it.polito.tdp.RyderCupSimulator.model.Evento.EventType;

public class Simulator {
	//parametri di ingresso
			
			
			//parametri
			private RyderCupDAO dao;
			private List<MatchDoppio>calendarioDay1;
			private List<MatchDoppio>calendarioDay2;
			private List<MatchSingolo>calendarioDay3;
			
			//variabili di uscita
			private List<MatchDoppio>risultatiDay1;
			private List<MatchDoppio>risultatiDay2;
			private List<MatchSingolo>risultatiDay3;
			private Double puntiEUR;
			private Double puntiUSA;
			private int scoreActual;//è il risultato fino a quando si è simulata la partita
			
			//stato del mondo
			private int Q;
			
			//coda degli eventi
			PriorityQueue<Evento> queue;
			
			
			
			public Simulator(List<MatchDoppio> calendarioDay1, List<MatchDoppio> calendarioDay2,
					List<MatchSingolo> calendarioDay3) {
				super();
				this.calendarioDay1 = calendarioDay1;
				this.calendarioDay2 = calendarioDay2;
				this.calendarioDay3 = calendarioDay3;
			}

			/**
			 * metodo che popola la coda degli eventi
			 */
			public void initialize() {
				this.queue = new PriorityQueue<Evento>();
				this.risultatiDay1 = new ArrayList<>();
				this.risultatiDay2 = new ArrayList<>();
				this.risultatiDay3 = new ArrayList<>();
				//eventi matchDay1
				for (MatchDoppio x : this.calendarioDay1) {
					Double scoreEUR = (x.getPlayer1EUR().getMediaScore()+x.getPlayer2EUR().getMediaScore())/2+Math.random()*5-Math.random()*7;
					Double scoreUSA = (x.getPlayer1USA().getMediaScore()+x.getPlayer2USA().getMediaScore())/2+Math.random()*5-Math.random()*7;
					Integer punteggioMatch = (int) (scoreEUR-scoreUSA);
					MatchDoppio m = new MatchDoppio(x.getPlayer1EUR(), x.getPlayer2EUR(), x.getPlayer1USA(), x.getPlayer2USA(), scoreEUR, scoreUSA, punteggioMatch);
					this.queue.add(new Evento(EventType.MATCHDOPPIO, 1, x.getPlayer1EUR(), x.getPlayer2EUR(), x.getPlayer1USA(), x.getPlayer2USA(), scoreEUR, scoreUSA, punteggioMatch));
					this.risultatiDay1.add(m);
				}
				
				//eventi match Day2
				for (MatchDoppio x : this.calendarioDay2) {
					Double scoreEUR = (x.getPlayer1EUR().getMediaScore()+x.getPlayer2EUR().getMediaScore())/2+Math.random()*5-Math.random()*7;
					Double scoreUSA = (x.getPlayer1USA().getMediaScore()+x.getPlayer2USA().getMediaScore())/2+Math.random()*5-Math.random()*7;
					Integer punteggioMatch = (int) (scoreEUR-scoreUSA);
					MatchDoppio m = new MatchDoppio(x.getPlayer1EUR(), x.getPlayer2EUR(), x.getPlayer1USA(), x.getPlayer2USA(), scoreEUR, scoreUSA, punteggioMatch);
					this.queue.add(new Evento(EventType.MATCHDOPPIO, 2, x.getPlayer1EUR(), x.getPlayer2EUR(), x.getPlayer1USA(), x.getPlayer2USA(), scoreEUR, scoreUSA, punteggioMatch));
					this.risultatiDay2.add(m);
				}
				//eventi match Day3
				for (MatchSingolo x : this.calendarioDay3) {
					Double scoreEUR = (x.getPlayerEUR().getMediaScore()+Math.random()*5-Math.random()*7);
					Double scoreUSA = (x.getPlayerUSA().getMediaScore()+Math.random()*5-Math.random()*7);
					Integer punteggioMatch = (int) (scoreEUR-scoreUSA);
					MatchSingolo m = new MatchSingolo(x.getPlayerEUR(), x.getPlayerUSA(), scoreEUR, scoreUSA, punteggioMatch);
					this.queue.add(new Evento(EventType.MATCHSINGOLO, 3, x.getPlayerEUR(), x.getPlayerUSA(), null, null, scoreEUR, scoreUSA, punteggioMatch));
					this.risultatiDay3.add(m);
				}

			}
			
			public void run() {
				this.puntiEUR = 0.0;
				this.puntiUSA = 0.0;
				while(!queue.isEmpty()) {
					Evento e = queue.poll();
					switch(e.getType()) {
					case MATCHDOPPIO:
						System.out.print("(Day:"+e.getDay()+"): ["+e.getPlayer1().getNome()+ e.getPlayer1().getCognome()+"+"+ e.getPlayer2().getNome()+e.getPlayer2().getCognome()+"] vs"+" ["+
					e.getPlayer3().getNome()+e.getPlayer3().getCognome()+"+"+ e.getPlayer4().getNome()+e.getPlayer4().getCognome()+"] result: "+e.getPunteggio()+"\n");
						if(e.getPunteggio()>0) {
							this.puntiUSA += 1.0;
						}
						if(e.getPunteggio()<0) {
							this.puntiEUR += 1.0;
						}
						if(e.getPunteggio()==0) {
							this.puntiUSA += 0.5;
							this.puntiEUR += 0.5;
						}
						break;
					case MATCHSINGOLO:
						System.out.print("(Day:"+e.getDay()+"): "+e.getPlayer1().getNome()+ e.getPlayer1().getCognome()+" vs "+ e.getPlayer2().getNome()+e.getPlayer2().getCognome()+" result: "+e.getPunteggio()+"\n");
						if(e.getPunteggio()>0) {
							this.puntiUSA += 1.0;
						}
						if(e.getPunteggio()<0) {
							this.puntiEUR += 1.0;
						}
						if(e.getPunteggio()==0) {
							this.puntiUSA += 0.5;
							this.puntiEUR += 0.5;
						}
						break;
					default:
						break;
					}
				}
				System.out.println("punti EUR: "+this.puntiEUR+" puntiUSA: "+this.puntiUSA);
			}
			
			public SimResult getRisultato() {
				double punteggioEUR = this.puntiEUR;
				double punteggioUSA = this.puntiUSA;
				List<MatchDoppio>risultatiDay1 = new ArrayList<>(this.risultatiDay1);
				List<MatchDoppio>risultatiDay2 = new ArrayList<>(this.risultatiDay2);
				List<MatchSingolo>risultatiDay3 = new ArrayList<>(this.risultatiDay3);
				SimResult res = new SimResult(punteggioEUR, punteggioUSA, risultatiDay1, risultatiDay2, risultatiDay3);
				return res;
			}
}
