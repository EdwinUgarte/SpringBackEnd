package com.ugarte.springboot.backend.apirest.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.ugarte.springboot.backend.apirest.models.entity.Cliente;
import com.ugarte.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins = {"http://localhost:4200"})//!El Cross es muy importante para que el dominio frontend pueda hacer las peticiones 
@RestController //!Esta anotacion nos creara esta clase como una API
@RequestMapping("/api")
public class ClienteRestController {
    
    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public List<Cliente> index(){
        return clienteService.findAll();
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){//!Se cambio el tipo a ResponseEntiry<?> porque nos ayuda a hacer el manejo de errores
       Cliente cliente = null;
       Map<String, Object> response = new HashMap<>();
       //!Se manejo el error por si ingresamos un id que no exista

       try {
        cliente = clienteService.findById(id);

       } catch (DataAccessException e) {
        response.put("mensaje", "Error al realizar el insert en la base de datos");
        response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
        
        if(cliente == null){
            response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @PostMapping("/clientes")
    public ResponseEntity<?> create(@RequestBody Cliente cliente){//!Se poone @RequestBody porque cuando el usuario lo manda del frontend viene en formato JSON y esta anotacion le ayuda a spring a saber que es un objeto
        Cliente clienteNew = null;
        Map<String, Object> response = new HashMap<>();

        try {
         cliente.setCreateAt(new Date());
           clienteNew = clienteService.save(cliente);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        
        response.put("mensaje",  "El cliente fue creado con exito");
        response.put("cliente", clienteNew);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);//!Created retorna un status correcto 201 de creado
    }




    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id){
        Cliente clienteActual = clienteService.findById(id);
        Cliente clienteUpdate = null;
        Map<String, Object> response = new HashMap<>();

        if(clienteActual == null){
            response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no se pudo editar!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            clienteActual = clienteService.findById(id);
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setEmail(cliente.getEmail());

            clienteUpdate = clienteService.save(clienteActual);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        response.put("mensaje",  "El cliente ha sido actualizado con exito");
        response.put("cliente", clienteUpdate);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }



    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        
        try {
            clienteService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar de la base de datos");
            response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
 
        response.put("mensaje", "El cliente se ha eliminado correctamente");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
