package za.ac.nwu.Unirepo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import za.ac.nwu.Unirepo.model.ResearchPaper;
import za.ac.nwu.Unirepo.repository.ResearchPaperRepository;

import java.util.List;

@Controller
public class SearchController {

    private final ResearchPaperRepository repository;

    public SearchController(ResearchPaperRepository repository) {
        this.repository = repository;
    }


    // =========================
    // SEARCH PAPERS
    // =========================

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Model model) {

        List<ResearchPaper> papers;

        boolean noKeyword =
                keyword == null ||
                        keyword.trim().isEmpty();

        boolean noCategory =
                category == null ||
                        category.trim().isEmpty();


        // No search filters
        if (noKeyword && noCategory) {

            papers = repository.findAll();

        }

        // Keyword only
        else if (!noKeyword && noCategory) {

            papers = repository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrAbstractTextContainingIgnoreCase(
                            keyword,
                            keyword,
                            keyword
                    );

        }

        // Category only
        else if (noKeyword) {

            papers = repository.findByCategoryIgnoreCase(category);

        }

        // Keyword + category
        else {

            papers = repository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrAbstractTextContainingIgnoreCase(
                            keyword,
                            keyword,
                            keyword
                    );

            papers.removeIf(paper ->
                    paper.getCategory() == null ||
                            !paper.getCategory().equalsIgnoreCase(category)
            );
        }


        model.addAttribute("papers", papers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        return "search";
    }


    // =========================
    // DOWNLOAD PAPER
    // =========================

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPaper(
            @PathVariable Long id) {

        ResearchPaper paper =
                repository.findById(id).orElse(null);


        // Paper does not exist
        if (paper == null) {
            return ResponseEntity.notFound().build();
        }


        // File does not exist
        if (paper.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }


        String fileName = paper.getFileName();

        if (fileName == null || fileName.isBlank()) {
            fileName = "research-paper.pdf";
        }


        return ResponseEntity.ok()

                // Tell browser this is a downloadable file
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\""
                )

                // PDF content type
                .contentType(MediaType.APPLICATION_PDF)

                // Actual PDF data
                .body(paper.getFileData());
    }
    // =========================
// VIEW PAPER
// =========================

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewPaper(
            @PathVariable Long id) {

        ResearchPaper paper =
                repository.findById(id).orElse(null);

        if (paper == null) {
            return ResponseEntity.notFound().build();
        }

        if (paper.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(paper.getFileData());
    }
}