package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import graphs.AllCouplesGraph;
import graphs.CallGraph;
import graphs.CoupleGraph;
import graphs.DynamicCallGraph;
import graphs.StaticCallGraph;
import metric.GenerateClassesAndContent;
import processors.ASTProcessor;
import processors.ProcessorClustering;

public class CallGraphMain extends AbstractMain {

	@Override
	protected void menu() {
		StringBuilder builder = new StringBuilder();
		builder.append("1. Static call graph.");
		builder.append("\n2. PreccessorClustering.");
		builder.append("\n3. CoupleGraph. (tel que classe1 = Point et class2 = Forme)");
		builder.append("\n4. AllCouplesGraph.");
		builder.append("\n5. Help menu.");
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
				case "1":
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);
					callGraph.log();
					break;
				
				case "2":
					/* /home/hayaat/Desktop/Master/M1/Java/TP4/src/ */
					// /home/hayaat/Desktop/Master/M2/Java2021/HAI913I_badSmell/src/
					// /home/hayaat/Desktop/Master/M1/Java/TP4/src/
					// /home/hayaat/Desktop/Master/M1/Java/HMIN210/TP1RMI/src/
					// /home/hayaat/Desktop/Master/M2/Git/HAI913I_TP3_SpoonCompr/design_patterns/src/
					// /home/hayaat/Desktop/Master/M2/Git/HAI913I_TP3_SpoonCompr/design_patterns/src/
					// /home/hayaat/Desktop/Master/M2/Git/HAI913I_TP3_SpoonCompr/tp2/target/test-classes/structural/src/composite/src/
					// /home/hayaat/Desktop/Master/M2/Git/HAI913I_TP3_SpoonCompr/design_patterns/src/structural/src/
					// /home/hayaat/Desktop/Master/M2/Git/HAI913I_TP3_SpoonCompr/design_patterns/src/structural/src/composite/src
					// /home/hayaat/Desktop/Master/M2/Git/HAI913I_TP3_SpoonCompr/src/
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					ProcessorClustering processorClustering = new ProcessorClustering(TEST_PROJECT_PATH,callGraph);
					processorClustering.log();
					break;
				case "3":
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					CoupleGraph coupleGraph = new CoupleGraph(TEST_PROJECT_PATH,callGraph);
					coupleGraph.createFiles("Point", "Forme");
					return;
				case "4":
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					AllCouplesGraph allCouplesGraph = new AllCouplesGraph(TEST_PROJECT_PATH,callGraph);
					allCouplesGraph.createFiles();
					return;
				
				case "5":
					return;
					
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
