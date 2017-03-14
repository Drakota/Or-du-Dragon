package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

import javafx.scene.text.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    List<List<Integer>> list = new ArrayList<List<Integer>>();
    List<Point> listPoint = new ArrayList<Point>();
    Text texte;

    public void LirePosition(Socket s){
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader rd = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String line;
            while (!(line = rd.readLine()).equals("")) {
                List<Integer> listpoint = new ArrayList<Integer>();
                Scanner scanner = new Scanner(line);
                while (scanner.hasNextInt()) listpoint.add(scanner.nextInt());
                list.add(listpoint);
            }
            while ((line = rd.readLine()) != null) {
                List<Integer> listpoint = new ArrayList<Integer>();
                Scanner scanner = new Scanner(line);
                while (scanner.hasNextInt()) listpoint.add(scanner.nextInt());
                for (int i = 1; i < listpoint.size(); i++) {
                    list.get(listpoint.get(0)).add(listpoint.get(i));
                }
            }
        }
        catch(IOException ioe){
                 ioe.printStackTrace();
        }
    }

    public void SetPosition(Group root, List<List<Integer>> list) {
            for (int i = 0; i < list.size(); i++)
            {
                Point pointTemp = new Point(list.get(i).get(0), list.get(i).get(1), list.get(i).get(2), list.get(i).get(3));
                Button buttonTemp = new Button();
                listPoint.add(pointTemp);
                pointTemp.Afficher(root);
                System.out.println("---------------");
                System.out.println("Num : " + list.get(i).get(0));
                System.out.println("X : " + list.get(i).get(1));
                System.out.println("Y : " + list.get(i).get(2));
                System.out.println("Disponibilite : "+ list.get(i).get(3));
            }
    }

    public void AfficherLiaison(Group root){
            for (int i = 0; i < list.size(); i++)
            {
                for (int y = 4; y < list.get(i).size(); y++)
                {
                    Line ligne = new Line(list.get(i).get(1), list.get(i).get(2), list.get(list.get(i).get(y)).get(1), list.get(list.get(i).get(y)).get(2));
                    ligne.setStroke(Color.BLACK);
                    ligne.setStrokeWidth(2);
                    root.getChildren().add(ligne);
                }
            }
    }

    public void gererClic(MouseEvent e,Group root) {
        texte.setText(null);
        if (e.getTarget() instanceof Circle) {
            Circle ptn = (Circle) e.getTarget();
            for(int i=0;i<listPoint.size();++i){
                if(ptn.getCenterX() == listPoint.get(i).GetX() && ptn.getCenterY() == listPoint.get(i).GetY()) {
                    System.out.println("Noeud selectionné : " + listPoint.get(i).GetNumero());
                    texte.setText("Noeud selectionné : "+ listPoint.get(i).GetNumero());
                    Circle cercle = new Circle(listPoint.get(i).GetX(), listPoint.get(i).GetY(), 10);
                    cercle.setStroke(Color.BLACK);
                    cercle.setFill(Color.BLUE);
                    cercle.setStrokeWidth(1);
                    root.getChildren().add(cercle);
                }
            }
        }
    }

    @Override public void start(Stage stage) {
        try {
            String imgName = "nowhereland.png";
            int port = 51005;
            String ip = "149.56.47.97";
            Socket s = new Socket(ip, port);

            ImageView iv1 = new ImageView(getClass().getResource(imgName).toExternalForm());

            Group root = new Group();
            Scene scene = new Scene(root);
            scene.setFill(Color.BLACK);
            HBox box = new HBox();
            box.getChildren().add(iv1);
            root.getChildren().add(box);

            LirePosition(s);

            texte = new Text(100, 100, "");
            texte.setFont(new Font(40));
            texte.setFill(Color.BLACK);
            root.getChildren().add(texte);

            stage.setTitle("L'Or du Dragon");
            stage.setWidth(415);
            stage.setHeight(200);
            stage.setScene(scene);
            stage.sizeToScene();
            AfficherLiaison(root);
            SetPosition(root, list);
            stage.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e,root));
            stage.show();

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
