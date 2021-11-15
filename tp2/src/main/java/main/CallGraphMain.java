package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

import dendrogramSpoon.DendrogramSpoon;
import dendrogramast.DendrogramAST;
import dendrogramast.DendrogramComposit;
import graphs.AllCouplesGraph;
import graphs.CallGraph;
import graphs.CoupleGraph;
import graphs.StaticCallGraph;
import parsers.Spoon;
import parsers.SpoonClustering;
import parsers.SpoonClusteringPartition;
import processors.ASTProcessor;
import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtParameter;

public class CallGraphMain extends AbstractMain {

	@Override
	protected void menu() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n---- AST part ----");
		builder.append("\n1. Static call graph.");
		builder.append("\n2. DendrogramGraph : Création d'un dendrogram en png et affichage des partitions");
		builder.append("\n3. CoupleGraph : Création des fichiers CoupleGraph.dot et graphCouple.png pour un couple donnée");
		builder.append("\n4. AllCouplesGraph : Création des fichiers CouplesGraph.dot et graphCouples.png pour un src donné, veuillez donner une liste de classe raisonnable");
		builder.append("\n---- Spoon part ----");
		builder.append("\n5. Static call graph (avec Spoon) optionnel.");
		builder.append("\n6. Calculer la métrique de couplage entre deux classes A et B (avec Spoon)");
		builder.append("\n7. Générez un graphe de couplage pondéré entre les classes de l'application en PNG. (avec Spoon)");
		builder.append("\n8. Générer le regroupement en cluster des classes (avec Spoon)");
		builder.append("\n9. Générer les groupes de classes couplés (avec une Pile) (avec Spoon)");
		builder.append("\n10. Ajout de IllegalArgumentException aux méthodes (avec Spoon) optionnel.");
		builder.append("\n11. Dendrogram des clusters avec Spoon au format PNG");
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
		Scanner sc = new Scanner(System.in);
		
		
		Launcher ourLauncher = new Launcher();
        ourLauncher.addInputResource(TEST_PROJECT_PATH);
        
        // Setting the environment for Spoon
        Environment environment = ourLauncher.getEnvironment();
        environment.setCommentEnabled(true); // represent the comments from the source code in the AST
        environment.setAutoImports(true);
        ourLauncher.getEnvironment().setNoClasspath(true);
        ourLauncher.run();
        CtModel model = ourLauncher.getModel();
        Spoon analyze = new Spoon(TEST_PROJECT_PATH,model,ourLauncher);
        
        
		try {
			switch(userInput) {
			// /home/hayaat/Desktop/Master/M2/Git/design_patterns
				case "1":
					long start = System.currentTimeMillis();
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);
					callGraph.log();
					long end = System.currentTimeMillis();
					callGraph.log();
					NumberFormat formatter = new DecimalFormat("#0.00000");
					System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
					break;
				case "2":
					start = System.currentTimeMillis();
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					DendrogramAST dendrogram = new DendrogramAST(TEST_PROJECT_PATH,callGraph);
					dendrogram.clustering();
					dendrogram.createFiles();
					System.out.println("Partition : (voir dendrogramAST.png pour voir à quoi correspond les C)");
					for(DendrogramComposit d : dendrogram.selection_Cluster()) {
						System.out.print(d.getName()+", ");
					}
					System.out.println();
					end = System.currentTimeMillis();
					dendrogram.log();
					formatter = new DecimalFormat("#0.00000");
					System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
					break;

				case "3":
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					CoupleGraph coupleGraph = new CoupleGraph(TEST_PROJECT_PATH,callGraph);
					System.out.println("Inserez le nom de la première classe (avec le package au besoin : package.class)");
					String className1 = sc.next();
					System.out.println("Inserez le nom de la deuxième classe (avec le package au besoin : package.class)");
					String className2 = sc.next();
					start = System.currentTimeMillis();
					coupleGraph.createFiles(className1, className2);
					end = System.currentTimeMillis();
					formatter = new DecimalFormat("#0.00000");
					System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
					break;
				case "4":
					start = System.currentTimeMillis();
					callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);					
					AllCouplesGraph allCouplesGraph = new AllCouplesGraph(TEST_PROJECT_PATH,callGraph);
					allCouplesGraph.createFiles();
					end = System.currentTimeMillis();
					formatter = new DecimalFormat("#0.00000");
					System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
					break;
				
				case "5":
					
					System.out.println("Récupération des données du model avec Spoon..");
                    analyze.getDataWithSpoon(model,ourLauncher);
                    
					System.out.println("Ecriture du callGraph dans le fichier static-callGraphSpoon..");
                    analyze.log();
                    System.out.println("Call graph disponible dans le fichier static-callGraphSpoon.info");
                    
                    break;
					
				case "6":
					System.out.println("Inserez le nom de  la classe A");
					String classNameA = sc.next();
					System.out.println("Inserez le nom de la classe B");
					String classNameB = sc.next();
                    System.out.println("Calcul de la métrique..");
                    analyze.getDataWithSpoon(model,ourLauncher);
                    System.out.println(analyze.getCouplingMetric(classNameA, classNameB));
					break;
				case "7":
					System.out.println("Début de génération du graphe de couplage pondéré...");
					 start = System.currentTimeMillis();
					 analyze.getDataWithSpoon(model,ourLauncher);
					 analyze.createCouplingGraph();
					 end = System.currentTimeMillis();
					 System.out.println("Temp d'exécution pour le graphe de couplage pondéré (en PNG) avec Spoon : " + ((end - start) / 1000) + " secondes");
					break;
				case "8":
					System.out.println("Début de génération du clustering...");
					start = System.currentTimeMillis();
					analyze.getDataWithSpoon(model,ourLauncher);
					analyze.createCouplingGraph();
					SpoonClustering clustering = new SpoonClustering(TEST_PROJECT_PATH,model,ourLauncher);
					clustering.createClustering(clustering.InitialiseClusterSpoon(),clustering.createCouple(analyze));
					end = System.currentTimeMillis();
					System.out.println("Temp d'éxecution pour la génération du clustering  avec Spoon : " + ((end - start) / 1000) + " secondes");
					break;
				case "9":
					System.out.println("Début de génération de l'indentation...");
					start = System.currentTimeMillis();
					analyze.getDataWithSpoon(model,ourLauncher);
					analyze.createCouplingGraph();
					SpoonClusteringPartition partition = new SpoonClusteringPartition(TEST_PROJECT_PATH,model,ourLauncher);
					partition.indentationClusterAlgorithm(partition.InitialiseClusterSpoon(),partition.createCouple(analyze));
					end = System.currentTimeMillis();
					System.out.println("Temp d'exécution pour l'indentation du clustering  avec Spoon : " + ((end - start) / 1000) + " secondes");
					break;
				case "10":
					
					System.out.println("Ajout du test : \n" + "IllegalArgumentException " + " toute les méthodes du code..");
					start = System.currentTimeMillis();
					analyze.addDataWithSpoon(model, ourLauncher);
					end = System.currentTimeMillis();
					System.out.println("Temp d'exécution pour l'ajout de Sensors simple dans le code : " + ((end - start) / 1000) + " secondes");
					break;
					
				case "11":
					
					System.out.println("Début de génération du clustering...");
					start = System.currentTimeMillis();
					analyze.getDataWithSpoon(model,ourLauncher);
					analyze.createCouplingGraph();
					DendrogramSpoon dendogramSpoon = new DendrogramSpoon(TEST_PROJECT_PATH,model,ourLauncher,analyze);
					dendogramSpoon.clustering();
					dendogramSpoon.createFiles();
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
