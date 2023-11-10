package it.polito.tdp.RyderCupSimulator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.RyderCupSimulator.model.Model;
import it.polito.tdp.RyderCupSimulator.model.Player;
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
    void doGenerateMatchTable(ActionEvent event) {
    	String input = this.nAppearances.getText();
    	Integer nApparizioni;
    	try {
    		nApparizioni = Integer.parseInt(input);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero nel campo numeroApparizioni");
    		return;
    	}
    	List<Player>giocatori ;
    	
    }

    @FXML
    void doSelectTeamEurope(ActionEvent event) {

    }

    @FXML
    void doSelectTeamUSA(ActionEvent event) {

    }

    @FXML
    void doSimulateDay1(ActionEvent event) {

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

}
