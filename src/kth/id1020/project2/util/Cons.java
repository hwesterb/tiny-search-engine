package kth.id1020.project2.util;

import java.util.Iterator;



/**
 * Collects expressions and performs actions on them.
 * @author Henrik Westerberg
 * @param <Item> An item of generic type.
 */
public class Cons<Item> implements Iterable<Item> {

	private final Item e1;
	private final Cons<Item> e2;
	private int length=0;

	/**
	 * A linked list that can collect items.
	 * @param item Item to be stored. Specify item type.
	 * @param cons Next cons.
	 * @throws NullObjectException 
	 */
	private Cons(Item item, Cons<Item> cons) {
		if (cons!=null) length = cons.length() + 1;
		else length = 1;
		this.e1 = item;
		this.e2 = cons;
	}
	/**
	 * Creates empty list.
	 */
	public Cons() {
		e1 = null;
		e2 = null;
	}
	/**
	 * @return First element of list.
	 */
	public Item first() { return e1; }
	/**
	 * @return The second link of the chain.
	 */
	public Cons<Item> rest() { return e2; }
	/**
	 * @param item Adds new first link.
	 * @return An item of generic type.
	 * @throws NullObjectException 
	 */
	public Cons<Item> prepend(Item item) {
		return new Cons<Item>(item, this);
	}
	/**
	 * @return the length of the chain.
	 */
	public int length() {
		return length;
	}
	/**
	 * @param index Index of item to get.
	 * @return Item at index.
	 * @throws ListEmptyException If list is empty.
	 */
	public Item get(int index) {
		if (index<0) throw new IndexOutOfBoundsException();
		Cons<Item> e = this;
		for (int i = 0; i<index; i++) {
			e = e.rest();
		}
		if (e.first()==null) throw new IndexOutOfBoundsException();
		return e.first();
	}
	/**
	 * @return true if list is empty.
	 */
	public boolean isEmpty() {
		return (e1==null);
	}

	@Override
	public Iterator<Item> iterator() {
		return new ListIterator(this);
	}


	private class ListIterator implements Iterator<Item>{
		Cons<Item> e;

		private ListIterator(Cons<Item> e) {
			this.e = e;
		}

		@Override
		public boolean hasNext() {
			return e.rest()!=null;
		}
		@Override
		public Item next() {
			Item item = e.first();
			e = e.rest();
			return item;
		}
	}

	/**
	 * Returns string in form (cons e1 (cons e2 ( ... nil)))
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Item item : this) {
			if (item!=null) {
				sb.append("(");
				sb.append(item.toString());
			}
			else sb.append(" nil");
		}
		int i = 0;
		while (i<length) {
			sb.append(")");
			i++;
		}
		return sb.toString();
	}



}

