package com.example.examplemod.magic.application.port;
import java.util.Optional;
import com.example.examplemod.magic.domain.model.Vec3d;
/** Instant placement, not a flying projectile. False guarantees no committed effect. */
public interface LocalEffect {
	Optional<Vec3d> locate(float yaw,float pitch);
	boolean place(Vec3d base,float yaw,float pitch);
}
