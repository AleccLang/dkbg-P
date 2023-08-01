package com.demo;

import java.util.*;
import java.util.concurrent.*;
// rankBuildAnt is always reused, 1 formula is generated first for each rank, no matter the rank
// No use of anyRankAtoms
// At the end it must generate 1 statement for each rank to tie them all together.
public class KBGeneratorThreaded{

    private static AtomBuilder generator = AtomBuilder.getInstance();
    private static int numThreads = Runtime.getRuntime().availableProcessors();
    


    public static LinkedHashSet<LinkedHashSet<Formula>> KBGenerate(int[] formulaDistribution, boolean simpleOnly, int[] complexityAnt, int[] complexityCon, int[] connectiveType){
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        LinkedHashSet<LinkedHashSet<Formula>> KB = new LinkedHashSet<LinkedHashSet<Formula>>();
        Atom rankBuildCons = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        Atom[] rankBuildAnts = new Atom[formulaDistribution.length]; // Stores each ranks rankBuildAnt to tie the ranks together.

        try{
            ArrayList<Future<LinkedHashSet<Formula>>> futures = new ArrayList<>();

            for (int rank = 0; rank < formulaDistribution.length; rank++){
                int r = rank;
                Future<LinkedHashSet<Formula>> future = executor.submit(() -> generateRank(r, formulaDistribution, simpleOnly, complexityAnt, complexityCon, connectiveType, rankBuildCons, rankBuildAnts));
                futures.add(future);
            }

            for (Future<LinkedHashSet<Formula>> future : futures){
                KB.add(future.get());
            }
        }
        catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        finally{
            executor.shutdown();
        }

        boolean firstSetProcessed = false;
        int i = 1;
        for(LinkedHashSet<Formula> set : KB){
            if (!firstSetProcessed) {
                firstSetProcessed = true;
                continue;
            }
            set.add(new Formula(rankBuildAnts[i].toString(), new Atom(rankBuildAnts[i-1]).toString()));
            i++;
        }
        return KB;
    }

    private static LinkedHashSet<Formula> generateRank(int rank, int[] formulaDistribution, boolean simpleOnly, int[] complexityAnt, int[] complexityCon, int[] connectiveType, Atom rankBuildCons, Atom[] rankBuildAnts){

        Random random = new Random();
        Atom rankBuildAnt = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        
        synchronized(rankBuildAnts){
            rankBuildAnts[rank] = rankBuildAnt;
        }
        
        ArrayList<Formula> formulas = new ArrayList<Formula>();
        ArrayList<Atom> curRankAtoms = new ArrayList<Atom>(); // Reusable atoms in current ranks antecedent.
        int formulaNum = formulaDistribution[rank];
        // rankBuildAnt is always reused, 1 formula is generated first for each rank, no matter the rank
        formulaNum--;
        if(rank % 2 == 0){
            FormulaBuilder.rankZero(formulas, rankBuildCons, rankBuildAnt);
        }
        else{
            Atom rBCNegated = new Atom(rankBuildCons);
            rBCNegated.negateAtom();
            FormulaBuilder.rankZero(formulas, rBCNegated, rankBuildAnt);
        }

        curRankAtoms.add(rankBuildAnt);
        if(!(rank == 0)){ // Leaves 1 extra formula to be generated for each rank other than 1st and last.
            formulaNum = formulaNum - 1;
        }
        while(formulaNum!=0){
            if(simpleOnly == true){
                int decision = random.nextInt(2);
                int i = (int)(Math.random() * curRankAtoms.size()); // Get random atom from atoms usable in current rank.
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
                        formulaNum--;
                        break;
                }
            }
            else{
                String key = Rules.keyGenerator(connectiveType, complexityAnt, complexityCon, curRankAtoms.size());
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
        return new LinkedHashSet<>(formulas);
    }
}
