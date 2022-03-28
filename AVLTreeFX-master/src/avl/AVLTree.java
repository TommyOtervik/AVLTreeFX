package avl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Modell klassen.
 *
 * AVL-treet med en indre klasse for noden.
 * Denne klassen kunne ha "extend'a" et BST tre, men vi valgte å opprette en egen klasse som implementerer Tree interface.
 *
 */
public class AVLTree<E extends Comparable<E>> implements Tree<E> {

    private final int MAX_RANDOM_INTEGERS = 10;
    private final int RANDOM_RANGE = 200;

    protected AVLTreeNode<E> root;
    protected int size;

    public AVLTree() {
    }

    public AVLTree(E[] objects) {
        for (E o : objects) add(o);
    }

    /**
     *
     * @param e Oppretter en ny node basert på denne
     */
    private AVLTreeNode<E> createNewNode(E e) {
        return new AVLTreeNode<>(e);
    }

    /**
     *
     * @param node Oppdaterer høyden til denne noden
     */
    private void updateHeight(AVLTreeNode<E> node) {
        if (node.left == null && node.right == null) // Node is a leaf
            node.height = 0;
        else if (node.left == null) // Node has no left subtree
            node.height = 1 + ((AVLTreeNode<E>)(node.right)).height;
        else if (node.right == null) // Node has no right subtree
            node.height = 1 + ((AVLTreeNode<E>)(node.left)).height;
        else
            node.height = 1 + Math.max(((AVLTreeNode<E>)(node.right)).height,
                    ((AVLTreeNode<E>)(node.left)).height);
    }

    /**
     * public metode for find
     */
    public E find(int k) {
        if (k < 1 || k > size )
            return null;
        return find(k, root);
    }

    /**
     * @param k finner minste verdi basert på denne
     *
     *  Rekursiv metode, besert på rot noden, ser på vestre og høyre barn.
     */
    private E find(int k, AVLTreeNode<E> root) {

        AVLTreeNode<E> A = root.left;
        AVLTreeNode<E> B = root.right;

        if (A == null && k == 1)
            return root.element;
        else if (A == null && k == 2)
            return B.element;
        else if (k <= A.size)
            return find(k, A);
        else if (k == A.size + 1)
            return root.element;
        else
            return find(k - A.size - 1, B);
    }


    private void updateSize() {
        updateSize((AVLTreeNode<E>)root);
    }

    /**
     *
     * @param node Oppdateren size av noden, dette tilfellet root.
     * Blir brukt etter innsetting og sletting.
     */
    private int updateSize(AVLTreeNode<E> node) {
        if (node == null) return 0;
        else {
            node.size = 1 + updateSize((AVLTreeNode<E>)node.left) + updateSize((AVLTreeNode<E>)node.right);
            return node.size;
        }
    }


    /**
     * @param e Søker etter en spesifikk node
     */
    @Override
    public boolean search(E e) {
        AVLTreeNode<E> current = root;

        while (current != null) {
            if (e.compareTo(current.element) < 0)
                current = current.left;
            else if (e.compareTo(current.element) > 0)
                current = current.right;
            else
                return true;
        }
        return false;
    }

    /**
     *
     * @param e Setter inn
     */
    @Override
    public boolean insert(E e) {
        if (root == null) root = createNewNode(e);
        else {
            AVLTreeNode<E> parent = null;
            AVLTreeNode<E> current = root;
            while (current != null)
                if (e.compareTo(current.element) < 0) {
                    parent = current;
                    current = current.left;
                }
                else if (e.compareTo(current.element) > 0) {
                    parent = current;
                    current = current.right;
                }
                else
                    return false;

            if (e.compareTo(parent.element) < 0)
                parent.left = createNewNode(e);
            else
                parent.right = createNewNode(e);



        }

        balancePath(e);
        updateSize();
        size++;
        return true;
    }

    /**
     * @param e Balansere noden i banen fra den angitte noden til roten om nødvendig.
     */
    private void balancePath(E e) {
        ArrayList<AVLTreeNode<E>> path = path(e);

        for (int i = path.size() - 1; i >= 0; i--) {
            AVLTreeNode<E> A = (AVLTreeNode<E>)(path.get(i));
            updateHeight(A);
            AVLTreeNode<E> parentOfA = (A == root) ? null : (AVLTreeNode<E>)(path.get(i - 1));

            switch (balanceFactor(A)) {
                case -2:
                    if (balanceFactor((AVLTreeNode<E>)A.left) <= 0) {
                        balanceLL(A, parentOfA);
                    }
                    else {
                        balanceLR(A, parentOfA);
                    }
                    break;
                case +2:
                    if (balanceFactor((AVLTreeNode<E>)A.right) >= 0) {
                        balanceRR(A, parentOfA);
                    }
                    else {
                        balanceRL(A, parentOfA);
                    }
            }
        }
    }

