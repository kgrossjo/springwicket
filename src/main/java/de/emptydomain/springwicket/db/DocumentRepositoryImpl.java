package de.emptydomain.springwicket.db;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.emptydomain.springwicket.helper.TextHelper;

@Repository
public class DocumentRepositoryImpl implements DocumentRepositoryCustom {
	
	@Autowired
	private InvertedIndexRepository invertedIndexRepository;

	@Override
	@Transactional
	public void updateIndex(Document d) {
		this.invertedIndexRepository.deleteByDocument(d.getId());
		String completeText = d.getTitle() + " " + d.getContent();
		Map<String, Integer> wordCounts = TextHelper.indexText(completeText);
		this.saveWordCounts(d, wordCounts);
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
