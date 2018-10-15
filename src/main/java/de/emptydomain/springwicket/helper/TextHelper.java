package de.emptydomain.springwicket.helper;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextHelper {

	private static final Pattern WORD_SPLIT = Pattern.compile("[.,?! ]+");

	public static Map<String, Long> indexText(String completeText) {
		// This is an exercise in stream processing.  I feel this
		// mostly reads quite nicely, the only slightly difficult part
		// is the collection phase.
		// I converted the original procedural code to this functional
		// style only after adding some unit tests for the `indexText`
		// method.  This way I could be sure that I didn't mess it up.
		return WORD_SPLIT.splitAsStream(completeText)	// split into words
			.map(String::strip)							// strip whitespace off each word
			.map(String::toLowerCase)					// convert to lower case
			.map(Stemming::stem)							// apply stemming
			.collect(Collectors.groupingBy(				// count the resulting words
				Function.identity(),
				Collectors.counting()));
	}

}
