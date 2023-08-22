package com.demo;

import java.util.*;

public class KBGenerator{
    
    private static AtomBuilder generator = AtomBuilder.getInstance();

    public static LinkedHashSet<LinkedHashSet<DefImplication>> KBGenerate(int[] defImplicationDistribution, boolean simpleOnly, boolean reuseConsequent, int[] complexityAnt, int[] complexityCon, int[] connectiveType){

        Random random = new Random();
        int rank = 0;
        LinkedHashSet<LinkedHashSet<DefImplication>> KB = new LinkedHashSet<LinkedHashSet<DefImplication>>(); // Creating KB.
        Atom rankBuildCons = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        Atom rankBuildAnt = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        ArrayList<Atom> anyRankAtoms = new ArrayList<Atom>(); // Reusable atoms in any rank.

        while(rank!=defImplicationDistribution.length){
            ArrayList<DefImplication> defImplications = new ArrayList<DefImplication>();
            ArrayList<Atom> curRankAtoms = new ArrayList<Atom>(); // Reusable atoms in current ranks antecedent.
            ArrayList<Atom> anyRankAtomsTemp = new ArrayList<Atom>();
            int defImplicationNum = defImplicationDistribution[rank];
            if(rank==0){
                defImplicationNum--;
                DefImplicationBuilder.rankZero(defImplications, rankBuildCons, rankBuildAnt);
            }
            else{
                if(reuseConsequent == false && defImplicationNum >=3){ // Don't reuse the original rankBuildCons in all ranks
                    DefImplicationBuilder.rankBuilder(generator, defImplications, rankBuildCons, rankBuildAnt);
                    defImplicationNum--;
                }
                else{ // Reuse the original rankBuildCons in all ranks
                    DefImplicationBuilder.rankBuilderConstricted(generator, defImplications, rankBuildCons, rankBuildAnt);
                }
                defImplicationNum = defImplicationNum - 2;
            }
            curRankAtoms.add(rankBuildAnt);
            ArrayList<String> c = new ArrayList<>();
            while(defImplicationNum!=0){
                if(simpleOnly == true){
                    int decision = random.nextInt(3);
                    int i = (int)(Math.random() * curRankAtoms.size()); // Get random atom from atoms usable in current rank.
                    //System.out.println("i= "+i+". curRankAtoms["+i+"]= "+curRankAtoms.get(i));
                    switch(decision){
                        case 0: 
                            // Adds defImplication with a new atom as antecedent and random curRankAtom as consequent.
                            Atom[] temp = DefImplicationBuilder.recycleAntecedent(generator, defImplications, curRankAtoms.get(i));
                            curRankAtoms.add(temp[0]);
                            defImplicationNum--;
                            break;
                        case 1:
                            // Adds defImplication with negated atom as antecedent and random curRankAtom as consequent.
                            temp = DefImplicationBuilder.negateAntecedent(generator, defImplications, curRankAtoms.get(i));
                            anyRankAtomsTemp.add(temp[0]);
                            defImplicationNum--;
                            break;
                        case 2:
                            // Reuses an antecedent from a previous rank as consequent in a new rank.
                            if(anyRankAtoms.size()==0){
                                temp = DefImplicationBuilder.recycleAntecedent(generator, defImplications, curRankAtoms.get(i));
                                curRankAtoms.add(temp[0]);
                            }
                            else{
                                int j = (int)(Math.random() * anyRankAtoms.size()); // Get random atom from atoms usable in any rank. 
                                DefImplicationBuilder.reuseConsequent(generator, defImplications, anyRankAtoms.get(j), curRankAtoms.get(i));
                                anyRankAtomsTemp.add(anyRankAtoms.get(j));
                                anyRankAtoms.remove(j);
                            }
                            defImplicationNum--;
                            break;
                    }
                }
                else{
                    String key = Rules.keyGenerator(connectiveType, complexityAnt, complexityCon, curRankAtoms.size());
                    c.add(key);
                    int s = Integer.parseInt(key.substring(0, 1));
                    switch(s){
                        case 1:
                            DefImplicationBuilder.disjunctionDefImplication(key, generator, defImplications, curRankAtoms);
                            break;
                        case 2:
                            DefImplicationBuilder.conjunctionDefImplication(key, generator, defImplications, curRankAtoms);
                            break;
                        case 3:
                            DefImplicationBuilder.implicationDefImplication(key, generator, defImplications, curRankAtoms);
                            break;
                        case 4:
                            DefImplicationBuilder.biImplicationDefImplication(key, generator, defImplications, curRankAtoms);
                            break;
                        case 5:
                            DefImplicationBuilder.mixedDefImplication(key, generator, defImplications, curRankAtoms);
                            break;
                    }
                    defImplicationNum--;
                }
            }
            rankBuildCons.negateAtom(); // Negates the consequent for formation of next rank.
            KB.add(new LinkedHashSet<DefImplication>(defImplications));
            anyRankAtoms.addAll(anyRankAtomsTemp);
            rank++;
        }
        return KB;
    }
}
