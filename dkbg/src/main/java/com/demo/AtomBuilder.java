package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AtomBuilder{
    private static AtomBuilder gen;
    private char startChar; 
    private char endChar;
    private List<String> atomList;
    private Random random;
    private int length = 1;
    private int atomCount = 0;

    public AtomBuilder(){
        atomList = new ArrayList<>();
        random = new Random();
        startChar = '\u0041'; 
        endChar = '\u0058';
    }

    public static AtomBuilder getInstance(){
        if (gen == null){
            gen = new AtomBuilder();
        }
        return gen;
    }

    // Resets the settings to default.
    public void reset(){
        atomList.clear();
        length = 1;
        atomCount = 0;
    }

    // Sets the character set to be used for atom generation.
    public void setCharacters(String characterSet){
        switch(characterSet){
            case "upperlatin":
                startChar = '\u0041'; 
                endChar = '\u0058';
                break;
            case "lowerlatin":
                startChar = '\u0061'; 
                endChar = '\u0078';
                break;
            case "greek":
                startChar = '\u03B1'; 
                endChar = '\u03C9';
                break;
            case "altlatin":
                startChar = '\u0250'; 
                endChar = '\u0267';
                break;
        }
    }

    // Generates an atoms.
    public Atom generateAtom(){
        Atom atom = new Atom();
        StringBuilder sb = new StringBuilder();
        countChecker();

        do{
            sb.setLength(0);
            for (int i = 0; i < length; i++){
                char randomChar = (char) (startChar + random.nextInt(endChar - startChar + 1));
                sb.append(randomChar);
            }   
            atom.setAtom(sb.toString());
        }while(atomList.contains(atom.toString()));
        synchronized(atomList){
            atomCount++;
            atomList.add(atom.toString());
        }
        
        return atom;
    }

    // Updates the atom length.
    public void countChecker(){
        int len = length;
        int temp = 0;
        while(len!=0){
            temp += Math.pow(24, len);
            len--;
        }
        if(atomCount == temp){
            length++;
        }
    }
}
