package de.emptydomain.springwicket.helper;

import org.tartarus.martin.Stemmer;

public class Stemming {
	
	/** Wrapper for the weird Porter Stemmer API. */
	public static final String stem(String word) {
		// The API for the Stemmer class is a bit weird.
		// But it's the official implementation...
		// We have to create a new stemmer, then add 
		// a word to it, then call stem, then you can 
		// retrieve the stem via toString.
		// Actually, maybe we don't need a new stemmer
		// for each word, but I don't want to risk it.
		Stemmer stemmer = new Stemmer();
		char[] cs = word.toCharArray();
		stemmer.add(cs, cs.length);
		stemmer.stem();
		return stemmer.toString();
	}

}
