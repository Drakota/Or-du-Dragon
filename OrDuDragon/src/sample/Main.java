package sample;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    public void SetPosition(Socket s, Group root) {
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader rd = new BufferedReader(new InputStreamReader(s.getInputStream()));
            int X;
            int Y;
            int num;
            int i = 1;
            String line;
            List<Integer> list = new ArrayList<Integer>();

            while (!(line = rd.readLine()).equals("")) {
                Scanner scanner = new Scanner(line);
                while (scanner.hasNextInt()) {
                    int v = scanner.nextInt();
                    list.add(v);
                }
            }
            while(i < list.size()){
                num = list.get(i);
                X = list.get(i);
                Y = list.get(i+1);
                AffichePosition(X,Y,root);
                i+=4;
                System.out.println("Num : " +num);
                System.out.println("X : " +X);
                System.out.println("Y : " +Y);
            }
        }catch(IOException ioe){
                ioe.printStackTrace();
            }
    }

    public void AffichePosition(int X, int Y,Group root){
        Circle cercle = new Circle(X, Y, 10);
        cercle.setStroke(Color.BLACK);
        cercle.setFill(Color.BLACK);
        cercle.setStrokeWidth(1);
        root.getChildren().add(cercle);
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

            stage.setTitle("L'Or du Dragon");
            stage.setWidth(415);
            stage.setHeight(200);
            stage.setScene(scene);
            stage.sizeToScene();
            SetPosition(s,root);
            stage.show();

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
