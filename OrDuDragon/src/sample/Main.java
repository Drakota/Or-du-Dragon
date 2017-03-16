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

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {
    public static List<Noeud> listNoeud = new ArrayList<Noeud>();
    Noeud LastSelectedNode;
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
                listNoeud.add(new Noeud(listpoint));
            }
            while ((line = rd.readLine()) != null) {
                List<Integer> listpoint = new ArrayList<Integer>();
                Scanner scanner = new Scanner(line);
                while (scanner.hasNextInt()) listpoint.add(scanner.nextInt());
                for (int i = 1; i < listpoint.size(); i++) {
                    listNoeud.get(listpoint.get(0)).list.add(listpoint.get(i));
                }
            }
        }
        catch(IOException ioe){
                 ioe.printStackTrace();
        }
    }
    public void RestartMap()
    {
        for (int i = 0; i < listNoeud.size(); i++)
        {
            if (listNoeud.get(i).list.get(3) == 1) listNoeud.get(i).setFill(Color.WHITE);
            else if (listNoeud.get(i).list.get(3) == 0) listNoeud.get(i).setFill(Color.RED);
        }
    }

    public void SetPosition(Group root) {
            for (int i = 0; i < listNoeud.size(); i++)
            {
                Button buttonTemp = new Button();
                root.getChildren().add(listNoeud.get(i));
                System.out.println("---------------");
                System.out.println("Num : " + listNoeud.get(i).list.get(0));
                System.out.println("X : " + listNoeud.get(i).list.get(1));
                System.out.println("Y : " + listNoeud.get(i).list.get(2));
                System.out.println("Disponibilite : "+ listNoeud.get(i).list.get(3));
            }
    }

    public void AfficherLiaison(Group root){
            for (int i = 0; i < listNoeud.size(); i++)
            {
                for (int y = 4; y < listNoeud.get(i).list.size(); y++)
                {
                    Line ligne = new Line(listNoeud.get(i).list.get(1), listNoeud.get(i).list.get(2),
                            listNoeud.get(listNoeud.get(i).list.get(y)).list.get(1), listNoeud.get(listNoeud.get(i).list.get(y)).list.get(2));
                    ligne.setStroke(Color.BLACK);
                    ligne.setStrokeWidth(4);
                    root.getChildren().add(ligne);
                }
            }
    }
    public void gererClic(MouseEvent e,Group root) {
        if(LastSelectedNode != null) listNoeud.get(LastSelectedNode.list.get(0)).setFill(LastSelectedNode.getFill());
        texte.setText(null);
        if (e.getTarget() instanceof Circle) {
            Circle ptn = (Circle) e.getTarget();
            for(int i = 0; i < listNoeud.size(); ++i){
                if(ptn.getCenterX() == listNoeud.get(i).list.get(1) && ptn.getCenterY() == listNoeud.get(i).list.get(2)) {
                    LastSelectedNode = listNoeud.get(i);
                    System.out.println("Noeud selectionné : " + listNoeud.get(i).list.get(0));
                    texte.setText("Noeud selectionné : "+ listNoeud.get(i).list.get(0) + "\n" +
                    "Coordonnées : " + "X : " + listNoeud.get(i).list.get(1) + " Y : " + listNoeud.get(i).list.get(2));
                    listNoeud.get(i).setFill(Color.BLUE);
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

            texte = new Text(0, 30, "");
            texte.setFont(new Font(20));
            texte.setFill(Color.BLACK);
            root.getChildren().add(texte);

            stage.setTitle("L'Or du Dragon");
            stage.setWidth(415);
            stage.setHeight(200);
            stage.setScene(scene);
            stage.sizeToScene();

            LirePosition(s);
            AfficherLiaison(root);
            SetPosition(root);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    RefreshMap();
                }
            });
            t.start();
            stage.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e, root));
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void RefreshMap()
    {
        try {
            int port = 51006;
            String ip = "149.56.47.97";
            Socket s = new Socket(ip, port);
            BufferedReader rd = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            while(true) {
                writer.print("\n");
                String line = rd.readLine();
                System.out.println(line);
                if (!line.equals(" ")) RestartMap();
                String[] parts = line.split(" ");
                for (int i = 0; i < parts.length; i++)
                {
                    String[] t = parts[i].split(":");
                    if (t[1].equals("T")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.GREEN);
                    if (t[1].equals("P")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.YELLOW);
                    if (t[1].equals("M")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.DARKGREEN);
                    if (t[1].equals("G")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.LIGHTGREEN);
                    if (t[1].equals("J")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.ROYALBLUE);
                    if (t[1].equals("D")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.ORANGE);
                    if (t[1].equals("A")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.BROWN);
                    if (t[1].equals("N")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.BLACK);
                    if (t[1].equals("C")) listNoeud.get(Integer.parseInt(t[0])).setFill(Color.DARKGREY);
                }
                writer.flush();
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
