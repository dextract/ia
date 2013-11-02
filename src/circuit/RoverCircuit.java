package circuit;

import java.util.*;

import circuit.ObservationData.*;

/**
 * 	Classe que instancia a classe abstracta Individual
 */
public class RoverCircuit extends Individual {
	
	
	//constantes para definir opçoes de crossover
	public final static int OPTION_OX1 = 0;
	public final static int OPTION_OX2 = 1;
	public final static int OPTION_PMX = 2;
	public final static int OPTION_CX = 3;
	//constantes para definir opçoes de mutaçao
	public final static int OPTION_REVERSE = 0;
	public final static int OPTION_INSERT = 1;
	public final static int OPTION_SWAP = 2;
	
	
	private List<Integer> spots; //indices relativos aos pontos a serem visitados 
	private ObservationData data; //informaçao relativa aos pontos que serao visitados
	private int nChildren = 2; //por defeito, cada crossover dá origem a 2 filhos
	
	Individual[] children = new Individual[nChildren]; //vector representante dos filhos, a ser utilizado nas funçoes de crossover
	RoverCircuit secondParent; //objecto rovercircuit, a ser utilizado nas funçoes de crossover
	Random rg = new Random(); 
	List<Integer> childCircuit1;
	List<Integer> childCircuit2;
	int fIndex;
	int sIndex; 
	int crossoverOption = OPTION_OX1; //OX1 por defeito 
	int mutationOption = OPTION_INSERT; //inserção por defeito
	
	public RoverCircuit(ObservationData data) {
		initIndividual(data);
		this.data = data;
	}
	
	public RoverCircuit(ObservationData data, List<Integer> rSpots) {
		this.spots = rSpots;
		this.data = data;
	}
	
	private void initIndividual(ObservationData d) {
		spots = new ArrayList<Integer>(d.getSize()+1);	
		for(int i = 0; i < d.getSize(); i++) { spots.add(i); }	
		Collections.shuffle(spots);	
	}
	
	@Override
	public double fitness() {
		
		int time = data.getSpot(spots.get(0)).firstTime(); //tempo a que começa a expediçao
		int i;
		
		for(i = 0; i < spots.size() - 1; i++) {
			time += data.getCost(spots.get(i), spots.get(i+1)) + data.getSpot(spots.get(i)).durationObservation(time); 
		}
		
		time += data.getCost(spots.get(i), spots.get(0)) + data.getSpot(0).durationObservation(time);
		
		return time;	
	}
	
	@Override
	public Individual[] crossover(Individual other) {
			
		switch(crossoverOption) {		
			case OPTION_OX1:
				return OXCrossover1(other);
			case OPTION_OX2:
				return OXCrossover2(other);
			case OPTION_PMX:
				return PMXCrossover(other);
			case OPTION_CX:
				return CXCrossover(other);
		}	
		
		return null;
	}

	@Override
	public void mutate() {
		
		switch(mutationOption) {	
			case 0:			
				reverseMutation();
				break;
			case 1:
				insertMutation();
				break;
			case 2:
				swapMutation();
				
		}
	}
	
	private Individual[] OXCrossover1(Individual rc2) {
		
		childCircuit1 = new ArrayList<Integer>(spots.size());
		childCircuit2 = new ArrayList<Integer>(spots.size());
		secondParent = (RoverCircuit) rc2;
		fIndex = rg.nextInt(spots.size()-2);
		sIndex = rg.nextInt(spots.size()-fIndex) + fIndex+1;
		boolean done = false;

		//inicializaçao dos filhos tendo em conta a subsequencia criada
		for(int i = 0; i < spots.size(); i++) {
			
			childCircuit1.add(-1);
			childCircuit2.add(-1);	
			
				if(i >= fIndex && i < sIndex) {
					childCircuit1.set(i, spots.get(i));
					childCircuit2.set(i, secondParent.spots.get(i));				
				}			
		}
	
		 
		//primeiro filho
		for(int k = sIndex, l = k; !done;) {
						
			if(k == spots.size()) //aux que percorre parent 1 volta ao inicio
				k = 0;
			if(l == spots.size()) //aux que percorre parent 2 volta ao inicio
				l = 0;
			if(k == fIndex) //terminou o ciclo, já se preencheram todas as posições
				done=true;
			
			if(childCircuit1.get(k) == -1) { //se ainda nao foi preenchido
				
				if(!childCircuit1.contains(secondParent.spots.get(l))) {
					childCircuit1.set(k, secondParent.spots.get(l));
					k++;
				} 
				else
					l++; 
			}
			else {
				k++;
			}			
			
		}
		
		done = false;
		
		//segundo filho
		for(int k = sIndex, l = k; !done;) {
			
			if(k == fIndex)
				done = true;
			if(k == spots.size()) 
				k = 0;
			if(l == spots.size())
				l = 0;
					
			if(childCircuit2.get(k) == -1) {			
				if(!childCircuit2.contains(spots.get(l))) {		
					childCircuit2.set(k, spots.get(l));	
					k++;
				}
				else 
					l++;
			}
			else
				k++;			
		}
		
		System.out.println(fIndex + " " + sIndex);
		for(int i = 0; i < childCircuit1.size(); i++) {
			System.out.print(childCircuit1.get(i) + " ");
		}
		System.out.println();
		for(int i = 0; i < childCircuit2.size(); i++) {
			System.out.print(childCircuit1.get(i) + " ");
		}
		System.out.println();
		for(int i = 0; i < spots.size(); i++) {
			System.out.print(secondParent.spots.get(i) + " ");
		}
		System.out.println();
		for(int i = 0; i < spots.size(); i++) {
			System.out.print(spots.get(i) + " ");
		}
		
		children[0] = new RoverCircuit(this.data, childCircuit1);
		children[1] = new RoverCircuit(this.data, childCircuit2);
		
		return children;		
	}
	
