package graphs;

import java.util.ArrayList;
import java.util.Collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.LongStream;

import loggers.ConsoleLogger;
import loggers.FileLogger;
import loggers.LogRequest;
import loggers.StandardLogRequestLevel;
import processors.ASTProcessor;
import models.ClassAndContent;
import models.Method;
import models.MethodInvocated;

public abstract class CallGraph extends ASTProcessor {
	/* ATTRIBUTES */
	private Set<String> methods = new HashSet<>();
	private Map<String, Map<String, Integer>> invocations = new HashMap<>();
	private FileLogger loggerChain;
	
	/* CONSTRUCTOR */
	public CallGraph(String projectPath) {
		super(projectPath);
		setLoggerChain();
	}
	
	/* METHODS */
	protected void setLoggerChain() {
		loggerChain = new FileLogger(StandardLogRequestLevel.DEBUG);
		loggerChain.setNextLogger(new ConsoleLogger(StandardLogRequestLevel.INFO));
	}
	
	public Set<String> getMethods() {
		return methods;
	}
	
	public long getNbMethods() {
		return methods.size();
	}
	
	public long getNbInvocations() {
		return invocations.keySet()
		.stream()
		.map(source -> invocations.get(source))
		.map(destination -> destination.values())
		.flatMap(Collection::stream)
		.flatMapToLong(value -> LongStream.of((long) value))
		.sum();
	}
	
	public Map<String, Map<String, Integer>> getInvocations() {
		return invocations;
	}
	
	public boolean addMethod(String method) {
		return methods.add(method);
	}
	
	public boolean addMethods(Set<String> methods) {
		return methods.addAll(methods);
	}
	
	public void addInvocation(String source, String destination) {
		
		if (invocations.containsKey(source)) {
			
			if (invocations.get(source).containsKey(destination)) {
				int numberOfArrows = invocations.get(source).get(destination);
				invocations.get(source).put(destination, numberOfArrows + 1);
			}
			
			else {
				methods.add(destination);
				invocations.get(source).put(destination, 1);
			}
		}
		
		else {
			methods.add(source);
			methods.add(destination);
			invocations.put(source, new HashMap<String, Integer>());
			invocations.get(source).put(destination, 1);
		}
	}
	
	public void addInvocation(String source, String destination, int occurrences) {
		methods.add(source);
		methods.add(destination);
		
		if (!invocations.containsKey(source))
			invocations.put(source, new HashMap<String, Integer>());
		
		invocations.get(source).put(destination, occurrences);
	}
	
	public void addInvocations(Map<String, Map<String, Integer>> map) {
		for (String source: map.keySet())
			for (String destination: map.get(source).keySet())
				this.addInvocation(source, destination, map.get(source).get(destination));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Static Call Graph");
		builder.append("\nMethods: "+methods.size()+".");
		builder.append("\nInvocations: "+getNbInvocations()+".");
		builder.append("\n");
		
		for (String source: invocations.keySet()) {
			builder.append(source + ":\n");
			
			for (String destination: invocations.get(source).keySet())
				builder.append("\t---> " + destination + 
						" (" + invocations.get(source).get(destination) + " time(s))\n");
			builder.append("\n");
		}
		System.out.println(this.GetModel());
		return builder.toString();
	}
	
	public String GetModel() {
		ArrayList<ClassAndContent> classes = new ArrayList<ClassAndContent>();
		/*
		 * /home/hayaat/Desktop/Master/M1/Java/TP4/src/
		 */
		StringBuilder builder = new StringBuilder();
		
		for (String source: invocations.keySet()) {
			//ajout de la class. Format initial de source : elemStockage.AElemStock2::absoluteAddess
			String[] words = source.split("::");
			ClassAndContent classToAdd = new ClassAndContent();
			Method method = new Method() ;
			if(words.length==2) {
			classToAdd = new ClassAndContent(words[0]);
			method = new Method(words[1]);
			//classToAdd.addMethod(words[1]);
			}			
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
				classToAdd.addMethod(method);
			}
			classes.add(classToAdd);
		}
		return classes.toString();
	}
	
	public void print() {
		loggerChain.log(new LogRequest(this.toString(), 
				StandardLogRequestLevel.INFO));
	}
	
	public void log() {
		loggerChain.setFilePath(parser.getProjectPath()+"static-callgraph.info");
		loggerChain.log(new LogRequest(this.toString(), 
				StandardLogRequestLevel.DEBUG));
	}
}
