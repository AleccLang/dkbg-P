package com.demo;

public class Atom{
    private static Connective con = Connective.getInstance();
    private String atom;

    public Atom(){
        this.atom = null;
    }

    public Atom(Atom x){
        this.atom = new String(x.atom);
    }

    // Sets an atom to a specified string.
    public void setAtom(String string){
        atom = string;
    }

    // Negates an atom.
    public void negateAtom(){
        if(atom.startsWith(con.getNegationSymbol())){
            atom = atom.substring(1);
        }
        else{
            atom = con.getNegationSymbol() + atom;
        }
    }

    // String representaion of an atom.
    @Override
    public String toString(){
        return  atom;
    }
}
