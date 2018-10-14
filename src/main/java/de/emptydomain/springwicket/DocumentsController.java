package de.emptydomain.springwicket;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.emptydomain.springwicket.db.Document;
import de.emptydomain.springwicket.db.DocumentRepository;
import de.emptydomain.springwicket.db.InvertedIndex;
import de.emptydomain.springwicket.db.InvertedIndexRepository;
import de.emptydomain.springwicket.helper.SearchResult;;
import de.emptydomain.springwicket.helper.TextHelper;

@Controller
public class DocumentsController {
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private InvertedIndexRepository invertedIndexRepository;
	
	@GetMapping("/documents")
	public String documents(Model model)
	{
		model.addAttribute("documents", this.documentRepository.findAll());
		return "documents.list";
	}
	
	@GetMapping("/documents/edit")
	public String document(
			@RequestParam(name="id", required=true) String id,
			Model model
	) {
		Long idNumeric = Long.valueOf(id);
		Document d = this.documentRepository.findById(idNumeric).get();
		model.addAttribute("document", d);
		return "documents.edit";
	}
	
	@PostMapping("/documents/edit")
	public ModelAndView editDocument(
			@RequestParam(name="id", required=true) String id,
			@RequestParam(name="title", required=true) String title,
			@RequestParam(name="content", required=true) String content,
			Model model
	) {
		Long idNumeric = Long.valueOf(id);
		Document d = this.documentRepository.findById(idNumeric).get();
		d.setTitle(title);
		d.setContent(content);
		d = this.documentRepository.save(d);
		this.documentRepository.updateIndex(d);
		return new ModelAndView("redirect:/documents");
	}
	
	@GetMapping("/documents/add")
	public String newDocument(Model model)
	{
		return "documents.new";
	}
	
	@PostMapping("/documents/add")
	public ModelAndView addDocument(
			@RequestParam(name="title", required=true) String title,
			@RequestParam(name="content", required=true) String content,
			Model model
	)
	{
		Document d = new Document();
		d.setTitle(title);
		d.setContent(content);
		d = this.documentRepository.save(d);
		this.documentRepository.updateIndex(d);
		return new ModelAndView("redirect:/documents");
	}
	
	@GetMapping("/search")
	public String searchForm(
			Model model
	)
	{
		return "search";
	}
	
	@PostMapping("/search")
	public String searchResult(
			@RequestParam(name="query", required=true) String query,
			Model model
	)
	{
		model.addAttribute("query", query);

		Map<String, Integer> queryWords = TextHelper.indexText(query);

		Map<String, Long> documentFrequency = new HashMap<>();
		for (String w : queryWords.keySet()) {
			documentFrequency.put(
					w, this.invertedIndexRepository.countByTerm(w)
			);
		}
		
		Map<Long, Double> scores = new HashMap<>();
		for (String w : queryWords.keySet()) {
			double queryFreq = queryWords.get(w);
			double docFreq = documentFrequency.get(w);
			Iterable<InvertedIndex> documentsForTerm = this.invertedIndexRepository.findByTerm(w);
			for (InvertedIndex x : documentsForTerm) {
				double existingScore = scores.getOrDefault(x.getDocument(), 0.0);
				scores.put(
					x.getDocument(),
					existingScore + ( queryFreq * (double)x.getCount() / docFreq )
				);
			}
		}
		
		List<SearchResult> result = new ArrayList<>();
		for (Long docid : scores.keySet()) {
			Double score = scores.get(docid);
			Document document = this.documentRepository.findById(docid).get();
			SearchResult x = new SearchResult();
			x.setDocument(docid);
			x.setScore(score);
			x.setTitle(document.getTitle());
			result.add(x);
		}
		result.sort((a, b) -> b.compareTo(a));
		model.addAttribute("result", result);

		return "search";
	}
}
