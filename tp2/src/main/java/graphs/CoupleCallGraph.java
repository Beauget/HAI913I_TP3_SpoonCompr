package graphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import loggers.FileLogger;

public class CoupleCallGraph extends StaticCallGraph {
	/* ATTRIBUTES */
	private Set<String> methods = new HashSet<>();
	private Map<String, Map<String, Integer>> invocations = new HashMap<>();
	private FileLogger loggerChain;
	
	/* CONSTRUCTOR */
	public CoupleCallGraph(String projectPath) {
		super(projectPath);
		setLoggerChain();
	}
}
