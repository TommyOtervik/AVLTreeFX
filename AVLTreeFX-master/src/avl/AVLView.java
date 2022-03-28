package avl;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Arrays;

/**
 * View klasse.
 * Oppretter et den grafiske delen av oppgaven, den har en instans av AVL-klassen. Her legges verdiene inn i treet og tegner det.
 */

public class AVLView extends Pane {

    private AVLTree<Integer> tree = new AVLTree<>();
    private final double RADIUS = 20;
    private final double VGAP = 60;

    private Circle circle;


    public AVLView(AVLTree<Integer> tree) {
        this.tree = tree;
        this.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        setStatus("Tree is empty");
    }

    public void setStatus(String s) {
        getChildren().add(new Text(20, 20, s));
    }

    /**
     *  Tegner treet med rekursjon
     */
    public void displayTree() {
        this.getChildren().clear();
        if (tree.getRoot() != null)
            displayTree(tree.getRoot(), getWidth() / 2, VGAP, getWidth() / 4);
    }

    /**
     * Tegner subtre ut i fra x, y
     *
     * @param root rot noden
     * @param x bruker bredde / 2 av vindu som inn param
     * @param y VGAP (finn en passelig størrelse)
     * @param hGap bruker høyde / 4 av vindu som inn param, og hGap/2 for å tegne
     */
    private void displayTree(AVLTree.AVLTreeNode<Integer> root, double x, double y, double hGap) {
        if (root.left != null) {

            getChildren().add(new Line(x - hGap, y + VGAP, x, y));
            displayTree(root.left, x - hGap, y + VGAP, hGap / 2);
        }

        if (root.right != null) {

            getChildren().add(new Line(x + hGap, y + VGAP, x, y));
            displayTree(root.right, x + hGap, y + VGAP, hGap / 2);
        }


        circle = new Circle(x, y, RADIUS);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.RED);
        getChildren().addAll(circle, new Text(x - 4, y + 4, root.element + ""));
    }

    /**
     * Henter ut 10 tilfeldige verdier og legger det til et nytt tre,
     * viser treet ved å kalle displayTree().
     */
    public void displayRandom() {
        this.getChildren().clear();
        tree.clear();

        tree.addAll(Arrays.asList(tree.randomIntegers()));

        if (tree.getRoot() != null)
            displayTree(tree.getRoot(), getWidth() / 2, VGAP, getWidth() / 4);

    }

}
