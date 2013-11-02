package circuit;

import java.io.*;

public class circuitTest {
	
	private static String data = "";
	private static ObservationData obsData;
	private static int popSize = 20;
	private static Population pop;
	private static GeneticAlgorithm ga;
	private static float pMutate = (float) 0.01;
	private static float pCrossover = (float) 0.9;

	public static void main(String[] args) {	
		
		try{
			  FileInputStream fstream = new FileInputStream("cincolinha.txt");
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String input;
			  while ((input = br.readLine()) != null)   {
				  data += input + '\n';
			  }
			  
			  in.close();
		}catch (Exception e){
			  System.err.println("Erro ao abrir ficheiro.");
		}
		
		//System.out.println(data);
		obsData = new ObservationData(data);
		initPop();
		ga = new GeneticAlgorithm(pop, pCrossover, pMutate);
		
		RoverCircuit best = (RoverCircuit) ga.search();
		
		System.out.println(best);
	}
	
	private static void initPop(){	
		pop = new Population();
		for(int i = 0; i < popSize; i++) {
			pop.addIndividual(new RoverCircuit(obsData));
		}
	}
}
