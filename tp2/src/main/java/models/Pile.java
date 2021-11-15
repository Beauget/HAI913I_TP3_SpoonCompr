package models;
import dendrogramast.DendrogramComposit;
import java.util.ArrayList;

public class Pile {
	ArrayList<DendrogramComposit> pileDendrogram = new ArrayList<DendrogramComposit>();
	
	public void empile(DendrogramComposit dendogram) {
		pileDendrogram.add(dendogram);
	}
	
	public boolean isEmpty() {
		return pileDendrogram.isEmpty();
	}
	
	private DendrogramComposit getLastObject() {
		return pileDendrogram.get(pileDendrogram.size()-1);
	}
	
	public DendrogramComposit depile() {
		if(!isEmpty()) {
			DendrogramComposit output = getLastObject();
			pileDendrogram.remove(getLastObject());
			return output;
			}
		return null;
	}
}
