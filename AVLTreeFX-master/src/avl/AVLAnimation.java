package avl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *  Kontroller klassen, styrer / sender input videre.
 */

public class AVLAnimation extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        AVLTree<Integer> tree = new AVLTree<>();
        BorderPane pane = new BorderPane();
        AVLView view = new AVLView(tree);
        pane.setCenter(view);

        TextField tfKey = new TextField();
        tfKey.setPrefColumnCount(5);
        tfKey.setAlignment(Pos.BASELINE_RIGHT);
        Button btInsert = new Button("Insert");
        Button btDelete = new Button("Delete");
        Button btRandom = new Button("Random_10");
        Button btSearch = new Button("Search");
        Button btKth = new Button("kTh element");
        HBox hBox = new HBox(5);
        hBox.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox.getChildren().addAll(new Label("Enter a key: "), tfKey, btSearch, btInsert, btDelete, btRandom, btKth);
        hBox.setAlignment(Pos.CENTER);
        pane.setTop(hBox);



        btInsert.setOnAction(e -> {
            int key = Integer.parseInt(tfKey.getText());
            if (tree.search(key)) { // Key already in the tree
                view.displayTree();
                view.setStatus(key + " is already in the tree");
            }
            else {
                tree.insert(key);
                view.displayTree();
                view.setStatus(key + " is inserted in the tree");
            }
        });

        btDelete.setOnAction(e -> {
            int key = Integer.parseInt(tfKey.getText());
            if (!tree.search(key)) { // key is not in the tree
                view.displayTree();
                view.setStatus(key + " is not in the tree");
            }
            else {
                tree.delete(key);
                view.displayTree();
                view.setStatus(key + " is deleted from the tree");
            }
        });

        btRandom.setOnAction(e -> {
            view.displayRandom();
            view.setStatus("10 random integers added");
            view.displayTree();
        });

        btSearch.setOnAction(e -> {
            int key = Integer.parseInt(tfKey.getText());
            if (!tree.search(key)) {
                view.displayTree();
                view.setStatus(key + " is not in the tree");
            }
            else {
                view.displayTree();
                view.setStatus(key + " is in the tree");
            }
        });

        btKth.setOnAction(e -> {
            int key = Integer.parseInt(tfKey.getText());
            view.displayTree();
            view.setStatus(key + "(th) smallest number is " + tree.find(key));
        });


        Scene scene = new Scene(pane, 600, 450);
        primaryStage.setTitle("AVLAnimation");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
