package com.demo;

import java.util.ArrayList;
import java.util.Collections;

public class FormulaBuilder{

    private static Connective con = Connective.getInstance();

    // Generates the minimum formulas and structure needed for a rank.
    public static void rankZero(ArrayList<Formula> formulas, Atom rankBuildCons, Atom rankBuildAnt){
        formulas.add(new Formula(rankBuildAnt.toString(), new Atom(rankBuildCons).toString())); //
    }

    // Baseline formulas and structure needed for a rank in a KB.
    public static void rankBuilderConstricted(AtomBuilder gen, ArrayList<Formula> formulas, Atom rankBuildCons, Atom rankBuildAnt){
        Atom atom = gen.generateAtom();
        formulas.add(new Formula(atom.toString(), new Atom(rankBuildCons).toString())); // 
        formulas.add(new Formula(atom.toString(), rankBuildAnt.toString())); 
        rankBuildAnt.setAtom(atom.toString());
    }

    // Generates baseline formulas and structure with a new consequent for next rank in a KB.
    public static void rankBuilder(AtomBuilder gen, ArrayList<Formula> formulas, Atom rankBuildCons, Atom rankBuildAnt){
        Atom newRankBaseCons = gen.generateAtom(); // Atom acts as the rankBuildCons in the next rank.
        Atom atom = gen.generateAtom();
        formulas.add(new Formula(atom.toString(), new Atom(rankBuildCons).toString())); // 
        formulas.add(new Formula(atom.toString(), new Atom(newRankBaseCons).toString()));
        formulas.add(new Formula(atom.toString(), rankBuildAnt.toString()));
        rankBuildCons.setAtom(newRankBaseCons.toString());
        rankBuildAnt.setAtom(atom.toString());
    }

    // Method to generate simple formulas by using a atom as antecedent and reusing the rankBuildAnt as consequent.
    public static Atom[] recycleAntecedent(AtomBuilder gen, ArrayList<Formula> formulas, Atom rankBuildAnt){ // Generates new consequent for next rank in an unrestriced KB.
        Atom atom = gen.generateAtom();
        Atom[] atoms = {atom};
        formulas.add(new Formula(atom.toString(), rankBuildAnt.toString())); 
        return atoms;
    }

    // Method to generate simple formulas by using a new negated atom as antecedent and a currRankAtom as consequent.
    public static Atom[] negateAntecedent(AtomBuilder gen, ArrayList<Formula> formulas, Atom currRankAtom){ // Generates new consequent for next rank in an unrestriced KB.
        Atom atom = gen.generateAtom();
        atom.negateAtom();
        Atom[] atoms = {atom};
        formulas.add(new Formula(new Atom(atom).toString(), currRankAtom.toString())); 
        return atoms;
    }

    // Method to generate simple formulas by using a currRankAtom as antecedent and a negated anyRankAtom as consequent.
    public static void reuseConsequent(AtomBuilder gen, ArrayList<Formula> formulas, Atom anyRankAtom, Atom currRankAtom){ // Generates new consequent for next rank in an unrestriced KB.
        anyRankAtom.negateAtom();
        formulas.add(new Formula(currRankAtom.toString(), new Atom(anyRankAtom).toString()));
    }

    // Method to generate complex formulas using the disjunction connective.
    public static void disjunctionFormula(String key, AtomBuilder gen, ArrayList<Formula> formulas, ArrayList<Atom> curRankAtoms){
        Collections.shuffle(curRankAtoms); //// optimise
        String disjunction = con.getDisjunctionSymbol();
        String antecedent = "";
        String consequent = "";
        int complexityAnt = Integer.parseInt(key.substring(2, 3));
        int complexityCon = Integer.parseInt(key.substring(4, 5));
        Atom a = gen.generateAtom();
        curRankAtoms.add(a); 
        switch(complexityAnt){
            case 0:
                antecedent = a.toString();
                break;
            case 1:
                Atom b = gen.generateAtom();
                curRankAtoms.add(b); 
                antecedent = a.toString() + disjunction + b.toString();
                break;
            case 2:
                b = gen.generateAtom();
                Atom c = gen.generateAtom();
                curRankAtoms.add(b);
                curRankAtoms.add(c);
                antecedent = a.toString() + disjunction + b.toString() + disjunction + c.toString();
                break;
        }
        switch(complexityCon){
            case 0:
                consequent = curRankAtoms.get(0).toString();
                break;
            case 1:
                consequent = curRankAtoms.get(0).toString() + disjunction + curRankAtoms.get(1).toString();
                break;
            case 2:
                consequent = curRankAtoms.get(0).toString() + disjunction + curRankAtoms.get(1).toString() + disjunction + curRankAtoms.get(2).toString();
                break;
        }
        formulas.add(new Formula(antecedent, consequent));
    }

    // Method to generate complex formulas using the conjunction connective.
    public static void conjunctionFormula(String key, AtomBuilder gen, ArrayList<Formula> formulas, ArrayList<Atom> curRankAtoms){
        Collections.shuffle(curRankAtoms); //// optimise
        String conjunction = con.getConjunctionSymbol();
        String antecedent = "";
        String consequent = "";
        int complexityAnt = Integer.parseInt(key.substring(2, 3));
        int complexityCon = Integer.parseInt(key.substring(4, 5));
        switch(complexityAnt){
            case 0:
                Atom a = gen.generateAtom();
                antecedent = a.toString();
                curRankAtoms.add(a);
                break;
            case 1:
                antecedent = gen.generateAtom().toString() + conjunction + gen.generateAtom().toString();
                break;
            case 2:
                antecedent = gen.generateAtom().toString() + conjunction + gen.generateAtom().toString() + conjunction + gen.generateAtom().toString();
                break;
        }
        switch(complexityCon){
            case 0:
                consequent = curRankAtoms.get(0).toString();
                break;
            case 1:
                consequent = curRankAtoms.get(0).toString() + conjunction + gen.generateAtom().toString();
                break;
            case 2:
                consequent = gen.generateAtom().toString() + conjunction + curRankAtoms.get(0).toString() + conjunction + gen.generateAtom().toString();
                break;
        }
        formulas.add(new Formula(antecedent, consequent));
    }