    /**
     * @return "Balanse faktor for en spesifikk node"
     *
     */
    private int balanceFactor(AVLTreeNode<E> node) {
        if (node.right == null)
            return -node.height;
        else if (node.left == null)
            return +node.height;
        else
            return ((AVLTreeNode<E>)node.right).height - ((AVLTreeNode<E>)node.left).height;
    }


    /**
     * LL ubalanse oppstår ved en node A, slik at A har en balansefaktor på -2 og
     * et venstre barn B med en balansefaktor på -1 eller 0. Utfør en enkelt høyre rotasjon ved A.
     */
    private void balanceLL(AVLTreeNode<E> A, AVLTreeNode<E> parentOfA) {
        AVLTreeNode<E> B = A.left;

        if (A == root) {
            root = B;
        }
        else {
            if (parentOfA.left == A) {
                parentOfA.left = B;
            }
            else {
                parentOfA.right = B;
            }
        }
        A.left = B.right; // Make T2 the left subtree of A
        B.right = A; // Make A the left child of B
        updateHeight((AVLTreeNode<E>)A);
        updateHeight((AVLTreeNode<E>)B);
    }


    /**
     * LR ubalanse oppstår ved en node A, slik at A har en balansefaktor på -2 og et venstre barn B med en balansefaktor på +1.
     * Utfør en dobbel rotasjon (først en enkelt venstre rotasjon ved B, deretter en enkelt høyre rotasjon ved A)
     */
    private void balanceLR(AVLTreeNode<E> A, AVLTreeNode<E> parentOfA) {
        AVLTreeNode<E> B = A.left; // A is left-heavy
        AVLTreeNode<E> C = B.right; // B is right heavy

        if (A == root) {
            root = C;
        }
        else {
            if (parentOfA.left == A) {
                parentOfA.left = C;
            }
            else {
                parentOfA.right = C;
            }
        }

        A.left = C.right; // Make T3 the left subtree of A
        B.right = C.left; // Make T2 the right subtree of B
        C.left = B;
        C.right = A;

        updateHeight((AVLTreeNode<E>)A);
        updateHeight((AVLTreeNode<E>)B);
        updateHeight((AVLTreeNode<E>)C);
    }

    /**
     * RR ubalanse oppstår ved en node A, slik at A har en balansefaktor på +2 og
     * et høyre barn B med en balansefaktor på +1 eller 0. Utfør en enkelt venstre rotasjon ved A.
     */
    private void balanceRR(AVLTreeNode<E> A, AVLTreeNode<E> parentOfA) {
        AVLTreeNode<E> B = A.right; // A is right-heavy and B is right-heavy
        if (A == root) {
            root = B;
        }
        else {
            if (parentOfA.left == A) {
                parentOfA.left = B;
            }
            else {
                parentOfA.right = B;
            }
        }
        A.right = B.left; // Make T2 the right subtree
        B.left = A;
        updateHeight((AVLTreeNode<E>)A);
        updateHeight((AVLTreeNode<E>)B);
    }

    /**
     * RL ubalanse oppstår ved en node A, slik at A har en balansefaktor på +2 og et høyre barn B med en balansefaktor på -1.
     * Utfør en dobbel rotasjon (først en enkelt høyre rotasjon ved B, deretter en enkelt venstre rotasjon ved A)
     */
    private void balanceRL(AVLTreeNode<E> A, AVLTreeNode<E> parentOfA) {
        AVLTreeNode<E> B = A.right; // A is right-heavy
        AVLTreeNode<E> C = B.left; // B is left-heavy

        if (A == root) {
            root = C;
        }
        else {
            if (parentOfA.left == A) {
                parentOfA.left = C;
            }
            else {
                parentOfA.right = C;
            }
        }
        A.right = C.left; // Make T2 the right subtree of A
        B.left = C.right; // Make T3 the left subtree of B
        C.left = A;
        C.right = B;

        updateHeight((AVLTreeNode<E>)A);
        updateHeight((AVLTreeNode<E>)B);
        updateHeight((AVLTreeNode<E>)C);
    }


