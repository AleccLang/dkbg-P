package com.demo;

public class DefImplication{

    private static Connective con = Connective.getInstance();

    String antecedent;
    String consequent;

    public DefImplication(String ant, String cons){
        this.antecedent = ant;
        this.consequent = cons;
    }

    public static boolean checkConsequent(DefImplication defImplication, Atom atom){ // Checks if an atom is included in the defImplications consequent.
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

    public static boolean checkAntecedent(DefImplication defImplication, Atom atom){ // Checks if an atom is included in the defImplications antecedent.
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

    public void setAntecedent(Object[] ant){
        StringBuilder antecedentBuilder = new StringBuilder();
        for(int i = 0; i < ant.length; i++){
            if(ant[i] != null){
                antecedentBuilder.append(ant[i].toString());
            } 
        }
        antecedent = antecedentBuilder.toString();
    }

    public void setConsequent(Object[] con){
        StringBuilder consequenBuilder = new StringBuilder();
        for(int i = 0; i < con.length; i++){
            if(con[i] != null){
                consequenBuilder.append(con[i].toString());
            } 
        }
        antecedent = consequenBuilder.toString();
    }

    public String getAntecedent(){ // Gets the defImplications antecedent
        return antecedent;
    }

    public String getConsequent(){ // Gets the defImplications consequent
        return consequent;
    }

    @Override
    public String toString(){
        return  antecedent + con.getDefImplicationSymbol() + consequent;
    }

}
