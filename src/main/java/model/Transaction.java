package model;

import java.util.Set;

public class Transaction {
    private final char id;
    private final char leftAction;
    private final Set<Character> rightActions;

    public Transaction(char id, char leftAction, Set<Character> rightActions){
        this.id = id;
        this.leftAction = leftAction;
        this.rightActions = rightActions;
    }

    public char getId() {
        return id;
    }

    public char getLeftAction(){
        return leftAction;
    }

    public Set<Character> getRightActions() {
        return rightActions;
    }

    @Override
    public String toString() {
        return "(%c) %c := %s".formatted(id,leftAction,rightActions.toString());
    }
}
