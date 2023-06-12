package com.github.sandroln.kanbanboard

import com.github.sandroln.kanbanboard.core.DispatchersList
import com.github.sandroln.kanbanboard.core.ManageResource
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.main.Screen
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert

abstract class BaseTest {

    protected interface FunctionsCallsStack {

        fun put(funName: String)
        fun checkCalled(funName: String)
        fun checkStack(size: Int)

        class Base : FunctionsCallsStack {
            private val list = mutableListOf<String>()
            private var count = 0

            override fun put(funName: String) {
                list.add(funName)
            }

            override fun checkCalled(funName: String) {
                Assert.assertEquals(funName, list[count++])
            }

            override fun checkStack(size: Int) {
                Assert.assertEquals(size, list.size)
            }
        }
    }

    protected class TestDispatchersList(
        private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
    ) : DispatchersList {

        override fun io() = dispatcher
        override fun ui() = dispatcher
    }

    protected class TestManageResource(private val string: String) : ManageResource {

        override fun string(id: Int): String {
            return string
        }
    }

    protected interface FakeNavigation : NavigationCommunication.Update {

        fun check(screen: Screen)

        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeNavigation {

            private val list = mutableListOf<Screen>()
            private var index = 0

            override fun check(screen: Screen) {
                TestCase.assertEquals(screen, list[index++])
                functionsCallsStack.checkCalled(MAP_CALL)
            }

            override fun map(source: Screen) {
                functionsCallsStack.put(MAP_CALL)
                list.add(source)
            }

            companion object {
                private const val MAP_CALL = "NavigationCommunication.Update#map"
            }
        }
    }
}