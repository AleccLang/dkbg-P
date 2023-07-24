package com.demo;

import java.util.*;

public class KBGenerator{
    
    private static Connective con = Connective.getInstance();
    private static AtomBuilder generator = AtomBuilder.getInstance();

    public static LinkedHashSet<LinkedHashSet<Formula>> KBGenerate(int[] formulaDistribution, boolean simpleOnly, boolean reuseAntecedent, int[] complexityAnt, int[] complexityCon, int[] connectiveType){

        ArrayList<ArrayList<String>> choice = new ArrayList<>();

        Random random = new Random();
        int rank = 0;
        LinkedHashSet<LinkedHashSet<Formula>> KB = new LinkedHashSet<LinkedHashSet<Formula>>(); // Creating KB.
        Atom rankBuildCons = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        Atom rankBuildAnt = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        ArrayList<Atom> anyRankAtoms = new ArrayList<Atom>(); // Reusable atoms in any rank.

        while(rank!=formulaDistribution.length){
            //System.out.println("Rank: " + rank);
            ArrayList<Formula> formulas = new ArrayList<Formula>();
            ArrayList<Atom> curRankAtoms = new ArrayList<Atom>(); // Reusable atoms in current ranks antecedent.
            ArrayList<Atom> cRA_noRBA = new ArrayList<Atom>(); // Reusable atoms in current ranks antecedent without the rank's base-antecedent.
            ArrayList<Atom> anyRankAtomsTemp = new ArrayList<Atom>();
            int formulaNum = formulaDistribution[rank];
            if(rank==0){
                formulaNum--;
                FormulaBuilder.rankZero(formulas, rankBuildCons, rankBuildAnt);
            }
            else{
                if(reuseAntecedent == false && formulaNum >=3){ // Don't reuse the original rankBuildAnt in all ranks
                    //System.out.println("In if ");
                    FormulaBuilder.rankBuilder(generator, formulas, rankBuildCons, rankBuildAnt);
                    formulaNum--;
                }
                else{ // Reuse the original rankBuildAnt in all ranks
                    //System.out.println("In else ");
                    FormulaBuilder.rankBuilderConstricted(generator, formulas, rankBuildCons, rankBuildAnt);
                }
                formulaNum = formulaNum - 2;
            }
            curRankAtoms.add(rankBuildAnt);
            ArrayList<String> c = new ArrayList<>();
            while(formulaNum!=0){
                //System.out.println("In while. fiormulaNum = " + formulaNum);
                if(simpleOnly == true){
                    int decision = random.nextInt(3); // Using this for now to pick between different rules to generate formulas.
                    // c.add(decision);
                    //System.out.println(decision);
                    int i = (int)(Math.random() * curRankAtoms.size()); // Get random atom from atoms usable in current rank.
                    //System.out.println("i= "+i+". curRankAtoms["+i+"]= "+curRankAtoms.get(i));
                    switch(decision){
                        case 0: 
                            // Adds formula with a new atom as antecedent and random curRankAtom as consequent.
                            Atom[] temp = FormulaBuilder.recycleAntecedent(generator, formulas, curRankAtoms.get(i));
                            curRankAtoms.add(temp[0]);
                            cRA_noRBA.add(temp[0]);
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
                                cRA_noRBA.add(temp[0]);
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
                    //System.out.println(curRankAtoms.size());
                    //System.out.println(curRankAtoms);
                    //System.out.println("In while-else ");
                    String key = Rules.keyGenerator(connectiveType, complexityAnt, complexityCon, curRankAtoms.size());
                    //System.out.println(key);
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
            choice.add(c);
            rankBuildCons.negateAtom(); // Negates the consequent for formation of next rank.
            KB.add(new LinkedHashSet<Formula>(formulas));
            anyRankAtoms.addAll(anyRankAtomsTemp);
            //System.out.println("rankBuildAnt: " + rankBuildAnt);
            //System.out.println("rankBuildCons: " + rankBuildCons);
            rank++;
        }
        // for (ArrayList<String> row : choice){
        //     for (String element : row){
        //         System.out.print(element + " ");
        //     }
        //     System.out.println(); // New line for each row
        // }
        return KB;
    }

    // public static void main(String[] args){
    //     Rules r = new Rules();
        
    //     con.setConjunctionSymbol("&");
    //     con.setDisjunctionSymbol("||");
    //     con.setImplicationSymbol("=>");
    //     con.setBiImplicationSymbol("<=>");
    //     con.setNegationSymbol("!");
    //     generator.setCharacters("lowerlatin");

    //     System.out.println("Knowledgebase:");
    //     int[] a ={2,4,2,10,2,8,3};
    //     int[] complexityAnt ={0,1,2};
    //     int[] complexityCon ={0,1,2};
    //     int[] connectiveType ={1,2,3,4,5};
    //     LinkedHashSet<LinkedHashSet<Formula>> KB = KBGenerate(a, false, false, complexityAnt, complexityCon, connectiveType);
    //     //System.out.println(KB);
    //     //ArrayList b = new ArrayList();
    //     int i = 0;
    //     for (LinkedHashSet<Formula> set : KB){
    //         System.out.print("Rank " + i + ": ");
    //         Iterator<Formula> iterator = set.iterator();
    //         while (iterator.hasNext()){
    //             Formula element = iterator.next();
    //             System.out.print(element.toString());
                
    //             // Print comma if there is a next element, otherwise print a newline
    //             if (iterator.hasNext()){
    //                 System.out.print(", ");
    //             } else{
    //                 System.out.println();
    //             }
    //         }
    //         i++;
    //     }

    //     for(LinkedHashSet<Formula> set : KB){
    //         for (Formula element : set){
    //             System.out.println(element.toString());
    //         }
    //     }

    //     // for(int i=0; i<b.size(); i++){
    //     //     System.out.println(b.get(i));
    //     // }
    // }
}
