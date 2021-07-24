package com.faustovaz.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.faustovaz.entity.Agenda;

@SpringBootTest
@ActiveProfiles("tests")
class AgendaRepositoryTest {

	@Autowired
	AgendaRepository repository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@BeforeEach
	void voidAgendaTable() {
		jdbcTemplate.execute("delete from agenda");
	}
	
	@Test
	void shoudSaveAgendamentoAndRetriveIt() {
		Agenda toSave = new Agenda("Fausto", LocalDateTime.now(), "Tests");
		repository.save(toSave);
		
		LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
		LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		
		List<Agenda> agendamentos = repository.findByAgendamentoBetween(start, end);
		assertThat(agendamentos).isNotEmpty();
		assertThat(agendamentos).hasSize(1);
	}
	
	@Test
	void shouldRetriveOnlyTheScheduledOnesInASpecificDate() {
		LocalDateTime start = LocalDateTime.of(LocalDate.of(2021, 6, 25), LocalTime.MIN);
		LocalDateTime end = LocalDateTime.of(LocalDate.of(2021, 6, 25), LocalTime.MAX);
		
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
		
		var agendamentos = repository.findByAgendamentoBetween(start, end);
		assertThat(agendamentos).hasSize(3);		
		assertThat(agendamentos).allMatch((a) -> a.getAgendamento().getDayOfMonth() == 25);
		assertThat(agendamentos).allMatch((a) -> a.getAgendamento().getMonth().equals(Month.JUNE));
	}
	

}
