package pgdp.threads;

import java.math.BigInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class ParallelCompute implements Runnable{
	private static BigInteger[] results;
	private int start, end;
	private static BigInteger[] parallelComputeData, reduceData;
	private static Function<BigInteger, BigInteger>[] threadFunctions;
	private static BinaryOperator<BigInteger> binaryOperator;
	private static boolean reduce;
	private static BigInteger reduceResult;

	public ParallelCompute(int start, int end, boolean reduce) {
		this.start = start;
		this.end = end;
		this.reduce = reduce;
	}

	public static BigInteger[] parallelComputeFunctions(BigInteger[] data, Function<BigInteger, BigInteger>[] functions,
														int threadCount) throws InterruptedException {

		if (data == null || functions == null) {
			throw new NullPointerException("neither data or functions should be null!");
		}

		if (data.length == 0 || functions.length == 0 || data.length != functions.length || threadCount <= 0) {
			throw new IllegalArgumentException("bad arguments!");
		}

		parallelComputeData = data;
		threadFunctions = functions;
		results = new BigInteger[data.length];

		Thread[] threads = new Thread[threadCount];

		int segment = (int)Math.ceil((double)data.length / threadCount);
		int s = 0;
		int e = segment;
		int n = data.length;
		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(new ParallelCompute(s,e,false));
			s = e;
			e += segment;
			if (e > n)
				e = n;
		}

		//start all threads
		for (Thread t : threads) t.start();

		//join all threads
		for (Thread t : threads) t.join();

		return results;
	}

	public static BigInteger parallelReduceArray(BigInteger[] data, BinaryOperator<BigInteger> binOp, int threadCount)
			throws InterruptedException {

		if (data == null || binOp == null) {
			throw new NullPointerException("neither data or binOp should be null!");
		}

		if (data.length == 0 || threadCount <= 0) {
			throw new IllegalArgumentException("length of data should not be 0 and threadCount should be positive");
		}

		reduceData = data;
		binaryOperator = binOp;
		reduceResult = data[0];

		Thread[] threads = new Thread[threadCount];
		int length = (int)Math.ceil((double)data.length / threadCount);
		int s = 1, e = length;
		int n = data.length;
		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(new ParallelCompute(s,e,true));
			s = e;
			e += length;
			if (e > n)
				e = n;
		}

		//start all threads
		for (Thread t : threads) t.start();

		//join all the threads
		for (Thread t : threads) t.join();

		return reduceResult;
	}

	@Override
	public void run() {
		if (reduce) {
			for (; start < end; start++) {
				synchronized (ParallelCompute.class) {
					reduceResult = binaryOperator.apply(reduceResult, reduceData[start]);
				}
			}
		}

		else {
			for (; start < end; start++) {
				results[start] = threadFunctions[start].apply(parallelComputeData[start]);
			}
		}
	}

}
