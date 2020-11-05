package com.genaku.statemachine.api

/**
 * Keeps information about a particular state-to-state transition mapping.
 * You should provide an action at creation time, and both **start** and **end** states surrounding the action.
 *
 * The easiest way to create one is by using the DSL:
 *
 * ```kotlin
 * val mapping = AtoB moves A to B
 * ```
 * where `AtoB` is the action, and `A` and `B` are states.
 *
 * @param source the origin state (where the transition would start from)
 * @param action the trigger/action (what makes the transition start)
 * @param destination the goal state (where the transition would end at)
 */
data class StateMapping(
    val source: IState,
    val action: IAction,
    val destination: IState
) {
    override fun toString() =
        "Mapping (${source.javaClass.simpleName}) ---[${action.javaClass.simpleName}]--> (${destination.javaClass.simpleName})"
}