package com.pauloandre7.todosimple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.pauloandre7.todosimple.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>  {
    
    // Essa seria uma forma de pegar os dados do banco usando métodos já definidos da iterface JpaRepository
    // Atualmente é a melhor maneira, para fins de refatoração
    List<Task> findByUser_Id(long id);
    
    /* LEARNING ANNOTATIONS
    // Essa query abaixo NÃO É UM SQL PURO, por isso tem coisas diferentes na query.
    // "from Task" referencia a model Task que foi definido como @Entity no package Models
    // :id referencia o valor do @Param("id"). Esse param recebe o valor de "Long id"
    @Query(value = "SELECT t from Task t WHERE t.user.id = :id")
    List<Task> findByUserId(@Param("id") Long id);

    // Como seria com SQL puro:
    @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
    List<Task> findByUserIdSQL(@Param("id") Long id);
    */
}