    // Method to generate complex formulas using the implication connective.
    public static void implicationFormula(String key, AtomBuilder gen, ArrayList<Formula> formulas, ArrayList<Atom> curRankAtoms){
        Collections.shuffle(curRankAtoms); //// optimise
        String implication = con.getImplicationSymbol();
        String antecedent = "";
        String consequent = "";
        int complexityAnt = Integer.parseInt(key.substring(2, 3));
        int complexityCon = Integer.parseInt(key.substring(4, 5));
        Atom a = gen.generateAtom();
        Atom b = gen.generateAtom();
        switch(complexityAnt){
            case 0:
                antecedent = curRankAtoms.get(0).toString();
                break;
            case 1:
                antecedent = a.toString() + implication + b.toString();
                // curRankAtoms.add(a);
                // curRankAtoms.add(b);
                break;
            case 2:
                Atom c = gen.generateAtom();
                antecedent = a.toString() + implication + b.toString() + implication + c.toString();
                // curRankAtoms.add(a);
                // curRankAtoms.add(b);
                // curRankAtoms.add(c);
                break;
        }
        switch(complexityCon){
            case 0:
                consequent = curRankAtoms.get(0).toString();
                break;
            case 1:
                consequent = a.toString() + implication + b.toString();
                // curRankAtoms.add(a);
                // curRankAtoms.add(b);
                break;
            case 2:
                Atom c = gen.generateAtom();
                consequent = a.toString() + implication + b.toString() + implication + c.toString();
                // curRankAtoms.add(a);
                // curRankAtoms.add(b);
                // curRankAtoms.add(c);
                break;
        }
        formulas.add(new Formula(antecedent, consequent));
    }

    // Method to generate complex formulas using the bi-implication connective.
    public static void biImplicationFormula(String key, AtomBuilder gen, ArrayList<Formula> formulas, ArrayList<Atom> curRankAtoms){
        Collections.shuffle(curRankAtoms); //// optimise
        String biimplication = con.getBiImplicationSymbol();
        String antecedent = "";
        String consequent = "";
        int complexityAnt = Integer.parseInt(key.substring(2, 3));
        int complexityCon = Integer.parseInt(key.substring(4, 5));
        Atom a = gen.generateAtom();
        Atom b = gen.generateAtom();
        switch(complexityAnt){
            case 0:
                antecedent = curRankAtoms.get(0).toString();
                break;
            case 1:
                antecedent = a.toString() + biimplication + b.toString();
                // curRankAtoms.add(a);
                // curRankAtoms.add(b);
                break;
            case 2:
                Atom c = gen.generateAtom();
                antecedent = a.toString() + biimplication + b.toString() + biimplication + c.toString();
                // curRankAtoms.add(a);
                // curRankAtoms.add(b);
                // curRankAtoms.add(c);
                break;
        }
        switch(complexityCon){
            case 0:
                consequent = curRankAtoms.get(0).toString();
                break;
            case 1:
                consequent = a.toString() + biimplication + b.toString();
                // curRankAtoms.add(a);
                // curRankAtoms.add(b);
                break;
            case 2:
                Atom c = gen.generateAtom();
                consequent = a.toString() + biimplication + b.toString() + biimplication + c.toString();
                // curRankAtoms.add(a);
                // curRankAtoms.add(b);
                // curRankAtoms.add(c);
                break;
        }
        formulas.add(new Formula(antecedent, consequent));
    }

    // Method to generate complex formulas using a mixture of connectives.
    public static void mixedFormula(String key, AtomBuilder gen, ArrayList<Formula> formulas, ArrayList<Atom> curRankAtoms){
        Collections.shuffle(curRankAtoms); //// optimise
        int[] connective = {0,1,2,3};
        String antecedent = "";
        String consequent = "";
        int complexityAnt = Integer.parseInt(key.substring(2, 3));
        int complexityCon = Integer.parseInt(key.substring(4, 5));
        switch(complexityAnt){
            case 0:
                antecedent = curRankAtoms.get(0).toString();
                break;
            case 1:
                int[] connective1 = {0,1};
                antecedent = curRankAtoms.get(0).toString() + Connective.getRandom(connective1, con) + curRankAtoms.get(1).toString();
                break;
            case 2:
                antecedent = curRankAtoms.get(0).toString() + con.getConjunctionSymbol() + "(" + curRankAtoms.get(1).toString() + Connective.getRandom(connective, con) + gen.generateAtom().toString() + ")";
                break;
        }
        switch(complexityCon){
            case 0:
                consequent = gen.generateAtom().toString();
                break;
            case 1:
                consequent = gen.generateAtom().toString() + Connective.getRandom(connective, con) + gen.generateAtom().toString();
                break;
            case 2:
                consequent = gen.generateAtom().toString() + Connective.getRandom(connective, con) + gen.generateAtom().toString() + Connective.getRandom(connective, con) + gen.generateAtom().toString();
                break;
        }
        formulas.add(new Formula(antecedent, consequent));
    }
}
