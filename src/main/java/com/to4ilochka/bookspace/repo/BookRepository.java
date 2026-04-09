package com.to4ilochka.bookspace.repo;

import com.to4ilochka.bookspace.model.Book;
import com.to4ilochka.bookspace.model.enums.AgeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String name, String author, Pageable pageable
    );

    boolean existsByName(String name);

    default Page<Book> searchByKeyword(String keyword, Pageable pageable) {
        return findAllByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                keyword, keyword, pageable
        );
    }

    Page<Book> findAllByAgeGroup(AgeGroup ageGroup, Pageable pageable);

    Page<Book> findAllByAgeGroupAndNameContainingIgnoreCaseOrAgeGroupAndAuthorContainingIgnoreCase(
            AgeGroup ageGroup, String keyword, AgeGroup ageGroup1, String keyword1, Pageable pageable
    );
}
