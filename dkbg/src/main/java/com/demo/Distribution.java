package com.demo;

import java.util.Arrays;

// Calculates the distribution of defImplications over the ranks, depending on the distribution type.
public class Distribution{

    public static int[] distributeDefImplications(int numDefImplications, int numRanks, String distribution){
        int[] ranks = new int[numRanks];

        switch (distribution){
            case "f":
                distributeFlat(numDefImplications, numRanks, ranks);
                break;
            case "lg":
                distributeLinearGrowth(numDefImplications, numRanks, ranks);
                break;
            case "ld":
                distributeLinearDecline(numDefImplications, numRanks, ranks);
                break;
            case "r":
                distributeRandom(numDefImplications, numRanks, ranks);
                break;
            default:
                throw new IllegalArgumentException("Invalid distribution type: " + distribution);
        }

        return ranks;
    }

    // Calculates a flat distribution of defImplications over the ranks
    private static void distributeFlat(int numDefImplications, int numRanks, int[] ranks){
        int defImplicationsPerRank = numDefImplications / numRanks;
        int remainder = numDefImplications % numRanks;

        for (int i = 0; i < numRanks; i++){
            ranks[i] = defImplicationsPerRank;
        }

        int i = numRanks-1;
        while (remainder > 0){
            ranks[i]++;
            remainder--;
            i--;
        }
    }

    // Calculates a linear growth distribution of defImplications over the ranks
    private static void distributeLinearGrowth(int numDefImplications, int numRanks, int[] ranks){
        int remainingDefImplications = numDefImplications;
        
        for (int i = 0; i < numRanks; i++){
            int defImplicationsToAdd = Math.min(remainingDefImplications, i + 1);
            ranks[i] = defImplicationsToAdd;
            remainingDefImplications -= defImplicationsToAdd;
        }
    
        int currentRank = numRanks - 1;
        while (remainingDefImplications > 0){
            if(currentRank < 0){
                currentRank = numRanks - 1;
            }
            int defImplicationsToAdd = Math.min(remainingDefImplications, 1);
            ranks[currentRank] += defImplicationsToAdd;
            remainingDefImplications -= defImplicationsToAdd;
            currentRank--;
        }
    }
    
    // Calculates a linear decline distribution of defImplications over the ranks
    private static void distributeLinearDecline(int numDefImplications, int numRanks, int[] ranks){
        int remainingDefImplications = numDefImplications;
    
        for (int i = 0; i < numRanks; i++){
            int defImplicationsToAdd = Math.min(remainingDefImplications, i + 1);
            ranks[i] = defImplicationsToAdd;
            remainingDefImplications -= defImplicationsToAdd;
        }
    
        int currentRank = numRanks - 1;
        while (remainingDefImplications > 0){
            if(currentRank < 0){
                currentRank = numRanks - 1;
            }
            int defImplicationsToAdd = Math.min(remainingDefImplications, 1);
            ranks[currentRank] += defImplicationsToAdd;
            remainingDefImplications -= defImplicationsToAdd;
            currentRank--;
        }
    
        for (int i = 0; i < numRanks / 2; i++){
            int temp = ranks[i];
            ranks[i] = ranks[numRanks - i - 1];
            ranks[numRanks - i - 1] = temp;
        }
    }

    // Calculates a random distribution of defImplications over the ranks
    private static void distributeRandom(int numDefImplications, int numRanks, int[] ranks){
        int remainingDefImplications = numDefImplications - numRanks * 2;
        Arrays.fill(ranks, 2);

        while(remainingDefImplications > 0){
            int i = (int)(Math.random() * ranks.length);
            ranks[i]++;
            remainingDefImplications--;
        }
    }
     
    // Minimum defImplications needed for a linear increase distribution
    public static int minDefImplicationsLinear(int numRanks){
        int sum = numRanks * (numRanks + 1) / 2;
        return sum;
    }

    // Minimum defImplications needed for a linear decrease distribution
    public static int minDefImplicationsLinearDecline(int numRanks){
        int sum = 0;
        int x = 2;
        for(int i = 0; i < numRanks; i++){
            sum += (x);
            x++;
        }
        return sum;
    }
}
