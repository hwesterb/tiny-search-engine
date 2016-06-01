package kth.id1020.project2.index;

import java.util.ArrayList;

import edu.princeton.cs.algorithms.Queue;
import kth.id1020.project2.query.KeyNotFoundException;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;

/**
 * An index which mangages storing of data.
 * 
 * @author Henrik Westerberg
 */
public class SymbolTable {

	private Entity[] index;
	Cache cache = new Cache(9973); 
	private int next = 0;
	private int M, N;
	private int collisions = 0;
	private int documents = 0;
	private String lastDocName = "";
	private Queue<KeyValue> kv = new Queue<KeyValue>();

	public SymbolTable(int M) {
		this.M = M;
		this.index = new Entity[M];
	}

	/**
	 * Puts value into symbol table.
	 */
	public void put(String key, Attributes value) {
		if (!newDocument(value.document.name)) {
			kv.enqueue(new KeyValue(key, value));			
		}	
		else {
			emptyQueue();
			kv.enqueue(new KeyValue(key, value));
		}
	}
	
	public void emptyQueue() {
		int docLength = kv.size();
		while (!kv.isEmpty()) {
			KeyValue k = kv.dequeue();
			add(k.key);
			add(k.attr, docLength);
		}
	}

	/**
	 * Gets an entity from the symbol table.
	 */
	public Entity get(String key) throws KeyNotFoundException {
		int i = find(key);
		if (i<0) throw new KeyNotFoundException(key);
		return index[i];
	}

	public boolean isEmpty() {
		return (N==0);
	}
	
	public int size() {
		return N;
	}
	
	public int collisions() {
		return collisions;
	}
	
	public int documents() {
		return documents;
	}

	private int hash(String key) {
		return (key.hashCode() & 0x7fffffff) % M;
	}
	
	private int find(String key) {
		int i = hash(key);
		while(true) {
			if (index[i]==null)  { next = i; return -1; }
			else if (index[i].word.contentEquals(key)) { next = i; return i; }
			collisions++;
			if (i<M-1) i++;
			else i = 0;
		}
	}

	/**
	 * Adds a word to the index. Uses binary search to accomplish this.
	 */
	private void add(String key) {
		Entity e = new Entity(key);
		int i = find(key);
		if (i<0) { index[next] = e; N++; }
	}
	
    public Iterable<Entity> values() {
        Queue<Entity> queue = new Queue<Entity>();
        for (int i = 0; i < M; i++)
            if (index[i] != null) queue.enqueue(index[i]);
        return queue;
    }

	/**
	 * Adds an attribute to the index. Documents won't be duplicated inside
	 * a word since documents are read one by one.
	 */
	private void add(Attributes attr, int docLength) {
		Entity e = index[next];
		ArrayList<EntityDocument> eDocs = e.getDocs();
		if (eDocs.isEmpty()) {
			eDocs.add(new EntityDocument(attr.document.name, attr.document.popularity, docLength));
			e.addOccurence(0, attr.occurrence);
		}
		else {
			int last = eDocs.size()-1;
			Document lastDoc = eDocs.get(eDocs.size()-1);
			if (lastDoc.equals(attr.document)) { 
				e.addOccurence(last, attr.occurrence);
			}
			else {
				eDocs.add(new EntityDocument(attr.document.name, attr.document.popularity, docLength));
				e.addOccurence(last+1, attr.occurrence);
			}
		}	
	}
	
	private boolean newDocument(String name) {
		if (!lastDocName.contentEquals(name)) {
			documents++;
			lastDocName = name;
			return true;
		}
		return false;
	}

	public Cache getCache() {
		return cache;
	}


}
