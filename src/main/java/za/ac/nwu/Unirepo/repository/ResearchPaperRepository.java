package za.ac.nwu.Unirepo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.nwu.Unirepo.model.ResearchPaper;

import java.util.List;

public interface ResearchPaperRepository
        extends JpaRepository<ResearchPaper, Long> {

    List<ResearchPaper>
    findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrAbstractTextContainingIgnoreCase(
            String title,
            String author,
            String abstractText
    );

    List<ResearchPaper> findByCategoryIgnoreCase(String category);
}