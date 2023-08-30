package com.demo;

public class Connective{
    private static Connective instance;
    private String defeasibleImplicationSymbol;
    private String conjunctionSymbol;
    private String disjunctionSymbol;
    private String implicationSymbol;
    private String biImplicationSymbol;
    private String negationSymbol;

    public Connective(){
        // Default symbols
        defeasibleImplicationSymbol = "~>";
        conjunctionSymbol = "&"; // ∧
        disjunctionSymbol = "||"; // ∨
        implicationSymbol = "=>"; // ⇒
        biImplicationSymbol = "<=>"; // ⇔
        negationSymbol = "!"; //¬
    }

    public static Connective getInstance(){
        if (instance == null){
            instance = new Connective();
        }
        return instance;
    }

    // Resets symbols to default.
    public void reset(){
        defeasibleImplicationSymbol = "~>";
        conjunctionSymbol = "&"; // ∧
        disjunctionSymbol = "||"; // ∨
        implicationSymbol = "=>"; // ⇒
        biImplicationSymbol = "<=>"; // ⇔
        negationSymbol = "!"; //¬
    }

    // Gets a random connective from the connectives provided.
    public static String getRandom(int[] conArr, Connective con){ 
        String connective = "";
        int type = (int)(Math.random() * conArr.length);
        switch(type){
            case 0:
                connective = con.getConjunctionSymbol();
                break;
            case 1:
                connective = con.getDisjunctionSymbol();
                break;
            case 2:
                connective = con.getImplicationSymbol();
                break;
            case 3:
                connective = con.getBiImplicationSymbol();
                break;
        }
        return connective;
    }
    
    // Allows the user to set a custom symbol for the defeaible implication
    public void setDefImplicationSymbol(String symbol){
        defeasibleImplicationSymbol = symbol;
    }

    // Allows the user to set a custom symbol for the conjunction connective
    public void setConjunctionSymbol(String symbol){
        conjunctionSymbol = symbol;
    }

    // Allows the user to set a custom symbol for the conjunction connective
    public void setDisjunctionSymbol(String symbol){
        disjunctionSymbol = symbol;
    }

    // Allows the user to set a custom symbol for the implication connective
    public void setImplicationSymbol(String symbol){
        implicationSymbol = symbol;
    }

    // Allows the user to set a custom symbol for the bi-implication connective
    public void setBiImplicationSymbol(String symbol){
        biImplicationSymbol = symbol;
    }

    // Allows the user to set a custom symbol for the negation connective
    public void setNegationSymbol(String symbol){
        negationSymbol = symbol;
    }

    public String getDefImplicationSymbol(){
        return defeasibleImplicationSymbol;
    }

    public String getConjunctionSymbol(){
        return conjunctionSymbol;
    }

    public String getDisjunctionSymbol(){
        return disjunctionSymbol;
    }

    public String getImplicationSymbol(){
        return implicationSymbol;
    }

    public String getBiImplicationSymbol(){
        return biImplicationSymbol;
    }

    public String getNegationSymbol(){
        return negationSymbol;
    }
}
