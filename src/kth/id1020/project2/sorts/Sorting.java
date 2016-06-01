package kth.id1020.project2.sorts;

import java.util.ArrayList;
import java.util.Comparator;

import kth.id1020.project2.index.EntityDocument;

public class Sorting {

	private Sorting() {}

	private static boolean isSorted(ArrayList<EntityDocument> eDocs, Comparator<EntityDocument> c, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(c, eDocs.get(i), eDocs.get(i-1))) return false;
		return true;
	}

	private static boolean less(Comparator<EntityDocument> c, EntityDocument v, EntityDocument w) {
		return (c.compare(v, w) < 0);
	}

	public static boolean isSorted(ArrayList<EntityDocument> eDocs, Comparator<EntityDocument> c) {
		return isSorted(eDocs, c, 0, eDocs.size() - 1);
	}
}