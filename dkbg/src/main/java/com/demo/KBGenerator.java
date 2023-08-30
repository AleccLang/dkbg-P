package com.demo;

import java.util.*;

public class KBGenerator{
    
    private static AtomBuilder gen = AtomBuilder.getInstance();

    public static LinkedHashSet<LinkedHashSet<DefImplication>> KBGenerate(int[] defImplicationDistribution, boolean simpleOnly, boolean reuseConsequent, int[] complexityAnt, int[] complexityCon, int[] connectiveType){

        Random random = new Random();
        int rank = 0;
        LinkedHashSet<LinkedHashSet<DefImplication>> KB = new LinkedHashSet<LinkedHashSet<DefImplication>>(); // Creating KB.
        Atom rankBaseCons = gen.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        Atom rankBaseAnt = gen.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        ArrayList<Atom> anyRankAtoms = new ArrayList<Atom>(); // Reusable atoms in any rank.

        while(rank!=defImplicationDistribution.length){
            ArrayList<DefImplication> DIs = new ArrayList<DefImplication>();
            ArrayList<Atom> curRankAtoms = new ArrayList<Atom>(); // Reusable atoms in current ranks antecedent.
            ArrayList<Atom> anyRankAtomsTemp = new ArrayList<Atom>();
            int defImplicationNum = defImplicationDistribution[rank];
            if(rank==0){
                defImplicationNum--;
                DefImplicationBuilder.rankZero(DIs, rankBaseCons, rankBaseAnt);
            }
            else{
                if(reuseConsequent == false && defImplicationNum >=3){ // Don't reuse the original rankBaseCons in all ranks
                    DefImplicationBuilder.rankBuilder(gen, DIs, rankBaseCons, rankBaseAnt);
                    defImplicationNum--;
                }
                else{ // Reuse the original rankBaseCons in all ranks
                    DefImplicationBuilder.rankBuilderConstricted(gen, DIs, rankBaseCons, rankBaseAnt);
                }
                defImplicationNum = defImplicationNum - 2;
            }
            curRankAtoms.add(rankBaseAnt);
            while(defImplicationNum!=0){
                if(simpleOnly == true){
                    int decision = random.nextInt(3);
                    DefImplicationBuilder.simpleDI(decision, gen, DIs, anyRankAtoms, curRankAtoms, anyRankAtomsTemp);
                    defImplicationNum--;
                }
                else{
                    String key = Rules.keyGenerator(connectiveType, complexityAnt, complexityCon, curRankAtoms.size());
                    DefImplicationBuilder.complexDI(key, gen, DIs, curRankAtoms);
                    defImplicationNum--;
                }
            }
            rankBaseCons.negateAtom(); // Negates the consequent for formation of next rank.
            KB.add(new LinkedHashSet<DefImplication>(DIs));
            anyRankAtoms.addAll(anyRankAtomsTemp);
            rank++;
        }
        return KB;
    }
}
