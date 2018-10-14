package de.emptydomain.springwicket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.emptydomain.springwicket.db.Document;
import de.emptydomain.springwicket.db.DocumentRepository;
import de.emptydomain.springwicket.db.InvertedIndexRepository;

@Controller
public class DocumentsController {
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@GetMapping("/documents")
	public String documents(Model model)
	{
		model.addAttribute("documents", this.documentRepository.findAll());
		return "documents";
	}
	
	@GetMapping("/documents/edit")
	public String document(
			@RequestParam(name="id", required=true) String id,
			Model model
	) {
		Long idNumeric = Long.valueOf(id);
		Document d = this.documentRepository.findById(idNumeric).get();
		model.addAttribute("document", d);
		return "document";
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
	
	@GetMapping("/documents/new")
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
}
