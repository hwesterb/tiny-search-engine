package kth.id1020.project2.index;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.kth.id1020.util.Attributes;

/**
 * Cache class based on hash table. Keeping most relevant keys in cache
 * by overwriting keys with same hashCode.
 * 
 * @author Henrik
 *
 */
public class Cache {

	private int M;
	private final CombinedEntity[] a;
	
	public Cache(int M) {
		this.M = M;
		 a = new CombinedEntity[M];
	}
	
	/**
	 * 
	 * @param key the prefix name.
	 * @param value a combined entity.
	 */
	public void put(String key, CombinedEntity value) {
		int index = hash(key);
		CombinedEntity ce = a[index];
		if (ce==null) a[index] = value;
		else {
			System.out.println("Overwriting \"" + ce.getPrefix() + "\" in cache.");
			a[index] = value;
		}
	}
	
	public CombinedEntity check(String key) {
		int index = hash(key);
		if (a[index]==null) return null;
		if (a[index].getPrefix().contentEquals(key)) {
			System.out.println("Successful hit in cache.");
			return a[index];
		}
		return null;
	}
	
	private int hash(String key) {
		return (key.hashCode() & 0x7fffffff) % M;
	}

}
