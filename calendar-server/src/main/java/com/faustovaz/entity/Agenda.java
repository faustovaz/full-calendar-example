package com.faustovaz.entity;

import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.faustovaz.dialect.SQLiteLocalDateTimeConverter;

@Entity
@Table(name ="agenda")
public class Agenda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	private String cliente;
	
	@NotNull
	@Convert(converter = SQLiteLocalDateTimeConverter.class)
	private LocalDateTime agendamento;
	
	private String lembrete;
	
	public Agenda() {
		
	}
		
	public Agenda(String cliente, LocalDateTime agendamento, String lembrete) {
		super();
		this.cliente = cliente;
		this.agendamento = agendamento;
		this.lembrete = lembrete;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCliente() {
		return cliente;
	}
	
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	public LocalDateTime getAgendamento() {
		return agendamento;
	}
	
	public void setAgendamento(LocalDateTime agendamento) {
		this.agendamento = agendamento;
	}
	
	public String getLembrete() {
		return lembrete;
	}
	
	public void setLembrete(String lembrete) {
		this.lembrete = lembrete;
	}
	
}
