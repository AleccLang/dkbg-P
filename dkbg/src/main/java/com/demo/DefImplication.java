package com.demo;

public class DefImplication{

    private static Connective con = Connective.getInstance();
    private String antecedent;
    private String consequent;

    public DefImplication(String ant, String cons){
        this.antecedent = ant;
        this.consequent = cons;
    }

    // Checks if an atom is included in the defImplications consequent.
    public static boolean checkConsequent(DefImplication defImplication, Atom atom){ 
        boolean val;
        String[] defImplicationSplit = defImplication.toString().split(con.getDefImplicationSymbol(), 2);
        if(defImplicationSplit[1].contains(atom.toString())){
            val = true;
        }
        else{
            val = false;
        }
        return val;
    }

    // Checks if an atom is included in the defImplications antecedent.
    public static boolean checkAntecedent(DefImplication defImplication, Atom atom){ 
        boolean val;
        String[] defImplicationSplit = defImplication.toString().split(con.getDefImplicationSymbol(), 2);
        if(defImplicationSplit[0].contains(atom.toString())){
            val = true;
        }
        else{
            val = false;
        }
        return val;
    }

    // Sets the antecedent of a DI.
    public void setAntecedent(Object[] ant){
        StringBuilder antecedentBuilder = new StringBuilder();
        for(int i = 0; i < ant.length; i++){
            if(ant[i] != null){
                antecedentBuilder.append(ant[i].toString());
            } 
        }
        antecedent = antecedentBuilder.toString();
    }

    // Sets the consequent of a DI.
    public void setConsequent(Object[] con){
        StringBuilder consequenBuilder = new StringBuilder();
        for(int i = 0; i < con.length; i++){
            if(con[i] != null){
                consequenBuilder.append(con[i].toString());
            } 
        }
        antecedent = consequenBuilder.toString();
    }

    // Gets the defImplications antecedent
    public String getAntecedent(){ 
        return antecedent;
    }

    // Gets the defImplications consequent
    public String getConsequent(){ 
        return consequent;
    }

    // String representation of a DI.
    @Override
    public String toString(){
        return  antecedent + con.getDefImplicationSymbol() + consequent;
    }

}
