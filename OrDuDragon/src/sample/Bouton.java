package sample;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

import java.util.Stack;


/**
 * Created by Charles on 3/20/2017.
 */
public class Bouton extends Rectangle {

    String role;
    StackPane SP = new StackPane();

    public String GetRole() {return role;}

    public void SetRole(String role){
        this.role = role;
    }

    public Bouton(String role, int PosX, int PosY, String Texte, Group root){
        super(130, 70);
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        setStrokeWidth(3);
        SetRole(role);
        SP.getChildren().addAll(this, new Label(Texte));
        SP.setLayoutX(PosX);
        SP.setLayoutY(PosY);
        root.getChildren().add(SP);
    }
}
