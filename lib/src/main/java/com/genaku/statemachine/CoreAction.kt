package com.genaku.statemachine

import com.genaku.statemachine.api.IAction

/**
 * A predefined list of core actions. Used only internally, **do not attempt to use**.
 */
internal sealed class CoreAction : IAction {

    /**
     * The first action that brings the state machine into existence. Replacement for `null`.
     */
    object Birth : CoreAction()
}