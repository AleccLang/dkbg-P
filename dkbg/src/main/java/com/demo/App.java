package com.demo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

// Program input is as follows:
/* 
* 1) No. of ranks [int]
* 2) Distribution [options] // min 2 per rank, other than rank 0
* 3) No. defImplications [int]
* 4) Simple only [y/n]
* 5) Reuse Consequent [y/n]
* 6) Antecedent complexity (if simple only then skip)  [0,1,2] choose any number
* 7) Consequent complexity (if simple only then skip) [0,1,2] choose any number
* 8) Connective types [1,2,3,4,5] choose any number (1=disjuntion, 2=conjunction, 3=implication, 4=biimplication, 5=mix) if 1:1 1:2 2:1 2:2 then imp/bi-imp won't work.
* 9) Choose connective symbols [defeasible implication, disjuntion, conjunction, implication, biimplication)]
* 10) Choose atom character set [lowerlatin, upperlatin, altlatin, greek]
* 11) Choose which generator to use [standard, threaded]
* 12) Choose if you just want it printed or if you also want a txt file.
* 13) Choose if you want to regenerate a new KB using same settings, change the settings, or quit.
*/
public class App 
{
    private static Connective con = Connective.getInstance();
    private static AtomBuilder gen = AtomBuilder.getInstance();
    private static long startTime;
    private static long endTime;
    private static int filenum = 1;
    static String choice;

