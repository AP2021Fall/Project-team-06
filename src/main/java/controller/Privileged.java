package controller;

/**
 * This is just a helper annotation.
 * Any function annotated with this requires either
 * LEADER or ADMIN access.
 *
 * Later we'll use reflection to make it autonomous.
 */
public @interface Privileged {
}
