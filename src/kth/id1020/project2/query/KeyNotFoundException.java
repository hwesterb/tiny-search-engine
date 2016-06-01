package kth.id1020.project2.query;

/**
 * Thrown to indicate that a key wasn't found in index.
 * 
 * @author Henrik Westerberg
 */
public class KeyNotFoundException extends Exception {

	private static final long serialVersionUID = 3911970466255452138L;

	public KeyNotFoundException(String key) {
		super("\"" + key + "\" does not occur in any document.");
	}
}
