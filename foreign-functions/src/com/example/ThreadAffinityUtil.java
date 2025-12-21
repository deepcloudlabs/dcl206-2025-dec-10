package com.example;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Locale;

public class ThreadAffinityUtil {

	private enum OS {
		WINDOWS, LINUX, UNSUPPORTED
	}

	private static final OS CURRENT_OS;
	private static MethodHandle setAffinityHandle;
	private static MethodHandle getThreadIdHandle;

	static {
		String name = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
		if (name.contains("win"))
			CURRENT_OS = OS.WINDOWS;
		else if (name.contains("nux"))
			CURRENT_OS = OS.LINUX;
		else
			CURRENT_OS = OS.UNSUPPORTED;

		try {
			setupNativeHandles();
		} catch (Exception e) {
			System.err.println("Failed to initialize native handles: " + e.getMessage());
		}
	}

	private static void setupNativeHandles() throws Exception {
		Linker linker = Linker.nativeLinker();
		if (CURRENT_OS == OS.WINDOWS) {
			SymbolLookup kernel32 = SymbolLookup.libraryLookup("Kernel32", Arena.global());
			getThreadIdHandle = linker.downcallHandle(kernel32.find("GetCurrentThread").get(),
					FunctionDescriptor.of(ValueLayout.ADDRESS));
			setAffinityHandle = linker.downcallHandle(kernel32.find("SetThreadAffinityMask").get(),
					FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
		} else if (CURRENT_OS == OS.LINUX) {
			SymbolLookup libc = linker.defaultLookup();
			getThreadIdHandle = linker.downcallHandle(libc.find("pthread_self").get(),
					FunctionDescriptor.of(ValueLayout.JAVA_LONG));
			setAffinityHandle = linker.downcallHandle(libc.find("pthread_setaffinity_np").get(), FunctionDescriptor
					.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
		}
	}

	@SuppressWarnings("unused")
	public static void setAffinity(int coreId) throws Throwable {
		if (CURRENT_OS == OS.UNSUPPORTED)
			return;

		if (CURRENT_OS == OS.WINDOWS) {
			MemorySegment thread = (MemorySegment) getThreadIdHandle.invokeExact();
			long mask = 1L << coreId;

			var previousMask = (long) setAffinityHandle.invokeExact(thread, mask);

		} else {
			try (Arena arena = Arena.ofConfined()) {
				long threadId = (long) getThreadIdHandle.invokeExact();
				long cpuSetSize = 128;
				MemorySegment cpuSet = arena.allocate(cpuSetSize);
				cpuSet.fill((byte) 0);

				byte current = cpuSet.get(ValueLayout.JAVA_BYTE, coreId / 8);
				cpuSet.set(ValueLayout.JAVA_BYTE, coreId / 8, (byte) (current | (1 << (coreId % 8))));

				int result = (int) setAffinityHandle.invokeExact(threadId, cpuSetSize, cpuSet);
				if (result != 0) {
					throw new RuntimeException("Failed to set affinity, error code: " + result);
				}
			}
		}
	}

	public static void main(String[] args) throws Throwable {
		System.out.println("Detected OS: " + CURRENT_OS);
		setAffinity(0); // Pin to core 0
		System.out.println("Thread affinity set to core 0.");
	}
}