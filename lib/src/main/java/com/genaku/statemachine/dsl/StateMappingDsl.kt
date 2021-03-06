package com.genaku.statemachine.dsl

import com.genaku.statemachine.api.IAction
import com.genaku.statemachine.api.IState
import com.genaku.statemachine.api.StateMapping

/**
 * Binds the source [IState] and an [IAction] from a [StateVector] with a destination state into a [StateMapping].
 */
infix fun StateVector.to(destination: IState): StateMapping = StateMapping(
  source = source,
  action = action,
  destination = destination
)

infix fun StateVector.goesTo(destination: IState): StateMapping = StateMapping(
  source = source,
  action = action,
  destination = destination
)

/**
 * Binds an [IAction] and a source [IState] into a [StateVector].
 */
infix fun IAction.moves(source: IState): StateVector = StateVector(source.javaClass, this.javaClass)
infix fun IAction.moves(source: Class<out IState>): StateVector = StateVector(source, this.javaClass)
infix fun IState.by(action: IAction): StateVector = StateVector(this.javaClass, action.javaClass)
infix fun Class<out IState>.by(action: Class<out IAction>): StateVector = StateVector(this, action)
infix fun Class<out IState>.by(action: IAction): StateVector = StateVector(this, action.javaClass)

/**
 * Holds a [IState] and an [IAction], without knowing anything about the resulting state.
 */
data class StateVector(val source: Class<out IState>, val action: Class<out IAction>)
