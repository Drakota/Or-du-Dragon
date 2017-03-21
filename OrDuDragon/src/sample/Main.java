package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    Noeud LastSelectedNode;
    Text texte;
    Boolean RejoindrePartie = false;
    Joueur j = new Joueur();
    Serveur sv = new Serveur();
    Thread q = new Thread(new Runnable() {
        @Override
        public void run() {
            sv.Questions();
        }
    });

    public void gererClic(MouseEvent e,Group root) {
        texte.setText(null);
        if(LastSelectedNode != null) {
            LastSelectedNode.setStroke(Color.BLACK);
            LastSelectedNode = null;
        }
        if (e.getTarget() instanceof Noeud) {
            Noeud ptn = (Noeud) e.getTarget();
            if(e.getClickCount() == 2) {
                if (!RejoindrePartie) {
                    j.CONNEXION();
                    q.start();
                    RejoindrePartie = true;
                }
                else
                {
                    j.GOTO(ptn.list.get(0));
                }
            }
            ptn.setStroke(Color.BLUE);
            LastSelectedNode = ptn;
            texte.setText("Noeud selectionné : "+ ptn.list.get(0) + "\n" +
                    "Coordonnées : " + "X : " + ptn.list.get(1) + " Y : " + ptn.list.get(2));
        }
        if (e.getTarget() instanceof Bouton) {
            Bouton btn = (Bouton) e.getTarget();
            if (btn.GetRole().equals("BUILD")) {
                j.BUILD();
            }
            else if (btn.GetRole().equals("QUIT")) {
                j.QUIT();
            }
        }
    }

    @Override public void start(Stage stage) {
            String imgName = "nowhereland.png";

            ImageView iv1 = new ImageView(getClass().getResource(imgName).toExternalForm());

            Group root = new Group();
            Scene scene = new Scene(root);
            scene.setFill(Color.BLACK);
            HBox box = new HBox();
            box.getChildren().add(iv1);
            root.getChildren().add(box);

            Bouton joinBtn = new Bouton("QUIT", 1450, 800, "Quitter la Partie", root);
            Bouton buildBtn = new Bouton("BUILD", 1150, 800, "Contruire", root);

            texte = new Text(0, 30, "");
            texte.setFont(new Font(20));
            texte.setFill(Color.BLACK);
            root.getChildren().add(texte);

            stage.setTitle("L'Or du Dragon");
            stage.setWidth(415);
            stage.setHeight(200);
            stage.setScene(scene);
            stage.sizeToScene();

            sv.LirePosition();
            sv.AfficherLiaison(root);
            sv.SetPosition(root);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    sv.RefreshMap();
                }
            });
            t.setDaemon(true);
            t.start();
            q.setDaemon(true);
            stage.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e, root));
            stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
