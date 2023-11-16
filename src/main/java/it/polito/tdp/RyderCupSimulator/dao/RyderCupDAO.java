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
				+ "FROM owgr r ";
		List<Player> result = new ArrayList<Player>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String nome = rs.getString("First Name");
				String cognome = rs.getString("Last Name"); 
				String nazione = rs.getString("CTRY");
				Integer nApparizioni = rs.getInt("EVENTS PLAYED (ACTUAL)");
				Integer posizioneRanking = rs.getInt("RankPosition");
				Integer totaleIncassiAnno = this.getTotaleIncassi(nome, cognome);//per calcolo incassi posso usare come chiave primaria nomecognomenazionalita
				Double mediaScore = this.getMediaScore(nome, cognome);
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
		
	private Double getMediaScore(String nome, String cognome) {
		String query = "SELECT a.AVG "
				+ "FROM scoringavgranking a "
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

	public Integer getTotaleIncassi(String nome, String cognome){
		String query = "SELECT m.EARNINGS "
				+ "FROM moneylist m "
				+ "WHERE m.NAME=? ";
		
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
	
}
