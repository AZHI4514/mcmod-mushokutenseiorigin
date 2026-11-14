package com.example.examplemod.magic.application.port;
import java.util.Optional;
import com.example.examplemod.magic.domain.model.Vec3d;
/** Read-only point resolution precedes payment; apply never reports a reversible failure after damage. */
public interface TargetedArea {
	Optional<Vec3d> locate(float yaw,float pitch);
	void apply(Vec3d center,float yaw,float pitch);
}
