package de.emptydomain.springwicket.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tartarus.martin.Stemmer;

@Repository
public class DocumentRepositoryImpl implements DocumentRepositoryCustom {
	
	@Autowired
	private InvertedIndexRepository invertedIndexRepository;

	@Override
	public void updateIndex(Document d) {
		this.invertedIndexRepository.deleteByDocument(d.getId());
		Collection<String> words = this.splitDocumentTextIntoWords(d);
		words = this.stemWords(words);
		Map<String, Integer> wordCounts = this.countWords(words);
		this.saveWordCounts(d, wordCounts);
	}
	
	private static final Pattern WORD_SPLIT = Pattern.compile("[.,?! ]+");
	
	private Collection<String> splitDocumentTextIntoWords(Document d)
	{
		Collection<String> result = new LinkedList<String>();
		String[] titleWords = WORD_SPLIT.split(d.getTitle());
		for (String w : titleWords) {
			result.add(w.strip()	);
		}
		String[] contentWords = WORD_SPLIT.split(d.getContent());
		for (String 	w : contentWords) {
			result.add(w.strip());
		}
		return result;
	}
	
	private Collection<String> stemWords(Collection<String> words)
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
	
	private Map<String, Integer> countWords(Collection<String> words)
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

	private void saveWordCounts(Document d, Map<String, Integer> wordCounts) 
	{
		long docid = d.getId();
		for (String w : wordCounts.keySet()) {
			Integer c = wordCounts.get(w);
			InvertedIndex x = new InvertedIndex();
			x.setDocument(docid);
			x.setTerm(w);
			x.setCount(c);
			this.invertedIndexRepository.save(x);
		}
	}

}
