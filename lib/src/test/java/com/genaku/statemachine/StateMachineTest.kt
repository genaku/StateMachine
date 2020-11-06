package com.genaku.statemachine

import com.genaku.statemachine.api.IAction
import com.genaku.statemachine.api.IState
import com.genaku.statemachine.api.IncubationPassedException
import com.genaku.statemachine.api.NoMappingException
import com.genaku.statemachine.dsl.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class StateMachineTest : FreeSpec({

    "empty state machine" - {
        val sm = stateMachine()
        "initial state should be Incubating" {
            sm.currentState shouldBe CoreState.Incubating
        }
        "initial action should be Birth" {
            sm.lastAction shouldBe CoreAction.Birth
        }
    }

    "initial state set - throws if incubation passed" {
        val sm = stateMachine {
            mappings(
                Heat moves CoreState.Incubating to Liquid
            )
        }
        sm.handleAction(Heat)
        shouldThrow<IncubationPassedException> {
            sm.setInitialState(Liquid)
        }
    }

    "mapping - conflict throws" {
        val sm = stateMachine {
            mappings(
                Heat moves CoreState.Incubating to Liquid
            )
        }
        shouldThrow<IllegalArgumentException> {
            sm.addStateMapping(Heat moves CoreState.Incubating to Ice)
        }
    }

    "can't transition into birth again" {
        val sm = stateMachine {
            mappings(
                Heat moves CoreState.Incubating to Liquid
            )
            initialState = Liquid
        }

        shouldThrow<IllegalStateException> {
            sm.handleAction(CoreAction.Birth)
        }
    }

    "mapping not found" {
        val sm = stateMachine {
            mappings(
                Heat moves CoreState.Incubating to Liquid
            )
            initialState = Liquid
        }
        shouldThrow<NoMappingException> {
            sm.handleAction(Chill)
        }
    }

    "check sequence" - {

        val sm = stateMachine {
            mappings(
                Heat moves Ice to Liquid,
                Heat moves Liquid to Steam,
                Heat moves Steam to Steam,

                Chill moves Steam to Liquid,
                Chill moves Liquid to Ice,
                Chill moves Ice to Ice,

                Drink moves Liquid to Empty,
                Fill moves Empty to Liquid
            )
            initialState = Empty
        }

        val sm1 = stateMachine {
            mappings(
                Ice by Heat goesTo Liquid,
                Ice by Chill goesTo Ice,

                Liquid by Heat goesTo Steam,
                Liquid by Chill goesTo Ice,
                Liquid by Drink goesTo Empty,

                Steam by Heat goesTo Steam,
                Steam by Chill goesTo Liquid,

                Empty by Fill goesTo Liquid
            )
            initialState = Empty
        }

        "initial state should be Empty" - {
            sm.currentState shouldBe Empty
            sm.lastAction shouldBe CoreAction.Birth

            "fill initial state should transit to liquid" - {
                sm.handleAction(Fill)
                sm.currentState shouldBe Liquid
                sm.lastAction shouldBe Fill

                "chill liquid should transit to ice" - {
                    sm.handleAction(Chill)
                    sm.currentState shouldBe Ice
                    sm.lastAction shouldBe Chill

                    "drink ice should throw NoMappingException" - {
                        shouldThrow<NoMappingException> {
                            sm.handleAction(Drink)
                        }
                        sm.currentState shouldBe Ice
                        sm.lastAction shouldBe Chill

                        "heat ice should transit to liquid" - {
                            sm.handleAction(Heat)
                            sm.currentState shouldBe Liquid
                            sm.lastAction shouldBe Heat

                            "heat liquid should transit to steam" - {
                                sm.handleAction(Heat)
                                sm.currentState shouldBe Steam
                                sm.lastAction shouldBe Heat

                                "drink steam should throw NoMappingException" - {
                                    shouldThrow<NoMappingException> {
                                        sm.handleAction(Drink)
                                    }
                                    sm.currentState shouldBe Steam
                                    sm.lastAction shouldBe Heat

                                    "chill steam should transit to liquid" - {
                                        sm.handleAction(Chill)
                                        sm.currentState shouldBe Liquid
                                        sm.lastAction shouldBe Chill

                                        "drink liquid should transit to empty" - {
                                            sm.handleAction(Drink)
                                            sm.currentState shouldBe Empty
                                            sm.lastAction shouldBe Drink

                                            "drink empty should throw NoMappingException" - {
                                                shouldThrow<NoMappingException> {
                                                    sm.handleAction(Drink)
                                                }
                                                sm.currentState shouldBe Empty
                                                sm.lastAction shouldBe Drink
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
})

sealed class WaterAction : IAction
object Heat : WaterAction()
object Chill : WaterAction()
object Drink : WaterAction()
object Fill : WaterAction()


// Singleton states are simple named objects
sealed class WaterState : IState
object Ice : WaterState() {
    override fun enter() {
        println("freezing")
    }

    override fun exit() {
        println("melting")
    }
}

object Steam : WaterState() {
    override fun enter() {
        println("heating")
    }

    override fun exit() {
        println("condensing")
    }
}

object Liquid : WaterState() {
    override fun enter() {
        println("to liquid")
    }

    override fun exit() {
        println("from liquid")
    }
}

object Empty : WaterState()
