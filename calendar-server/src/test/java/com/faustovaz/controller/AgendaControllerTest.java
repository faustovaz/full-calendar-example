package com.faustovaz.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.faustovaz.entity.Agenda;
import com.faustovaz.repository.AgendaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class AgendaControllerTest {

	@Autowired
	MockMvc mvc;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	AgendaRepository repository;
	
	@BeforeEach
	void cleanAgendaTable() {
		jdbcTemplate.execute("delete from agenda");
	}
	
	void saveDataForTests() {
		List.of(
				new Agenda("Fausto", LocalDateTime.now(), "Tests1"),
				new Agenda("Fausto", LocalDateTime.of(2021, 6, 25, 14, 0, 0),  "Tests2"),
				new Agenda("Fausto", LocalDateTime.of(2021, 6, 25, 14, 30, 0), "Tests3"),
				new Agenda("Fausto", LocalDateTime.of(2021, 6, 25, 17, 0, 0),  "Tests4"),
				new Agenda("Fausto", LocalDateTime.of(2021, 6, 30, 11, 0, 0),  "Tests5"),
				new Agenda("Fausto", LocalDateTime.of(2021, 6, 29, 11, 0, 0),  "Tests6"),
				new Agenda("Fausto", LocalDateTime.of(2021, 6, 28, 11, 0, 0),  "Tests7"),
				new Agenda("Fausto", LocalDateTime.of(2021, 6, 27, 11, 0, 0),  "Tests8"),
				new Agenda("Fausto", LocalDateTime.now(), "Tests9"),
				new Agenda("Fausto", LocalDateTime.now(), "Tests10"),
				new Agenda("Fausto", LocalDateTime.now(), "Tests11"),
				new Agenda("Fausto", LocalDateTime.now(), "Tests12")
		).forEach((a) -> repository.save(a));
	}
	
	@Test
	void shouldReturnAllStoredAgenda() throws Exception {
		saveDataForTests();
		mvc.perform(
			get("/agenda")
			.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk())
		.andExpect(jsonPath("$").isNotEmpty())
		.andExpect(jsonPath("$[0].id", isA(Integer.class)))
		.andExpect(jsonPath("$.[0].cliente", is("Fausto")));
	}
	
	@Test
	void shoudSaveAgendamento() throws Exception {
		List<Map<String, Object>> agendamentos = jdbcTemplate.queryForList("select * from agenda");
		assertThat(agendamentos).isEmpty();
		
		String data = new ObjectMapper()
				.writeValueAsString(
					Map.of(
						"cliente", "fausto",
						"lembrete", "test",
						"agendamento", "2021-06-30T14:00"
					)
				);
		mvc
			.perform(
				post("/agenda")
				.contentType(MediaType.APPLICATION_JSON)
				.content(data)
			)
			.andExpect(status().isOk());
		
		agendamentos = jdbcTemplate.queryForList("select * from agenda");
		assertThat(agendamentos).hasSize(1);
	}
	
	@Test
	void shoudReturnErroWhenSavingIncompleteAgenda() throws Exception {
		var data = Map.of("lembrete", "Tests", "agendamento", "2021-01-01T14:00");
		String jsonData = new ObjectMapper().writeValueAsString(data);
		mvc
			.perform(
				post("/agenda")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnErrorWhenSavingAgendaWithoutAgendamento() throws Exception {
		var data = Map.of("cliente", "fausto", "lembrete", "tests");
		var jsonData = new ObjectMapper().writeValueAsString(data);
		mvc	
			.perform(
				post("/agenda")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnAllScheduleForASpecificDate() throws Exception {
		saveDataForTests();
		mvc
			.perform(get("/agenda/2021-06-25"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	
	

}
