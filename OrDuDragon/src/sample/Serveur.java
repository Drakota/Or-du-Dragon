package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Charles on 3/20/2017.
 */
public class Serveur {

    public static List<Noeud> listNoeud = new ArrayList<Noeud>();
    public Serveur(){}
    public void LirePosition(){
        try {
            int port = 51005;
            String ip = "149.56.47.97";
            Socket s = new Socket(ip, port);
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

    public void SetPosition(Group root) {
        for (int i = 0; i < listNoeud.size(); i++)
        {
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

    public void RestartMap()
    {
        for (int i = 0; i < listNoeud.size(); i++)
        {
            if (listNoeud.get(i).list.get(3) == 1) listNoeud.get(i).setFill(Color.WHITE);
            else if (listNoeud.get(i).list.get(3) == 0) listNoeud.get(i).setFill(Color.RED);
        }
    }
}
