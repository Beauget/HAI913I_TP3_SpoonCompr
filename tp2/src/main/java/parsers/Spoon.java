package parsers;

import java.util.List;

import java.util.regex. * ;

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
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtTypeParameter;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtStatementListImpl;
;

public class Spoon {
	
	private FileLogger loggerChain;
	
//Spoon pour l’identification de modules
	
	public Spoon() {
		setLoggerChain();
	}
	
	protected void setLoggerChain() {
		loggerChain = new FileLogger(StandardLogRequestLevel.DEBUG);
		loggerChain.setNextLogger(new ConsoleLogger(StandardLogRequestLevel.INFO));
	}
	
	
	public static String removeLastChar(String s) {
	    if (s == null || s.length() == 0) {
	      return s;
	    }
	    return s.substring(0, s.length() - 1);
	  }
	
public static String analyzeWithSpoon(CtModel model, Launcher our) {

	StringBuilder builder = new StringBuilder();
	int nbMethod = 0;
	int nbRefs = 0;
	
	
	for (CtType <?>s: model.getAllTypes()) {
		  builder.append(s.getQualifiedName() + ":\n");
	      for (CtMethod m: s.getMethods()) {
	    	  nbMethod += s.getMethods().size();
	    	  
	        List< CtInvocation > refs = m.getElements(new TypeFilter < CtInvocation > (CtInvocation.class));
	        nbRefs += refs.size(); 
	        int nbE = refs.size();
	        
	        for (CtInvocation inv: refs) {
	      //    System.out.println(s.getSimpleName() + " : " + m.getSimpleName() + " -> " + inv.toString());
	          String invName = "";

	          	          
	          builder.append("\t---> " + m.getSimpleName() + 
						" (" + inv.toString() + nbE + " time(s))\n");
			builder.append("\n");
	   
	       //     System.out.println(m.getSimpleName() + ", " + invName);

	            CtCodeSnippetStatement expr = our.getFactory().Code().createCodeSnippetStatement("logs.LogProcessor.logAppel( \"" + m.getSimpleName() + "\", \" " + invName + "\");");
	            inv.getParent(CtBlock.class).insertBegin(expr);

	        }
	      }
	      
	      
	}
	
	builder.append("Static Call Graph");
	builder.append("\nMethods: "+nbMethod+".");
    builder.append("\nInvocations: "+nbRefs+".");
    builder.append("\n");

	
	our.setSourceOutputDirectory("/home/dnspc/Desktop/M2/Evo-restru/TP4ASTSpoon/HAI913I_TP3_SpoonCompr/SpoonOutput/");
    our.prettyprint();

    System.out.println(builder.toString());
    return builder.toString();
}

public void log() {
	loggerChain.setFilePath("/home/dnspc/Desktop/M2/Evo-restru/TP4ASTSpoon/HAI913I_TP3_SpoonCompr/static-callgraph.info");
	loggerChain.log(new LogRequest(this.toString(), 
			StandardLogRequestLevel.DEBUG));
}

}