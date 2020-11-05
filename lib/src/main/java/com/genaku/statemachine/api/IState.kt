package com.genaku.statemachine.api

/**
 * A fundamental building block of a [IStateMachine].
 *
 * Formally, we consider states to be static checkpoints for the state machine to go through.
 * A state machine can have only one 'current' state, and it can transition from it using an action (also called input).
 *
 * Using the current API, each state must have a unique name.
 */
interface IState {

    /**
     * Do something on enter into state
     */
    fun enter() {}

    /**
     * Do something on exit from state
     */
    fun exit() {}
}