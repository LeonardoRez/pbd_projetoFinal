/*
 classe que controla a cena principal
 */
package frontend.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import backend.*;
import java.io.FileNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * FXML Controller class
 *
 * @author Élenn Dypaulla Silva Milhomem
 * @author Nikael Fernandes Rodrigues Xavier
 * @author Mateus Ferreira da Silva
 *
 */
public class TelaPrincipalController implements Initializable {

    @FXML
    private TableView<Object[]> tabela;
    private ArrayList<TableColumn<Object, String>> colunas;
    private ObservableList<Object[]> obRows;

    public TelaPrincipalController() {
        colunas = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBFGerenciador dbfG = new DBFGerenciador("../backend/arquivo.dbf");

        try {
            obRows = FXCollections.observableArrayList(dbfG.carregarRegistros());
            int i=0;
            for (String s : dbfG.carregarColunas()) {
                TableColumn tc = new TableColumn(s);
                final int c = i++;
                TableColumn<Object[], String> column = new TableColumn<>(s);
                tc.setCellValueFactory(param -> {
////                    ArrayList<Object[]> cells = param;
////                    return new SimpleStringProperty(cells.size() > c ? cells.get(c).toString() : null);
                });
                tabela.getColumns().add(tc);
                tabela.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            }
            tabela.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            obRows = FXCollections.observableArrayList(dbfG.carregarRegistros());
            tabela.setItems(obRows);
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo dbf não encontrado!");

        }
    }

}
