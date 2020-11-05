package com.genaku.statemachine

import com.genaku.statemachine.api.*

/**
 * Implementation of [IStateMachine]
 */
class StateMachine : IStateMachine {

    private var _currentState: IState = CoreState.Incubating
    private var _lastAction: IAction = CoreAction.Birth
    private val _stateMappings = mutableSetOf<StateMapping>()

    override val currentState: IState
        get() = _currentState

    override val lastAction: IAction
        get() = _lastAction

    @Synchronized
    @Throws(IncubationPassedException::class, IllegalArgumentException::class)
    override fun addStateMapping(mapping: StateMapping) {
        requireIncubationFor("Adding mappings")

        _stateMappings
            .firstOrNull { existing ->
                haveSameSourceAndActionButDifferentDestination(existing, mapping)
            }?.let { conflict ->
                throw IllegalArgumentException("Conflict detected!\nExisting: $conflict\nYours: $mapping")
            }

        _stateMappings += mapping
    }

    private fun haveSameSourceAndActionButDifferentDestination(
        existing: StateMapping,
        mapping: StateMapping
    ): Boolean {
        return statesAreEqual(existing.source, mapping.source) &&
                actionsAreEqual(existing.action, mapping.action) &&
                !areEqual(existing.destination, mapping.destination)
    }

    @Synchronized
    @Throws(IllegalStateException::class, NoMappingException::class)
    override fun transition(action: IAction) {
        if (actionsAreEqual(action.javaClass, CoreAction.Birth.javaClass)) {
            throw IllegalStateException("State machine can't be reborn, create a new instance")
        }

        findMappingOrNull(action)?.let { mapping ->
            makeTransition(action, mapping)
            return
        }

        // no mapping found
        throw NoMappingException(_currentState, action)
    }

    private fun findMappingOrNull(action: IAction): StateMapping? = _stateMappings
        .firstOrNull {
            statesAreEqual(it.source, _currentState.javaClass) && actionsAreEqual(
                it.action,
                action.javaClass
            )
        }

    private fun makeTransition(action: IAction, mapping: StateMapping) {
        _currentState.exit()
        _lastAction = action
        mapping.destination.enter()
        _currentState = mapping.destination
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

    private fun areEqual(a: IState, b: IState) = (a.javaClass == b.javaClass)
    private fun statesAreEqual(a: Class<out IState>, b: Class<out IState>) = (a == b)
    private fun actionsAreEqual(a: Class<out IAction>, b: Class<out IAction>) = (a == b)
}