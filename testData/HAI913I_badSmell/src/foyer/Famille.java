package foyer;

import java.util.ArrayList;

public class Famille {

	public String idFoyer;	
	public ArrayList<String> roleListe = new ArrayList<String>();
	public ArrayList<Personne> personnes = new ArrayList<Personne>();
	
	public Famille(String id, ArrayList<Personne> p, ArrayList<String> role){
		this.idFoyer = id;
		this.personnes = p;
		this.roleListe = role;
		}
	public Famille() {}
	
	
	public Personne newPersonne(String n, String p, String a) {
		Personne personne = new Personne(n,p,a);
		return personne;
	}
	
	public class Personne {
		String nom;
		String prenom;
		String anniversaire;
		
		public Personne(String n , String p,String a) {
			this.nom = n;
			this.prenom = p;
			this.anniversaire = a;
		}
		public String getNom() {return nom;}
		public String getPrenom() {return prenom;}
		public String getAnniversaire() {return anniversaire;}	
	}
	
	public ArrayList<Personne> getPersonnes(){
		return this.personnes;
	}
	
	public String toString() {
		String rslt= null;
		for (int i=0 ; i<personnes.size();i++) {
			rslt+="Nom : " + personnes.get(i).getNom()+"\n";
			rslt+="Prenom : " +  personnes.get(i).getPrenom()+"\n";
			rslt+="Role : " + roleListe.get(i)+"/n";
			rslt+="Anniversaire : " + personnes.get(i).getAnniversaire().toString()+"\n";
			rslt+="idFoyer : " + idFoyer+"\n \n";
		}
		return rslt;

		
	}
	
}
