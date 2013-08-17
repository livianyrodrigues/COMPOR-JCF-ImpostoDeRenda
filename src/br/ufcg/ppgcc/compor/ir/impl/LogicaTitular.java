package br.ufcg.ppgcc.compor.ir.impl;

import java.util.ArrayList;
import java.util.List;

import br.ufcg.ppgcc.compor.ir.fachada.Titular;

public class LogicaTitular {

	private List<Titular> titulares = new ArrayList<Titular>();

	public void criarNovoTitular(Titular titular) {
		Validacao.obrigatorio(titular.getNome(),
				"O campo nome é obrigatório para o titular");
		Validacao.obrigatorio(titular.getCpf(),
				"O campo CPF é obrigatório para o titular");
		titulares.add(titular);
	}

	public List<Titular> listarTitulares() {
		return titulares;
	}

}