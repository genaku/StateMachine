package com.genaku.statemachine

import com.genaku.statemachine.api.*

/**
 * Implementation of [IStateMachine]
 */
class StateMachine : IStateMachine {

    private var _currentState: IState = CoreState.Incubating
    private var _lastAction: IAction = CoreAction.Birth
    private val stateMappings = mutableSetOf<StateMapping>()

    override val currentState: IState
        get() = _currentState

    override val lastAction: IAction
        get() = _lastAction

    @Synchronized
    @Throws(IncubationPassedException::class, IllegalArgumentException::class)
    override fun addStateMapping(mapping: StateMapping) {
        requireIncubationFor("Adding mappings")

        stateMappings
            .firstOrNull { existing ->
                haveSameSourceAndActionButDifferentDestination(existing, mapping)
            }?.let { conflict ->
                throw IllegalArgumentException("Conflict detected!\nExisting: $conflict\nYours: $mapping")
            }

        stateMappings += mapping
    }

    private fun haveSameSourceAndActionButDifferentDestination(
        existing: StateMapping,
        mapping: StateMapping
    ): Boolean {
        return (existing.source == mapping.source) &&
                (existing.action == mapping.action) &&
                (existing.destination != mapping.destination)
    }

    @Synchronized
    @Throws(IllegalStateException::class, NoMappingException::class)
    override fun transition(action: IAction) {
        if (action == CoreAction.Birth) {
            throw IllegalStateException("State machine can't be reborn, create a new instance")
        }

        // try to find a mapping
        stateMappings
            .firstOrNull {
                (it.source == _currentState) && (it.action == action)
            }?.let { mapping ->
                // make the transition
                _currentState.exit()
                _lastAction = action
                mapping.destination.enter()
                _currentState = mapping.destination
                return
            }

        // no mapping found
        throw NoMappingException(_currentState, action)
    }

    @Synchronized
    @Throws(IncubationPassedException::class)
    override fun setInitialState(state: IState) {
        requireIncubationFor("Setting initial state")
        _currentState = state
        _lastAction = CoreAction.Birth
    }

    @Throws(IncubationPassedException::class)
    private fun requireIncubationFor(operation: String) {
        if (_currentState != CoreState.Incubating) throw IncubationPassedException(operation)
    }
}