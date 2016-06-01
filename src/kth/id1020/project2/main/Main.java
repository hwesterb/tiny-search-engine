package kth.id1020.project2.main;

import se.kth.id1020.Driver;
import se.kth.id1020.TinySearchEngineBase;

/**
 * Starts the program.
 * 
 * @author Henrik
 */

public class Main {

		public static void main(String[] args) throws Exception{
			TinySearchEngineBase searchEngine = new TinySearchEngine();
			Driver.run(searchEngine);
		}

}
