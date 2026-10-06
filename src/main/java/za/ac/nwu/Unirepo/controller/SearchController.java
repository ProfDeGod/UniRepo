package za.ac.nwu.Unirepo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/search")
    public String search(

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            String category,

            Model model) {


        List<ResearchPaper> papers;


        boolean noKeyword =
                keyword == null ||
                        keyword.trim().isEmpty();


        boolean noCategory =
                category == null ||
                        category.trim().isEmpty();


        /*
         * No filters
         * Show all papers
         */
        if (noKeyword && noCategory) {

            papers = repository.findAll();

        }


        /*
         * Keyword only
         */
        else if (!noKeyword && noCategory) {

            papers =
                    repository
                            .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrAbstractTextContainingIgnoreCase(
                                    keyword,
                                    keyword,
                                    keyword
                            );

        }


        /*
         * Category only
         */
        else if (noKeyword) {

            papers =
                    repository.findByCategoryIgnoreCase(category);

        }


        /*
         * Keyword AND category
         */
        else {

            papers =
                    repository
                            .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrAbstractTextContainingIgnoreCase(
                                    keyword,
                                    keyword,
                                    keyword
                            );

            papers.removeIf(paper ->
                    !paper.getCategory()
                            .equalsIgnoreCase(category)
            );

        }


        model.addAttribute("papers", papers);

        model.addAttribute("keyword", keyword);

        model.addAttribute("category", category);


        return "search";
    }
}