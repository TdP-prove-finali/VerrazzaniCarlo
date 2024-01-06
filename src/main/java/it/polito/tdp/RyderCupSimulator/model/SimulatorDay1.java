package it.polito.tdp.RyderCupSimulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.RyderCupSimulator.dao.RyderCupDAO;
import it.polito.tdp.RyderCupSimulator.model.Evento.EventType;

public class SimulatorDay1 {
	//parametri di ingresso
			
			
			//parametri
			private RyderCupDAO dao;
			private List<MatchDoppio>calendarioDay1;
			
			//variabili di uscita
			private List<MatchDoppio>risultatiDay1;
			
			private Double puntiEUR;
			private Double puntiUSA;
			private int scoreActual;//è il risultato fino a quando si è simulata la partita
			
			//stato del mondo
			private int Q;
			
			//coda degli eventi
			PriorityQueue<Evento> queue;
						
			public SimulatorDay1(List<MatchDoppio> calendarioDay1) {
				this.calendarioDay1 = calendarioDay1;
				
			}

			/**
			 * metodo che popola la coda degli eventi
			 */
			public void initialize() {
				this.queue = new PriorityQueue<Evento>();
				this.risultatiDay1 = new ArrayList<>();
				
				//eventi matchDay1
				for (MatchDoppio x : this.calendarioDay1) {
					Double scoreEUR = (x.getPlayer1EUR().getMediaScore()+x.getPlayer2EUR().getMediaScore())/2+Math.random()*5-Math.random()*7;
					Double scoreUSA = (x.getPlayer1USA().getMediaScore()+x.getPlayer2USA().getMediaScore())/2+Math.random()*5-Math.random()*7;
					Integer punteggioMatch = (int) (scoreEUR-scoreUSA);
					x.setScoreEUR(scoreEUR);
					x.setScoreUSA(scoreUSA);
					x.setRisultatoMatch(punteggioMatch);
					this.queue.add(new Evento(EventType.MATCHDOPPIO, 1, x.getPlayer1EUR(), x.getPlayer2EUR(), x.getPlayer1USA(), x.getPlayer2USA(), scoreEUR, scoreUSA, punteggioMatch));
					this.risultatiDay1.add(x);
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
				SimResult res = new SimResult(punteggioEUR, punteggioUSA, risultatiDay1, null , null);
				return res;
			}
}
