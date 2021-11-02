package processors;

import java.util.ArrayList;

import graphs.DynamicCallGraph;
import models.ClassAndContent;
import models.ClassCouple;

public class ProcessorClustering {
	/*
	 * /home/hayaat/Desktop/Master/M1/Java/TP4/src/
	 */
	static ArrayList<ClassAndContent> classes = new ArrayList<ClassAndContent>();
	static ArrayList<ClassCouple> couples = new ArrayList<ClassCouple>();
	
	public ProcessorClustering(ArrayList<ClassAndContent> classes) {
		this.classes = classes;
		this.couples = this.getCouples();
	}
	
	private static ArrayList<ClassCouple> getCouples() {
		ArrayList<ClassCouple> couplesList= new ArrayList<ClassCouple>();
		for(int i=0 ; i<classes.size(); i++) {
			for(int j = i+1; j<classes.size(); j++) {
				couplesList.add(new ClassCouple(classes.get(i),classes.get(j)));
			}
		}
		return  couplesList;
	}
	
	public Integer getNumberOfClasses() {return classes.size();}
	
	/* Renvoie les combinaison de classes initial du header pour le clustering*/
	private ArrayList<ArrayList<String>> initHeader(){
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
		for( ClassCouple classCouple: couples) {
			output.add(classCouple.getCoupleNames());
		}
		return output;		
	}
	
	/* Renvoie les valeur initial du header pour le clustering*/
	private ArrayList<Double> initHeaderValues(){
		ArrayList<Double> output = new ArrayList<Double>();
		for( ClassCouple classCouple: couples) {
			output.add(classCouple.getAvgOfCalls());
		}
		return output;		
	}
	/*Renvoie l'index du cluster avec la plus grande valeur*/
	private Integer getIndexOfHighestValueFromHeaderValues(ArrayList<Double> headerValues) {
		Integer output = 0;
		double maxValue =  headerValues.get(0);
		for (int i = 1; i < headerValues.size(); i++) {
			if(headerValues.get(i)>maxValue) {
				output=i;
				maxValue = headerValues.get(i);
			}
		}
		return output;
	}
	
	private boolean partOfClusterIsInColumnHeader(ArrayList<String> columnHeader, ArrayList<String> cluster) {
		for(String s : cluster) {
			if(columnHeader.contains(s))
				return true;
		}
		return false;
	}
	
	private ArrayList<ArrayList<String>> updateHeader( ArrayList<ArrayList<String>>header,  ArrayList<String> cluster){
		for(ArrayList<String> headerColumn : header) {
			/*seulement une partie du cluster est present et le but est de tout avoir
			 * exemple :  headerColumn(a, b, c) , cluster (b, f) 
			 * ce qu'on veut : headerColumn(a, b, c, f)
			 * Pour eviter le doublon on enleve toute pesence et on ajoute le cluster
			 * */
			if(partOfClusterIsInColumnHeader(headerColumn, cluster)) {
				headerColumn.removeAll(cluster);
				headerColumn.addAll(cluster);}
			}
		/*Attention ici on a probablement des headerColumn identique donc il faudra les enlever
		 * exemple :  headerColumn(a, f,c) , cluster (b, f) => headerColumn(a, b, c, f) il est égale a celui au dessus
		 * */
		
		for (int i = 0; i < header.size(); i++) {
			for (int j = i+1; j <header.size(); j++) {
				/*il est normalement impossible d'avoir une liste sous ensemble de l'autre , elle est egal ou differente*/
				if(header.get(i).containsAll(header.get(j)))
					header.remove(j);
			}
		}
		return header;
	}
	
	/*Renvoie la valeur du couple , si il n'existe pas (ce qui est techniquement impossible) envoie -1*/
	private double getValueInCoupleFromClassNames(String name1, String name2) {
		for(ClassCouple classCouple : couples) {
			if(classCouple.isSameCouple(name1, name2))
				return classCouple.getAvgOfCalls();
		}
		return -1;
	}
	/*Envoie la somme des valeurs de toute les combinaisons de couples possible*/
	private double getValueFromClassNames(ArrayList<String> classesNames) {
		double output = 0;
		
		for(int i=0; i<classesNames.size(); i++) {
			for (int j = i+1; j <classesNames.size(); j++) {
				String name1= classesNames.get(i);
				String name2= classesNames.get(j);
				double value= getValueInCoupleFromClassNames(name1,name2);
				if(value!=-1)
					output+=value;
			}
		}		
		return output;
	}
	
	/*Met a jour les valeurs de chaque cluster*/
	private ArrayList<Double> updateValuesOfHeader(ArrayList<ArrayList<String>> header){
		ArrayList<Double> output = new ArrayList<Double>();
		for(ArrayList<String> headerColumn : header) {
			output.add(getValueFromClassNames(headerColumn));
		}
		return output;
	}
	
	
	public ArrayList<ArrayList<String>> clustering() {
		/* exemple d'un tableau
		 * Initial :
		 * header : classe1/classe2    classe1/classe3   classe1/classe4   classe2/classe3   classe2/classe4   classe3/classe4
		 * A chaque header on a une value qui est initialement le calcule pondéré des couple de l'exo 1
		 * 
		 * On suppose que classe2/classe3 à la valeur la plus grande, on fait le cluster dessus ce qui renvoie:
		 * header : classe1/classe2/class3   classe1/classe4      classe2/classe3/classe4  (toute presence de classe2 ou classe3 "fusionne")
		 * 
		 * On doit alors re calculer les valeur suivant les nouveau groupes 
		 * On utilisera alors : getValueFromClassNames(ArrayList<String> classesNames)
		 * */

		
		//INITIALISATION, les deux doivent etre de la meme taille
		ArrayList<ArrayList<String>> tableHeader = initHeader();
		ArrayList<Double> valuesOfHeader  = initHeaderValues();		
		ArrayList<ArrayList<String>> clusters  = new ArrayList<ArrayList<String>>();

		
		//On continue tant que la fusion n'a pas pas etais complete et on verifie que les tableaux on toujours la meme taille
		while(tableHeader.size()!=1 && valuesOfHeader.size()==tableHeader.size()) {
			Integer indexOfHighestValue=getIndexOfHighestValueFromHeaderValues(valuesOfHeader);
			ArrayList<String> highestCluster = tableHeader.get(indexOfHighestValue);
			//On ajoute le cluster dans la liste des cluster
			clusters.add(highestCluster);
			tableHeader = updateHeader(tableHeader, highestCluster);
			valuesOfHeader = updateValuesOfHeader(tableHeader);
		}
		return clusters;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Clusters = \n");
		ArrayList<ArrayList<String>>  clusters = clustering();
		
		for (ArrayList<String> cluster : clusters) {
			builder.append(cluster.toString()+ "\n");
		}	
		return builder.toString();
	}
	

}
