package com.github.sandroln.kanbanboard.createboard.presentation

import com.github.sandroln.kanbanboard.BaseTest
import com.github.sandroln.kanbanboard.core.ManageResource
import com.github.sandroln.kanbanboard.createboard.data.CreateBoardRepository
import com.github.sandroln.kanbanboard.createboard.data.CreateBoardResult
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CreateBoardViewModelTest : BaseTest() {

    //region fields
    private lateinit var functionsCallsStack: FunctionsCallsStack
    private lateinit var dispatchersList: TestDispatchersList
    private lateinit var repository: FakeRepository
    private lateinit var communication: FakeCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var manageResource: ManageResource
    private lateinit var viewModel: CreateBoardViewModel
    //endregion

    @Before
    fun setUp() {
        functionsCallsStack = FunctionsCallsStack.Base()
        repository = FakeRepository.Base(functionsCallsStack)
        dispatchersList = TestDispatchersList()
        navigation = FakeNavigation.Base(functionsCallsStack)
        communication = FakeCommunication.Base(functionsCallsStack)
        manageResource = TestManageResource("board already exists")
        viewModel = CreateBoardViewModel(
            manageResource = manageResource,
            repository = repository,
            communication = communication,
            navigation = navigation,
            dispatchersList = dispatchersList
        )
    }

    @Test
    fun `test create first board successfully`() {
        repository.initWithExistingBoards(emptyList())
        repository.initWithCreateBoardResult(CreateBoardResult.Success)

        val firstBoardName = "First Board Name"
        viewModel.checkBoard(name = firstBoardName)
        repository.checkContainsCalled(value = firstBoardName)
        communication.check(CreateBoardUiState.CanCreateBoard)

        viewModel.createBoard(name = firstBoardName)
        communication.check(CreateBoardUiState.Progress)
        repository.checkCreateCalled(value = firstBoardName)
        navigation.check(BoardScreen)
        functionsCallsStack.checkStack(5)
    }

    @Test
    fun `test create first board failed`() {
        repository.initWithExistingBoards(emptyList())
        repository.initWithCreateBoardResult(CreateBoardResult.Failed(errorMessage = "network problem"))

        val firstBoardName = "First Board Name"
        viewModel.checkBoard(name = firstBoardName)
        repository.checkContainsCalled(value = firstBoardName)
        communication.check(CreateBoardUiState.CanCreateBoard)

        viewModel.createBoard(name = firstBoardName)
        communication.check(CreateBoardUiState.Progress)
        repository.checkCreateCalled(value = firstBoardName)
        communication.check(CreateBoardUiState.Error(errorMessage = "network problem"))
        functionsCallsStack.checkStack(5)
    }

    @Test
    fun `test create board with name already existing`() {
        val firstBoardName = "First Board Name"
        repository.initWithExistingBoards(listOf(firstBoardName))

        viewModel.checkBoard(name = firstBoardName)
        repository.checkContainsCalled(value = firstBoardName)
        communication.check(CreateBoardUiState.BoardAlreadyExists("board already exists"))
        functionsCallsStack.checkStack(2)
    }

    private interface FakeRepository : CreateBoardRepository {

        fun checkContainsCalled(value: String)
        fun checkCreateCalled(value: String)
        fun initWithExistingBoards(list: List<String>)
        fun initWithCreateBoardResult(result: CreateBoardResult)

        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeRepository {

            private lateinit var createResult: CreateBoardResult
            private val nameList = mutableListOf<String>()
            private val containsCallList = mutableListOf<String>()
            private var containsCallIndex = 0
            private val createCallList = mutableListOf<String>()
            private var createCallIndex = 0

            override fun initWithExistingBoards(list: List<String>) {
                nameList.clear()
                nameList.addAll(list.map { it.lowercase() })
            }

            override fun initWithCreateBoardResult(result: CreateBoardResult) {
                createResult = result
            }

            override fun contains(name: String): Boolean {
                containsCallList.add(name)
                functionsCallsStack.put(CONTAINS_CALLED)
                return nameList.contains(name.lowercase())
            }

            override fun checkContainsCalled(value: String) {
                functionsCallsStack.checkCalled(CONTAINS_CALLED)
                assertEquals(value, containsCallList[containsCallIndex++])
            }

            override fun checkCreateCalled(value: String) {
                functionsCallsStack.checkCalled(CREATE_CALLED)
                assertEquals(value, createCallList[createCallIndex++])
            }

            override suspend fun create(name: String): CreateBoardResult {
                createCallList.add(name)
                functionsCallsStack.put(CREATE_CALLED)
                return createResult
            }

            companion object {
                private const val CONTAINS_CALLED = "CreateBoardRepository#contains"
                private const val CREATE_CALLED = "CreateBoardRepository#create"
            }
        }
    }

    private interface FakeCommunication : CreateBoardCommunication {

        fun check(state: CreateBoardUiState)

        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeCommunication {

            private val list = mutableListOf<CreateBoardUiState>()
            private var index = 0

            override fun map(source: CreateBoardUiState) {
                functionsCallsStack.put(MAP_CALL)
                list.add(source)
            }

            override fun check(state: CreateBoardUiState) {
                assertEquals(state, list[index++])
                functionsCallsStack.checkCalled(MAP_CALL)
            }

            companion object {
                private const val MAP_CALL = "CreateBoardCommunication#map"
            }
        }
    }
}