package com.demo;

import java.util.Arrays;

// Calculates the distribution of formulas over the ranks, depending on the distribution type.
public class Distribution{

    public static int[] distributeFormulas(int numFormulas, int numRanks, String distribution){
        int[] ranks = new int[numRanks];

        switch (distribution){
            case "f":
                distributeFlat(numFormulas, numRanks, ranks);
                break;
            case "lg":
                distributeLinearGrowth(numFormulas, numRanks, ranks);
                break;
            case "ld":
                distributeLinearDecline(numFormulas, numRanks, ranks);
                break;
            case "r":
                distributeRandom(numFormulas, numRanks, ranks);
                break;
            default:
                throw new IllegalArgumentException("Invalid distribution type: " + distribution);
        }

        return ranks;
    }

    // Calculates a flat distribution of formulas over the ranks
    private static void distributeFlat(int numFormulas, int numRanks, int[] ranks){
        int formulasPerRank = numFormulas / numRanks;
        int remainder = numFormulas % numRanks;

        for (int i = 0; i < numRanks; i++){
            ranks[i] = formulasPerRank;
        }

        int i = numRanks-1;
        while (remainder > 0){
            ranks[i]++;
            remainder--;
            i--;
        }
    }

    // Calculates a linear growth distribution of formulas over the ranks
    private static void distributeLinearGrowth(int numFormulas, int numRanks, int[] ranks){
        int remainingFormulas = numFormulas;
        
        for (int i = 0; i < numRanks; i++){
            int formulasToAdd = Math.min(remainingFormulas, i + 1);
            ranks[i] = formulasToAdd;
            remainingFormulas -= formulasToAdd;
        }
    
        int currentRank = numRanks - 1;
        while (remainingFormulas > 0){
            if(currentRank < 0){
                currentRank = numRanks - 1;
            }
            int formulasToAdd = Math.min(remainingFormulas, 1);
            ranks[currentRank] += formulasToAdd;
            remainingFormulas -= formulasToAdd;
            currentRank--;
        }
    }
    
    // Calculates a linear decline distribution of formulas over the ranks
    private static void distributeLinearDecline(int numFormulas, int numRanks, int[] ranks){
        int remainingFormulas = numFormulas;
    
        for (int i = 0; i < numRanks; i++){
            int formulasToAdd = Math.min(remainingFormulas, i + 1);
            ranks[i] = formulasToAdd;
            remainingFormulas -= formulasToAdd;
        }
    
        int currentRank = numRanks - 1;
        while (remainingFormulas > 0){
            if(currentRank < 0){
                currentRank = numRanks - 1;
            }
            int formulasToAdd = Math.min(remainingFormulas, 1);
            ranks[currentRank] += formulasToAdd;
            remainingFormulas -= formulasToAdd;
            currentRank--;
        }
    
        for (int i = 0; i < numRanks / 2; i++){
            int temp = ranks[i];
            ranks[i] = ranks[numRanks - i - 1];
            ranks[numRanks - i - 1] = temp;
        }
    }

    // Calculates a random distribution of formulas over the ranks
    private static void distributeRandom(int numFormulas, int numRanks, int[] ranks){
        int remainingFormulas = numFormulas - numRanks * 2;
        Arrays.fill(ranks, 2);

        while(remainingFormulas > 0){
            int i = (int)(Math.random() * ranks.length);
            ranks[i]++;
            remainingFormulas--;
        }
    }
     
    // Minimum formulas needed for a linear increase distribution
    public static int minFormulasLinear(int numRanks){
        int sum = numRanks * (numRanks + 1) / 2;
        return sum;
    }

    // Minimum formulas needed for a linear decrease distribution
    public static int minFormulasLinearDecline(int numRanks){
        int sum = 0;
        int x = 2;
        for(int i = 0; i < numRanks; i++){
            sum += (x);
            x++;
        }
        return sum;
    }
}
