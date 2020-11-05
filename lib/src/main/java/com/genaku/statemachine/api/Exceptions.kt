package com.genaku.statemachine.api

/**
 * Signals the catcher that [IStateMachine] has passed the incubation state.
 * No more configuration changes should be made on the state machine instance.
 */
data class IncubationPassedException(val operation: String) :
    IllegalStateException("State machine is alive! $operation is not allowed anymore")

/**
 * Signals the catcher that [IStateMachine] doesn't have the mapping registered for the given state and action.
 * The easiest fix is to check your mappings and add the missing ones.
 */
data class NoMappingException(val state: IState, val action: IAction) :
    IllegalStateException("No mapping found for state (${state.javaClass.simpleName}) using action [${action.javaClass.simpleName}]")