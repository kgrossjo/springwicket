package de.emptydomain.springwicket.helper;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class TextHelperTest {

	@Test
	public void testWordCounting() {
		String text1 = "abc xyz abc xyz xyz";
		Map<String, Long> index = TextHelper.indexText(text1);
		assertEquals("abc: 2", Long.valueOf(2), index.get("abc"));
		assertEquals("xyz: 3", Long.valueOf(3), index.get("xyz"));
		String[] words = index.keySet().stream().sorted().toArray(String[]::new);
		String[] expectedWords = {"abc", "xyz"};
		assertArrayEquals("Terms abc and xyz", expectedWords, words);
	}
	
	@Test
	public void testStemming() {
		String text1 = "edit edited editor";
		Map<String, Long> index = TextHelper.indexText(text1);
		assertEquals("edit: 2", Long.valueOf(2), index.get("edit"));
		assertEquals("editor: 1", Long.valueOf(1), index.get("editor"));
	}

}
