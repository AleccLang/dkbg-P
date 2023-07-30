package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AtomBuilder{
    private static AtomBuilder generator;
    private static char startChar; 
    private static char endChar;

    private static List<String> atomList;
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
        if (generator == null){
            generator = new AtomBuilder();
        }
        return generator;
    }

    public void reset(){
        atomList.clear();
        length = 1;
        atomCount = 0;
    }

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
        }while(atomList.contains(atom.toString())!=false);
        atomCount++;
        atomList.add(atom.toString());
        return atom;
    }

    public void countChecker(){
        switch (atomCount){
            case 24:
                length = 2;
                break;
            case 600:
                length = 3;
                break;
            case 14424:
                length = 4;
                break;
            case 346200:
                length = 5;
                break;
            case 8308824:
                length = 6;
                break;
            case 199411800:
                length = 7;
                break;
        }
    }

    // public static void main(String[] args){
        
    //     AtomBuilder generator = new AtomBuilder();

    //     for (int i = 0; i < 100; i++){
    //         Atom atom = generator.generateAtom();
    //         System.out.println("Atom: " + atom.toString());
    //     }
    //     generator.reset();
    //     System.out.println("////////////////////////////////////");
    //     for (int i = 0; i < 100; i++){
    //         Atom atom = generator.generateAtom();
    //         System.out.println("Atom: " + atom.toString());
    //     }
    // }
}