    /**
     *
     * @return Returnerer en bane fra roten som fører til det angitte elementet
     */
    public ArrayList<AVLTreeNode<E>> path(E e) {
        ArrayList<AVLTreeNode<E>> list = new ArrayList<>();
        AVLTreeNode<E> current = root;

        while (root != null) {
            list.add(current);
            if (e.compareTo(current.element) < 0)
                current = current.left;
            else if (e.compareTo(current.element) > 0)
                current = current.right;
            else
                break;
        }
        return list;
    }


    /**
     *
     * @param element Slett et element fra AVL-treet.
     * @return Returner true hvis elementet er slettet. Returner false hvis elementet ikke er i treet.
     */
    @Override
    public boolean delete(E element) {
        if (root == null)
            return false;
        // Finn noden som skal slettes, og finn også den overordnede noden
        AVLTreeNode<E> parent = null;
        AVLTreeNode<E> current = root;
        while (current != null) {
            if (element.compareTo(current.element) < 0) {
                parent = current;
                current = current.left;
            }
            else if (element.compareTo(current.element) > 0) {
                parent = current;
                current = current.right;
            }
            else
                break;
        }

        if (current == null)
            return false;


        // Case 1: nåværende har ingen vestre barn
        if (current.left == null) {
            // Koble foreldrene med det høyre barnet til den nåværende noden
            if (parent == null) {
                root = current.right;
            }
            else {
                if (element.compareTo(parent.element) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;

                balancePath(parent.element);
            }
        }
        else {
            // Case 2: Den nåværende noden har et venstre barn
            // Finn noden lengst til høyre i det venstre subtreet til den nåværende noden, og også dens overordnede.
            AVLTreeNode<E> parentOfRightMost = current;
            AVLTreeNode<E> rightMost = current.left;

            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right; // Forsett til høyre
            }
            // Erstatt elementet i gjeldende med elementet i rightMost
            current.element = rightMost.element;

            // Fjern node lengst til høyre
            if (parentOfRightMost.right == rightMost)
                parentOfRightMost.right = rightMost.left;
            else
                // Special case: parentOfRightMost er aktuell
                parentOfRightMost.left = rightMost.left;

            // Balansere treet om nødvendig
            balancePath(parentOfRightMost.element);

        }

        updateSize();
        size--;
        return true;
    }

    @Override
    public void inOrder() {
        inOrder(root);
    }


    protected void inOrder(AVLTreeNode<E> root) {
        if (root == null) return;
        inOrder(root.left);
        System.out.print(root.element + " ");
        inOrder(root.right);
    }

    /**
     *
     * @return tabell med tilfeldige tall, basert på MAX_RANDOM_INTEGERS og RANDOM_RANGE
     * I dette tilfellet legges 0-200 inn i en liste (unike), omrokkerer disse og sender de videre
     * som en mindre tabell.
     */
    public Integer[] randomIntegers() {
        Integer[] intArr = new Integer[MAX_RANDOM_INTEGERS];
        ArrayList<Integer> intList = new ArrayList<>();

        for (int i = 0; i <= RANDOM_RANGE; i++) {
            intList.add(i);
        }
        Collections.shuffle(intList);

        intArr = intList.subList(0, MAX_RANDOM_INTEGERS).toArray(intArr);

        return intArr;
    }



    @Override
    public int getSize() {
        return size;
    }

    public AVLTreeNode<E> getRoot() {
        return root;
    }

    @Override
    public Iterator<E> iterator() {
        return new InorderIterator();
    }

    private class InorderIterator implements Iterator<E> {
        private ArrayList<E> list = new ArrayList<>();
        private int current = 0;

        public InorderIterator() {
            inOrder();
        }

        private void inOrder() {
            inOrder(root);
        }

        private void inOrder(AVLTreeNode<E> root) {
            if (root == null) return;
            inOrder(root.left);
            list.add(root.element);
            inOrder(root.right);
        }

        @Override
        public boolean hasNext() {
            return current < list.size() ? true : false;
        }

        @Override
        public E next() {
            return list.get(current++);
        }

        @Override
        public void remove() {
            if (current == 0) // next() has not been called yet
                throw new IllegalStateException();

            delete(list.get(--current));
            list.clear(); // Clear the list
            inOrder(); // Rebuild the list
        }
    }


    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * statisk indre klasse for nodene.
     */
    public static class AVLTreeNode<E> {
        protected int height = 0;
        protected int size = 0;
        protected E element;
        protected AVLTreeNode<E> left;
        protected AVLTreeNode<E> right;

        public AVLTreeNode(E e) {
            this.element = e;
        }


    }
}
