package parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import processors.ASTProcessor;
import java.util.regex. * ;
import processors.Processor;
import loggers.ConsoleLogger;
import loggers.FileLogger;
import loggers.LogRequest;
import loggers.StandardLogRequestLevel;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.compiler.Environment;
import spoon.processing.AbstractProcessor;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtTargetedExpression;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtTypeParameter;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtStatementListImpl;
;

public class Spoon extends ASTProcessor {
	
	private FileLogger loggerChain;
	
	ArrayList<CtMethod> methodList = new ArrayList<CtMethod>();
	ArrayList<CtType> typeList = new ArrayList<CtType>();
	ArrayList<CtInvocation> invocationList = new ArrayList<CtInvocation>();
	CtModel model;
	int nbMethod = 0;
	int nbClass = 0;
	long nbAppels = 0;
	
	public Spoon(String path, CtModel model) {
		super(path);
		this.model = model;
		setLoggerChain();
	}
	
	protected void setLoggerChain() {
		loggerChain = new FileLogger(StandardLogRequestLevel.DEBUG);
		loggerChain.setNextLogger(new ConsoleLogger(StandardLogRequestLevel.INFO));
	}

	public boolean isBusinessMethod(String invokedMethodSignature) {
		String declaringTypeFQN = invokedMethodSignature.split("::")[0];
		int indexOfTypeDotInFQN = declaringTypeFQN.lastIndexOf(".");
		String containingPackageFQN = declaringTypeFQN.substring(0, indexOfTypeDotInFQN);
		return new File(
				parser.getProjectSrcPath(), 
				containingPackageFQN.replace(".", File.separator)
				).exists();
	}
	

public long getDataWithSpoon(CtModel model) {
	
	
	
	long result = 0;

	for (CtType<?> type : model.getAllTypes()) { 
		if(type.isClass()) {
			nbClass++;
			typeList.add(type);
		}
		for (CtMethod<?> method : type.getAllMethods()) { 
			if(!methodList.contains(method)) {
				methodList.add(method);
				nbMethod += 1;
			}
			for (CtInvocation<?> methodInvocation : Query.getElements(method,
					new TypeFilter<CtInvocation<?>>(CtInvocation.class))) { 
				if (methodInvocation.getTarget().getType() != null) {
					
					if ((!methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName().equals("void"))
							&& (!methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName().contains("java.io."))
							&&  (!methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName().contains("java.util."))
							 && (!methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName().contains("java.lang"))
							) {
						        	
						         
						invocationList.add(methodInvocation);
						System.out.println(methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName() + "::" + method.getSignature());
						result++;
					}
				}
			}
		}
	
	}
	
	nbAppels = result;
	return result;
	
}

public String toString() {
	StringBuilder builder = new StringBuilder();
	ArrayList<String> listVu = new ArrayList<String>();
	
	String invocName = "";
	
	builder.append("Static Call Graph");
	builder.append("\nMethods: "+nbMethod+".");
    builder.append("\nInvocations: "+nbAppels+".");
    builder.append("\nClass: "+nbClass+".");
    builder.append("\n");
    
    for (CtType<?> type : model.getAllTypes()) { 
		if(type.isClass()) {
			nbClass++;
			typeList.add(type);
			
		}
		
		System.out.println(type.getQualifiedName());
		builder.append(type.getQualifiedName() + ":" + "\n");
		for (CtMethod<?> method : type.getAllMethods()) { 
			if(!methodList.contains(method)) {
				methodList.add(method);
				nbMethod += 1;
			}
			
			
			int nbE = 0;
			for (CtInvocation<?> methodInvocation : Query.getElements(method,
					new TypeFilter<CtInvocation<?>>(CtInvocation.class))) { 
				if (methodInvocation.getTarget().getType() != null) {
					
					if ((!methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName().equals("void"))
							&& (!methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName().contains("java.io."))
							&&  (!methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName().contains("java.util."))
							&& (!methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName().contains("java.lang"))
							) {
						
						 if(listVu.contains(methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName())) {
		    	        	 nbE +=1;
		    	         }
		    	         else {
		    	        	 listVu.add(methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName());
		    	        	 nbE = 1;
		    	         }
						        	
						invocName = methodInvocation.getTarget().getType().getTypeDeclaration().getQualifiedName();
						invocationList.add(methodInvocation);
						System.out.println(invocName);
					}
					
				}
				
				builder.append("\t---> " + invocName  + "::" + method.getSignature() + " " +  "[" + nbE + " time(s)" + "]" + "\n");
	 	     	builder.append("\n");
			}
			

			
		}
    }
    System.out.println(builder.toString());
	
    return builder.toString();
}



public void log() {
	loggerChain.setFilePath("C:\\Users\\beaug\\Desktop\\M2\\M2\\Evo-restru\\TP3\\HAI913I_TP3_SpoonCompr\\design_patterns\\static-callgraphSpoon.info");
	loggerChain.log(new LogRequest(this.toString(), 
			StandardLogRequestLevel.DEBUG));
}


}