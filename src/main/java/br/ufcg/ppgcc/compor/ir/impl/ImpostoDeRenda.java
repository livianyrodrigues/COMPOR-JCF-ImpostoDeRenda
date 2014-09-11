package br.ufcg.ppgcc.compor.ir.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufcg.ppgcc.compor.ir.Dependente;
import br.ufcg.ppgcc.compor.ir.FachadaExperimento;
import br.ufcg.ppgcc.compor.ir.Resultado;
import br.ufcg.ppgcc.compor.ir.Titular;
import br.ufcg.ppgcc.compor.ir.impl.excecaoCriarDependente;
import br.ufcg.ppgcc.compor.ir.impl.excecaoCriarFonte;
import br.ufcg.ppgcc.compor.ir.impl.excecaoCriarTitular;

import java.util.Map;
import java.util.LinkedHashMap;

import br.ufcg.ppgcc.compor.ir.FontePagadora;

public class ImpostoDeRenda implements FachadaExperimento {

	private Map<Titular, List<FontePagadora>> titulares = new LinkedHashMap<Titular, List<FontePagadora>>();
	private Map<Titular, List<Dependente>> dependentes = new HashMap<Titular, List<Dependente>>();

	public void criarNovoTitular(Titular titular) {
		if (titular.getNome() == null) {
			throw new excecaoCriarTitular("O campo nome é obrigatório");
		}

		if (titular.getCpf() == null) {
			throw new excecaoCriarTitular("O campo CPF é obrigatório");
		}

		if (titular.getCpf().matches("\\d\\d\\d.\\d\\d\\d.\\d\\d\\d-\\d\\d") == false) {
			throw new excecaoCriarTitular("O campo CPF está inválido");
		}

		titulares.put(titular, new ArrayList<FontePagadora>());
		dependentes.put(titular, new ArrayList<Dependente>());
	}

	public List<Titular> listarTitulares() {
		return new ArrayList<Titular>(titulares.keySet());
	}

	public void criarFontePagadora(Titular titular, FontePagadora fonte) {
	
		if (fonte.getNome() == null) {
			throw new excecaoCriarFonte("O campo nome é obrigatório");
		}

		if (fonte.getRendimentoRecebidos() == 0.0) {
			throw new excecaoCriarFonte(
					"O campo rendimentos recebidos é obrigatório");
		}
	
		if (fonte.getRendimentoRecebidos() < 0.0) {
			throw new excecaoCriarFonte(
					"O campo rendimentos recebidos deve ser maior que zero");
		}


		if (fonte.getCpfCnpj() == null) {
			throw new excecaoCriarFonte("O campo CPF/CNPJ é obrigatório");
		} else if (!fonte.getCpfCnpj().matches(
				"[\\d]{2}\\.[\\d]{3}\\.[\\d]{3}\\/[\\d]{4}\\-[\\d]{2}")) {
			throw new excecaoCriarFonte("O campo CPF/CNPJ é inválido");
		}
	
		if (titulares.containsKey(titular) == false) {
			throw new excecaoCriarFonte("Titular não cadastrado");
		}
	
		ArrayList<FontePagadora> fontesDoTitular = (ArrayList<FontePagadora>) titulares
				.get(titular);
		fontesDoTitular.add(fonte);
	}

	public List<FontePagadora> listarFontes(Titular titular) {
		return new ArrayList<FontePagadora>(titulares.get(titular));
	}

	public void criarDependente(Titular titular, Dependente dependente) {
	
		if (dependente.getTipo() < 0) {
			throw new excecaoCriarDependente("O campo tipo é inválido");
			
		}else if (dependente.getCpf().matches("\\d\\d\\d.\\d\\d\\d.\\d\\d\\d-\\d\\d") == false) {
			throw new excecaoCriarDependente("O campo CPF é inválido");
			
		}else if (dependente.getTipo() == 0) {
			throw new excecaoCriarDependente("O campo tipo é obrigatório");
			
		}
		else if (dependente.getNome() == null) {
			throw new excecaoCriarDependente("O campo nome é obrigatório");
			
		}else if (dependente.getCpf() == null) {
			throw new excecaoCriarDependente("O campo CPF é obrigatório");
			
		}if (dependentes.containsKey(titular)) {
			ArrayList<Dependente> dependentesDoTitular = (ArrayList<Dependente>) dependentes.get(titular);
			dependentesDoTitular.add(dependente);
			dependentes.put(titular, dependentesDoTitular);
		}
		
		if (dependentes.size()== 0 ){
			throw new excecaoCriarDependente("Titular não cadastrado");
		}
		
	}

