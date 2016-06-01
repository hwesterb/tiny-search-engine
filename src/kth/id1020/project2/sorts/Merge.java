package kth.id1020.project2.sorts;

import java.util.ArrayList;
import java.util.Comparator;

import kth.id1020.project2.index.EntityDocument;

/**
 * Modified version of http://algs4.cs.princeton.edu/22mergesort/Merge.java.html.
 * 
 * @author Henrik Westerberg
 */

public class Merge {
	private static EntityDocument[] aux; // auxiliary array for merges
	private static Comparator<EntityDocument> c;
	private static int CUTOFF = 4;

	private Merge() {}

	public static void sort(ArrayList<EntityDocument> eDocs, Comparator<EntityDocument> comparator,  boolean reverse)
	{
		if (reverse) c = comparator.reversed();
		else c = comparator;
		//if (Sorting.isSorted(eDocs, c)) return; // optimized for same queries, linear time.
		aux = new EntityDocument[eDocs.size()];
		sort(eDocs, 0, eDocs.size() - 1);
	}

	private static void sort(ArrayList<EntityDocument> eDocs, int lo, int hi)
	{ 
		if (hi <= lo + CUTOFF - 1) Insertion.sort(eDocs, c, lo, hi); 
		if (hi <= lo) return;
		int mid = lo + (hi - lo)/2;
		sort(eDocs, lo, mid); 
		sort(eDocs, mid+1, hi); 
		merge(eDocs, lo, mid, hi);
	}

	public static void merge(ArrayList<EntityDocument> eDocs, int lo, int mid, int hi)
	{
		int i = lo, j = mid+1;
		for (int k = lo; k <= hi; k++) { 
			aux[k] =  eDocs.get(k);
		}
		for (int k = lo; k <= hi; k++)
		{
			if (i > mid) 							eDocs.set(k, aux[j++]);
			else if (j > hi ) 						eDocs.set(k, aux[i++]);
			else if (less(c, aux[j], aux[i])) 				eDocs.set(k, aux[j++]);
			else 								eDocs.set(k, aux[i++]); 
		}
	}

	private static boolean less(Comparator<EntityDocument> c, EntityDocument v, EntityDocument w) {
		return (c.compare(v, w) < 0);
	}


}