    public static void main( String[] args ){
        Rules r = new Rules();
        Scanner in = new Scanner(System.in);

        do{
            con.reset();
            System.out.println( "Defeasible Knowledge Base Generator:");
            List<Integer> complexityAntList = new ArrayList<>(); // Number of possible connectives in a defImplications antecedent
            List<Integer> complexityConList = new ArrayList<>(); // Number of possible connectives in a defImplications consequent
            List<Integer> connectiveList = new ArrayList<>(); // Number of different connectives a defImplication can have

            System.out.println("Enter the number of ranks in the KB:");
            System.out.print("> ");
            int numRanks = in.nextInt(); // Number of ranks in the knowledgebase (including rank 0)
            while ((numRanks <= 0)){
                System.out.println("Enter a non-negative number of ranks in the KB:");
                System.out.print("> ");
                numRanks = in.nextInt();
            }

            // Maybe have distribution first, because the number of defImplications are dependent on the distribution and the number of ranks.
            System.out.println("Enter the defImplication distribution [f (flat), lg (linear-growth), ld (linear-decline), r (random)]:");
            System.out.print("> ");
            String distribution = in.next(); // Distribution of the defImplications in the knowledge base
            while (!validDistribution(distribution)){
                System.out.println("Enter valid defImplication distribution [f (flat), lg (linear-growth), ld (linear-decline), r (random)]:");
                System.out.print("> ");
                distribution = in.next();
            }

            int min = minDefImplications(distribution, numRanks);
            System.out.println("Enter the number of defImplications in the KB (Must be greater than or equal to " + min + "):");
            System.out.print("> ");
            int numDefImplications = in.nextInt(); // Number of defImplications in the knowledge base
            while (!(numDefImplications >= min)){
                System.out.println("Enter a valid number of defImplications in the KB (Must be greater than or equal to " + min + "):");
                System.out.print("> ");
                numDefImplications = in.nextInt();
            }

            int[] defImplicationDistribution = Distribution.distributeDefImplications(numDefImplications, numRanks, distribution);

            System.out.println("Simple defImplications only? [y, n]:");
            System.out.print("> ");
            String smple = in.next(); // Knowledge base generation using only simple defImplications
            boolean simple = (smple.equalsIgnoreCase("y")) ? true : false;

            System.out.println("Reuse Consequent? [y, n]:");
            System.out.print("> ");
            String reuseAnt = in.next(); // Reuse the rankBuildAntecedent to generate ranks in the knowledge base
            boolean reuseConsequent = (reuseAnt.equalsIgnoreCase("y")) ? true : false;

            if(simple == false){
                System.out.println("Antecedent complexity [0, 1, 2]:");
                System.out.println("Enter chosen numbers seperated by commas:");
                System.out.print("> ");
                String antComplexity = in.next();

                String[] antStrings = antComplexity.split(",");
                for (int i = 0; i < antStrings.length; i++){
                    int temp = Integer.parseInt(antStrings[i].trim());
                    if (temp != 0 && temp != 1 && temp != 2){
                        // Skip invalid numbers
                    }
                    else{
                        complexityAntList.add(temp);
                    }
                }

                System.out.println("Consequent complexity [0, 1, 2]:");
                System.out.println("Enter chosen numbers separated by commas:");
                System.out.print("> ");
                String conComplexity = in.next();

                String[] conStrings = conComplexity.split(",");
                for (int i = 0; i < conStrings.length; i++){
                    int temp = Integer.parseInt(conStrings[i].trim());
                    if (temp != 0 && temp != 1 && temp != 2){
                        // Skip invalid numbers
                    }
                    else{
                        complexityConList.add(temp);
                    }
                }

                System.out.println("Connective types [1, 2, 3, 4, 5]:");
                System.out.println("1 = disjuntion, 2 = conjunction, 3 = implication, 4 = bi-implication, 5 = mixture");
                System.out.println("Enter chosen numbers separated by commas:");
                System.out.print("> ");
                String connectiveTypes = in.next();

                String[] connectiveStrings = connectiveTypes.split(",");
                for (int i = 0; i < connectiveStrings.length; i++){
                    int temp = Integer.parseInt(connectiveStrings[i].trim());
                    if (temp != 1 && temp != 2 && temp != 3 && temp != 4 && temp != 5){
                        // Skip invalid numbers
                    }
                    else{
                        connectiveList.add(temp);
                    }
                }
                }

            System.out.println("Would you like to change connective symbols? [y, n]");
            System.out.print("> ");
            String chnge = in.next(); // Change the connective symbols used in the defImplications
            boolean change = (chnge.equalsIgnoreCase("y")) ? true : false;
            if(change == true){
                System.out.println("Default Defeasible Implication symbol: ~> ['s' to skip]");
                System.out.print("> ");
                String defImp = in.next();
                boolean chng = (defImp.equalsIgnoreCase("s")) ? true : false;
                if(chng == false){con.setDefImplicationSymbol(defImp);} // Sets defeasible implication symbol
                
                if(simple == false){
                    System.out.println("Default Conjunction symbol: & ['s' to skip]");
                    System.out.print("> ");
                    String conj = in.next();
                    chng = (conj.equalsIgnoreCase("s")) ? true : false;
                    if(chng == false){con.setConjunctionSymbol(conj);} // Sets conjunction symbol

                    System.out.println("Default Disjunction symbol: || ['s' to skip]");
                    System.out.print("> ");
                    String disj = in.next();
                    chng = (disj.equalsIgnoreCase("s")) ? true : false;
                    if(chng == false){con.setDisjunctionSymbol(disj);}  // Sets disjunction symbol

                    System.out.println("Default Implication symbol: => ['s' to skip]");
                    System.out.print("> ");
                    String imp = in.next();
                    chng = (imp.equalsIgnoreCase("s")) ? true : false;
                    if(chng == false){con.setImplicationSymbol(imp);}  // Sets implication symbol

                    System.out.println("Default Bi-Implication symbol: <=> ['s' to skip]");
                    System.out.print("> ");
                    String biimp = in.next();
                    chng = (biimp.equalsIgnoreCase("s")) ? true : false;
                    if(chng == false){con.setBiImplicationSymbol(biimp);}  // Sets bi-implication symbol

                    System.out.println("Default Negation symbol: ! ['s' to skip]");
                    System.out.print("> ");
                    String negation = in.next();
                    chng = (negation.equalsIgnoreCase("s")) ? true : false;
                    if(chng == false){con.setNegationSymbol(negation);}  // Sets negation symbol
                }
            }
            
            System.out.println("Enter the character set for the knowledge base [lowerlatin, upperlatin, altlatin, greek]");
            System.out.println("Greek character set requires code page 65001");
            System.out.println("Can set this in the terminal using 'chcp 65001'");
            System.out.print("> ");
            String characterSet = in.next(); // The character set used for the atoms
            while (!validCharacterSet(characterSet)){
                System.out.println("Enter valid character set [lowerlatin, upperlatin, altlatin, greek]:");
                System.out.print("> ");
                characterSet = in.next();
            }
            gen.setCharacters(characterSet);

            do{
                int[] complexityAnt = new int[complexityAntList.size()];
                for (int i = 0; i < complexityAntList.size(); i++){
                        complexityAnt[i] = complexityAntList.get(i);
                }

                int[] complexityCon = new int[complexityConList.size()];
                for (int i = 0; i < complexityConList.size(); i++){
                        complexityCon[i] = complexityConList.get(i);
                }

                int[] connectiveTypes = new int[connectiveList.size()];
                for (int i = 0; i < connectiveList.size(); i++){
                        connectiveTypes[i] = connectiveList.get(i);
                }

                System.out.println("Generator type? [s (standard), e (efficient)]:");
                System.out.print("> ");
                String type = in.next(); // Knowledge base generation using only simple defImplications
                System.out.println("Generating Knowledge Base:");
                LinkedHashSet<LinkedHashSet<DefImplication>> KB = new LinkedHashSet<>();
                boolean rerun = true;
                if(type.equalsIgnoreCase("s")){
                    startTime = System.currentTimeMillis();
                    KB = KBGenerator.KBGenerate(defImplicationDistribution, simple, reuseConsequent, complexityAnt, complexityCon, connectiveTypes);
                    endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;
                    System.out.println("Time taken for standard KB generation (in milliseconds): " + executionTime);
                }
                else{
                    do{
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        long timeoutDuration = 1000;
                        try{
                            Callable<LinkedHashSet<LinkedHashSet<DefImplication>>> kbGenerationTask = () -> {
                                return KBGeneratorThreaded.KBGenerate(defImplicationDistribution, simple, complexityAnt, complexityCon, connectiveTypes);
                            };

                            startTime = System.currentTimeMillis();
                            Future<LinkedHashSet<LinkedHashSet<DefImplication>>> future = executor.submit(kbGenerationTask);
                            KB = future.get(timeoutDuration, TimeUnit.MILLISECONDS);

                            endTime = System.currentTimeMillis();
                            long executionTime = endTime - startTime;
                            System.out.println("Time taken for threaded KB generation (in milliseconds): " + executionTime);
                            rerun = false;

                        }catch(TimeoutException e){
                            System.out.println("Timeout occurred during KB generation. Retrying...");
                            executor.shutdownNow();
                            gen.reset();
                            rerun = true;
                        }catch(InterruptedException | ExecutionException e){
                            //e.printStackTrace();
                        }finally{
                            executor.shutdownNow();
                        }
                    }while(rerun == true);
                }

                System.out.println("Save to text file? [y, n]:");
                System.out.print("> ");
                String save = in.next(); // Save the knowledge base to a text file
                if(save.equalsIgnoreCase("y")){
                kbToFile(KB);
                }

                System.out.println("Print to terminal? [y, n]:");
                System.out.print("> ");
                String print = in.next(); // Print knowledge base to terminal
                if(print.equalsIgnoreCase("y")){
                    System.out.println("Knowledge base:");
                    int i = 0;
                    for (LinkedHashSet<DefImplication> set : KB){
                        System.out.print("Rank " + i + ": ");
                        Iterator<DefImplication> iterator = set.iterator();
                        while (iterator.hasNext()){
                            DefImplication element = iterator.next();
                            System.out.print(element.toString());
                            
                            if(iterator.hasNext()){
                                System.out.print(", ");
                            }
                            else{
                                System.out.println();
                            }
                        }
                        i++;
                    }
                }
                
                gen.reset();
                System.out.println("Regenerate new knowledge base? [r]:");
                System.out.println("Change settings? [c]:");
                System.out.println("Quit? [q]:");
                System.out.print("> ");
                choice = in.next();
                
            }while(choice.equalsIgnoreCase("r"));
        }while(choice.equalsIgnoreCase("c"));
        System.out.println("Quitting");
        in.close();
    }
    
