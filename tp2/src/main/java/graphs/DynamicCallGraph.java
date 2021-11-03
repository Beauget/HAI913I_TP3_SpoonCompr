package graphs;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import utility.Utility;
import visitors.ClassDeclarationsCollector;
import visitors.MethodDeclarationsCollector;
import visitors.MethodInvocationsCollector;
import models.ClassAndContent;
import models.ClassCouple;
import processors.ProcessorClustering;

public class DynamicCallGraph extends CallGraph  {

	public DynamicCallGraph(String projectPath) {
		super(projectPath);
		// TODO Auto-generated constructor stub
	}


}
