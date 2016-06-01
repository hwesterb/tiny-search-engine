package kth.id1020.project2.sorts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kth.id1020.project2.index.EntityDocument;


/**
 * Insertion sort.
 * 
 * @author Henrik Westerberg
 */

public class Insertion {

	private Insertion() { }

	public static void sort(List<EntityDocument> eDocs, Comparator<EntityDocument> c, int lo, int hi) {
		for (int i = lo; i <= hi; i++) {
			for (int j = i; j > 0 && less(c, eDocs.get(j), eDocs.get(j-1)); j--) {
				exch(eDocs, j, j-1);
			}
		}
	}

	
	public static void sort(List<EntityDocument> eDocs, Comparator c) {
		int N = eDocs.size();
		for (int i = 0; i < N; i++) {
			for (int j = i; j > 0 && less(c, eDocs.get(j), eDocs.get(j-1)); j--) {
				exch(eDocs, j, j-1);
			}
		}
	}

	private static boolean less(Comparator c, Object v, Object w) {
		return (c.compare(v, w) < 0);
	}


	private static void exch(List<EntityDocument> small, int i, int j) {
		Collections.swap(small, i, j);
	}



}
