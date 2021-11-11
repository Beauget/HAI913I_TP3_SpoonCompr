package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

import graphs.AllCouplesGraph;
import graphs.CallGraph;
import graphs.CoupleGraph;
import graphs.DendrogramGraph;
import graphs.StaticCallGraph;
import parsers.Spoon;
import processors.ASTProcessor;
import processors.ProcessorClustering;
import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;

public class CallGraphMain extends AbstractMain {

	@Override
	protected void menu() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n1. Static call graph.");
		builder.append("\n2. ProcessorClustering.");
		builder.append("\n3. DendrogramGraph : Création d'un dendrogram en png (n'envoie plus le bon)");
		builder.append("\n4. CoupleGraph : Création des fichiers CoupleGraph.dot et graphCouple.png pour un couple donnée");
		builder.append("\n5. AllCouplesGraph : Création des fichiers CouplesGraph.dot et graphCouples.png pour un src donné, veuillez donner une liste de classe raisonnable");
		builder.append("\n6. Spoon.");
		builder.append("\n7. Dynamic call graph.");
		builder.append("\n8. Help menu.");
		builder.append("\n"+QUIT+". To quit.");
		
		System.out.println(builder);
	}

	public static void main(String[] args) {	
		CallGraphMain main = new CallGraphMain();
		BufferedReader inputReader;
		CallGraph callGraph = null;
		try {
			inputReader = new BufferedReader(new InputStreamReader(System.in));
			if (args.length < 1)
				setTestProjectPath(inputReader);
			else
				verifyTestProjectPath(inputReader, args[0]);
			String userInput = "";
			
			do {	
				main.menu();			
				userInput = inputReader.readLine();
				main.processUserInput(userInput, callGraph);
				Thread.sleep(3000);
				
			} while(!userInput.equals(QUIT));
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void processUserInput(String userInput, ASTProcessor processor) {
		CallGraph callGraph = (CallGraph) processor;
		try {
			switch(userInput) {
			// /home/hayaat/Desktop/Master/M2/Git/design_patterns
				case "1":
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);
					callGraph.log();
					break;
				case "2":
					long start = System.currentTimeMillis();
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);
					ProcessorClustering processorClustering = new ProcessorClustering(TEST_PROJECT_PATH,callGraph);
					long end = System.currentTimeMillis();
					processorClustering.log();
					NumberFormat formatter = new DecimalFormat("#0.00000");
					System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
					break;
					
				case "3":
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					DendrogramGraph dendrogramGraph = new DendrogramGraph(TEST_PROJECT_PATH,callGraph);
					dendrogramGraph.createFiles();
					
					break;
				case "4":
					Scanner sc = new Scanner(System.in);
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					CoupleGraph coupleGraph = new CoupleGraph(TEST_PROJECT_PATH,callGraph);
					System.out.println("Inserez le nom de la première classe (avec le package au besoin : package.class)");
					String className1 = sc.next();
					System.out.println("Inserez le nom de la deuxième classe (avec le package au besoin : package.class)");
					String className2 = sc.next();
					coupleGraph.createFiles(className1, className2);
					return;
				case "5":
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					AllCouplesGraph allCouplesGraph = new AllCouplesGraph(TEST_PROJECT_PATH,callGraph);
					allCouplesGraph.createFiles();
					return;
				
				case "6":
                    System.out.println("Someone trying to do same things with Spoon");
                    Launcher ourLauncher = new Launcher();
                    ourLauncher.addInputResource(TEST_PROJECT_PATH);
                    
                    // Setting the environment for Spoon
                    Environment environment = ourLauncher.getEnvironment();
                    environment.setCommentEnabled(true); // represent the comments from the source code in the AST
                    environment.setAutoImports(true);
                    ourLauncher.getEnvironment().setNoClasspath(true);
                    ourLauncher.run();
                    CtModel model = ourLauncher.getModel();
                    Spoon.analyzeWithSpoon(model,ourLauncher);
                    break;
					
				case "7":
					System.err.println("Not implemented yet");
					break;
				case "8":
					System.err.println("Not implemented yet");
					break;
					
				case QUIT:
					System.out.println("Bye...");
					return;
				
				default:
					System.err.println("Sorry, wrong input. Please try again.");
					return;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
