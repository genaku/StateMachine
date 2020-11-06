package com.genaku.statemachine.api

interface IStateMachine {

    /**
     * The [IState] that this state machine is currently in. Initially, that's [CoreState.Incubating]
     */
    val currentState: IState

    /**
     * The last [IAction] executed successfully by this state machine. Initially, that's [CoreAction.Birth]
     */
    val lastAction: IAction

    /**
     * Manually add new [StateMapping]s to this state machine. It is better to use the mappings DSL, if possible.
     *
     * @param mapping The state-action-state mapping. Best constructed using the mapping DSL
     *
     * @throws IncubationPassedException if state machine has already started processing events
     * @throws IllegalArgumentException if provided mapping has a conflict with another mapping
     */
    @Throws(IncubationPassedException::class, IllegalArgumentException::class)
    fun addStateMapping(mapping: StateMapping)

    /**
     * Sets the initial state for this state machine. If not set, the initial state will be [CoreState.Incubating].
     *
     * @throws IncubationPassedException if state machine has already started processing events
     */
    @Throws(IncubationPassedException::class)
    fun setInitialState(state: IState)

    /**
     * Attempts to transit the state machine into a new [IState] using the given [IAction].
     *
     * @throws IllegalStateException if transition tries to return state machine to incubation state
     * @throws NoMappingException when proper mapping for [action] from [currentState] not found
     */
    @Throws(IllegalStateException::class, NoMappingException::class)
    fun handleAction(action: IAction)
}