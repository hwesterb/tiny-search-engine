package kth.id1020.project2.sorts;

import java.util.ArrayList;
import java.util.Comparator;

import kth.id1020.project2.index.EntityDocument;

public class Bubble {

	private Bubble() {}

	public static ArrayList<EntityDocument> sort(ArrayList<EntityDocument> eDocs, Comparator c, boolean reverse) {
		if (reverse) c = c.reversed();
		int R = eDocs.size()-1;
		boolean swapped = true;
		while (R>=0 && swapped==true) {
			swapped = false;
			for (int i = 0; i<R; i++) {
				swapped = true;
				swap(eDocs, i);
			}
			R = R-1;
		}
		return eDocs;
	}

	private static ArrayList<EntityDocument> swap(ArrayList<EntityDocument> eInfo, int i) {
		EntityDocument e = eInfo.get(i);
		EntityDocument e2 = eInfo.get(i+1);
		eInfo.set(i+1, e);
		eInfo.add(i, e2);
		return eInfo;
	}
}
