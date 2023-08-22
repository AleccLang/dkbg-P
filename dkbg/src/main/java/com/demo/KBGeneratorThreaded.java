package com.demo;

import java.util.*;
import java.util.concurrent.*;
// rankBuildAnt is always reused, 1 defImplication is generated first for each rank, no matter the rank
// No use of anyRankAtoms
// At the end it must generate 1 statement for each rank to tie them all together.
public class KBGeneratorThreaded{

    private static AtomBuilder generator = AtomBuilder.getInstance();
    private static int numThreads = Runtime.getRuntime().availableProcessors();
    


    public static LinkedHashSet<LinkedHashSet<DefImplication>> KBGenerate(int[] defImplicationDistribution, boolean simpleOnly, int[] complexityAnt, int[] complexityCon, int[] connectiveType){
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        LinkedHashSet<LinkedHashSet<DefImplication>> KB = new LinkedHashSet<LinkedHashSet<DefImplication>>();
        Atom rankBuildCons = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        Atom[] rankBuildAnts = new Atom[defImplicationDistribution.length]; // Stores each ranks rankBuildAnt to tie the ranks together.

        try{
            ArrayList<Future<LinkedHashSet<DefImplication>>> futures = new ArrayList<>();

            for (int rank = 0; rank < defImplicationDistribution.length; rank++){
                int r = rank;
                Future<LinkedHashSet<DefImplication>> future = executor.submit(() -> generateRank(r, defImplicationDistribution, simpleOnly, complexityAnt, complexityCon, connectiveType, rankBuildCons, rankBuildAnts));
                futures.add(future);
            }

            for (Future<LinkedHashSet<DefImplication>> future : futures){
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
        for(LinkedHashSet<DefImplication> set : KB){
            if (!firstSetProcessed) {
                firstSetProcessed = true;
                continue;
            }
            set.add(new DefImplication(rankBuildAnts[i].toString(), new Atom(rankBuildAnts[i-1]).toString()));
            i++;
        }
        return KB;
    }

    private static LinkedHashSet<DefImplication> generateRank(int rank, int[] defImplicationDistribution, boolean simpleOnly, int[] complexityAnt, int[] complexityCon, int[] connectiveType, Atom rankBuildCons, Atom[] rankBuildAnts){

        Random random = new Random();
        Atom rankBuildAnt = generator.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        
        synchronized(rankBuildAnts){
            rankBuildAnts[rank] = rankBuildAnt;
        }
        
        ArrayList<DefImplication> defImplications = new ArrayList<DefImplication>();
        ArrayList<Atom> curRankAtoms = new ArrayList<Atom>(); // Reusable atoms in current ranks antecedent.
        int defImplicationNum = defImplicationDistribution[rank];
        // rankBuildAnt is always reused, 1 defImplication is generated first for each rank, no matter the rank
        defImplicationNum--;
        if(rank % 2 == 0){
            DefImplicationBuilder.rankZero(defImplications, rankBuildCons, rankBuildAnt);
        }
        else{
            Atom rBCNegated = new Atom(rankBuildCons);
            rBCNegated.negateAtom();
            DefImplicationBuilder.rankZero(defImplications, rBCNegated, rankBuildAnt);
        }

        curRankAtoms.add(rankBuildAnt);
        if(!(rank == 0)){ // Leaves 1 extra defImplication to be generated for each rank other than 1st and last.
            defImplicationNum = defImplicationNum - 1;
        }
        while(defImplicationNum!=0){
            if(simpleOnly == true){
                int decision = random.nextInt(2);
                int i = (int)(Math.random() * curRankAtoms.size()); // Get random atom from atoms usable in current rank.
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
                        defImplicationNum--;
                        break;
                }
            }
            else{
                String key = Rules.keyGenerator(connectiveType, complexityAnt, complexityCon, curRankAtoms.size());
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
        return new LinkedHashSet<>(defImplications);
    }
}