    private static boolean validDistribution(String input){
        return input.equalsIgnoreCase("f") || input.equalsIgnoreCase("lg") ||
               input.equalsIgnoreCase("ld") || input.equalsIgnoreCase("r");
    }

    private static boolean validCharacterSet(String input){
        return input.equalsIgnoreCase("lowerlatin") || input.equalsIgnoreCase("upperlatin") ||
               input.equalsIgnoreCase("altlatin") || input.equalsIgnoreCase("greek");
    }

    private static int minDefImplications(String distribution, int numRanks){
        int min = 0;
        switch(distribution){
            case "f":
                min = (numRanks*2)-1;
                break;
            case "lg":
                min = Distribution.minDefImplicationsLinear(numRanks);
                break;
            case "ld":
                min = Distribution.minDefImplicationsLinearDecline(numRanks);
                break;
            case "r":
                min = (numRanks*2);
                break;
        }
        return min;
    }

    private static void kbToFile(LinkedHashSet<LinkedHashSet<DefImplication>> KB){
        String filePath = "output" + filenum + ".txt"; // Specify the file path
        filenum++;
        try{
            File file = new File(filePath);
            FileWriter fw = new FileWriter(file);

            for(LinkedHashSet<DefImplication> set : KB){
                for (DefImplication element : set){
                    fw.write(element.toString() + "\n");
                }
            }
            fw.close();
        } 
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