	private Individual[] OXCrossover2(Individual rc2) {
		
		childCircuit1 = new ArrayList<Integer>(spots.size());
		childCircuit2 = new ArrayList<Integer>(spots.size());
		secondParent = (RoverCircuit) rc2;
		fIndex = rg.nextInt(spots.size()-1);
		sIndex = rg.nextInt(spots.size()-fIndex) + fIndex+1;
		
		//inicializaçao dos filhos tendo em conta a subsequencia criada
		for(int i = 0; i < spots.size(); i++) {
			
			childCircuit1.add(i, -1);
			childCircuit2.add(i, -1);	
			
			if(i >= fIndex && i < sIndex) {
				childCircuit1.set(i, spots.get(i));
				childCircuit2.set(i, secondParent.spots.get(i));				
			}
		}
		
		//primeiro filho
		for(int k = 0, l = 0; k < spots.size();) {
			
			if(l==spots.size())
				l = 0;
			
			if(childCircuit1.get(k) == -1) {
				
				if(!childCircuit1.contains(secondParent.spots.get(l))) {
					childCircuit1.set(k, secondParent.spots.get(l));
					k++;
				}			
				else 
					l++;
			}
			else
				k++;		
		}
	
		//segundo filho
		for(int k = 0, l = 0; k < spots.size();) {				
			if(childCircuit2.get(k) == -1) {			
				if(!childCircuit2.contains(spots.get(l))) {		
					childCircuit2.set(k, spots.get(l));	
					k++;
				}
				else 
					l++;
			}
			else
				k++;		
		}
		
		children[0] = new RoverCircuit(this.data, childCircuit1);
		children[1] = new RoverCircuit(this.data, childCircuit2);
		
		return children;
	}
	
	private Individual[] PMXCrossover(Individual rc2) {
		
		childCircuit1 = new ArrayList<Integer>(spots.size());
		childCircuit2 = new ArrayList<Integer>(spots.size());
		secondParent = (RoverCircuit) rc2;
		fIndex = rg.nextInt(spots.size()-1);
		sIndex = rg.nextInt(spots.size()-fIndex) + fIndex+1;
		int[] eqMap = new int[spots.size()];
		
		//inicializaçao dos filhos tendo em conta a subsequencia criada
		for(int i = 0; i < spots.size(); i++) {		
			childCircuit1.add(i, -1);
			childCircuit2.add(i, -1);
			eqMap[i] = -1;
			
			if(i >= fIndex && i < sIndex) {
				childCircuit1.set(i, secondParent.spots.get(i));
				childCircuit2.set(i, spots.get(i));
				eqMap[childCircuit2.get(i)] = childCircuit1.get(i);
			}

		}
		
		//primeiro filho
		for(int i = 0; i < spots.size(); i++) {			
			if(childCircuit1.get(i) == -1) {
				
				if(!childCircuit1.contains(spots.get(i))) {
					childCircuit1.set(i, spots.get(i));
				}	
				else 
				{
					for(int j = 0; j < eqMap.length; j++) {
						if(spots.get(i) == eqMap[j])
							childCircuit1.set(i, j);
					}
				}

			}		
		}
		
		//segundo filho
		for(int i = 0; i < spots.size(); i++) {
					
			if(childCircuit2.get(i) == -1) {
				if(!childCircuit2.contains(secondParent.spots.get(i))) {
					childCircuit2.set(i, secondParent.spots.get(i));
				}	
				else 
				{
					for(int j = 0; j < eqMap.length; j++) {
						if(secondParent.spots.get(i) == j)
							childCircuit2.set(i, eqMap[j]);
					}
				}
			}
		}
		
		children[0] = new RoverCircuit(data, childCircuit1);
		children[1] = new RoverCircuit(data, childCircuit2);
		return children;
	}
	
