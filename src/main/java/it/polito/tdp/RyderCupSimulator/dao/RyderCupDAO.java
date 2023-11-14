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
				+ "FROM ranking r ";
		List<Player> result = new ArrayList<Player>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String nomeS = rs.getString("First Name");
				String nome = nomeS.substring(1, nomeS.length()-1);//devo togliere virgolette dai valori del db!!!
				
				String cognomeS = rs.getString("Last Name"); 
				String cognome = cognomeS.substring(1, cognomeS.length()-1);
				
				String nazioneS = rs.getString("CTRY");
				String nazione = nazioneS.substring(1, nazioneS.length()-1);
				
				String nAppS = rs.getString("EVENTS PLAYED (ACTUAL)");
				String nApp = nAppS.substring(1, nAppS.length()-1);
				Integer appearances = Integer.parseInt(nApp); 
				
				
				String posizioneRankingS = rs.getString("RankPosition");
				String pos = posizioneRankingS.substring(1, posizioneRankingS.length()-1);
				Integer posizioneRanking = Integer.parseInt(pos); 
				
				Integer totaleIncassiAnno = 0;//per calcolo incassi posso usare come chiave primaria nomecognomenazionalita
				Player p = new Player(nome, cognome, nazione, appearances, posizioneRanking, totaleIncassiAnno);
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
		
	
	
}
