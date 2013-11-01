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
	public final static int OPTION_DISPLACEMENT = 2;
	public final static int OPTION_SWAP = 3;
	
	
	private List<Integer> spots; //indices relativos aos pontos a serem visitados 
	private ObservationData data; //informaçao relativa aos pontos que serao visitados
	private int nChildren = 2; //por defeito, cada crossover dá origem a 2 filhos
	
	Individual[] children = new Individual[nChildren]; //vector representante dos filhos, a ser utilizado nas funçoes de crossover
	RoverCircuit secondParent; //objecto rovercircuit, a ser utilizado nas funçoes de crossover
	Random rg = new Random(); 
	List<Integer> childCircuit1 = new ArrayList<Integer>(spots.size());
	List<Integer> childCircuit2 = new ArrayList<Integer>(spots.size());
	int fIndex;
	int sIndex; 
	int crossoverOption = OPTION_OX1; //OX1 por defeito DEFINIR CONSTANTES DEPOIS
	int mutationOption = OPTION_INSERT; //inserção por defeito DEFINIR CONSTANTES DEPOIS
	
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
		spots.add(spots.get(0)); //o rover volta ao ponto inicial
	}
	
	@Override
	public double fitness() {
		
		int time = data.getSpot(spots.get(0)).firstTime(); //tempo a que começa a expediçao
		
		for(int i = 0; i < spots.size() - 1; i++) {
			time += data.getCost(spots.get(i), spots.get(i+1)) + data.getSpot(spots.get(i)).durationObservation(time); 
		}

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
			case 1:
				insertMutation();
			case 2:
				displMutation();
			case 3:
				swapMutation();
		}
	}
	
	private Individual[] OXCrossover1(Individual rc2) {
		
		secondParent = (RoverCircuit) rc2;
		fIndex = rg.nextInt(data.getSize()/2);
		sIndex = rg.nextInt(data.getSize()/2) + data.getSize()/2 + 1;
		boolean done = false;
		
		while(sIndex - fIndex <= 1) //subsequencia tem de ter pelo menos 2 pontos
			sIndex = rg.nextInt(data.getSize()/2) + data.getSize()/2;	
		
		//inicializaçao dos filhos tendo em conta a subsequencia criada
		for(int i = 0; i < spots.size(); i++) {
				if(i >= fIndex && i < sIndex) {
					childCircuit1.add(i, spots.get(i));
					childCircuit2.add(i, secondParent.spots.get(i));				
				}
				else {
					childCircuit1.add(i, -1);
					childCircuit2.add(i, -1);	
				}
		}
				
		//primeiro filho
		for(int k = sIndex, l = k; !done;) {
			
			if(k == spots.size()) //aux que percorre parent 1 volta ao inicio
				k = 0;
			if(l == spots.size()) //aux que percorre parent 2 volta ao inicio
				l = 0;
			if(k == fIndex) //terminou o ciclo, já se preencheram todas as posições
				done = true;
			
			if(childCircuit1.get(k) == -1) {
				
				if(!childCircuit1.contains(secondParent.spots.get(l))) {
					childCircuit1.add(k, secondParent.spots.get(l));
					k++;
				}
			
				l++;
			}
			else
				k++;
			
		}
	
		done = false;
		
		//segundo filho
		for(int k = sIndex, l = k; !done;) {
			
			if(k == spots.size()) 
				k = 0;
			if(l == spots.size())
				l = 0;
			if(k == fIndex)
				done = true;			
			if(childCircuit2.get(k) == -1) {			
				if(!childCircuit2.contains(spots.get(l))) {		
					childCircuit2.add(k, spots.get(l));	
					k++;
				}
				l++;
			}
			else
				k++;			
		}
		
		children[0] = new RoverCircuit(this.data, childCircuit1);
		children[1] = new RoverCircuit(this.data, childCircuit2);
		
		return children;		
	}
	
	private Individual[] OXCrossover2(Individual rc2) {
		
		secondParent = (RoverCircuit) rc2;
		int fIndex = rg.nextInt(spots.size()/2);
		int sIndex = rg.nextInt(spots.size()) + spots.size()/2 + 1;
			
		while(sIndex - fIndex <= 1) //subsequencia tem de ter pelo menos 2 pontos
			sIndex = rg.nextInt(spots.size()/2) + spots.size()/2;	
		
		//inicializaçao dos filhos tendo em conta a subsequencia criada
		for(int i = 0; i < spots.size(); i++) {
			if(i >= fIndex && i < sIndex) {
				childCircuit1.add(i, spots.get(i));
				childCircuit2.add(i, secondParent.spots.get(i));				
			}
			else {
				childCircuit1.add(i, -1);
				childCircuit2.add(i, -1);	
			}
		}
		
		//primeiro filho
		for(int k = 0, l = 0; k < spots.size();) {		
			if(childCircuit1.get(k) == -1) {
				
				if(!childCircuit1.contains(secondParent.spots.get(l))) {
					childCircuit1.add(k, secondParent.spots.get(l));
					k++;
				}
			
				l++;
			}
			else
				k++;		
		}
	
		//segundo filho
		for(int k = 0, l = 0; k < spots.size();) {				
			if(childCircuit2.get(k) == -1) {			
				if(!childCircuit2.contains(spots.get(l))) {		
					childCircuit2.add(k, spots.get(l));	
					k++;
				}
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
		secondParent = (RoverCircuit) rc2;
		fIndex = rg.nextInt(data.getSize()/2);
		sIndex = rg.nextInt(data.getSize()/2) + data.getSize()/2 + 1;
		int[] eqMap = new int[spots.size()];
		
		while(sIndex - fIndex <= 1) //subsequencia tem de ter pelo menos 2 pontos
			sIndex = rg.nextInt(spots.size()/2) + spots.size()/2;	
	
		
		//inicializaçao dos filhos tendo em conta a subsequencia criada
		for(int i = 0; i < spots.size(); i++) {
			if(i >= fIndex && i < sIndex) {
				childCircuit1.add(i, secondParent.spots.get(i));
				childCircuit2.add(i, spots.get(i));
				eqMap[childCircuit2.get(i)] = childCircuit1.get(i);
			}
			else {
				childCircuit1.add(i, -1);
				childCircuit2.add(i, -1);
				eqMap[i] = -1;
			}
		}
		
		//primeiro filho
		for(int i = 0; i < spots.size(); i++) {			
			if(childCircuit1.get(i) == -1) {
				
				if(!childCircuit1.contains(spots.get(i))) {
					childCircuit1.add(i, spots.get(i));
				}	
				else 
				{
					for(int j = 0; j < eqMap.length; j++) {
						if(spots.get(i) == eqMap[j])
							childCircuit1.add(i, j);
					}
				}

			}		
		}
		
		//segundo filho
		for(int i = 0; i < spots.size(); i++) {
					
			if(childCircuit2.get(i) == -1) {
				if(!childCircuit2.contains(secondParent.spots.get(i))) {
					childCircuit2.add(i, secondParent.spots.get(i));
				}	
				else 
				{
					for(int j = 0; j < eqMap.length; j++) {
						if(secondParent.spots.get(i) == j)
							childCircuit2.add(i, eqMap[j]);
					}
				}
			}
		}
		
		children[0] = new RoverCircuit(data, childCircuit1);
		children[1] = new RoverCircuit(data, childCircuit2);
		return children;
	}
	
	private Individual[] CXCrossover(Individual rc2) {
			
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
				childCircuit1.add(ind, spots.get(ind));
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
						childCircuit1.add(k, secondParent.spots.get(k));
				}			
			}		
		}
		
		//segundo filho
		for(int i = 0; i < secondParent.spots.size(); i++) {
			
			if(childCircuit2.get(ind) == -1) {
				childCircuit2.add(ind, secondParent.spots.get(ind));
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
						childCircuit2.add(k, spots.get(k));
				}			
			}		
		}
		
		children[0] = new RoverCircuit(this.data, childCircuit1);
		children[1] = new RoverCircuit(this.data, childCircuit2);
		return children;
	}
	
	private void reverseMutation() { //inversao
		
		Random rg = new Random();
		int fIndex = rg.nextInt(data.getSize()/2);
		int sIndex = rg.nextInt(data.getSize()/2) + data.getSize()/2 + 1;
		int tmp = 0;
		
		//adicionar mais verificaçoes consoante a qualidade do individuo
		while(sIndex - fIndex <= 1) //subsequencia tem de ter pelo menos 2 pontos
			sIndex = rg.nextInt(data.getSize()/2) + data.getSize()/2;	
				
		for(int i = fIndex, j = sIndex - 1; i < j; i++, j--) {
			tmp = spots.get(i);
			spots.add(i, spots.get(j));
			spots.add(j, tmp);
		}	
	}
	
	private void insertMutation() { //inserçao
		
		Random rg = new Random();
		int fIndex; //indice cujo valor vamos retirar
		int sIndex; //indice no qual vamos colocar o valor que retiramos em fIndex
		int tmp = 0;
		
		fIndex = rg.nextInt(data.getSize()-1);
		sIndex = rg.nextInt(data.getSize()-1);
		
		while(fIndex == sIndex) { //gerar dois indices diferentes
			sIndex = rg.nextInt(data.getSize()-1);
		}
				
		if(fIndex < sIndex) {		
			tmp = spots.get(fIndex);					
			for(int i = fIndex; i < sIndex; i++) {
				spots.add(i, spots.get(i+1));
			}
			spots.add(sIndex, tmp);
		}
		else {
			tmp = spots.get(fIndex);					
			for(int i = fIndex; i > fIndex; i--) {
				spots.add(i, spots.get(i-1));
			}
			spots.add(sIndex, tmp);
		}
	}
	
	private void displacementMutation() { //deslocamento
	//fazer	
	}
	
	private void swapMutation() { //troca
		
		Random rg = new Random();		
		int fIndex; 
		int sIndex;
		int tmp = 0;
		
		fIndex = rg.nextInt(data.getSize()-1);
		sIndex = rg.nextInt(data.getSize()-1);
		
		while(fIndex == sIndex) { //gerar dois indices diferentes
			sIndex = rg.nextInt(data.getSize()-1);
		}
		
		tmp = spots.get(fIndex);
		spots.add(fIndex, spots.get(sIndex));
		spots.add(sIndex, tmp);		
	}
	
	@Override
	public Object clone() {
		return new RoverCircuit(this.data,this.spots);
	}
}