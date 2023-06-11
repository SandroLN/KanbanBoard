package com.github.sandroln.kanbanboard.login

import com.github.sandroln.kanbanboard.core.DispatchersList
import com.github.sandroln.kanbanboard.core.ManageResource
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
}