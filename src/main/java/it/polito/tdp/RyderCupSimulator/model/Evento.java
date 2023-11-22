package it.polito.tdp.RyderCupSimulator.model;

	public class Evento implements Comparable<Evento>{

		public enum EventType{
			MATCHSINGOLO,
			MATCHDOPPIO
		}
		
		private EventType type;
		private Integer day;
		private Player player1;
		private Player player2;
		private Player player3;//in caso di match singolo: player1 vs player2(player3 e player4 == NULL); in caso di match doppio: player1+player2 vs player3+player4 
		private Player player4;
		private Double score1;
		private Double score2;
		private Integer punteggio;
		public Evento(EventType type, Integer day, Player player1, Player player2, Player player3, Player player4,
				Double score1, Double score2, Integer punteggio) {
			super();
			this.type = type;
			this.day = day;
			this.player1 = player1;
			this.player2 = player2;
			this.player3 = player3;
			this.player4 = player4;
			this.score1 = score1;
			this.score2 = score2;
			this.punteggio = punteggio;
		}
		public EventType getType() {
			return type;
		}
		public void setType(EventType type) {
			this.type = type;
		}
		public Integer getDay() {
			return day;
		}
		public void setDay(Integer day) {
			this.day = day;
		}
		public Player getPlayer1() {
			return player1;
		}
		public void setPlayer1(Player player1) {
			this.player1 = player1;
		}
		public Player getPlayer2() {
			return player2;
		}
		public void setPlayer2(Player player2) {
			this.player2 = player2;
		}
		public Player getPlayer3() {
			return player3;
		}
		public void setPlayer3(Player player3) {
			this.player3 = player3;
		}
		public Player getPlayer4() {
			return player4;
		}
		public void setPlayer4(Player player4) {
			this.player4 = player4;
		}
		public Double getScore1() {
			return score1;
		}
		public void setScore1(Double score1) {
			this.score1 = score1;
		}
		public Double getScore2() {
			return score2;
		}
		public void setScore2(Double score2) {
			this.score2 = score2;
		}
		public Integer getPunteggio() {
			return punteggio;
		}
		public void setPunteggio(Integer punteggio) {
			this.punteggio = punteggio;
		}
		
		@Override
		public int compareTo(Evento o) {
			return this.day-o.day;
		}
		
		
}
