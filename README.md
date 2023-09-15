# dkbg-P

Program input is as follows:
     1) No. of ranks [int]
     2) Distribution [options]
     3) No. defImplications [int]
     4) Simple only [y/n]
     5) Reuse Consequent [y/n]
     6) Antecedent complexity (if simple only then skip)  [0,1,2] choose any number
     7) Consequent complexity (if simple only then skip) [0,1,2] choose any number
     8) Connective types [1,2,3,4,5] choose any number (1=disjuntion, 2=conjunction, 3=implication, 4=biimplication, 5=mix)
     9) Choose connective symbols [defeasible implication, disjuntion, conjunction, implication, biimplication)]
     10) Choose atom character set [lowerlatin, upperlatin, altlatin, greek]
     11) Choose which generator to use [standard, threaded]
     12) Choose if you just want it printed or if you also want a txt file.
     13) Choose if you want to regenerate a new KB using same settings, change the settings, or quit.

Build: mvn package
Run: java -cp target/dkbg-1.0.jar com.demo.App or
     java -cp target/dkbg-1.0.jar com.demo.App < input.txt
     
