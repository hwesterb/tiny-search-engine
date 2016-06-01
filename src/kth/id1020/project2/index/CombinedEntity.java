package kth.id1020.project2.index;

import java.util.ArrayList;
import java.util.Arrays;

import kth.id1020.project2.sorts.Merge;
import edu.princeton.cs.algs4.Insertion;
/**
 * A union of unique words/entitys. 
 * 
 * @author Henrik Westerberg
 */
public class CombinedEntity {

	private ArrayList<EntityDocument> eDocs = new ArrayList<EntityDocument>();
	private String[] keys = new String[0];
	private final String prefix;
	private final String infix;
	
	public CombinedEntity(String infix, String prefix, ArrayList<EntityDocument> eDocs) {
		this.infix = infix;
		this.prefix = prefix;
		copyReferences(eDocs);
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getInfix() {
		return infix;
	}

	/**
	 * Adds key to key register.
	 */
	public void addKey(String key){
		keys = Arrays.copyOf(keys, keys.length+1);
		keys[keys.length-1] = key;
	}

	public ArrayList<EntityDocument> getDocs() {
		return eDocs;
	}
	
	/**
	 * Adds the first word and it's associated documents.
	 */
	private void copyReferences(ArrayList<EntityDocument> documents) {
		if (documents.isEmpty()) return;
		for (EntityDocument eDoc : documents) {
			EntityDocument newDoc = new EntityDocument(eDoc.name, eDoc.popularity, eDoc.docLength);
			newDoc.occurence = eDoc.occurence;
			newDoc.relevance = eDoc.relevance;
			eDocs.add(newDoc);
		}
		Merge.sort(eDocs, EntityDocument.BY_NAME, false);
	}
	
	/**
	 * Intersection operation. Use "+" in query language.
	 */
	public void intersection(ArrayList<EntityDocument> incDocs) {
		ArrayList<EntityDocument> aux = new ArrayList<EntityDocument>();
		Merge.sort(incDocs, EntityDocument.BY_NAME, false);
		int i = 0;
		int j = 0;	
		while (i<eDocs.size() && j<incDocs.size()) {
			if (incDocs.get(j).compareTo(eDocs.get(i))>0) {
				i++;
			}
			else if (incDocs.get(j).compareTo(eDocs.get(i))<0) {
				j++;
			}
			else if (incDocs.get(j).compareTo(eDocs.get(i))==0) {
				aux.add(makeNewCombinedDocument(incDocs.get(j), eDocs.get(i)));
				i++;
				j++;
			}
		}
		eDocs.clear();
		eDocs.addAll(aux);
	}
	
	/**
	 * Union operation. Use "|" in query language.
	 */
	public void union(ArrayList<EntityDocument> incDocs) {
		Merge.sort(incDocs, EntityDocument.BY_NAME, false);
		int i = 0;
		int j = 0;	
		while (i<eDocs.size() && j<incDocs.size()) {
			if (incDocs.get(j).compareTo(eDocs.get(i))>0) {
				i++;
			}
			else if (incDocs.get(j).compareTo(eDocs.get(i))<0) {
				eDocs.add(i, makeNewDocument(incDocs.get(j)));
				j++;
			}
			else if (incDocs.get(j).compareTo(eDocs.get(i))==0) {
				addStats(i, incDocs.get(j));
				j++;
			}
		}
		while (incDocs.size()>j) {
			int lastIndex = eDocs.size()-1;
			if (lastIndex<0) lastIndex = 0;
			eDocs.add(lastIndex, makeNewDocument(incDocs.get(j)));
			j++;
		}
	}

	
	/**
	 * Difference operation. Use "-" in query language.
	 */
	public void difference(ArrayList<EntityDocument> incDocs) {
		ArrayList<EntityDocument> aux = new ArrayList<EntityDocument>();
		Merge.sort(incDocs, EntityDocument.BY_NAME, false);
		int i = 0;
		int j = 0;	
		while (i<eDocs.size() && j<incDocs.size()) {
			if (incDocs.get(j).compareTo(eDocs.get(i))>0) {
				aux.add(eDocs.get(i));
				i++;
			}
			else if (incDocs.get(j).compareTo(eDocs.get(i))<0) {
				j++;
			}
			else if (incDocs.get(j).compareTo(eDocs.get(i))==0) {
				i++;
				j++;
			}
		}
		while (eDocs.size()>i) {
			aux.add(eDocs.get(i));
			i++;
		}
		eDocs.clear();
		eDocs.addAll(aux);
	}
	
	
	private EntityDocument makeNewDocument(EntityDocument eDoc) {
		EntityDocument newDoc = new EntityDocument(eDoc.name, eDoc.popularity, eDoc.docLength);
		newDoc.occurence = eDoc.occurence;
		newDoc.relevance = eDoc.relevance;
		return eDoc;
	}

	private void addStats(int i, EntityDocument eDoc) {
		eDocs.get(i).relevance += eDoc.relevance;
		if (eDocs.get(i).occurence>eDoc.occurence) eDocs.get(i).occurence = eDoc.occurence;
	}
	
	private EntityDocument makeNewCombinedDocument(EntityDocument eDoc1, EntityDocument eDoc2) {
		EntityDocument newDoc = new EntityDocument(eDoc1.name, eDoc1.popularity, eDoc1.docLength);
		newDoc.occurence = eDoc1.occurence + eDoc2.occurence;
		newDoc.relevance = eDoc1.relevance + eDoc2.relevance;
		return newDoc;
	}
	
	/**
	 * Checks if string array is same as string array stored in CombinedEntity.
	 * Used by cache.
	 */
	public boolean checkIfSame(String[] k) {
		if (k.length != keys.length) return false;
		Insertion.sort(k);
		Insertion.sort(keys);
		int same = 0;
		for (int i=0; i<keys.length; i++){
			if (k[i].contentEquals(keys[i])) same++;
		}
		if (same==keys.length) {System.out.println("Help-message: Used cache successfully."); return true; }
		else return false;
	}

}
