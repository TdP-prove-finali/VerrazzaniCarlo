package it.polito.tdp.RyderCupSimulator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.RyderCupSimulator.model.Player;

public class RyderCupDAO {

	/*public List<Player> getAllPlayers(){
		String query = "SELECT DISTINCT * "
				+ "FROM ranking r ";
		List<Player> result = new ArrayList<Player>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String nome = rs.getString("First Name");
				String cognome; 
				String nazione;
				Integer appearances; 
				Integer posizioneRanking;
				Integer totaleIncassiAnno;
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
	}*/
		
	
}
