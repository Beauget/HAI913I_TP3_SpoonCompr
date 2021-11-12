package parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import models.ClassCoupleSpoon;
import models.Cluster;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtType;
import parsers.Spoon;

public class SpoonClustering extends Spoon {
	
	ArrayList<Cluster> cluster;

	public SpoonClustering(String path, CtModel model) {
		super(path, model);
		this.cluster = new ArrayList<Cluster>();
	}
	
	
	
	@SuppressWarnings("unlikely-arg-type")
	public ArrayList<Cluster> InitialiseClusterSpoon() {
		Cluster toAdd;
		for (CtType<?> type : model.getAllTypes()) {
			if (!this.cluster.contains(type.getQualifiedName())){
				toAdd = Cluster.createClusterStatic(type.getQualifiedName());
				this.cluster.add(toAdd);
			}
		}
		return this.cluster;
	}
	
	
	public ArrayList<ClassCoupleSpoon> createListOfClassesCouple( Spoon spoonCouplingGraph) {

		ArrayList<ClassCoupleSpoon> coupledClassesList = new ArrayList<ClassCoupleSpoon>();
		ClassCoupleSpoon newClassCoupleSpoon;
		Map<String, Double> tempMap;
		for (String classA : spoonCouplingGraph.getCouplingGraph().keySet()) {
			tempMap = spoonCouplingGraph.getCouplingGraph().get(classA);
			for (String classB : tempMap.keySet()) {
				newClassCoupleSpoon = new ClassCoupleSpoon(classA, classB, tempMap.get(classB));
				coupledClassesList.add(newClassCoupleSpoon);
			}
		}
		for(ClassCoupleSpoon i : coupledClassesList) {
			i.toString();
		}
		return coupledClassesList;
	}
	
	private double getScoreClusters(Cluster clusterA, Cluster clusterB,
			ArrayList<ClassCoupleSpoon> couplesOfClasses) {
		double couplingValue = 0;
		for (String classOfClusterA : clusterA.getClassList()) {
			for (String classOfClusterB : clusterB.getClassList()) {
				if (!classOfClusterA.equals(classOfClusterB)) {
					for (ClassCoupleSpoon couple : couplesOfClasses) {
						if (couple.getClassNameA().equals(classOfClusterA)
								&& couple.getClassNameB().equals(classOfClusterB)) {
							couplingValue += couple.getCouplageMetricValue();
						}
					}
				}
			}
		}
		return couplingValue;
	}
	
	public void createHierarchicalClustering(ArrayList<Cluster> clusters,
			ArrayList<ClassCoupleSpoon> couplesOfClasses) throws IOException {
		Cluster clusterA, clusterB, partieGauche, partieDroite, tempCluster;
		double CouplingMax, tempCouplingMax;
		int indexPartA, indexPartB;
		ArrayList<String> tempClasses;

		
		
		System.out.println("\nCréation de la hiérarchie des clusters :");

		while (clusters.size() > 1) {
			indexPartA = 0;
			indexPartB = 0;
			CouplingMax = 0;
			for (int i = 0; i < clusters.size(); i++) {
				
				clusterA = clusters.get(i);
				for (int j = 0; j < clusters.size(); j++) {
					
					clusterB = clusters.get(j);
					if (i != j) {
						
						tempCouplingMax = getScoreClusters(clusterA, clusterB, couplesOfClasses);
						if (tempCouplingMax > CouplingMax) {
							CouplingMax = tempCouplingMax;
							indexPartA = i;
							indexPartB = j;
						}
					}
				}
			}
			if (CouplingMax > 0) {
				partieGauche = clusters.get(indexPartA);
				partieDroite = clusters.get(indexPartB);
				
				
				tempClasses = new ArrayList<String>(partieGauche.getClassList());
				tempCluster = new Cluster(tempClasses, CouplingMax);
				tempCluster.add(partieDroite.getClassList());
				
				//Algo du cours : enlève, enlève, ajoute
				
				clusters.remove(partieGauche);
				clusters.remove(partieDroite);
				clusters.add(tempCluster);
				
				
				
				// En cas de modifications
				
				
				System.out.println("\nChangement : fusion entre clusters");
				System.out.println("Première partie de la fussion : ");
				partieGauche.getClassList().forEach(classe -> System.out.println("Classe :  " + classe.toString()));
				System.out.println("Deuxième partie de la fusion");
				partieDroite.getClassList().forEach(classe -> System.out.println("Classe :  " + classe.toString()));
				System.out.println(" Nouvelle valeurCouplage : " + CouplingMax + "\n");
			}
		}
		saveGraphAsPNG();
	}
	
	
	
	public String getGraphAsDot() {
		StringBuilder builder = new StringBuilder("digraph G {\n");
		Integer c = 1;
		for(Cluster cluster :  cluster) {
			String clusterName = "C"+c;
			//les feuilles
			if(cluster.size()==2) {
				builder.append('"'+ cluster.className.get(0) +'"').append(" -> ").append('"'+clusterName+'"').append(" ");
				builder.append('"'+cluster.className.get(1)+'"').append(" -> ").append('"'+clusterName+'"').append(" ");
			}
			else//On verifie si le cluster et sous ensemble d'un autre
				{
				Integer IndexPlusGrandSousEnsemble=-1;
				Integer GrandPlusGrandSousEnsemble=0;
				for (int i = 0; i < c-1; i++) {
					//cluster actuel
					Cluster array1 = new Cluster(cluster);
					//ancien cluster
					Cluster array2 = new Cluster(cluster.className.get(i));
				
					//si 2 est sous ensembles alors
					if(array1.className.containsAll(array2.className) && array2.size() > GrandPlusGrandSousEnsemble) {
						IndexPlusGrandSousEnsemble=i;
						}
				}
				if (IndexPlusGrandSousEnsemble>-1) {
					Cluster array1 = new Cluster(cluster);
					Cluster array2 = new Cluster(cluster.className.get(IndexPlusGrandSousEnsemble));
					array1.className.removeAll(array2.className);
					
					builder.append('"'+"C"+(IndexPlusGrandSousEnsemble+1)+'"').append(" -> ").append('"'+clusterName+'"').append(" ");
					for(String s : array1.className) {
						builder.append('"'+s+'"').append(" -> ").append('"'+clusterName+'"').append(" ");
					}
				}
			}
			c++;
		}		
		builder.append("\n}");
		return builder.toString();
	}
	
	public void saveGraphAsPNG() throws IOException {
		MutableGraph g = new Parser().read(this.getGraphAsDot());
		Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("dendrogram.png"));
	}

}
