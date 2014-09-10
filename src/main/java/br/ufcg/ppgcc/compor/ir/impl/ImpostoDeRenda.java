package br.ufcg.ppgcc.compor.ir.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ufcg.ppgcc.compor.ir.impl.excecaoCriarTitular;
import br.ufcg.ppgcc.compor.ir.Dependente;
import br.ufcg.ppgcc.compor.ir.FachadaExperimento;
import br.ufcg.ppgcc.compor.ir.FontePagadora;
import br.ufcg.ppgcc.compor.ir.Titular;
import br.ufcg.ppgcc.compor.ir.impl.excecaoCriarFonte;

public class ImpostoDeRenda implements FachadaExperimento{

	private Map<Titular, List<FontePagadora>> titulares = new LinkedHashMap<Titular, List<FontePagadora>>();
	private Map<Titular, List<Dependente>> titDependente = new LinkedHashMap<Titular, List<Dependente>>();
		
	public void criarNovoTitular(Titular titular){
		
		if(titular.getNome() == null ){
			throw new excecaoCriarTitular("O campo nome é obrigatório");
		}else if(titular.getCpf() == null){
			throw new excecaoCriarTitular("O campo CPF é obrigatório");
		
		}else if(titular.getCpf().length() != 14 ){
			throw new excecaoCriarTitular("O campo CPF está inválido");
		}
		
		titulares.put(titular, new ArrayList<FontePagadora>());
	}

	public List<Titular> listarTitulares() {
		return new ArrayList<Titular>(titulares.keySet());
	}

	public void criarFontePagadora(Titular titular, FontePagadora fonte) {
		if (fonte.getNome() == null) {
			throw new excecaoCriarFonte("O campo nome é obrigatório");
		}else if (fonte.getRendimentoRecebidos() == 0.0) {
			throw new excecaoCriarFonte("O campo rendimentos recebidos é obrigatório");
		}else if (fonte.getRendimentoRecebidos() < 0.0) {
			throw new excecaoCriarFonte("O campo rendimentos recebidos deve ser maior que zero");
		}else if (fonte.getCpfCnpj() == null) {
			throw new excecaoCriarFonte("O campo CPF/CNPJ é obrigatório");
		}else if (!fonte.getCpfCnpj().matches("[\\d]{2}\\.[\\d]{3}\\.[\\d]{3}\\/[\\d]{4}\\-[\\d]{2}")) {
			throw new excecaoCriarFonte("O campo CPF/CNPJ é inválido");
		}else if (titulares.size()== 0){
			throw new excecaoCriarFonte("Titular não cadastrado");
		}
		ArrayList<FontePagadora> fontesDoTitular = (ArrayList<FontePagadora>) titulares.get(titular);
			fontesDoTitular.add(fonte);
		}

	public List<FontePagadora> listarFontes(Titular titular) {
		return new ArrayList<FontePagadora>(titulares.get(titular));
	}

	public List<Dependente> listarDependentes(Titular titular) {
		// T.03.03
		return new ArrayList<Dependente> (titDependente.get(titular));
	}

	public void criarDependente(Titular titular, Dependente dependente) {
		//T.03.03
		if(titDependente.containsKey(titular)){
			List<Dependente> listDependente = titDependente.get(titular);
			listDependente.add(dependente);
			titDependente.put(titular, listDependente);
		}
	}
}

