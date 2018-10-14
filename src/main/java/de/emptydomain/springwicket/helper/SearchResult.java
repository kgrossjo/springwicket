package de.emptydomain.springwicket.helper;

public class SearchResult implements Comparable<SearchResult> {
	private long document;
	private double score;
	private String title;

	public long getDocument() {
		return document;
	}
	public void setDocument(long document) {
		this.document = document;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public int compareTo(SearchResult o) {
		if (this.getScore() > o.getScore()) {
			return +1;
		} else if (this.getScore() < o.getScore()) {
			return -1;
		} else if (this.getDocument() > o.getDocument()) {
			return +1;
		} else if (this.getDocument() < o.getDocument()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
