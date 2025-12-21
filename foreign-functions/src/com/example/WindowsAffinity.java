package com.example;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.concurrent.TimeUnit;

public class WindowsAffinity {

    public static void main(String[] args) throws Throwable {
        // 1. Get a linker and look up the Kernel32 library
        Linker linker = Linker.nativeLinker();
        SymbolLookup kernel32 = SymbolLookup.libraryLookup("Kernel32", Arena.global());

        // 2. Locate SetThreadAffinityMask and GetCurrentThread
        // DWORD_PTR SetThreadAffinityMask(HANDLE hThread, DWORD_PTR dwThreadAffinityMask);
        MethodHandle setAffinity = linker.downcallHandle(
                kernel32.find("SetThreadAffinityMask").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
        );

        // HANDLE GetCurrentThread(VOID);
        MethodHandle getCurrentThread = linker.downcallHandle(
                kernel32.find("GetCurrentThread").get(),
                FunctionDescriptor.of(ValueLayout.ADDRESS)
        );

        // 3. Obtain the handle for the current thread
        MemorySegment threadHandle = (MemorySegment) getCurrentThread.invokeExact();

        // 4. Set affinity mask (e.g., bitmask 0x1 for CPU 0, 0x3 for CPU 0 and 1)
        long mask = 0x1L; // Lock to the first core
        long previousMask = (long) setAffinity.invokeExact(threadHandle, mask);

        if (previousMask != 0) {
            System.out.println("Successfully set thread affinity to mask: " + Long.toBinaryString(mask));
            System.out.println("Previous mask was: " + Long.toBinaryString(previousMask));
        } else {
            System.err.println("Failed to set thread affinity.");
        }

        // Run some work to observe the affinity
        System.out.println("Processing on restricted core...");
        System.out.println(ProcessHandle.current().pid());
        TimeUnit.MINUTES.sleep(5);
    }
}