package it.polito.tdp.RyderCupSimulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.RyderCupSimulator.dao.RyderCupDAO;
import it.polito.tdp.RyderCupSimulator.model.Evento.EventType;

public class SimulatorDay3 {
	//parametri di ingresso
			
			
			//parametri
			private RyderCupDAO dao;
			private List<MatchSingolo>calendarioDay3;
			
			//variabili di uscita
			private List<MatchSingolo>risultatiDay3;
			
			private Double puntiEUR;
			private Double puntiUSA;
			private int scoreActual;//è il risultato fino a quando si è simulata la partita
			
			//stato del mondo
			private int Q;
			
			//coda degli eventi
			PriorityQueue<Evento> queue;
						
			public SimulatorDay3(List<MatchSingolo> calendarioDay3) {
				this.calendarioDay3 = calendarioDay3;
				
			}

			/**
			 * metodo che popola la coda degli eventi
			 */
			public void initialize() {
				this.queue = new PriorityQueue<Evento>();
				this.risultatiDay3 = new ArrayList<>();
				
				//eventi matchDay1
				for (MatchSingolo x : this.calendarioDay3) {
					Double scoreEUR = (x.getPlayerEUR().getMediaScore()+Math.random()*5-Math.random()*7);
					Double scoreUSA = (x.getPlayerUSA().getMediaScore()+Math.random()*5-Math.random()*7);
					Integer punteggioMatch = (int) (scoreEUR-scoreUSA);
					x.setScorePlayerEUR(scoreEUR);
					x.setScorePlayerUSA(scoreUSA);
					x.setPunteggio(punteggioMatch);
					this.queue.add(new Evento(EventType.MATCHSINGOLO, 3, x.getPlayerEUR(), x.getPlayerUSA(), null, null, scoreEUR, scoreUSA, punteggioMatch));
					this.risultatiDay3.add(x);
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
				List<MatchSingolo>risultatiDay3 = new ArrayList<>(this.risultatiDay3);
				SimResult res = new SimResult(punteggioEUR, punteggioUSA, null, null, risultatiDay3);
				return res;
			}
}
