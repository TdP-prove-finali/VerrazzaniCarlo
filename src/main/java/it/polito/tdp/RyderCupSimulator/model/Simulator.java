package it.polito.tdp.RyderCupSimulator.model;

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
				
				//eventi matchDay1
				for (MatchDoppio x : this.calendarioDay1) {
					Double scoreEUR = (x.getPlayer1EUR().getMediaScore()+x.getPlayer2EUR().getMediaScore())/2+Math.random()*4-Math.random()*4;
					Double scoreUSA = (x.getPlayer1USA().getMediaScore()+x.getPlayer2USA().getMediaScore())/2+Math.random()*4-Math.random()*4;
					Integer punteggioMatch = (int) (scoreEUR-scoreUSA);
					this.queue.add(new Evento(EventType.MATCHDOPPIO, 1, x.getPlayer1EUR(), x.getPlayer2EUR(), x.getPlayer1USA(), x.getPlayer2USA(), scoreEUR, scoreUSA, punteggioMatch));
				}
				
				//eventi match Day2
				for (MatchDoppio x : this.calendarioDay2) {
					Double scoreEUR = (x.getPlayer1EUR().getMediaScore()+x.getPlayer2EUR().getMediaScore())/2+Math.random()*4-Math.random()*4;
					Double scoreUSA = (x.getPlayer1USA().getMediaScore()+x.getPlayer2USA().getMediaScore())/2+Math.random()*4-Math.random()*4;
					Integer punteggioMatch = (int) (scoreEUR-scoreUSA);
					this.queue.add(new Evento(EventType.MATCHDOPPIO, 2, x.getPlayer1EUR(), x.getPlayer2EUR(), x.getPlayer1USA(), x.getPlayer2USA(), scoreEUR, scoreUSA, punteggioMatch));
				}
				//eventi match Day3
				for (MatchSingolo x : this.calendarioDay3) {
					Double scoreEUR = (x.getPlayerEUR().getMediaScore()+Math.random()*4-Math.random()*4);
					Double scoreUSA = (x.getPlayerUSA().getMediaScore()+Math.random()*4-Math.random()*4);
					Integer punteggioMatch = (int) (scoreEUR-scoreUSA);
					this.queue.add(new Evento(EventType.MATCHSINGOLO, 3, x.getPlayerEUR(), x.getPlayerUSA(), null, null, scoreEUR, scoreUSA, punteggioMatch));
				}

			}
			
			public void run() {
				
				while(!queue.isEmpty()) {
					Evento e = queue.poll();
					System.out.print(e.toString());
					/*switch(e.getType()) {
					case MATCHDOPPIO:
						
						break;
					case VENDITA:
						this.clientiTot++;
						if (Q >= 0.9*avgQ) {
							this.clientiSoddisfatti++;
						}
						if(Q >=avgQ) {
							this.ricavo += this.prezzoUnitario*avgQ;
							Q-=avgQ;
						}else {
							Q = 0;
							this.ricavo += this.prezzoUnitario*Q;
						}
						break;
					default:
						break;
					}*/
				}
			}
			
			/*public SimResult getRisultato() {
				double ricavo = this.ricavo;
				double costo = this.costo;
				double percentClientiSodd = (double)(this.clientiSoddisfatti*100)/this.clientiTot;
				SimResult res = new SimResult(costo, ricavo, percentClientiSodd);
				return res;
			}*/
}
