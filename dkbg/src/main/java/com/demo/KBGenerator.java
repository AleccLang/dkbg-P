package com.demo;

import java.util.*;

public class KBGenerator{
    
    private static AtomBuilder generator = AtomBuilder.getInstance();

    public static LinkedHashSet<LinkedHashSet<Formula>> KBGenerate(int[] formulaDistribution, boolean simpleOnly, boolean reuseConsequent, int[] complexityAnt, int[] complexityCon, int[] connectiveType){

        Random random = new Random();
        int rank = 0;
        LinkedHashSet<LinkedHashSet<Formula>> KB = new LinkedHashSet<LinkedHashSet<Formula>>(); // Creating KB.
        Atom rankBuildCons = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        Atom rankBuildAnt = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        ArrayList<Atom> anyRankAtoms = new ArrayList<Atom>(); // Reusable atoms in any rank.

        while(rank!=formulaDistribution.length){
            ArrayList<Formula> formulas = new ArrayList<Formula>();
            ArrayList<Atom> curRankAtoms = new ArrayList<Atom>(); // Reusable atoms in current ranks antecedent.
            ArrayList<Atom> anyRankAtomsTemp = new ArrayList<Atom>();
            int formulaNum = formulaDistribution[rank];
            if(rank==0){
                formulaNum--;
                FormulaBuilder.rankZero(formulas, rankBuildCons, rankBuildAnt);
            }
            else{
                if(reuseConsequent == false && formulaNum >=3){ // Don't reuse the original rankBuildCons in all ranks
                    FormulaBuilder.rankBuilder(generator, formulas, rankBuildCons, rankBuildAnt);
                    formulaNum--;
                }
                else{ // Reuse the original rankBuildCons in all ranks
                    FormulaBuilder.rankBuilderConstricted(generator, formulas, rankBuildCons, rankBuildAnt);
                }
                formulaNum = formulaNum - 2;
            }
            curRankAtoms.add(rankBuildAnt);
            ArrayList<String> c = new ArrayList<>();
            while(formulaNum!=0){
                if(simpleOnly == true){
                    int decision = random.nextInt(3);
                    int i = (int)(Math.random() * curRankAtoms.size()); // Get random atom from atoms usable in current rank.
                    //System.out.println("i= "+i+". curRankAtoms["+i+"]= "+curRankAtoms.get(i));
                    switch(decision){
                        case 0: 
                            // Adds formula with a new atom as antecedent and random curRankAtom as consequent.
                            Atom[] temp = FormulaBuilder.recycleAntecedent(generator, formulas, curRankAtoms.get(i));
                            curRankAtoms.add(temp[0]);
                            formulaNum--;
                            break;
                        case 1:
                            // Adds formula with negated atom as antecedent and random curRankAtom as consequent.
                            temp = FormulaBuilder.negateAntecedent(generator, formulas, curRankAtoms.get(i));
                            anyRankAtomsTemp.add(temp[0]);
                            formulaNum--;
                            break;
                        case 2:
                            // Reuses an antecedent from a previous rank as consequent in a new rank.
                            if(anyRankAtoms.size()==0){
                                temp = FormulaBuilder.recycleAntecedent(generator, formulas, curRankAtoms.get(i));
                                curRankAtoms.add(temp[0]);
                            }
                            else{
                                int j = (int)(Math.random() * anyRankAtoms.size()); // Get random atom from atoms usable in any rank. 
                                FormulaBuilder.reuseConsequent(generator, formulas, anyRankAtoms.get(j), curRankAtoms.get(i));
                                anyRankAtomsTemp.add(anyRankAtoms.get(j));
                                anyRankAtoms.remove(j);
                            }
                            formulaNum--;
                            break;
                    }
                }
                else{
                    String key = Rules.keyGenerator(connectiveType, complexityAnt, complexityCon, curRankAtoms.size());
                    c.add(key);
                    int s = Integer.parseInt(key.substring(0, 1));
                    switch(s){
                        case 1:
                            FormulaBuilder.disjunctionFormula(key, generator, formulas, curRankAtoms);
                            break;
                        case 2:
                            FormulaBuilder.conjunctionFormula(key, generator, formulas, curRankAtoms);
                            break;
                        case 3:
                            FormulaBuilder.implicationFormula(key, generator, formulas, curRankAtoms);
                            break;
                        case 4:
                            FormulaBuilder.biImplicationFormula(key, generator, formulas, curRankAtoms);
                            break;
                        case 5:
                            FormulaBuilder.mixedFormula(key, generator, formulas, curRankAtoms);
                            break;
                    }
                    formulaNum--;
                }
            }
            rankBuildCons.negateAtom(); // Negates the consequent for formation of next rank.
            KB.add(new LinkedHashSet<Formula>(formulas));
            anyRankAtoms.addAll(anyRankAtomsTemp);
            rank++;
        }
        return KB;
    }
}
