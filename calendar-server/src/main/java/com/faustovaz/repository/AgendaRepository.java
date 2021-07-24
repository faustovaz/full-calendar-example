package com.faustovaz.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.faustovaz.entity.Agenda;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

	public List<Agenda> findByAgendamentoBetween(LocalDateTime start, LocalDateTime end);
	
}
