package com.demo;

import java.util.*;
import java.util.concurrent.*;

// rankBaseAnt is always reused, 1 defImplication is generated first for each rank, no matter the rank
// At the end it must generate 1 statement for each rank to tie them all together.
public class KBGeneratorThreaded{

    private static AtomBuilder gen = AtomBuilder.getInstance();
    private static int numThreads = Runtime.getRuntime().availableProcessors();

    public static LinkedHashSet<LinkedHashSet<DefImplication>> KBGenerate(int[] defImplicationDistribution, boolean simpleOnly, int[] complexityAnt, int[] complexityCon, int[] connectiveType){
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        LinkedHashSet<LinkedHashSet<DefImplication>> KB = new LinkedHashSet<LinkedHashSet<DefImplication>>();
        ArrayList<Atom> anyRankAtoms = new ArrayList<Atom>(); // Reusable atoms in any rank.
        Atom rankBaseCons = gen.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        Atom[] rankBaseAnts = new Atom[defImplicationDistribution.length]; // Stores each ranks rankBaseAnt to tie the ranks together.

        try{
            ArrayList<Future<LinkedHashSet<DefImplication>>> futures = new ArrayList<>();

            for (int rank = 0; rank < defImplicationDistribution.length; rank++){
                int r = rank;
                Future<LinkedHashSet<DefImplication>> future = executor.submit(() -> generateRank(r, defImplicationDistribution, simpleOnly, complexityAnt, complexityCon, connectiveType, rankBaseCons, rankBaseAnts, anyRankAtoms));
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
            set.add(new DefImplication(rankBaseAnts[i].toString(), new Atom(rankBaseAnts[i-1]).toString()));
            i++;
        }
        return KB;
    }

    
    private static LinkedHashSet<DefImplication> generateRank(int rank, int[] defImplicationDistribution, boolean simpleOnly, int[] complexityAnt, int[] complexityCon, int[] connectiveType, Atom rankBaseCons, Atom[] rankBaseAnts, ArrayList<Atom> anyRankAtoms){

        Random random = new Random();
        Atom rankBaseAnt = gen.generateAtom(); // Atom acts as the lynchpin for generating new ranks.
        ArrayList<Atom> anyRankAtomsTemp = new ArrayList<Atom>();
        
        synchronized(rankBaseAnts){
            rankBaseAnts[rank] = rankBaseAnt;
        }
        
        ArrayList<DefImplication> DIs = new ArrayList<DefImplication>();
        ArrayList<Atom> curRankAtoms = new ArrayList<Atom>(); // Reusable atoms in current ranks antecedent.
        int defImplicationNum = defImplicationDistribution[rank];
        // rankBaseCons is always reused, 1 defImplication is generated first for each rank, no matter the rank
        defImplicationNum--;
        if(rank % 2 == 0){
            DefImplicationBuilder.rankZero(DIs, rankBaseCons, rankBaseAnt);
        }
        else{
            Atom rBCNegated = new Atom(rankBaseCons);
            rBCNegated.negateAtom();
            DefImplicationBuilder.rankZero(DIs, rBCNegated, rankBaseAnt);
        }

        curRankAtoms.add(rankBaseAnt);
        if(!(rank == 0)){ // Leaves 1 extra defImplication to be generated for each rank other than 1st and last.
            defImplicationNum = defImplicationNum - 1;
        }
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
        anyRankAtoms.addAll(anyRankAtomsTemp);
        return new LinkedHashSet<>(DIs);
    }
}
