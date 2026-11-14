package com.example.examplemod.magic.application.port;
/** Returns false only when no projectile has been added; no additional mana debit. */
@FunctionalInterface
public interface PaidProjectile { boolean spawn(int paid); }
