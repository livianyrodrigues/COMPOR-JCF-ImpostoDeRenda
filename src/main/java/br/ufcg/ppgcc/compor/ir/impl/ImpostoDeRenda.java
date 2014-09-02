package br.ufcg.ppgcc.compor.ir.impl;

import java.util.ArrayList;
import java.util.List;


import br.ufcg.ppgcc.compor.ir.FachadaExperimento;
import br.ufcg.ppgcc.compor.ir.Titular;

public class ImpostoDeRenda implements FachadaExperimento{

	List<Titular> titulares = new ArrayList<Titular>();
	
	public void criarNovoTitular(Titular titular){
		
		titulares.add(titular);
				
	}

	public List<Titular> listarTitulares() {
		// TODO Auto-generated method stub
		return titulares;
	}

	

	
}

