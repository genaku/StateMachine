package com.genaku.statemachine.api

/**
 * A fundamental building block of a [IStateMachine].
 *
 * Formally, we consider actions to be `inputs` for the state machine.
 * Actions can either move the state machine into a different state, keep it in the same state, or simply fail if
 * state change is not possible from the current state using the given input/action.
 *
 * Using the current API, each action must have a unique name.
 */
interface IAction