package com.anmol.util;

public class BenchmarkUtil {

	private static final long MEGABYTE = 1024L * 1024L;

	public static Double bytesToMegabytes(long bytes) {
		Double megabytes = (new Long(bytes)).doubleValue();
		return megabytes / MEGABYTE;
	}

	public static long getMemoryUsed() {
		// Get the Java runtime
		Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		// runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		return memory;
	}

}
