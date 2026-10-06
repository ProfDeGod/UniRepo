package za.ac.nwu.Unirepo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import za.ac.nwu.Unirepo.model.ResearchPaper;
import za.ac.nwu.Unirepo.repository.ResearchPaperRepository;

@Controller
public class UploadController {

    private final ResearchPaperRepository repository;

    public UploadController(ResearchPaperRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }


    @PostMapping("/upload")
    public String uploadPaper(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("category") String category,
            @RequestParam("abstractText") String abstractText,
            @RequestParam("year") int year,
            @RequestParam("file") MultipartFile file) {

        try {

            ResearchPaper paper = new ResearchPaper();

            paper.setTitle(title);
            paper.setAuthor(author);
            paper.setCategory(category);
            paper.setAbstractText(abstractText);
            paper.setYear(year);

            paper.setFileName(file.getOriginalFilename());
            paper.setFileData(file.getBytes());

            repository.save(paper);

            System.out.println("Research paper saved successfully.");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return "redirect:/search";
    }
}