package avl;

import java.util.Collection;

/**
 * Gir standard implementeringer til AVLTree'et.
 *
 * Her blir det noen ubrukte metoder siden vi extender Collection, inOrder er brukt i en liten test applikasjon.
 *
 */

public interface Tree<E> extends Collection<E> {


    boolean search(E e);

    boolean insert(E e);

    boolean delete(E e);

    int getSize();

    default void inOrder(){}

    @Override
    default boolean isEmpty(){
        return size() == 0;
    }

    @Override
    default boolean contains(Object e){
        return search( (E)e );
    }


    @Override
    default boolean add(E e){
        return insert(e);
    }

    @Override
    default boolean remove(Object e) {
        return delete((E)e);
    }

    @Override
    default int size() {
        return getSize();
    }

    @Override
    default boolean addAll(Collection<? extends E> collection) {
        boolean mod = false;
        for (E value : collection) {
            if (add(value))
                mod = true;
        }
        return mod;
    }

    @Override
    default boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    default boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    default boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    default Object[] toArray() {
        return null;
    }

    @Override
    default <T> T[] toArray(T[] ts) {
        return null;
    }



}
