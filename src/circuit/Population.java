package circuit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
/**
 * 	Classe usada para a representação de uma população.
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
	 * 	Construtor relativo à classe Population
	 */
	
	public Population() {
		init();
	}
	
	/**
	 * 	Construtor onde se especifica a população
	 * @param p um array de indivíduos
	 */
	
	public Population(Individual[] p) {	
		init();
		for(int i = 0; i < p.length; i++) {
			 addIndividual(p[i]);
		}		
	}
	
	private void init() {
		pop = new ArrayList<Individual>();
		size = 0;
		sumOfFitness = 0;
		bestInd = null;
		acum = new ArrayList<Double>();
		bestFit = 0;
		worstFit = 0;
		bestInd = null;
		worstInd = null;
	}
	
	/**
	 * Selecciona e devolve um indivíduo da população, tendo em conta a sua fitness
	 * @return um indivíduo
	 */
	public Individual selectIndividual() {
		
		// Verifica se necessita de calcular os valores de probabilidade de selecção de cada indivíduo
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

	public void removeIndividual(Individual ind) {
		size--;
		pop.remove(ind);
		double f = ind.fitness();
		sumOfFitness -= 1/f;
		if(f==bestFit) {
			bestFit=worstFit;
			for(Individual i : pop) {
				if(i.fitness()<=bestFit) {
					bestFit = i.fitness();
					bestInd = i;
				}
			}
		}
		if(f==worstFit) {
			worstFit=bestFit;
			for(Individual i : pop) {
				if(i.fitness()>=worstFit) {
					worstFit = i.fitness();
					worstInd = i;
				}
			}
		}
	}
	
	
	/**
	 * Adiciona um indivíduo à população
	 * @param ind, um indivíduo
	 */
	public void addIndividual(Individual ind) {
		size++;
		pop.add(ind);
		double f = ind.fitness();
		sumOfFitness += 1/f; 
		if(size == 1) {
			bestFit = worstFit = f;
			bestInd = ind;
			worstInd = ind;
		}
		else
			if( f < bestFit ) {
				bestFit = f;
				bestInd = ind;
			}
			if( f > worstFit ) {
				worstFit = f;
				worstInd = ind;
			}
	}
	
	public int getSize() {
		return size;
	}
	
	public Individual getBestIndividual() {		
		return bestInd;
	}
	
	public Individual getWorstIndividual() {		
		return worstInd;
	}
	
	public String toString() {
		String s = "";
	  
		for(Individual i: pop) {
			s += i.toString() + " | Fitness: " + i.fitness() + "\n";
		}
	  
		return s;
	}
	
}
