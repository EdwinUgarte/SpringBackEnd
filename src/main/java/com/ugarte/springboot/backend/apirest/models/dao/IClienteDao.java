package com.ugarte.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.ugarte.springboot.backend.apirest.models.entity.Cliente;


public interface IClienteDao extends CrudRepository<Cliente, Long>{//!CrudRepository es una libreria bastante completa para hacer consultas

    
}
