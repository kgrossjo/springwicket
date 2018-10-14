package de.emptydomain.springwicket.db;

import org.springframework.data.repository.CrudRepository;

public interface InvertedIndexRepository 
extends CrudRepository<InvertedIndex, Long>
{
	
	public void deleteByDocument(long document);
	public Iterable<InvertedIndex> findByDocument(long document);
	public Iterable<InvertedIndex> findByTerm(String term);
	public long countByTerm(String term);

}
