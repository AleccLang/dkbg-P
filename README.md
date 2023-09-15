# dkbg-P

## Contents
* [Project Description](#project_description)
* [Requirements](#requirements)
* [Build](#build)
* [Run](#run)
* [Program Input](#program_input)

## Project Description
The goal of this project is to develop a non-deterministic defeasible knowledge base generator, as well as an optimised variant, that generates KBs according to Rational Closures BaseRank.

## Requirements
Requirements for the project to be run:
* Java version: 20
* Apache Maven (https://maven.apache.org/download.cgi)

## Build
Run the following command in the main directory to build the package.

Build: $ mvn clean package

## Run
The program may then be run.
Input may be directly input via the command-line interface or placed in a textfile and directly run with the program as shown indicated below.

Run: $ java -cp target/dkbg-1.0.jar com.demo.App or
     $ java -cp target/dkbg-1.0.jar com.demo.App < input.txt

## Program Input

Program input is as follows:
* No. of ranks [int]
* Distribution [options]
* No. defImplications [int]
* Simple only [y/n]
* Reuse Consequent [y/n]
* Antecedent complexity (if simple only then skip)  [0,1,2] choose any number
* Consequent complexity (if simple only then skip) [0,1,2] choose any number
* Connective types [1,2,3,4,5] choose any number (1=disjuntion, 2=conjunction, 3=implication, 4=biimplication, 5=mix)
* Choose connective symbols [defeasible implication, disjuntion, conjunction, implication, biimplication)]
* Choose atom character set [lowerlatin, upperlatin, altlatin, greek]
* Choose which generator to use [standard, threaded]
* Choose if you want the KB printed to the terminal and/or if you also want it as a text file.
* Choose if you want to regenerate a new KB using same settings, change the settings, or quit.
