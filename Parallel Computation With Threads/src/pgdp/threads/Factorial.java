package pgdp.threads;

import java.math.BigInteger;

public class Factorial implements Runnable{

	private static BigInteger fac;
	private int end, start, id;
	private static BigInteger[] partResults;
	private static boolean parallel = false;

	public Factorial(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public Factorial(int startVal, int endVal, int id) {
		start = startVal;
		end = endVal;
		this.id = id;
	}

	public static BigInteger facSequential(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n should not be negative!");
		Thread t = new Thread(new Factorial(1, n));
		parallel = false;
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return new BigInteger("1");
		}
		return fac;
	}

	public static BigInteger facParallel(int n, int threadCount) throws InterruptedException {
		if (n < 0 || threadCount <= 0)
			throw new IllegalArgumentException("n should not be negative and threadCount should be positive");

		Thread[] threads = new Thread[threadCount];
		parallel = true;
		partResults = new BigInteger[threadCount];


		for (int i = 0; i < threadCount; i++) {
			partResults[i] = new BigInteger("1");
		}

		int segment = (int)Math.ceil((double) n / threadCount);
		int s = 1;
		int e = segment;
		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(new Factorial(s,e,i));
			s = e + 1;
			e += segment;
			if (e > n)
				e = n;
		}

		//start all threads
		for (Thread t : threads) t.start();

		//join all threads
		for (Thread t : threads) t.join();

		BigInteger product = new BigInteger("1");
		for (int i = 0; i < threadCount; i++) {
			product = product.multiply(partResults[i]);
		}

		return product;
	}

	@Override
	public void run() {
		synchronized (Factorial.class) {
			fac = new BigInteger("1");
			for (;start <= end; start++) {
				fac = fac.multiply(new BigInteger(""+ start));
			}
			if (parallel) {
				partResults[id] = fac;
			}
		}
	}
}
