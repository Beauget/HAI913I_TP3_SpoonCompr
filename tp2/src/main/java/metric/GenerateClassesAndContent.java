package metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import graphs.CallGraph;
import graphs.StaticCallGraph;
import loggers.FileLogger;
import loggers.LogRequest;
import loggers.StandardLogRequestLevel;
import models.ClassAndContent;
import models.Method;
import models.MethodInvocated;

public class GenerateClassesAndContent {
	private CallGraph callGraph;
	private Map<String, Map<String, Integer>> invocations = new HashMap<>();
	ArrayList<ClassAndContent> classes = new ArrayList<ClassAndContent>();
	
	public GenerateClassesAndContent(CallGraph callGraph2) {
		this.callGraph =callGraph2;
		this.invocations = callGraph2.getInvocations();
		this.classes = this.getModel();
	}
	public ArrayList<ClassAndContent> getClasses() {
		return classes;
	}
	/* * * * * * * * * * * *
	 * 
	 *  METHODS PUBLIC ADDED
	 *  
	 *  * * * * * * * * * */
	//verifie si la class est dans la liste, si c'est le cas renvois sont index
	private Integer classIsAlreadyIn(ArrayList<ClassAndContent> classes, String className) {
		Integer rslt = 0;
		Integer i = 0;
		for(ClassAndContent classAndContent : classes) {
			if(classAndContent.getName().equals(className)) {
				rslt = i;
			}
			i++;
		}
		return rslt;
	}
	
	//verifie si la class est dans la liste, si c'est le cas renvois sont index
	private boolean MethodIsAlreadyIn(ClassAndContent classe, String methodName) {
		boolean rslt = false;
		for(Method method: classe.getMethods()) {
			if(method.getName().equals(methodName))
				rslt=true;
			}		
		return rslt;
	}
	
	public ArrayList<ClassAndContent> getModel() {
		ArrayList<ClassAndContent> classes = new ArrayList<ClassAndContent>();
		for (String source: invocations.keySet()) {
			//ajout de la class. Format initial de source : elemStockage.AElemStock2::absoluteAddess
			String[] words = source.split("::");
			ClassAndContent classToAdd = new ClassAndContent();
			Method method = new Method() ;
			if(words.length==2) {
				//verifie si la classe n'existe pas deja, sinon la crée
				if(classIsAlreadyIn(classes,words[0])>0){
					classToAdd = classes.get(classIsAlreadyIn(classes,words[0]));
					//on supprime sont ancienne version
					classes.remove(classes.get(classIsAlreadyIn(classes,words[0])));
				}
				else
					classToAdd = new ClassAndContent(words[0]);
				method = new Method(words[1]);					
				for (String destination: invocations.get(source).keySet()) {
					MethodInvocated  methodInvocated = new MethodInvocated();
					//ajout de la method invoque. Format initial de la source elemStockage.AElemStock2::size
					String[] words2 = destination.split("::");
					if(words2.length==2) {
						methodInvocated = new MethodInvocated(words2[1],words2[0]);
						//recuperation du nombre d'invocation
						methodInvocated.setNumberOfTime(invocations.get(source).get(destination));
						}
					method.addMethodInvocation(methodInvocated);
					}
				classToAdd.addMethod(method);
					
				}
			if(classToAdd!=null)
				classes.add(classToAdd);
			}
		return classes;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(ClassAndContent cac : classes) {
			builder.append(cac.toString()+"\n");
		}
		return builder.toString();
	}
	

	
}