	private Individual[] CXCrossover(Individual rc2) {
			
		childCircuit1 = new ArrayList<Integer>(spots.size());
		childCircuit2 = new ArrayList<Integer>(spots.size());
		secondParent = (RoverCircuit) rc2;
		int ind = 0;
		int val = 0;
	
		for(int i = 0; i < this.spots.size(); i++) { //inicializar filhos
			childCircuit1.add(i, -1);
			childCircuit2.add(i, -1);
		}
		
		//primeiro filho
		for(int i = 0; i < this.spots.size(); i++) {
			
			if(childCircuit1.get(ind) == -1) {
				childCircuit1.set(ind, spots.get(ind));
				val = childCircuit2.get(ind);
				
				for(int j = 0; j < spots.size(); j++) {			
					if(spots.get(j) == val) {
						ind = j;
					    break;
					}			
				}
			} else {
			
				for(int k = 0; k < secondParent.spots.size(); k++) {
					if(childCircuit1.get(k) == -1)
						childCircuit1.set(k, secondParent.spots.get(k));
				}			
			}		
		}
		
		//segundo filho
		for(int i = 0; i < secondParent.spots.size(); i++) {
			
			if(childCircuit2.get(ind) == -1) {
				childCircuit2.set(ind, secondParent.spots.get(ind));
				val = childCircuit1.get(ind);
				
				for(int j = 0; j < secondParent.spots.size(); j++) {			
					if(secondParent.spots.get(j) == val) {
						ind = j;
					    break;
					}			
				}
			} else {
			
				for(int k = 0; k < spots.size(); k++) {
					if(childCircuit2.get(k) == -1)
						childCircuit2.set(k, spots.get(k));
				}			
			}		
		}
		
		children[0] = new RoverCircuit(this.data, childCircuit1);
		children[1] = new RoverCircuit(this.data, childCircuit2);
		return children;
	}
	
	private void reverseMutation() { //inversao
		
		Random rg = new Random();
		fIndex = rg.nextInt(spots.size()-1);
		sIndex = rg.nextInt(spots.size()-fIndex) + fIndex+1;
		int tmp = 0;
				
		for(int i = fIndex, j = sIndex - 1; i < j; i++, j--) {
			tmp = spots.get(i);
			spots.set(i, spots.get(j));
			spots.set(j, tmp);
		}	
	}
	
	private void insertMutation() { //inserçao
		
		Random rg = new Random();
		int tmp = 0;
		
		fIndex = rg.nextInt(data.getSize());
		sIndex = rg.nextInt(data.getSize());
		
		while(fIndex == sIndex) { //gerar dois indices diferentes
			sIndex = rg.nextInt(data.getSize());
		}
				
		if(fIndex < sIndex) {		
			tmp = spots.get(fIndex);					
			for(int i = fIndex; i < sIndex; i++) {
				spots.set(i, spots.get(i+1));
			}
			spots.set(sIndex, tmp);
		}
		else {
			tmp = spots.get(fIndex);					
			for(int i = fIndex; i > fIndex; i--) {
				spots.set(i, spots.get(i-1));
			}
			spots.set(sIndex, tmp);
		}
	}
	
	private void swapMutation() { //troca
		
		Random rg = new Random();		
		int tmp = 0;
		
		fIndex = rg.nextInt(data.getSize());
		sIndex = rg.nextInt(data.getSize());
		
		while(fIndex == sIndex) { //gerar dois indices diferentes
			sIndex = rg.nextInt(data.getSize());
		}
		
		tmp = spots.get(fIndex);
		spots.set(fIndex, spots.get(sIndex));
		spots.set(sIndex, tmp);		
	}
	
	@Override
	public Object clone() {
		return new RoverCircuit(this.data,this.spots);
	}
	
	public String toString() {
		return spots.toString();
	}
}