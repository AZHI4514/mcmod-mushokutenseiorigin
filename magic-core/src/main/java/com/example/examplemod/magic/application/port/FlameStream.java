package com.example.examplemod.magic.application.port;
/** One paid emission, including world hit queries and existing particle output. */
@FunctionalInterface
public interface FlameStream {
	void emit(float yaw,float pitch);
}
