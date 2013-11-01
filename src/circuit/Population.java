package circuit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
/**
 * 	Classe usada para a representa��o de uma popula��o.
 */
public class Population {
	
	private boolean corrupt;
	private List<Individual> pop;
	private List<Double> acum;
	private Individual bestInd;
	private Individual worstInd;
	private int size;
	private int sumOfFitness;
	private double worstFit;
	private double bestFit;

	
	/**
	 * 	Construtor relativo � classe Population
	 */
	
	public Population() {
		pop = new ArrayList<Individual>();
		bestInd = null;
		size = 0;
		sumOfFitness = 0;
	}
	
	/**
	 * 	Construtor onde se especifica a popula��o
	 * @param p um array de indiv�duos
	 */
	
	public Population(Individual[] p) {
		
		for(int i = 0; i < p.length; i++) {
			 addIndividual(p[i]);
		}
		
	}
	
	/**
	 * Selecciona e devolve um indiv�duo da popula��o, tendo em conta a sua fitness
	 * @return um indiv�duo
	 */
	public Individual selectIndividual() {
		
		// Verifica se necessita de calcular os valores de probabilidade de selec��o de cada indiv�duo
		if( corrupt ) {
			double total=0.0;
			for(int i=0; i < pop.size(); i++) {
				total += 1/pop.get(i).fitness();
				acum.add(total/sumOfFitness);
			}
			corrupt = false;
		}
		
		Random gen = new Random();
		
		double r = gen.nextDouble();
		int pos = Collections.binarySearch(acum, r);
		
		if( pos >= 0)
			return pop.get(pos);
		else
			return pop.get(-(pos+1));
		
	}
	
	/**
	 * Adiciona um indiv�duo � popula��o
	 * @param ind, um indiv�duo
	 */
	public void addIndividual(Individual ind) {
		size++;
		pop.add(ind);
		double f = ind.fitness();
		sumOfFitness += 1/f; 
		if( f > worstFit ) {
			worstFit = f;
			worstInd = ind;
		}
		if( f < bestFit ) {
			bestFit = f;
			bestInd = ind;
		}
	}
	
	public Individual getBestIndividual() {		
		return bestInd;
	}
	
	public Individual getWorstIndividual() {		
		return worstInd;
	}
	
	public int getSize() {
		return size;
	}

}
