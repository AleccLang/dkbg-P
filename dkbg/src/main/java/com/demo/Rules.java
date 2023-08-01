package com.demo;

import java.util.HashMap;

// Determines if a complex statement can be generated given the current state of the knowledgebase.
public class Rules{
    
    private static HashMap<String, String> keyMap; // Hashmap of complex statements and their required available currRankAtoms

    public Rules(){
        // Key = "connectionType,complexityAnt,complexityCon"
        // Value = "currRankAtoms.length()"
        keyMap = new HashMap<>();
        
        // connectionType = disjunction
        keyMap.put("1,0,1", "2");
        keyMap.put("1,0,2", "3");
        keyMap.put("1,1,0", "1");
        keyMap.put("1,1,1", "2");
        keyMap.put("1,1,2", "3");
        keyMap.put("1,2,0", "1");
        keyMap.put("1,2,1", "2");
        keyMap.put("1,2,2", "3");
        // connectionType = conjunction
        keyMap.put("2,0,1", "1");
        keyMap.put("2,0,2", "1");
        keyMap.put("2,1,0", "1");
        keyMap.put("2,1,1", "1");
        keyMap.put("2,1,2", "1");
        keyMap.put("2,2,0", "1");
        keyMap.put("2,2,1", "1");
        keyMap.put("2,2,2", "1");
        // connectionType = implication
        keyMap.put("3,0,1", "1");
        keyMap.put("3,0,2", "1");
        keyMap.put("3,1,0", "1");
        keyMap.put("3,2,0", "1");
        // connectionType = bi-implication
        keyMap.put("4,0,1", "1");
        keyMap.put("4,0,2", "1");
        keyMap.put("4,1,0", "1");
        keyMap.put("4,2,0", "1");
        // connectionType = mixed connectives
        keyMap.put("5,0,1", "1");
        keyMap.put("5,0,2", "1");
        keyMap.put("5,1,0", "2");
        keyMap.put("5,1,1", "2");
        keyMap.put("5,1,2", "2");
        keyMap.put("5,2,0", "2");
        keyMap.put("5,2,1", "2");
        keyMap.put("5,2,2", "2");
    }

    // Checks if there are enough currRankAtoms available to generate a statement for a given key and returns key if
    // it is valid.
    public static String checker(String key, int curRankAtomNum){
        String temp = keyMap.get(key);
        // Do this to avoid problems with implication and bi-implication.
        if(temp == null){
            return "1,0,0";
        }
        else{
            int curRankAtomMin = Integer.parseInt(temp.substring(0,1));
            if(Integer.parseInt(key.substring(0, 1)) == 5){ // For mixed statements, could optimise this?
                return (curRankAtomNum>=curRankAtomMin) ? key : "5,0," + key.substring(4,5);
            }
            else{
                return (curRankAtomNum>=curRankAtomMin) ? key : key.substring(0,4)+ "0";
            }
        }
    }

    // Generates a statement key.
    public static String keyGenerator(int[] connectionType, int[] complexityAnt, int[] complexityCon, int curRankAtomNum){
        int i = (int)(Math.random() * connectionType.length); // Get random connectionType from those available.
        int j = (int)(Math.random() * complexityAnt.length); // Get random connectionType from those available.
        int z; do{ z = (int)(Math.random() * complexityCon.length);} while ((complexityCon[z] == 0 && complexityAnt[j] == 0)); // Get random connectionType from those available (z & j can't both be 0).
        String key = connectionType[i] + "," + complexityAnt[j] + "," + complexityCon[z];
        return checker(key, curRankAtomNum);
    }
}