package foyer;

import java.util.ArrayList;
import java.util.Iterator;

import foyer.Famille.Personne;

public class Main {
	  public static void main (String[] args){
		  ArrayList<String> prenom = new ArrayList<String>(), 
				  nom = new ArrayList<String>(),
				  roles = new ArrayList<String>(),
				  anniversaires = new ArrayList<String>();
		  ArrayList<Personne> personnes = new ArrayList<Personne>();
		  
		  prenom.add("Paul");
		  prenom.add("Pierre");
		  prenom.add("Marie");
		  prenom.add("Julie");
		  
		  nom.add("France");
		  nom.add("France");
		  nom.add("Paris");
		  nom.add("France");
		 
		  roles.add("papa");
		  roles.add("fils");
		  roles.add("maman");
		  roles.add("fille");
		  
		  anniversaires.add("20/03/1960");
		  anniversaires.add("09/10/1990");
		  anniversaires.add("24/01/1960");
		  anniversaires.add("09/10/2000");
		 
		  Famille f = new Famille();
		  for(int i=0 ; i< nom.size(); i++) {
			  Personne p = f.newPersonne(nom.get(i),prenom.get(i), anniversaires.get(i));
			  personnes.add(p);
		  }
		  
		  Famille famille = new Famille("idFoyer", personnes, roles);
		  
		  System.out.println(famille.toString());
	  }
}
