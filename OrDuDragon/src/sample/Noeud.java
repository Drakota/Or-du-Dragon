package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.paint.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Charles, Jonathan and Philippe on 3/12/2017.
 */
public class Noeud extends Circle {
    List<Integer> list;
    public Noeud(List<Integer> List){
        super(List.get(1), List.get(2), 10);
        list = List;
        setStroke(Color.BLACK);
        if (List.get(3) == 1)
            setFill(Color.WHITE);
        if (List.get(3) == 0)
            setFill(Color.RED);
    }
}
