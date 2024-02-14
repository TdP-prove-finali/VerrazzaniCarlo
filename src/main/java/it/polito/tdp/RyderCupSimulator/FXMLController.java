package it.polito.tdp.RyderCupSimulator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.RyderCupSimulator.model.MatchDoppio;
import it.polito.tdp.RyderCupSimulator.model.MatchSingolo;
import it.polito.tdp.RyderCupSimulator.model.Model;
import it.polito.tdp.RyderCupSimulator.model.Player;
import it.polito.tdp.RyderCupSimulator.model.SimResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxPlayer;

    @FXML
    private Button btnCalcolaStats;
    
    @FXML
    private Button generateMatchTable1;

    @FXML
    private Button generateMatchTable2;
    
    @FXML
    private Button generateMatchTable3;

    @FXML
    private Button btnSimulateMatchesDay1;

    @FXML
    private Button btnSimulateMatchesDay2;

    @FXML
    private Button btnSimulateMatchesDay3;

    @FXML
    private TextField nAppearances;

    @FXML
    private TextField rankMax;
    
    @FXML
    private TextArea txtCalendar;

    @FXML
    private TextArea txtEUR;

    @FXML
    private TextArea txtStatsPlayer;

    @FXML
    private TextArea txtUSA;
    
    @FXML
    private TextField puntiEUR;

    @FXML
    private TextField puntiUSA;

    @FXML
    void doGenerateMatchTable1(ActionEvent event) {
    	if(model.getTeamEUR().isEmpty()) {
    		this.txtCalendar.setText("Select European Team");
    		return;
    	}
    	if(model.getTeamUSA().isEmpty()) {
    		this.txtCalendar.setText("Select American Team");
    		return;
    	}
    	model.generaCalendarioDay1();
    	
    	String s = "\nDay1:\n";
    	List<MatchDoppio>calDay1 = new ArrayList<>(model.getMatchesDay1());
    	//List<MatchDoppio>calDay2 = new ArrayList<>(model.getMatchesDay2());
    	//List<MatchSingolo>calDay3 = new ArrayList<>(model.getMatchesDay3());
    	for(MatchDoppio x : calDay1) {
    		s += x.toString();
    	}
    	
    	this.txtCalendar.setText(s);
    	for(Player x : model.getTeamEUR()) {
    		String nomeCogn = x.getNome()+x.getCognome();
    		this.boxPlayer.getItems().add(nomeCogn);
    	}
    	for(Player x : model.getTeamUSA()) {
    		String nomeCogn = x.getNome()+x.getCognome();
    		this.boxPlayer.getItems().add(nomeCogn);
    	}
    this.btnSimulateMatchesDay1.setDisable(false);
    }
    
    @FXML
    void doGenerateMatchTable2(ActionEvent event) {
    	String s = "";
    	model.generaCalendarioDay2();
    	this.btnSimulateMatchesDay2.setDisable(false);
    	s += "\nDay2:\n";
    	for(MatchDoppio x : model.getMatchesDay2()) {
    		s += x.toString();
    	}
    	this.btnSimulateMatchesDay2.setDisable(false);
    	this.txtCalendar.appendText(s);
    }

    @FXML
    void doGenerateMatchTable3(ActionEvent event) {
    	String s = "";
    	model.generaCalendarioDay3();
    	this.btnSimulateMatchesDay3.setDisable(false);
    	s += "\nDay3:\n";
    	for(MatchSingolo x : model.getMatchesDay3()) {
    		s += x.toString();
    	}
    	this.txtCalendar.appendText(s);

    }

    @FXML
    void doSelectTeamEurope(ActionEvent event) {
    	String input1 = this.nAppearances.getText();
    	String input2 = this.rankMax.getText();
    	Integer nApparizioni;
    	Integer rank;
    	String s = "\nTEAM EUROPE:\n";
    	try {
    		nApparizioni = Integer.parseInt(input1);
    	}catch(NumberFormatException e) {
    		this.txtEUR.setText("Insert a number in field appearances");
    		return;
    	}
    	try {
    		rank = Integer.parseInt(input2);
		}catch(NumberFormatException e) {
			this.txtEUR.setText("Insert a number in field rank");
		return;
		}
    	if(model.loadPlayersEUR(nApparizioni, rank).size()<12) {
    		this.txtEUR.setText("Not enough players!");
    		return;
    	}
    	
    	List<Player>giocatori = new ArrayList<>(model.calcolaTeamEUR(nApparizioni, rank));
    	for(Player x : giocatori) {
    		s += x.toString();
    	}
    	
    	this.txtEUR.appendText(s);
    }

    @FXML
    void doSelectTeamUSA(ActionEvent event) {
    	String input = this.nAppearances.getText();
    	String input2 = this.rankMax.getText();
    	Integer nApparizioni;
    	Integer rank;
    	String s = "\nTEAM USA:\n";
    	try {
    		nApparizioni = Integer.parseInt(input);
    	}catch(NumberFormatException e) {
    		this.txtUSA.setText("Insert a number in field appearances");
    		return;
    	}
    	try {
    		rank = Integer.parseInt(input2);
		}catch(NumberFormatException e) {
			this.txtUSA.setText("Insert a number in field rank");
		return;
		}
    	if(model.loadPlayersUSA(nApparizioni, rank).size()<12) {
    		this.txtUSA.setText("Not enough players!");
    		return;
    	}
    	
    	List<Player>giocatori = new ArrayList<>(model.calcolaTeamUSA(nApparizioni, rank));
    	
    	for(Player x : giocatori) {
    		s += x.toString();
    	}
    	
    	this.txtUSA.appendText(s);
    	this.generateMatchTable1.setDisable(false);
    }

    @FXML
    void doSimulateMatches1(ActionEvent event) {
    SimResult res = model.simulaDay1(model.getMatchesDay1());
    Double parzialeUSA1 = 0.0;
    Double parzialeEUR1 = 0.0;
    	String s ="\nRyder Cup simulation for round 1 succeded!\n";
    	for(MatchDoppio m: res.getRisultatiDay1()) {
    		String risMatch = "";
    		if(m.getRisultatoMatch()<0) {
    			Integer delta = -m.getRisultatoMatch();
    			risMatch += delta+" UP (EU)";
    			parzialeEUR1 += 1;
    		}
    		if(m.getRisultatoMatch()==0) {
    			risMatch += "EVEN";
    			parzialeEUR1+=0.5;
    			parzialeUSA1+=0.5;
    		}
    		if(m.getRisultatoMatch()>0) {
    			risMatch += m.getRisultatoMatch()+" DOWN (EU)";
    			parzialeUSA1+=1;
    		}
    		s+= "(Day:1) " + "["+m.getPlayer1EUR().getNome()+m.getPlayer1EUR().getCognome()+"+"+ m.getPlayer2EUR().getNome()+m.getPlayer2EUR().getCognome()+"] vs"+" ["+m.getPlayer1USA().getNome()+ m.getPlayer1USA().getCognome()+"+"+ m.getPlayer2USA().getNome()+m.getPlayer2USA().getCognome()+"] result: "+risMatch+"\n";
    	}
    	
    	this.generateMatchTable2.setDisable(false);
    	this.txtCalendar.appendText(s);
    	this.puntiEUR.setText(""+parzialeEUR1);
    	this.puntiUSA.setText(""+parzialeUSA1);
    	
    	
    }
    
    @FXML
    void doSimulateMatches2(ActionEvent event) {
    	SimResult res = model.simulaDay2(model.getMatchesDay2());
    	Double parzialeUSA1 = Double.parseDouble(this.puntiUSA.getText());
    	Double parzialeEUR1 = Double.parseDouble(this.puntiEUR.getText());
    	String s ="\nRyder Cup simulation for round 2 succeded!\n"; 
    	for(MatchDoppio m: model.getRisultatiDay2()) {
    		String risMatch = "";
    		if(m.getRisultatoMatch()<0) {
    			Integer delta = -m.getRisultatoMatch();
    			risMatch += delta+" UP (EU)";
    			parzialeEUR1+=1;
    		}
    		if(m.getRisultatoMatch()==0) {
    			risMatch += "EVEN";
    			parzialeEUR1+=0.5;
    			parzialeUSA1+=0.5;
    		}
    		if(m.getRisultatoMatch()>0) {
    			risMatch += m.getRisultatoMatch()+" DOWN (EU)";
    			parzialeUSA1+=1;
    		}
    		s+= "(Day:2) " + "["+m.getPlayer1EUR().getNome()+m.getPlayer1EUR().getCognome()+"+"+ m.getPlayer2EUR().getNome()+m.getPlayer2EUR().getCognome()+"] vs"+" ["+m.getPlayer1USA().getNome()+ m.getPlayer1USA().getCognome()+"+"+ m.getPlayer2USA().getNome()+m.getPlayer2USA().getCognome()+"] result: "+risMatch+"\n";
    	}
    	this.generateMatchTable3.setDisable(false);
		this.txtCalendar.appendText(s);
    	this.puntiEUR.setText(""+parzialeEUR1);
    	this.puntiUSA.setText(""+parzialeUSA1);
	}

    @FXML
    void doSimulateMatches3(ActionEvent event) {
    	SimResult res = model.simulaDay3(model.getMatchesDay3());
    	Double parzialeUSA2 = Double.parseDouble(this.puntiUSA.getText());
    	Double parzialeEUR2 = Double.parseDouble(this.puntiEUR.getText());
    	String s ="\nRyder Cup simulation for round 3 succeded!\n"; ;
    	for(MatchSingolo m: model.getRisultatiDay3()) {
    		String risMatch = "";
    		if(m.getPunteggio()<0) {
    			Integer delta = -m.getPunteggio();
    			risMatch += delta+" UP (EU)";
    			parzialeEUR2+=1;
    		}
    		if(m.getPunteggio()==0) {
    			risMatch += "EVEN";
    			parzialeEUR2 += 0.5;
    			parzialeUSA2 += 0.5;
    		}
    		if(m.getPunteggio()>0) {
    			risMatch += m.getPunteggio()+" DOWN (EU)";
    			parzialeUSA2+=1;
    		}
    		s+= "(Day:3) " + "["+m.getPlayerEUR().getNome()+m.getPlayerEUR().getCognome()+"] vs"+" ["+m.getPlayerUSA().getNome()+ m.getPlayerUSA().getCognome()+"] result: "+risMatch+"\n";
    	}
    	this.txtCalendar.appendText(s);
    	this.puntiEUR.setText(""+parzialeEUR2);
    	this.puntiUSA.setText(""+parzialeUSA2);
		this.boxPlayer.setDisable(false);
    	this.btnCalcolaStats.setDisable(false);
    }

    @FXML
    void doCalcolaStats(ActionEvent event) {
    	String nomeCognome = this.boxPlayer.getValue();
    	if(nomeCognome==null) {
    		this.txtStatsPlayer.setText("Select a player!");
    	}
    	String res = ""; 
    	res = this.model.satsPlayer(nomeCognome);
    	this.txtStatsPlayer.setText(res);
    }


    @FXML
    void initialize() {
    	assert boxPlayer != null : "fx:id=\"boxPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaStats != null : "fx:id=\"btnCalcolaStats\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulateMatchesDay1 != null : "fx:id=\"btnSimulateMatchesDay1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulateMatchesDay2 != null : "fx:id=\"btnSimulateMatchesDay2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulateMatchesDay3 != null : "fx:id=\"btnSimulateMatchesDay3\" was not injected: check your FXML file 'Scene.fxml'.";
        assert generateMatchTable1 != null : "fx:id=\"generateMatchTable1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert generateMatchTable2 != null : "fx:id=\"generateMatchTable2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert generateMatchTable3 != null : "fx:id=\"generateMatchTable3\" was not injected: check your FXML file 'Scene.fxml'.";
        assert nAppearances != null : "fx:id=\"nAppearances\" was not injected: check your FXML file 'Scene.fxml'.";
        assert puntiEUR != null : "fx:id=\"puntiEUR\" was not injected: check your FXML file 'Scene.fxml'.";
        assert puntiUSA != null : "fx:id=\"puntiUSA\" was not injected: check your FXML file 'Scene.fxml'.";
        assert rankMax != null : "fx:id=\"rankMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCalendar != null : "fx:id=\"txtCalendar\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEUR != null : "fx:id=\"txtEUR\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStatsPlayer != null : "fx:id=\"txtStatsPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtUSA != null : "fx:id=\"txtUSA\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}

	

}

