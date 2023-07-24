package com.demo;

public class Formula{

    private static Connective con = Connective.getInstance();

    String antecedent;
    String consequent;

    public Formula(String ant, String cons){
        this.antecedent = ant;
        this.consequent = cons;
    }

    public static boolean checkConsequent(Formula formula, Atom atom){ // Checks if an atom is included in the formulas consequent.
        boolean val;
        String[] formulaSplit = formula.toString().split(con.getDefImplicationSymbol(), 2);
        if(formulaSplit[1].contains(atom.toString())){
            val = true;
        }
        else{
            val = false;
        }
        return val;
    }

    public static boolean checkAntecedent(Formula formula, Atom atom){ // Checks if an atom is included in the formulas antecedent.
        boolean val;
        String[] formulaSplit = formula.toString().split(con.getDefImplicationSymbol(), 2);
        if(formulaSplit[0].contains(atom.toString())){
            val = true;
        }
        else{
            val = false;
        }
        return val;
    }

    public void setAntecedent(Object[] ant){
        StringBuilder antecedentBuilder = new StringBuilder();
        for(int i = 0; i < ant.length; i++){
            if(ant[i] == null){
                
            }
            else{
                antecedentBuilder.append(ant[i].toString());
            } 
        }
        antecedent = antecedentBuilder.toString();
    }

    public void setConsequent(Object[] con){
        StringBuilder consequenBuilder = new StringBuilder();
        for(int i = 0; i < con.length; i++){
            if(con[i] == null){
                
            }
            else{
                consequenBuilder.append(con[i].toString());
            } 
        }
        antecedent = consequenBuilder.toString();
    }

    public String getAntecedent(){ // Gets the formulas antecedent
        return antecedent;
    }

    public String getConsequent(){ // Gets the formulas consequent
        return consequent;
    }

    @Override
    public String toString(){
        return  antecedent + con.getDefImplicationSymbol() + consequent;
    }

}
