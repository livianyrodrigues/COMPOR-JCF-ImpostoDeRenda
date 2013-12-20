package br.ufcg.ppgcc.compor.ir.impl;

import java.util.List;

import br.ufcg.ppgcc.compor.ir.fachada.Dependente;
import br.ufcg.ppgcc.compor.ir.fachada.GastoDedutivel;
import br.ufcg.ppgcc.compor.ir.fachada.GastoDedutivel.TipoGasto;
import br.ufcg.ppgcc.compor.ir.fachada.Resultado;
import br.ufcg.ppgcc.compor.ir.fachada.Titular;

public class LogicaDeclaracaoCompleta {

	public Resultado declaracaoCompleta(Titular titular) {
		NovaLogicaAuditoria.getInstancia().iniciarTransacao(
				"Relatório da Declaração completa para " + titular.getNome());
		LogicaAutenticacao.getInstancia().verificarLogin();

		List<Dependente> dependentes = 
				LogicaDependente.getInstancia().getDependentes(titular);
		
		double totalRecebido = LogicaFontePagadora.getInstance().totalRecebido(titular);
		double baseCalculo = descontoDependentes(totalRecebido, dependentes);
		
		List<GastoDedutivel> gastosEducacao = 
				LogicaGastoDedutivel.getInstancia().getGastosEducacao(titular, dependentes);
		baseCalculo = descontoEducacao(baseCalculo, gastosEducacao);
		List<GastoDedutivel> gastosSaude = 
				LogicaGastoDedutivel.getInstancia().getGastosSaude(titular, dependentes);
		baseCalculo = descontoSaude(baseCalculo, gastosSaude);
		
		double impostoDevido = impostoDevido(baseCalculo);
		double impostoPago = LogicaFontePagadora.getInstance().totalPago(titular);

		Resultado resultado = new Resultado();
		resultado.setImpostoDevido(impostoDevido);
		resultado.setImpostoPago(impostoPago);
		resultado.setImpostoAPagar(impostoDevido - impostoPago); 
		
		NovaLogicaAuditoria.getInstancia().concluirTransacao();
		return resultado;
	}

	public double impostoDevido(double baseCalculo) {		
		if (baseCalculo < 1637.11 * 12) { //isento
			return 0.0; 
		} 

		if (baseCalculo < 2453.51 * 12) {
			return impostoDevidoFaixa2(baseCalculo);
		} 

		if (baseCalculo < 3271.39 * 12) {
			return impostoDevidoFaixa3(baseCalculo);
		} 

		if (baseCalculo < 4087.66 * 12) {
			return impostoDevidoFaixa4(baseCalculo);
		} 
		
		return impostoDevidoFaixa5(baseCalculo);
	}

	private double impostoDevidoFaixa2(double totalRecebido) {
		return calculoGenerico(totalRecebido, 0.07500, 122.78 * 12);
	}

	private double impostoDevidoFaixa3(double totalRecebido) {
		return calculoGenerico(totalRecebido, 0.1500, 306.80 * 12);
	}

	private double impostoDevidoFaixa4(double totalRecebido) {
		return calculoGenerico(totalRecebido, 0.22500, 552.15 * 12);
	}

	private double impostoDevidoFaixa5(double totalRecebido) {
		return calculoGenerico(totalRecebido, 0.27500, 756.53 * 12);
	}

	private double calculoGenerico(double totalRecebido, double taxa,
			double parcelaADeduzir) {
		return (totalRecebido * taxa) - parcelaADeduzir;
	}

	public double descontoDependentes(double totalRecebido,
			List<Dependente> dependentes) {
		return Math.max(0, totalRecebido - (dependentes.size() * 1974.72));
	}
	
	public double descontoSaude(double totalRecebido, List<GastoDedutivel> gastos) {
		double somaSaude = 0.0;
		
		for (GastoDedutivel gasto : gastos) {
			if (TipoGasto.Saude.equals(gasto.getTipo())) {
				somaSaude += gasto.getValor();
			}
		}
		
		return Math.max(0, totalRecebido - somaSaude);
	}

	public double descontoEducacao(double totalRecebido, List<GastoDedutivel> gastos) {
		double somaEducacao = 0.0;
		
		for (GastoDedutivel gasto : gastos) {
			if (TipoGasto.Educacao.equals(gasto.getTipo())) {
				somaEducacao += gasto.getValor();
			}
		}
		
		somaEducacao = Math.min(3091.35, somaEducacao);
		
		return Math.max(0, totalRecebido - somaEducacao);
	}

}