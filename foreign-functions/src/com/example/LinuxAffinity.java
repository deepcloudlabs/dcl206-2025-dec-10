package com.example;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public class LinuxAffinity {

    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        SymbolLookup libc = linker.defaultLookup();

        // pthread_t pthread_self(void);
        MethodHandle pthreadSelf = linker.downcallHandle(
                libc.find("pthread_self").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG)
        );

        // int pthread_setaffinity_np(pthread_t thread, size_t cpusetsize, const cpu_set_t *cpuset);
        MethodHandle setAffinity = linker.downcallHandle(
                libc.find("pthread_setaffinity_np").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_LONG, // pthread_t
                        ValueLayout.JAVA_LONG, // size_t
                        ValueLayout.ADDRESS)   // cpu_set_t*
        );

        try (Arena arena = Arena.ofConfined()) {
            long cpuSetSize = 128; 
            MemorySegment cpuSet = arena.allocate(cpuSetSize);
            cpuSet.fill((byte) 0); // Clear all bits

            int targetCore = 0;
            byte currentByte = cpuSet.get(ValueLayout.JAVA_BYTE, targetCore / 8);
            cpuSet.set(ValueLayout.JAVA_BYTE, targetCore / 8, (byte) (currentByte | (1 << (targetCore % 8))));

            // 6. Get current thread ID and apply the mask
            long threadId = (long) pthreadSelf.invokeExact();
            int result = (int) setAffinity.invokeExact(threadId, cpuSetSize, cpuSet);

            if (result == 0) {
                System.out.println("Successfully pinned thread to Core " + targetCore);
            } else {
                System.err.println("Failed to set affinity. Error code: " + result);
            }
        }
    }
}