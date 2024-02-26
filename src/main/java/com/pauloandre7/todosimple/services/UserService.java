package com.pauloandre7.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pauloandre7.todosimple.models.User;
import com.pauloandre7.todosimple.repositories.UserRepository;


@Service
public class UserService {
    
    /* O Autowired vai utilizar as anotações Spring que estão na classe para instanciar o atributo 
    *  Pois não podemos instanciar uma interface. */
    @Autowired
    private UserRepository userRepo;
    

    public User findById(Long id){
        /* Optional é um recurso java para evitar o null. Se vier null, ele atribui vazio
         * no objeto ao invés de "null". */
        Optional<User> user = this.userRepo.findById(id);

        /* Envia o user se ele tiver preenchido. Se não tiver, ele vai ser do tipo Optional, não podendo ser retornado.
        * Sendo Optional, não podemos retornar, portanto o método em questão irá lançar uma
        * exceção.*/
        return user.orElseThrow(() -> new RuntimeException(
            "Usuário não encontrado! Id: " +id+ ", Tipo: " +User.class.getName()
            ));
        // esse '()' é uma arrow function, usada para fazer lambda body
    }

    // Transactinal é útil para operações de Inserção/Modificação, mas não de selects.
    // Transactional garante o conceito de atomicidade da operação no banco.
    @Transactional
    public User create(User obj) {
        /* Se o usuário mandar um objeto com id, ou seja, um já instanciado (criado)
        *  Ele poderia modificar pela função de save. Portanto, é importante resetar o id*/
        obj.setId(null);

        obj = this.userRepo.save(obj);

        return obj;
    }

    @Transactional
    public User update(User obj){
        /* Para determinar que iremos atualizar apenas a senha, usamos o método acima para
         * verificar, primeiramente, se o objeto existe. Se existir, vamos receber a instância
         * dele e, logo após, setar a senha informada pelo usuário. */
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());

        return this.userRepo.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        
        try{
            this.userRepo.deleteById(id);
        } catch(Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas!");
        }
    }
}
