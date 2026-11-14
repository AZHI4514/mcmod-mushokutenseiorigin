package com.example.examplemod.magic.application.port;
/** A self effect needs aim, but no world target. False guarantees no committed effect. */
public interface SelfEffect { boolean apply(float yaw,float pitch); }
