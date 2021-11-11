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
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtStatementListImpl;
;

public class Spoon extends ASTProcessor {
	
	private FileLogger loggerChain;
	
//Spoon pour l’identification de modules
	
	public Spoon(String path) {
		super(path);
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
	
public void callGraphWithSpoon(CtModel model, Launcher our) {

	StringBuilder builder = new StringBuilder();
	int nbMethod = 0;
	int nbRefs = 0;
	int nbClass = 0;
	
	
	ArrayList<String> listVu = new ArrayList<String>();
	for (CtType <?>s: model.getAllTypes()) {
		if(s.isClass()) {
			nbClass++;
		}
		  builder.append(s.getQualifiedName() + "::");
		  String actual = s.getQualifiedName();
	      for (CtMethod m: s.getMethods()) {
	    	  builder.append(m.getSignature() + "\n");
	    	  
	    	if(!isBusinessMethod(s.getQualifiedName()))
	    	return;
	    	nbMethod += s.getMethods().size();
	    	  
	        List<CtInvocation> refs = m.getElements(new TypeFilter < CtInvocation > (CtInvocation.class));
	        nbRefs += refs.size(); 
	        int nbE = 0;
	        String invName = "";
	       
	        for (CtInvocation inv: refs) {

	         if(listVu.contains(m.getSignature())) {
	        	 nbE +=1;
	         }
	         else {
	        	 listVu.add(m.getSignature());
	         }
	         
	         invName = inv.getTarget().getType().getTypeDeclaration().getQualifiedName();
	         

	         
	            
	            
	        }

	        builder.append("\t---> " + invName + "::" + m.getSignature() + " " +  "[" + nbE + " time(s)" + "]" + "\n");
     		builder.append("\n");
	        
	        	
       
	      }
	     
	      
	      
	}
	
	builder.append("Static Call Graph");
	builder.append("\nMethods: "+nbMethod+".");
    builder.append("\nInvocations: "+nbRefs+".");
    builder.append("\nClass: "+nbClass+".");
    builder.append("\n");

	
	our.setSourceOutputDirectory("/home/dnspc/Desktop/M2/Evo-restru/TP4ASTSpoon/HAI913I_TP3_SpoonCompr/SpoonOutput/");
    our.prettyprint();

    System.out.println(builder.toString());
   // return builder.toString();
}

public void log() {
	loggerChain.setFilePath("/home/dnspc/Desktop/M2/Evo-restru/TP4ASTSpoon/HAI913I_TP3_SpoonCompr/static-callgraph.info");
	loggerChain.log(new LogRequest(this.toString(), 
			StandardLogRequestLevel.DEBUG));
}


}