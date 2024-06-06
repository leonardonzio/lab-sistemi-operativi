package mainpackage;

import java.util.Random;

import javax.swing.plaf.synth.SynthPasswordFieldUI;

public class Main {

	public static void main(String[] args) {
		
		var m = new Monitor();
		var r = new Random();
		int nF = r.nextInt(5) + 1;
		int nS = r.nextInt(5) + 1;
		var fulls = new Full[nF];
		var swimonlys = new SwimOnly[nS];
		
		System.out.println("ci sono "+nF+" fulls, "+nS+" e swimonlys");
		
		for (int i = 0; i < nS; i++)
			swimonlys[i] = new SwimOnly(m, r);
		for (int i = 0; i < nF; i++)
			fulls[i] = new Full(m, r);
		
		for (Full f : fulls) 
			f.start();
		for (SwimOnly s : swimonlys) 
			s.start();
		
		
		
		
	}

}
