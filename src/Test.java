import java.util.Random;

public class Test {


	public static void main(String[] args) {
		int[] r1 = {1,2,3,4,5,6,7,8,9};
		int[] r2 = {4,5,2,1,8,7,6,9,3};
		int[] f1 = new int[r1.length];
		int[] f2 = new int[r1.length];
		int[] eqMap = new int[r1.length];
		Random r = new Random();
		
		int fIndex = r.nextInt(r1.length/2);
		int sIndex = r.nextInt(r1.length/2) + r1.length/2;
		
		while(sIndex - fIndex <= 1) //subsequencia tem de ter pelo menos 2 pontos
			sIndex = r.nextInt(r1.length/2) + r1.length/2;	
	
		
		//inicializaçao dos filhos tendo em conta a subsequencia criada
		for(int i = 0; i < r1.length; i++) {
			if(i >= fIndex && i < sIndex) {
				f1[i] = r2[i];
				f2[i] = r1[i];	
				eqMap[f2[i]] = f1[i];
			}
			else {
				f1[i] = -1;
				f2[i] = -1;
			}
		}
		
		//primeiro filho
		for(int i = 0; i < r1.length; i++) {			
			if(f1[i] == -1) {
				if(!contains(f1, r1[i]))
					f1[i] = r1[i];
				else
				{
					for(int j = 0; j < eqMap.length; j++) {
						if(r1[i] == eqMap[j])
							f1[i] = j;
					}
				}
			}		
		}
		
		//segundo filho
		for(int i = 0; i < r1.length; i++) {
					
			if(f2[i] == -1) {
				if(!contains(f2, r2[i]))
						f2[i] = r2[i];
				else
				{
					for(int j = 0; j < eqMap.length; j++) {
						if(r2[i] == j)
							f2[i] = eqMap[j];
					}
				}
			}
		}
		
		for(int i = 0; i < r1.length; i++) {
			System.out.print(f1[i] + " ");
		}
		System.out.println();
		for(int i = 0; i < r1.length; i++) {
			System.out.print(f2[i] + " ");
		}
		System.out.println();
		for(int i = 0; i < r1.length; i++) {
			if(eqMap[i] != 0)
				System.out.print(i + " -> " + eqMap[i] + "|");
		}

	}

	
	private static boolean contains(int[] a, int v) {
		for(int i = 0; i < a.length; i++) {
			if(a[i] == v)
				return true;
		}
			
		return false;
	}
}
