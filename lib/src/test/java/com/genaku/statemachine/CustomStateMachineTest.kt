package com.genaku.statemachine

import com.genaku.statemachine.api.IAction
import com.genaku.statemachine.api.IState
import com.genaku.statemachine.dsl.*
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CustomStateMachineTest : FreeSpec() {

    private val sm = stateMachine {
        mappings(
            LoginPassPreCheck() by Reset goesTo LoginPassInput,
            LoginPassPreCheck() by Cancel goesTo LoginPassInput,
            LoginPassPreCheck() by NoConnection goesTo LoginPassInput,
            LoginPassPreCheck() by ServerAnswerNo goesTo LoginPassInput,
            LoginPassPreCheck() by ServerAnswerYes goesTo OpenNewPinInput(),

            OpenNewPinInput() by Reset goesTo LoginPassInput,

            LoginPassInput by LoginPassEntered goesTo LoginPassPreCheck()
        )
        initialState = LoginPassInput
    }

    private val sm1 = stateMachine {
        mappings(
            Reset moves LoginPassPreCheck() to LoginPassInput,
            Reset moves OpenNewPinInput() to LoginPassInput,

            LoginPassEntered moves LoginPassInput to LoginPassPreCheck(),

            Cancel moves LoginPassPreCheck() to LoginPassInput,

            NoConnection moves LoginPassPreCheck() to LoginPassInput,

            ServerAnswerYes moves LoginPassPreCheck() to OpenNewPinInput(),
            ServerAnswerNo moves LoginPassPreCheck() to LoginPassInput
        )
        initialState = LoginPassInput
    }

    interface ICommands {
        fun showNewPinForm()
        fun showProgress(visible: Boolean)
    }

    val commands: ICommands = object : ICommands {
        override fun showNewPinForm() {
            println("showNewPinForm")
        }

        override fun showProgress(visible: Boolean) {
            println("showProgress $visible")
        }
    }

    object LoginPassInput : IState
    inner class LoginPassPreCheck : IState {
        override fun enter() {
            commands.showProgress(true)
        }

        override fun exit() {
            commands.showProgress(false)
        }
    }

    inner class OpenNewPinInput : IState {
        override fun enter() {
            commands.showNewPinForm()
        }
    }

    object Reset : IAction
    object LoginPassEntered : IAction
    object Cancel : IAction
    object NoConnection : IAction
    object ServerAnswerYes : IAction
    object ServerAnswerNo : IAction

    init {
        "sss" {
            sm.transition(LoginPassEntered)
            sm.currentState.javaClass shouldBe LoginPassPreCheck().javaClass
            sm.transition(ServerAnswerYes)
            sm.currentState.javaClass shouldBe OpenNewPinInput().javaClass
        }
    }
}
