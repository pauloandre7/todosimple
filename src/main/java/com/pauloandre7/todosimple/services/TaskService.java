package com.pauloandre7.todosimple.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pauloandre7.todosimple.models.Task;
import com.pauloandre7.todosimple.models.User;
import com.pauloandre7.todosimple.repositories.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserService userService;
    
    public Task findById(Long id){
        Optional<Task> task = this.taskRepo.findById(id);

        return task.orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));
    }

    public List<Task> findAllByUserId(Long userId){
        List<Task> tasks = this.taskRepo.findByUser_Id(userId);
        
        return tasks;
    }

    @Transactional
    public Task create(Task obj){
        User user = this.userService.findById(obj.getUser().getId());

        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepo.save(obj);

        return obj;
    }

    @Transactional
    public Task update(Task obj){
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());

        return this.taskRepo.save(newObj);
    }

    public void delete(Long id){
        findById(id);

        try{
            this.taskRepo.deleteById(id);
        } catch(Exception e){
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas!");
        }
    }
}
