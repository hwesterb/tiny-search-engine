package kth.id1020.project2.index;

import java.util.Comparator;

import kth.id1020.project2.util.Cons;
import se.kth.id1020.util.Document;
/**
 * An document with relevant attributes.
 * 
 * @author Henrik Westerberg
 */
public class EntityDocument extends Document {

	private Cons<Integer> occurences = new Cons<Integer>();
	public int occurence = Integer.MAX_VALUE;
	public double relevance;
	public int appearsInDocument;
	public int docLength;
	public static final Comparator<EntityDocument> BY_NAME = new ByName();
	public static final Comparator<EntityDocument> BY_POPULARITY = new ByPopularity();
	public static final Comparator<EntityDocument> BY_RELEVANCE = new ByRelevance();
	public static final Comparator<EntityDocument> BY_OCCURENCE = new ByOccurence();
	
	public EntityDocument(String name, int popularity, int docLength) {
		super(name, popularity);
		this.docLength = docLength;
		
	}
	
	
	/**
	 * Adds an occurence of a word to the document.
	 */
	public void add(int incOccurence) {
		if (incOccurence<occurence) occurence = incOccurence;
		occurences = occurences.prepend(incOccurence);
		appearsInDocument = occurences.length();
	}

	/**
	 * Compares by name, ascending.
	 * 
	 * @author Henrik Westerberg
	 */
	public static class ByName implements Comparator<EntityDocument> {
		@Override
		public int compare(EntityDocument eDoc1, EntityDocument eDoc2) {
			return eDoc1.compareTo(eDoc2);
		}
	}
	/**
	 * Compares by popularity, ascending.
	 * 
	 * @author Henrik Westerberg
	 */
	public static class ByPopularity implements  Comparator<EntityDocument> {
		@Override
		public int compare(EntityDocument eDoc1, EntityDocument eDoc2) {
			if(eDoc1.popularity>eDoc2.popularity) return 1;
			if(eDoc1.popularity<eDoc2.popularity) return -1;
			return 0;
		}
	}
	
	public void calcTFIDF(int appearsInAllDocs, int documents) {
		relevance = termFrequency()*inverseDocumentFrequency(appearsInAllDocs, documents);
	}
	
	private double termFrequency() {
		return ((double) appearsInDocument/(double) docLength);
	}
	
	public double inverseDocumentFrequency(int appearsInAllDocs, int documents) {
		return Math.log10((double) documents/ (double) appearsInAllDocs);
	}
	/**
	 * Compares by relevance, how many times a word exists in a document, ascending.
	 * 
	 * @author Henrik Westerberg
	 */
	public static class ByRelevance implements  Comparator<EntityDocument> {
		@Override
		public int compare(EntityDocument eDoc1, EntityDocument eDoc2) {		
			if (eDoc1.relevance>eDoc2.relevance) return 1;
			if (eDoc1.relevance<eDoc2.relevance) return -1;
			return 0;
		}
	}
	/**
	 * Compares by occurence, where in a document a word occurs, ascending.
	 * 
	 * @author Henrik Westerberg
	 */
	public static class ByOccurence implements Comparator<EntityDocument> {
		@Override
		public int compare(EntityDocument eDoc1, EntityDocument eDoc2) {
			if (eDoc1.occurence>eDoc2.occurence) return 1;
			if (eDoc1.occurence<eDoc2.occurence) return -1;
			return 0;
		}
	}

	@Override
	public String toString() {
		return "Document {" + this.name + ", relevance="
				+ relevance + ", occurence=" + occurence + ", popularity=" + popularity + "}";
	}

}
