package kth.id1020.project2.query;

import java.util.ArrayList;
import java.util.List;

import kth.id1020.project2.index.CombinedEntity;
import kth.id1020.project2.index.EntityDocument;
import kth.id1020.project2.index.SymbolTable;
import kth.id1020.project2.sorts.Merge;
import se.kth.id1020.util.Document;
/**
 * Query class that handles queries. Parses them, sorts them,
 * looks in cache for a sequence of multiple words,
 * and sends back a list of sorted documents.
 * 
 * @author Henrik Westerberg
 */
public class QueryHandler {

	private String[] keys;
	private final Parser parser = new Parser();
	private SymbolTable ST;
	ParsingTree tree;

	public QueryHandler(SymbolTable ST) {
		this.ST = ST;
		this.tree = new ParsingTree(ST);
	}

	/**
	 * Searches for documents based on a query.
	 * @throws KeyNotFoundException if key is not found in the index.
	 * @throws InvalidQueryException if query is invalid.
	 */
	public List<Document> search(String query) throws KeyNotFoundException, InvalidQueryException {
		CombinedEntity ce = checkCache(query);
		if (ce!=null) { tree.setRootInfix(ce.getInfix()); return result(ce.getDocs()); } //quick precheck
		keys = parser.parse(query);
		ArrayList<EntityDocument> documents = null;
		if (keys.length>1) {
			ce = checkCache(parser.getPrefix());
			if (ce==null) documents = getSeveralDocuments(keys);
			else {
				documents = ce.getDocs();
				tree.setRootInfix(ce.getInfix());
			}
		}
		else {
			tree.setRootInfix(keys[0]);
			documents = ST.get(keys[0]).getDocs();
		}
		if (!documents.isEmpty()) { sort(documents); return result(documents); }
		return null;
	}
	
	private CombinedEntity checkCache(String key) {
		return ST.getCache().check(key);
	}
	/**
	 * Sorts based property and direction.
	 * @param eDocs the search result.
	 */
	private void sort(ArrayList<EntityDocument> eDocs) {
		boolean reverse = false;
		switch(parser.getDirection()) {
		case ASCENDING:
			reverse = false;
			break;
		case DESCENDING:
			reverse = true;
		}
		switch(parser.getProperty()) {
		case RELEVANCE:
			Merge.sort(eDocs, EntityDocument.BY_RELEVANCE, reverse);
			break;
		case POPULARITY:
			Merge.sort(eDocs, EntityDocument.BY_POPULARITY, reverse);
			break;
		case OCCURENCE:
			Merge.sort(eDocs, EntityDocument.BY_OCCURENCE, reverse);
			break;
		case NAME:
			Merge.sort(eDocs, EntityDocument.BY_NAME, reverse);
			break;
		}
	}

	private List<Document> result(ArrayList<EntityDocument> eDocs) {
		List<Document> result = new ArrayList<Document>();
		result.addAll(eDocs);
		return result;
	}

	private ArrayList<EntityDocument> getSeveralDocuments(String[] keys) {
		try {
			return tree.parse(keys);
		} catch (InvalidQueryException e) {
			System.out.println(e.getMessage());
			return new ArrayList<EntityDocument>(); //empty list
		}
	}


	public String getInfix() {
		return tree.getRootInfix();
	}
}


