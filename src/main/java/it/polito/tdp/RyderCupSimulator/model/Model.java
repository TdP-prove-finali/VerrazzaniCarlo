package it.polito.tdp.RyderCupSimulator.model;

import java.util.ArrayList;
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

	
	public Model() {
		this.dao = new RyderCupDAO();
		this.teamEUR = new ArrayList<>();
		this.teamUSA = new ArrayList<>();
		this.idMapPlayers = new HashMap<>();
	}
	
	public void loadPlayers() {
		this.players = new ArrayList<>(dao.getAllPlayers());
	}
	
	/*public List<Player>selezionaTeam(Integer nAppMin, List<Player>disponibili){//la lista disponibili contiene tutti i playerUSA se sto facendo il teamUSA, tutti quelli EURO se sto facendo team EURO
		
		return ;
		
	}*/
}
