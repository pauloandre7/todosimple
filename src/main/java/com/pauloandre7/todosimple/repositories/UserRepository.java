package com.pauloandre7.todosimple.repositories;

import com.pauloandre7.todosimple.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Interface para se comunicar com o Banco de Dados;
    // Ela possui a maioria dos métodos necessários para uma busca básica
}
