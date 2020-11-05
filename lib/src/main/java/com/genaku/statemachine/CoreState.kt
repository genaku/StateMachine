package com.genaku.statemachine

import com.genaku.statemachine.api.IState

/**
 * A predefined list of core states. Used only internally, **do not attempt to use**.
 */
internal sealed class CoreState : IState {

    /**
     * Configuring and preparing the state machine. Don't react to this.
     */
    object Incubating : CoreState()
}