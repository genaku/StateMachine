package com.genaku.statemachine.dsl

import com.genaku.statemachine.StateMachine
import com.genaku.statemachine.api.IState
import com.genaku.statemachine.api.IStateMachine
import com.genaku.statemachine.api.StateMapping
import kotlin.DeprecationLevel.HIDDEN

/**
 * The DSL (domain-specific language) wrapper. Allows easy state machine configuration and instantiation.
 *
 * This wrapper respects the bounds set by the [StateMachine] API, meaning that you might get configuration exceptions
 * if you try to reconfigure things after state machine has started working.
 *
 * For example, [initialState] should be your last configuration call.
 * Another example - handlers can be added at any time, as they are not state machine's configuration elements.
 *
 * Here's how to use it:
 *
 * ```kotlin
 * object AtoB : SingletonAction()
 * object AtoC : SingletonAction()
 * object A : SingletonState()
 *
 * val sm = stateMachine {
 *   mappings(
 *     AtoB moves A to namedState("B"),
 *     AtoC moves A to payloadState("C", 12.41),
 *     namedAction("CtoA") moves payloadState("C", 12.41) to A
 *   )
 *   useStrictMatching = false
 *   transitionsDispatcher = Dispatchers.Main // optional
 *   transitionHandler { /* inline handler */ }
 *   initialState = A
 * }
 *
 * // and then on that instance:
 * sm.transition(AtoB)
 * ```
 *
 * @see [StateMachine]
 * @see [stateMachine]
 */
class StateMachineDsl(
  internal val instance: IStateMachine
) {

    /**
     * Takes a list of [StateMapping]s and adds them into the state machine.
     *
     * @see [StateMachine.addStateMapping]
     */
    fun mappings(vararg mappings: StateMapping) = apply {
        mappings.forEach { instance.addStateMapping(it) }
    }

    /**
     * Sets the initial state. Make this call your last configuration call as it creates a transition internally.
     *
     * @see [StateMachine.setInitialState]
     */
    var initialState: IState
        @Deprecated("Write-only.", level = HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) = instance.setInitialState(value)

}

/**
 * Starts the DSL ([StateMachineDsl]) with a configuration block on a new state machine instance.
 */
fun stateMachine(initialize: StateMachineDsl.() -> Unit): IStateMachine =
    StateMachineDsl(StateMachine()).apply(initialize).instance

/**
 * Starts the DSL ([StateMachineDsl]) without a configuration block on a new state machine instance.
 */
fun stateMachine(): IStateMachine = StateMachine()
