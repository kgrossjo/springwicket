package de.emptydomain.springwicket.helper;

import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextHelper {

	public static Map<String, Integer> indexText(String completeText) {
		List<String> stems = splitTextIntoWords(completeText).stream()
				.map(String::toLowerCase)
				.map(Stemming::stem)
				.collect(Collectors.toList());
		Map<String, Integer> wordCounts = countWords(stems);
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
