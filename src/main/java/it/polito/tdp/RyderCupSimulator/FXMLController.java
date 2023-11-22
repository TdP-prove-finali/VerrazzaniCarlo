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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nAppearances;

    @FXML
    private TextArea txtResult;

    @FXML
    void doGenerateMatchTable(ActionEvent event) {//per ora il metodo è impostato per stampare tutti i giocatori
    	model.generaCalendarioDay1();
    	model.generaCalendarioDay2();
    	model.generaCalendarioDay3();
    	String s = "\nDay1:\n";
    	List<MatchDoppio>calDay1 = new ArrayList<>(model.getMatchesDay1());
    	List<MatchDoppio>calDay2 = new ArrayList<>(model.getMatchesDay2());
    	List<MatchSingolo>calDay3 = new ArrayList<>(model.getMatchesDay3());
    	for(MatchDoppio x : calDay1) {
    		s += x.toString();
    	}
    	s += "\nDay2:\n";
    	for(MatchDoppio x : calDay2) {
    		s += x.toString();
    	}
    	
    	s += "\nDay3:\n";
    	for(MatchSingolo x : calDay3) {
    		s += x.toString();
    	}
    	this.txtResult.appendText(s);
    }
    	

    @FXML
    void doSelectTeamEurope(ActionEvent event) {
    	String input = this.nAppearances.getText();
    	Integer nApparizioni;
    	String s = "\nTEAM EUROPE:\n";
    	try {
    		nApparizioni = Integer.parseInt(input);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero nel campo numeroApparizioni");
    		return;
    	}
    	
    	List<Player>giocatori = new ArrayList<>(model.calcolaTeamEUR(nApparizioni));
    	for(Player x : giocatori) {
    		s += x.toString();
    	}
    	this.txtResult.appendText(s);
    }

    @FXML
    void doSelectTeamUSA(ActionEvent event) {
    	String input = this.nAppearances.getText();
    	Integer nApparizioni;
    	String s = "\nTEAM USA:\n";
    	try {
    		nApparizioni = Integer.parseInt(input);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero nel campo numeroApparizioni");
    		return;
    	}
    	
    	List<Player>giocatori = new ArrayList<>(model.calcolaTeamUSA(nApparizioni));
    	for(Player x : giocatori) {
    		s += x.toString();
    	}
    	this.txtResult.appendText(s);
    }

    @FXML
    void doSimulateDay1(ActionEvent event) {
    	SimResult res = model.simula(model.getMatchesDay1(), model.getMatchesDay2(), model.getMatchesDay3());
    	String s = "\nRisultato Ryder Cup: [EUROPE: "+res.getPunteggioEUR()+"], [USA: "+res.getPunteggioUSA()+"]\n";
    	for(MatchDoppio m: res.getRisultatiDay1()) {
    		String risMatch = "";
    		if(m.getRisultatoMatch()<0) {
    			Integer delta = -m.getRisultatoMatch();
    			risMatch += delta+" UP (EU)";
    		}
    		if(m.getRisultatoMatch()==0) {
    			risMatch += "EVEN";
    		}
    		if(m.getRisultatoMatch()>0) {
    			risMatch += m.getRisultatoMatch()+" DOWN (EU)";
    		}
    		s+= "(Day:1) " + "["+m.getPlayer1EUR().getNome()+m.getPlayer1EUR().getCognome()+"+"+ m.getPlayer2EUR().getNome()+m.getPlayer2EUR().getCognome()+"] vs"+" ["+m.getPlayer1USA().getNome()+ m.getPlayer1USA().getCognome()+"+"+ m.getPlayer2USA().getNome()+m.getPlayer2USA().getCognome()+"] result: "+risMatch+"\n";

    	}
    	for(MatchDoppio m: res.getRisultatiDay2()) {
    		String risMatch = "";
    		if(m.getRisultatoMatch()<0) {
    			Integer delta = -m.getRisultatoMatch();
    			risMatch += delta+" UP (EU)";
    		}
    		if(m.getRisultatoMatch()==0) {
    			risMatch += "EVEN";
    		}
    		if(m.getRisultatoMatch()>0) {
    			risMatch += m.getRisultatoMatch()+" DOWN (EU)";
    		}
    		s+= "(Day:2) " + "["+m.getPlayer1EUR().getNome()+m.getPlayer1EUR().getCognome()+"+"+ m.getPlayer2EUR().getNome()+m.getPlayer2EUR().getCognome()+"] vs"+" ["+m.getPlayer1USA().getNome()+ m.getPlayer1USA().getCognome()+"+"+ m.getPlayer2USA().getNome()+m.getPlayer2USA().getCognome()+"] result: "+risMatch+"\n";

    	}
    	for(MatchSingolo m: res.getRisultatiDay3()) {
    		String risMatch = "";
    		if(m.getPunteggio()<0) {
    			Integer delta = -m.getPunteggio();
    			risMatch += delta+" UP (EU)";
    		}
    		if(m.getPunteggio()==0) {
    			risMatch += "EVEN";
    		}
    		if(m.getPunteggio()>0) {
    			risMatch += m.getPunteggio()+" DOWN (EU)";
    		}
    		s+= "(Day:3) " + "["+m.getPlayerEUR().getNome()+m.getPlayerEUR().getCognome()+"] vs"+" ["+m.getPlayerUSA().getNome()+ m.getPlayerUSA().getCognome()+"] result: "+risMatch+"\n";
    	}
    	this.txtResult.appendText(s);
    }

    @FXML
    void doSimulateDay2(ActionEvent event) {

    }

    @FXML
    void doSimulateDay3(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert nAppearances != null : "fx:id=\"nAppearances\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}

	

}
