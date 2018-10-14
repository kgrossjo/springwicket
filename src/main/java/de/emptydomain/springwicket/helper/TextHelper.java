package de.emptydomain.springwicket.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

import org.tartarus.martin.Stemmer;

public class TextHelper {

	public static Map<String, Integer> indexText(String completeText) {
		Collection<String> words = splitTextIntoWords(completeText);
		words = lowercaseWords(words);
		words = stemWords(words);
		Map<String, Integer> wordCounts = countWords(words);
		return wordCounts;
	}

	private static final Pattern WORD_SPLIT = Pattern.compile("[.,?! ]+");

	private static Collection<String> splitTextIntoWords(String text)
	{
		Collection<String> result = new LinkedList<String>();
		String[] words = WORD_SPLIT.split(text);
		for (String w : words) {
			result.add(w.strip()	);
		}
		return result;
	}
	
	private static Collection<String> lowercaseWords(Collection<String> words)
	{
		return words.stream().map(String::toLowerCase).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		/*
		Collection<String> result = new LinkedList<String>();
		for (String w : words) {
			result.add(w.toLowerCase());
		}
		return result;
		*/
	}

	private static Collection<String> stemWords(Collection<String> words)
	{
		Collection<String> result = new LinkedList<String>();
		for (String w : words) {
			// The API for the Stemmer class is a bit weird.
			// But it's the official implementation...
			// We have to create a new stemmer, then add 
			// a word to it, then call stem, then you can 
			// retrieve the stem via toString.
			// Actually, maybe we don't need a new stemmer
			// for each word, but I don't want to risk it.
			Stemmer stemmer = new Stemmer();
			char[] cs = w.toCharArray();
			stemmer.add(cs, cs.length);
			stemmer.stem();
			result.add(stemmer.toString());
		}
		return result;
	}

	private static Map<String, Integer> countWords(Collection<String> words)
	{
		// I guess there is a streams API that does this, but the code
		// is short enough, I'm not having too much pain.
		// Maybe Collectors.groupingBy is the answer?
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (String w : words) {
			if (! result.containsKey(w)) {
				result.put(w, Integer.valueOf(0));
			}
			result.put(w, Integer.valueOf(1 + result.get(w)));
		}
		return result;
	}

}
