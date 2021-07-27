package com.faustovaz.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.faustovaz.entity.Agenda;
import com.faustovaz.repository.AgendaRepository;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

	private AgendaRepository repository;

	public AgendaController(AgendaRepository repository) {
		this.repository = repository;
	}

	@CrossOrigin(origins = "http://127.0.0.1:3000")
	@GetMapping
	public List<Agenda> getAll() {
		return this.repository.findAll();
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:3000")
	@GetMapping("/{date}")
	public ResponseEntity<List<Agenda>> getAllByDate(@PathVariable("date") String strDate) {
		try {
			LocalDate date = LocalDate.parse(strDate);
			LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
			LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
			return new ResponseEntity<>(this.repository.findByAgendamentoBetween(start, end, Sort.by(Sort.Direction.ASC, "agendamento")), HttpStatus.OK);
		} 
		catch(DateTimeParseException d) {
			return ResponseEntity.badRequest().header("error", "Data Inválida").build();
		}
		
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:3000")
	@PostMapping
	public ResponseEntity<Agenda> save(@Valid @RequestBody Agenda agenda, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(this.repository.save(agenda), HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:3000")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id){
		try {
			this.repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	

}