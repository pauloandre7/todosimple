package com.pauloandre7.todosimple.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = User.TABLE_NAME)
public class User {
    
    public interface CreateUser {
    
    }
    public interface UpdateUser {

    }

    public static final String TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", unique = true)
    private Long id;

    @Column(name="username", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100)
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name="password", length = 60, nullable = false)
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 5, max = 60)
    private String password;

    @OneToMany(mappedBy = "user") // mappedBy deve conter o nome da variável na classe Task que mapeia as associações
    private List<Task> tasks = new ArrayList<Task>();

    public User(){

    }

    public User(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    @JsonIgnore // não inclui o dado no json (otimizacao)
    public List<Task> getTasks(){
        return tasks;
    }

    public void setId(Long id){
        this.id = id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) 
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof User)) 
            return false;

        User user = (User) obj;

        if (this.id == null){
            if (user.id != null)
                return false;
            else if (!this.id.equals(user.id))
                return false;
        }

        return Objects.equals(id, user.id) 
            && Objects.equals(username, user.username)
            && Objects.equals(password, user.password);
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime + result + ((this.id == null) ? 0 : this.id.hashCode());

        return result;
    }
}
