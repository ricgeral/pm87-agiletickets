package br.com.caelum.agiletickets.models;

import java.sql.Time;
import java.util.Date;
import java.util.List;

interface CriadorDeSessoes {
	List<Sessao> cria(Date inicio,Date fim,Time horario, Espetaculo espetaculo);
}
