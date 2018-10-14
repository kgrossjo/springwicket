package de.emptydomain.springwicket.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InvertedIndex {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private long document;

	private String term;
	
	private int count;

	public long getDocument() {
		return document;
	}
	public void setDocument(long document) {
		this.document = document;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
