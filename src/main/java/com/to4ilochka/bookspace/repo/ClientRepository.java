package com.to4ilochka.bookspace.repo;

import com.to4ilochka.bookspace.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByUserEmailIn(Collection<String> emails);
}