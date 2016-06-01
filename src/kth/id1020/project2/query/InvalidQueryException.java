package kth.id1020.project2.query;

/**
 * Thrown to indicate an invalid query.
 * 
 * @author Henrik Westerberg
 */
public class InvalidQueryException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidQueryException(String msg) {
		super(msg);
	}
}
