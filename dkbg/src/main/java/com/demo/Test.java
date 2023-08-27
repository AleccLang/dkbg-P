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

public class Test
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
        int[] defImplicationDistribution = Distribution.distributeDefImplications(100, 10, "f");
            int[] complexityAnt = {0};
            int[] complexityCon = {0};
            int[] connectionType = {0};
            long AvExecutionTime = 0;
            for(int i = 0; i<6; i++){   
                LinkedHashSet<LinkedHashSet<DefImplication>> KB = new LinkedHashSet<>();
                startTime = System.currentTimeMillis();
                KB = KBGenerator.KBGenerate(defImplicationDistribution, true, true, complexityAnt, complexityCon, connectionType);
                endTime = System.currentTimeMillis();
                if(i!=0){
                    long executionTime = endTime - startTime;
                    AvExecutionTime = AvExecutionTime + executionTime;
                    System.out.println("Time taken for standard KB generation (in milliseconds): " + executionTime);
                }
            }
            AvExecutionTime = AvExecutionTime / 5;
            System.out.println("Time taken for standard KB generation (in milliseconds): " + AvExecutionTime);

                    // do{
                    //     ExecutorService executor = Executors.newSingleThreadExecutor();
                    //     long timeoutDuration = 15000;
                    //     try{
                    //         Callable<LinkedHashSet<LinkedHashSet<DefImplication>>> kbGenerationTask = () -> {
                    //             return KBGeneratorThreaded.KBGenerate(defImplicationDistribution, simple, complexityAnt, complexityCon, connectiveTypes);
                    //         };

                    //         startTime = System.currentTimeMillis();
                    //         Future<LinkedHashSet<LinkedHashSet<DefImplication>>> future = executor.submit(kbGenerationTask);
                    //         KB = future.get(timeoutDuration, TimeUnit.MILLISECONDS);

                    //         endTime = System.currentTimeMillis();
                    //         long executionTime = endTime - startTime;
                    //         System.out.println("Time taken for threaded KB generation (in milliseconds): " + executionTime);
                    //         rerun = false;

                    //     }catch(TimeoutException e){
                    //         System.out.println("Timeout occurred during KB generation. Retrying...");
                    //         executor.shutdownNow();
                    //         gen.reset();
                    //         rerun = true;
                    //     }catch(InterruptedException | ExecutionException e){
                    //         //e.printStackTrace();
                    //     }finally{
                    //         executor.shutdownNow();
                    //     }
                    // }while(rerun == true);]
    }
}