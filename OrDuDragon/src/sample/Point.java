package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Charles, Jonathan and Philippe on 3/12/2017.
 */
public class Point extends Circle {
    int numero;
    int X;
    int Y;
    boolean etat;

    public Point(int numero, int X, int Y, int etat){
        SetNumero(numero);
        SetX(X);
        SetY(Y);
        SetEtat(etat);
    }

    public void SetNumero(int numero){
        this.numero = numero;
    }

    public void SetX(int X){
        this.X = X;
    }

    public void SetY(int Y){
        this.Y = Y;
    }

    public void SetEtat(int dispo){
        if(dispo == 1)
            this.etat = true;
        else
            this.etat = false;
    }

    public int GetNumero(){return numero;}

    public int GetX(){return X;}

    public int GetY(){return Y;}

    public boolean GetEtat(){return etat;}

    public void Afficher(Group root){
        Circle cercle = new Circle(X, Y, 10);
        cercle.setStrokeWidth(100);
        cercle.setStroke(Color.BLACK);

        if(this.etat == false)
            cercle.setFill(Color.RED);
        else
            cercle.setFill(Color.WHITE);
        cercle.setStrokeWidth(1);
        root.getChildren().add(cercle);
    }

}
