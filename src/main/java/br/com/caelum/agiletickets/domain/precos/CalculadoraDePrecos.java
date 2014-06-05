package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {

	public static BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco;
		
		switch (sessao.getEspetaculo().getTipo()) {
		case CINEMA:
		case SHOW:
			preco = precoEspetaculoCinemaShow(sessao);
			break;
		case BALLET:
		case ORQUESTRA:
			preco = precoEspetaculoBalletOrchestra(sessao);
			break;
		default:
			preco = sessao.getPreco();
			break;
		}
		

		return preco.multiply(BigDecimal.valueOf(quantidade));
	}

	private static BigDecimal precoEspetaculoBalletOrchestra(Sessao sessao) {
		BigDecimal preco;
		int vagasDisponiveis = vagasDisponiveis(sessao);
		preco = sessao.getPreco();
		boolean estorouVagas = estorouLimiteVagas(sessao, vagasDisponiveis, 0.50);
		boolean estorouTempoSessao = estorouTempoSessao(sessao);
		if (estorouVagas) { 
			preco = aplicaTaxa(sessao, preco, 0.20);
		} 
		if(estorouTempoSessao){
			preco = aplicaTaxa(sessao, preco, 0.10);
		}
		return preco;
	}

	private static boolean estorouTempoSessao(Sessao sessao) {
		boolean estorouTempoSessao = sessao.getDuracaoEmMinutos() > 60;
		return estorouTempoSessao;
	}

	private static BigDecimal aplicaTaxa(Sessao sessao, BigDecimal preco, Double taxa) {
		preco = preco.add(sessao.getPreco().multiply(BigDecimal.valueOf(taxa)));
		return preco;
	}

	private static int vagasDisponiveis(Sessao sessao) {
		int vagasDisponiveis = sessao.getTotalIngressos() - sessao.getIngressosReservados();
		return vagasDisponiveis;
	}

	private static BigDecimal precoEspetaculoCinemaShow(Sessao sessao) {
		BigDecimal preco;
		int vagasDisponiveis = vagasDisponiveis(sessao);
		preco = sessao.getPreco();
		boolean estorouVagas = estorouLimiteVagas(sessao, vagasDisponiveis, 0.05);
		//quando estiver acabando os ingressos... 
		if (estorouVagas) { 
			preco = aplicaTaxa(sessao, preco, 0.10);
		} 
		return preco;
	}

	private static boolean estorouLimiteVagas(Sessao sessao, int vagasDisponiveis, double percentualLimite) {
		return ((vagasDisponiveis / sessao.getTotalIngressos().doubleValue() <= percentualLimite));
	}

}