	public List<Dependente> listarDependentes(Titular titular) {
		return dependentes.get(titular);
	}

	public Resultado declaracaoCompleta(Titular titular) {
		
		double aliquota = 0;
		double parcelaDeDeducao = 0;
		double impostoDevido = 0;
		Resultado resultado = new Resultado();
		double somatorioDeRendimentos = 0;
		for (FontePagadora fp : listarFontes(titular)) {
			somatorioDeRendimentos += fp.getRendimentoRecebidos();
		}
		if (somatorioDeRendimentos < 19645.33) {
			aliquota = 0;
			parcelaDeDeducao = 0;
		} else if (somatorioDeRendimentos >= 19645.33
				&& somatorioDeRendimentos <= 29442.0) {
			aliquota = 7.5 / 100;
			parcelaDeDeducao = 1473.4;
		} else if (somatorioDeRendimentos >= 29442.01
				&& somatorioDeRendimentos <= 39256.56) {
			aliquota = 15.0 / 100;
			parcelaDeDeducao = 3681.55;
		} else if (somatorioDeRendimentos >= 39256.57
				&& somatorioDeRendimentos <= 49051.8) {
			aliquota = 22.5 / 100;
			parcelaDeDeducao = 6625.79;
		} else if (somatorioDeRendimentos > 49051.80) {
			aliquota = 27.5 / 100;
			parcelaDeDeducao = 9078.38;
		}
		
		impostoDevido = (somatorioDeRendimentos * aliquota) - parcelaDeDeducao;
		resultado.setImpostoDevido(impostoDevido);
		resultado.setImpostoDevido(this.calcularImpostoDevido(titular));
		return resultado;
	}

	public int calcularFaixa(double totalRendimentos) {
		if (totalRendimentos < 19645.33) {
			return 1;
		} else if (totalRendimentos >= 19645.33 && totalRendimentos < 29442.01) {
			return 2;
		} else if (totalRendimentos >= 29442.01 && totalRendimentos < 39256.57) {
			return 3;
		} else if (totalRendimentos >= 39256.57 && totalRendimentos < 49051.9) {
			return 4;
		} else if (totalRendimentos > 49051.80) {
			return 5;
		}
		return 0;
	}

	public double calcularImpostoDevido(Titular titular) {
		double aliquota = 0, parcelaDeducao = 0, totalRendimentos = 0, deducaoPorDependente = 0;
		for (FontePagadora fp : this.listarFontes(titular)) {
			totalRendimentos += fp.getRendimentoRecebidos();
		}
		deducaoPorDependente = 1974.72 * this.listarDependentes(titular).size();
		totalRendimentos -= deducaoPorDependente;
		switch (this.calcularFaixa(totalRendimentos)) {
		case 1:
			aliquota = 0;
			parcelaDeducao = 0;
			break;
		case 2:
			aliquota = 7.5 / 100;
			parcelaDeducao = 1473.36;
			break;
		case 3:
			aliquota = 15.0 / 100;
			parcelaDeducao = 3681.60;
			break;
		case 4:
			aliquota = 22.5 / 100;
			parcelaDeducao = 6625.80;
			break;
		case 5:
			aliquota = 27.5 / 100;
			parcelaDeducao = 9078.36;
			break;
		default:
			aliquota = 0;
			parcelaDeducao = 0;
			break;
		}
		return (totalRendimentos * aliquota) - parcelaDeducao;
	}

}