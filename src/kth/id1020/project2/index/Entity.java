package kth.id1020.project2.index;

import java.util.ArrayList;
/**
 * A single unique word in the index.
 * 
 * @author Henrik Westerberg
 */
public class Entity {

	public final String word;
	private ArrayList<EntityDocument> eDocs = new ArrayList<EntityDocument>();
	
	/**
	 * Constructs an entity.
	 */
	public Entity(String word) {
		this.word = word;
	}
	/**
	 * A list of documents.
	 * @return A list of all the documents where this word occurs.
	 */
	public ArrayList<EntityDocument> getDocs() {
		return eDocs;
	}

	public void addOccurence(int correspondingDocument, int occurence) {
		eDocs.get(correspondingDocument).add(occurence);
	}
	
	
}
