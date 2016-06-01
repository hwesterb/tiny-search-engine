package kth.id1020.project2.main;

import java.util.List;

import kth.id1020.project2.index.Entity;
import kth.id1020.project2.index.EntityDocument;
import kth.id1020.project2.index.SymbolTable;
import kth.id1020.project2.query.InvalidQueryException;
import kth.id1020.project2.query.KeyNotFoundException;
import kth.id1020.project2.query.Parser;
import kth.id1020.project2.query.ParsingTree;
import kth.id1020.project2.query.QueryHandler;
import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Sentence;
import se.kth.id1020.util.Word;

/**
 * Search engine which builds an index.
 * 
 * @author Henrik Westerberg
 */
public class TinySearchEngine implements TinySearchEngineBase {

	//	Webster's Third New International Dictionary,
	//	Unabridged, together with its 1993 Addenda Section,
	//	includes some 470,000 entries. The Oxford English Dictionary,
	//	Second Edition, reports that it includes a similar number.
	// 	Source: http://www.merriam-webster.com/help/faq/total_words.htm
	private SymbolTable ST = new SymbolTable(499979); //high prime number
	QueryHandler q;
	int counter = 0;

	@Override
	public List<Document> search(String query) {
		q = new QueryHandler(ST); //attaching the cache interface to the query
		try {
			return q.search(query);
		} catch (KeyNotFoundException | InvalidQueryException e) {
			System.out.println("Help-message: " + e.getMessage());
			return null;
		}
	}

	@Override
	public String infix(String infix) {
		return q.getInfix();
	}

	@Override
	public void insert(Sentence sent, Attributes attr) {
		for (Word word : sent.getWords()) {
			ST.put(word.word, attr);
		}
	}

	@Override
	public void postInserts() {
		ST.emptyQueue();
		System.out.println("Collisons: " + ST.collisions() + " Size: " + ST.size());
		System.out.println("Documents: " + ST.documents());
		for (Entity e : ST.values()) {
			int appearsIn = e.getDocs().size();
			for (EntityDocument eDoc : e.getDocs()) {
				eDoc.calcTFIDF(appearsIn, ST.documents());
			}
		}
	}

	@Override
	public void preInserts() {
		System.out.println("Welcome to search engine!");
	}

}
