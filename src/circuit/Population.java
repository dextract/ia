package circuit;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
/**
 * 	Classe usada para a representação de uma população.
 */
public class Population {
	
	/**
	 * 	Construtor relativo à classe Population
	 */
	
	/**
	 * 	Construtor onde se especifica a popolação
	 * @param p um array de indivíduos
	 */
	
	/**
	 * Selecciona e devolve um indivíduo da população, tendo em conta a sua fitness
	 * @return um array de indivíduos
	 */
	public Individual selectIndividual() {
		
		// Verifica se necessita de calcular os valores de probabilidade de selecção de cada indivíduo
		if( currupt ) {
			double total=0.0;
			for(int i=0; i < pop.size(); i++) {
				total += 1/pop.get(i).fitness();
				acum.add(total/sumOfFitness);
			}
			currupt = false;
		}
		
		double r = gen.nextDouble();
		int pos = Collections.binarySearch(acum, r);
		
		if( pos >= 0)
			return pop.get(pos);
		else
			return pop.get(-(pos+1));
		
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
		if( f > worstFit ) {
			worstFit = f;
			worstInd = ind;
		}
		if( f < bestFit ) {
			bestFit = f;
			bestInd = ind;
		}
	}
	

}
