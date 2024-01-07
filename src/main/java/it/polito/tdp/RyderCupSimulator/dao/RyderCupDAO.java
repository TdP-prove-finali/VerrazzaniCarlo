package it.polito.tdp.RyderCupSimulator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.RyderCupSimulator.model.Player;

public class RyderCupDAO {

	public List<Player> getAllPlayers(){
		String query = "SELECT DISTINCT * "
				+ "FROM owgr r "
		        + "ORDER BY r.RANKING ";
		List<Player> result = new ArrayList<Player>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String nome = rs.getString("First Name");
				String cognome = rs.getString("Last Name"); 
				String nazione = rs.getString("CTRY");
				Integer nApparizioni = rs.getInt("EVENTS_PLAYED_ACTUAL");
				Integer posizioneRanking = rs.getInt("RANKING");
				Integer totaleIncassiAnno = this.getTotaleIncassiUSA(nome, cognome);//per calcolo incassi posso usare come chiave primaria nomecognomenazionalita
				Double mediaScore = this.getMediaScoreUSA(nome, cognome);
				Player p = new Player(nome, cognome, nazione, nApparizioni, posizioneRanking, totaleIncassiAnno, mediaScore);
				result.add(p);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Player> getAllPlayersEUR(Integer n, Integer rank){
		String query = "SELECT * "
				+ "FROM owgr o "
				+ "WHERE o.CTRY IN ( "
				+ "    'Albania', 'Andorra', 'England', 'Northern Ireland', 'Austria', 'Belarus', 'Belgium', 'Bosnia and Herzegovina', 'Bulgaria', 'Croatia', 'Cyprus', 'Czech Republic', 'Denmark', 'Estonia', 'Finland', 'France', 'Germany', 'Greece', 'Hungary', 'Iceland', 'Ireland', 'Italy', 'Kazakhstan', 'Kosovo', 'Latvia', 'Liechtenstein', 'Lithuania', 'Luxembourg', 'Malta', 'Moldova', 'Monaco', 'Montenegro', 'Netherlands', 'North Macedonia', 'Norway', 'Poland', 'Portugal', 'Romania', 'Russia', 'San Marino', 'Serbia', 'Slovakia', 'Slovenia', 'Spain', 'Sweden', 'Switzerland', 'Ukraine', 'United Kingdom', 'Vatican City' "
				+ ")"
				+ "AND o.EVENTS_PLAYED_ACTUAL > ? "
				+ "AND o.RANKING < ? "
				+ "ORDER BY o.RANKING ";
		
		List<Player> result = new ArrayList<Player>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, n);
			st.setInt(2, rank);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String nome = rs.getString("First Name");
				String cognome = rs.getString("Last Name"); 
				String nazione = rs.getString("CTRY");
				Integer nApparizioni = rs.getInt("EVENTS_PLAYED_ACTUAL");
				Integer posizioneRanking = rs.getInt("RANKING");
				Integer totaleIncassiAnno = this.getTotaleIncassiEUR(nome, cognome);
				if(totaleIncassiAnno == 0) {
					totaleIncassiAnno = this.getTotaleIncassiUSA(nome, cognome);
				}
				Double mediaScore = this.getMediaScoreEUR(nome, cognome);
				if(mediaScore == 0.0) {
					mediaScore = this.getMediaScoreUSA(nome, cognome);
				}
				Player p = new Player(nome, cognome, nazione, nApparizioni, posizioneRanking, totaleIncassiAnno, mediaScore);
				result.add(p);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Player> getAllPlayersUSA(Integer n, Integer rank) {
		String query = "SELECT * "
				+ "FROM owgr o "
				+ "WHERE o.CTRY = 'United States' "
				+ "AND o.EVENTS_PLAYED_ACTUAL > ? "
				+ "AND o.RANKING < ? "
				+ "ORDER BY o.RANKING ";
		List<Player> result = new ArrayList<Player>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, n);
			st.setInt(2, rank);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String nome = rs.getString("First Name");
				String cognome = rs.getString("Last Name"); 
				String nazione = rs.getString("CTRY");
				Integer nApparizioni = rs.getInt("EVENTS_PLAYED_ACTUAL");
				Integer posizioneRanking = rs.getInt("RANKING");
				Integer totaleIncassiAnno = this.getTotaleIncassiUSA(nome, cognome);
				if(totaleIncassiAnno == 0) {
					totaleIncassiAnno = this.getTotaleIncassiEUR(nome, cognome);
				}
				Double mediaScore = this.getMediaScoreUSA(nome, cognome);
				if(mediaScore == 0.0) {
					mediaScore = this.getMediaScoreEUR(nome, cognome);
				}
				Player p = new Player(nome, cognome, nazione, nApparizioni, posizioneRanking, totaleIncassiAnno, mediaScore);
				result.add(p);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	private Double getMediaScoreUSA(String nome, String cognome) {
		String query = "SELECT a.AVG "
				+ "FROM scoringaverage_usa a "
				+ "WHERE a.PLAYER = ? ";		
		String fullName = nome+" "+cognome;
		Double mediaScore = 0.0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, fullName);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				mediaScore = rs.getDouble("AVG");
			}
			conn.close();
			return mediaScore;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	private Double getMediaScoreEUR(String nome, String cognome) {
		String query = "SELECT s.ScoringAverage "
				+ "FROM scoringaverage_eur s "
				+ "WHERE s.player = ? ";		
		String fullName = cognome.toUpperCase()+nome;
		Double mediaScore = 0.0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, fullName);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				mediaScore = rs.getDouble("ScoringAverage");
			}
			conn.close();
			return mediaScore;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public Integer getTotaleIncassiUSA(String nome, String cognome){
		String query = "SELECT m.EARNINGS "
				+ "FROM moneylist_pga m "
				+ "WHERE m.NAME = ? ";
		
		String fullNameX = nome+cognome;
		String fullName = fullNameX.strip();
		Integer totIncassi = 0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, fullName);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String totaleIncassiAnno = rs.getString("EARNINGS");
				String totIncassiB = totaleIncassiAnno.substring(1, totaleIncassiAnno.length());
				totIncassi = Integer.parseInt(totIncassiB);
			
			}
			conn.close();
			return totIncassi;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public Integer getTotaleIncassiEUR(String nome, String cognome){
		String query = "SELECT m.Prize_money "
				+ "FROM moneylist_eur m "
				+ "WHERE m.Player = ? ";
		
		String fullName = cognome.toUpperCase()+" "+nome;
		Integer totIncassi = 0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, fullName);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String totaleIncassiAnno = rs.getString("Prize_money");
				String totIncassiB = totaleIncassiAnno.substring(1, totaleIncassiAnno.length());
				totIncassi = Integer.parseInt(totIncassiB);
			
			}
			conn.close();
			return totIncassi;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	
	
}