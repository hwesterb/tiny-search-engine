package kth.id1020.project2.query;

import java.util.ArrayList;

import edu.princeton.cs.algorithms.Stack;
import kth.id1020.project2.index.CombinedEntity;
import kth.id1020.project2.index.Entity;
import kth.id1020.project2.index.EntityDocument;
import kth.id1020.project2.index.SymbolTable;

/**
 * Parses prefixes into infixes, builds a tree and traveses it. 
 * 
 * @author Henrik
 *
 */
public class ParsingTree {

	private Stack<Word> stack = new Stack<Word>();
	private SymbolTable ST;
	private String rootInfix = "No prefix generated.";

	public  ParsingTree(SymbolTable ST){
		this.ST = ST;
	}

	public void setRootInfix(String rootInfix) {
		this.rootInfix = rootInfix;
	}

	/**
	 * @return infix of the root of the tree.
	 */
	public String getRootInfix() {
		return rootInfix;
	}

	public ArrayList<EntityDocument> parse(String[] keys) throws InvalidQueryException {
		T root = buildTree(setUpStack(keys));
		traverseTree(root);
		Word rootWord = stack.pop();
		rootInfix = rootWord.infix;
		return rootWord.ce.getDocs();
	}

	/**
	 * Builds tree.
	 * @throws InvalidQueryException if tree structure is not balanced.
	 */
	private T buildTree(Stack<T> stack) throws InvalidQueryException {
		if (stack.size()==1) return stack.pop();
		Stack<T> aux = new Stack<T>();
		try {
			while (!stack.isEmpty()) {
				T t = stack.pop();
				Symbol s = (Symbol) t.value;
				if (s.inPlace==true) aux.push(t);
				else {
					T left = aux.pop();
					T right = aux.pop();
					aux.push(addBranch(s.operator, left, right));
				}
			}
		} catch (RuntimeException e) {
			throw new InvalidQueryException("Tree structure could not be built.");
		}
		return aux.pop();
	}

	/**
	 * Sets up initial stack.
	 * @param keys set off all values.
	 * @return a stack.
	 * @throws InvalidQueryException if structure is not correct.
	 */
	private Stack<T> setUpStack(String[] keys) throws InvalidQueryException {
		Stack<T> stack = new Stack<T>();
		final char intersection = '+';
		final char union = '|';
		final char difference = '-';
		for (int i = 0; i<keys.length ; i++) {
			switch (keys[i].charAt(0)) {
			case intersection:
				stack.push(new T(new Symbol(Operator.INTERSECTION)));
				break;
			case union:
				stack.push(new T(new Symbol(Operator.UNION)));
				break;
			case difference:
				stack.push(new T(new Symbol(Operator.DIFFERENCE)));
				break;
			default:
				try {
					Operator operator = ((Symbol) stack.pop().value).operator;
					String l = keys[i];
					T left = createNewWord(l);
					i++;
					while (true) {
						String r = keys[i];
						T right = createNewWord(r);
						stack.push(addBranch(operator, left, right));
						if (i+1==keys.length) break;
						char next = keys[i+1].charAt(0);
						if (next!=union&&next!=difference&&next!=intersection) {
							left = stack.pop();
							i++; }
						else break;
					}

				} catch (RuntimeException e) {
					throw new InvalidQueryException("Initial stack could not be set up.");
				}

				break;
			}
		}
		return stack;
	}

	private T createNewWord(String key) {
		CombinedEntity ce = ST.getCache().check(key);
		if (ce!=null) return new T(new Word(ce));
		ce = new CombinedEntity(key, key, lookTable(key).getDocs());
		Word word = new Word(ce);
		word.cache();
		return new T(word);
	}

	private Entity lookTable(String key) {
		try {
			return ST.get(key);
		} catch (KeyNotFoundException e) {
			System.out.println(e.getMessage());
			return new Entity(key);
		}
	}

	private T addBranch(Operator operator, T left, T right) {
		return new T(new Symbol(operator, left, right));
	}
	
	/**
	 * Traverses tree recursively. From left to right.
	 */
	private void traverseTree(T t) {
		Symbol s  = (Symbol) t.value;
		boolean left = s.left.value instanceof Symbol;
		boolean right = s.right.value instanceof Symbol;

		if (left && !right) {
			traverseTree(s.left);
			Word leftw = stack.pop();
			Word rightw = (Word) s.right.value;
			stack.push(combine(leftw, s, rightw));
		}
		if (left && right) {
			traverseTree(s.left);
			traverseTree(s.right);
			Word rightw = stack.pop();
			Word leftw = stack.pop();
			stack.push(combine(leftw, s, rightw));
		}
		if (!left && right) {
			stack.push((Word) s.left.value);
			traverseTree(s.right);
			Word rightw = stack.pop();
			Word leftw = stack.pop();
			stack.push(combine(leftw, s, rightw));
		}
		if (!left && !right) {
			Word leftw = (Word) s.left.value;
			Word rightw = (Word) s.right.value;
			stack.push(combine(leftw, s, rightw));
		}
	}
	
	private Word combine(Word l, Symbol s, Word r) {
		String symbol = "";
		String values = " " + l.prefix + " " + r.prefix;
		CombinedEntity ce = null;
		switch (s.operator) {
		case INTERSECTION:
			symbol = "+";
			ce = ST.getCache().check(symbol + values);
			if (ce!=null) return new Word(ce);
			ce = makeCombinedEntity(symbol, l, r, l.ce.getDocs());
			ce.intersection(r.ce.getDocs());
			break;
		case UNION:
			symbol = "|";
			ce = ST.getCache().check(symbol + values);
			if (ce!=null) return new Word(ce);
			ce = makeCombinedEntity(symbol, l, r, l.ce.getDocs());
			ce.union(r.ce.getDocs());
			break;
		case DIFFERENCE:
			symbol = "-";
			ce = ST.getCache().check(symbol + values);
			if (ce!=null) return new Word(ce);
			ce = makeCombinedEntity(symbol, l, r, l.ce.getDocs());
			ce.difference(r.ce.getDocs());
			break;
		}
		Word word = new Word(ce);
		word.cache();
		return word;
	}

	private CombinedEntity makeCombinedEntity(String symbol, Word l, Word r, ArrayList<EntityDocument> eDocs) {
		String infix = "(" + l.infix + " " + symbol + " " + r.infix + ")";
		String prefix = symbol + " " + l.prefix + " " + r.prefix;
		return new CombinedEntity(infix, prefix, eDocs);
	}

	private  class T {
		Value value;

		public T(Value value) {
			this.value = value;
		}
	}

	/**
	 * http://en.wikipedia.org/wiki/Marker_interface_pattern
	 */
	private interface Value {}

	private class Word implements Value {

		public Word(CombinedEntity ce) { 
			this.infix = ce.getInfix();
			this.prefix = ce.getPrefix();
			this.ce = ce;
		}

		void cache() {
			ST.getCache().put(prefix, ce);
		}

		final CombinedEntity ce;
		final String infix;
		final String prefix;
	}

	private class Symbol implements Value {

		public Symbol(Operator operator) { 
			this.operator = operator;
			inPlace = false;
		}

		public Symbol(Operator operator, T left, T right) { 
			this.operator = operator; 
			this.left = left;
			this.right = right;
			inPlace = true;
		}

		boolean inPlace;
		Operator operator;
		T left;
		T right;
	}



}
