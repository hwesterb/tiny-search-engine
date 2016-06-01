package kth.id1020.project2.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parses a string, specifies property, direction and keywords.
 * 
 * @author Henrik Westerberg
 */
public class Parser {
				//FULL		//ABBREVIATION
	private String[] occ = {"occurence", 	"occ"};
	private String[] rel = {"relevance", 	"rel"};
	private String[] pop = {"popularity", 	"pop"};
	private String[] nam = {"name", 		"nam"};
	private String[] asc = {"ascending", 	"asc"};
	private String[] des = {"descending", 	"desc"};
	private Property property;
	private Direction direction;
	private String advanced = 	"(.+\\s)+(orderby)\\s"+
			"("+rel[0]+"|"+rel[1]+"|"+pop[0]+"|"+pop[1]+"|"+occ[0]+"|"+occ[1]+"|"+nam[0]+"|"+nam[1]+")\\s"+ 
			"("+asc[0]+"|"+asc[1]+"|"+des[0]+"|"+des[1] +")";
	private String simple = 	"((.+\\s)+.+)?(.+)?";
	private String prefix;

	public String getPrefix() {
		return prefix;
	}

	public Property getProperty() {
		return property;
	}

	public Direction getDirection() {
		return direction;
	}

	/**
	 * Parses a query with regular expression.
	 * @param query the query.
	 * @return an array of strings containing the words to search for in index.
	 * @throws InvalidQueryException if query does not match neither "advanced" nor "simple".
	 */
	public String[] parse(String query) throws InvalidQueryException {
		if (query.matches(advanced)) {
			String start = "(.*orderby\\s)";
			String end = "\\s(orderby)\\s(\\w+)\\s(\\w+)";
			String[] propertyDirection = replace(start, query).split("\\s");
			prefix = replace(end, query);
			String[] keys = prefix.split("\\s");
			setUpAdvanced(propertyDirection);
			return keys;
		} else if (query.matches(simple)) {
			prefix = query;
			String[] keys = query.split("\\s");
			direction = Direction.ASCENDING;
			property = Property.NAME;
			return keys;
		}
		throw new InvalidQueryException("\"" + query + "\" is not valid query.");
	}

	private String replace(String regex, String key){
		Pattern replace = Pattern.compile(regex);
		Matcher matcher = replace.matcher(key);
		return matcher.replaceAll("");
	}

	private void setUpAdvanced(String[] propertyAndDirection) {
		for (String s : propertyAndDirection) {
			if (s.contentEquals(rel[0]) || s.contentEquals(rel[1])) { property = Property.RELEVANCE; 	continue; }
			if (s.contentEquals(pop[0]) || s.contentEquals(pop[1])) { property = Property.POPULARITY; 	continue; }
			if (s.contentEquals(occ[0]) || s.contentEquals(occ[1])) { property = Property.OCCURENCE; 	continue; }
			if (s.contentEquals(nam[0]) || s.contentEquals(nam[1])) { property = Property.NAME; 		continue; }
			if (s.contentEquals(asc[0]) || s.contentEquals(asc[1])) { direction = Direction.ASCENDING; 	continue; }
			if (s.contentEquals(des[0]) || s.contentEquals(des[1])) { direction = Direction.DESCENDING; }
		}
	}

}
