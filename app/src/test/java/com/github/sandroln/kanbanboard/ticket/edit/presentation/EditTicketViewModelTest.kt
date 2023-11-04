package com.github.sandroln.kanbanboard.ticket.edit.presentation

import com.github.sandroln.kanbanboard.BaseTest
import com.github.sandroln.kanbanboard.board.main.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.board.main.presentation.Column
import com.github.sandroln.kanbanboard.board.main.presentation.TicketUi
import com.github.sandroln.kanbanboard.main.Screen
import com.github.sandroln.kanbanboard.ticket.edit.data.EditTicketRepository
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class EditTicketViewModelTest : BaseTest() {

    private lateinit var functionsCallsStack: FunctionsCallsStack
    private lateinit var stateCommunication: FakeStateCommunication
    private lateinit var repository: FakeRepository
    private lateinit var navigation: FakeNavigation
    private lateinit var viewModel: EditTicketViewModel

    @Before
    fun setup() {
        functionsCallsStack = FunctionsCallsStack.Base()
        repository = FakeRepository.Base(functionsCallsStack)
        navigation = FakeNavigation.Base(functionsCallsStack)
        stateCommunication = FakeStateCommunication.Base(functionsCallsStack)
        viewModel = EditTicketViewModel(
            stateCommunication,
            repository,
            FakeBoardsMembersCommunication(),
            FakeEditTicketCommunication(),
            navigation
        )
    }

    @Test
    fun test_no_changes() {
        viewModel.assign(BoardUser.Base("fakeUserId", "Fake User", "fake@mail.ru"))
        val ticketUi = TicketUi(
            "#FF0000",
            "fakeId0",
            "initialTitle",
            "Fake User",
            Column.Todo,
            "description 0"
        )
        viewModel.update(ticketUi)
        stateCommunication.checkCalledWith(EditTicketUiState.ShowTicketUpdate(ticketUi, viewModel))

        viewModel.edit("initialTitle", "#FF0000", "description 0")
        repository.checkClearEditingTicketId()
        navigation.check(Screen.Pop)
        functionsCallsStack.checkStack(3)
    }

    @Test
    fun test_title_changes() {
        viewModel.assign(BoardUser.Base("fakeUserId", "Fake User", "fake@mail.ru"))
        val ticketUi = TicketUi(
            "#FF0000",
            "fakeId0",
            "initialTitle",
            "Fake User",
            Column.Todo,
            "description 0"
        )
        viewModel.update(ticketUi)
        stateCommunication.checkCalledWith(EditTicketUiState.ShowTicketUpdate(ticketUi, viewModel))

        viewModel.edit("new Title", "#FF0000", "description 0")
        repository.checkChangesCalled(listOf(TicketChange.Title("new Title")))
        repository.checkClearEditingTicketId()
        navigation.check(Screen.Pop)
        functionsCallsStack.checkStack(4)
    }

    @Test
    fun test_color_changes() {
        viewModel.assign(BoardUser.Base("fakeUserId", "Fake User", "fake@mail.ru"))
        val ticketUi = TicketUi(
            "#FF0000",
            "fakeId0",
            "initialTitle",
            "Fake User",
            Column.Todo,
            "description 0"
        )
        viewModel.update(ticketUi)
        stateCommunication.checkCalledWith(EditTicketUiState.ShowTicketUpdate(ticketUi, viewModel))

        viewModel.edit("initialTitle", "#FFFFFF", "description 0")
        repository.checkChangesCalled(listOf(TicketChange.Color("#FFFFFF")))
        repository.checkClearEditingTicketId()
        navigation.check(Screen.Pop)
        functionsCallsStack.checkStack(4)
    }

    @Test
    fun test_description_changes() {
        viewModel.assign(BoardUser.Base("fakeUserId", "Fake User", "fake@mail.ru"))
        val ticketUi = TicketUi(
            "#FF0000",
            "fakeId0",
            "initialTitle",
            "Fake User",
            Column.Todo,
            "description 0"
        )
        viewModel.update(ticketUi)
        stateCommunication.checkCalledWith(EditTicketUiState.ShowTicketUpdate(ticketUi, viewModel))

        viewModel.edit("initialTitle", "#FF0000", "new description")
        repository.checkChangesCalled(listOf(TicketChange.Description("new description")))
        repository.checkClearEditingTicketId()
        navigation.check(Screen.Pop)
        functionsCallsStack.checkStack(4)
    }

    @Test
    fun test_assignee_changes() {
        val ticketUi = TicketUi(
            "#FF0000",
            "fakeId0",
            "initialTitle",
            "Fake User",
            Column.Todo,
            "description 0"
        )
        viewModel.update(ticketUi)
        stateCommunication.checkCalledWith(EditTicketUiState.ShowTicketUpdate(ticketUi, viewModel))

        viewModel.assign(BoardUser.Base("anotherId", "Other User", "other@mail.ru"))

        viewModel.edit("initialTitle", "#FF0000", "description 0")
        repository.checkChangesCalled(listOf(TicketChange.Assignee("Other User")))
        repository.checkClearEditingTicketId()
        navigation.check(Screen.Pop)
        functionsCallsStack.checkStack(4)
    }

    @Test
    fun test_title_and_description_changed() {
        viewModel.assign(BoardUser.Base("fakeUserId", "Fake User", "fake@mail.ru"))
        val ticketUi = TicketUi(
            "#FF0000",
            "fakeId0",
            "initialTitle",
            "Fake User",
            Column.Todo,
            "description 0"
        )
        viewModel.update(ticketUi)
        stateCommunication.checkCalledWith(EditTicketUiState.ShowTicketUpdate(ticketUi, viewModel))

        viewModel.edit("new Title", "#FF0000", "new description")
        repository.checkChangesCalled(
            listOf(
                TicketChange.Title("new Title"),
                TicketChange.Description("new description")
            )
        )
        repository.checkClearEditingTicketId()
        navigation.check(Screen.Pop)
        functionsCallsStack.checkStack(4)
    }

    @Test
    fun test_delete() {
        viewModel.deleteTicket()
        repository.checkDeleteCalled()
        repository.checkClearEditingTicketId()
        navigation.check(Screen.Pop)
        functionsCallsStack.checkStack(3)
    }

    @Test
    fun first_update() {
        val ticketUi = TicketUi("#000000", "0", "title", "me", Column.Todo, "")
        viewModel.update(ticketUi)

        stateCommunication.checkCalledWith(EditTicketUiState.ShowTicketUpdate(ticketUi, viewModel))
        functionsCallsStack.checkStack(1)
    }

    @Test
    fun second_update() {
        val ticketUi = TicketUi("#000000", "0", "title", "me", Column.Todo, "")
        viewModel.update(ticketUi)
        viewModel.update(TicketUi("#000000", "0", "NEW", "me", Column.Todo, ""))

        stateCommunication.checkCalledWith(EditTicketUiState.ShowTicketUpdate(ticketUi, viewModel))
        stateCommunication.checkCalledWith(EditTicketUiState.ShowRefresh)
        functionsCallsStack.checkStack(2)
    }

    private interface FakeRepository : EditTicketRepository {

        fun checkDeleteCalled()
        fun checkClearEditingTicketId()
        fun checkChangesCalled(list: List<TicketChange>)

        class Base(
            private val functionsCallsStack: FunctionsCallsStack
        ) : FakeRepository {

            private var index = 0

            override fun checkChangesCalled(list: List<TicketChange>) {
                assertEquals(list, makeChangesCalledList[index++])
                functionsCallsStack.checkCalled(MAKE_CHANGES_CALL)
            }

            private val makeChangesCalledList = mutableListOf<List<TicketChange>>()

            override fun makeChanges(changes: List<TicketChange>) {
                functionsCallsStack.put(MAKE_CHANGES_CALL)
                makeChangesCalledList.add(changes)
            }

            override fun clearTicketId() = functionsCallsStack.put(CLEAR_TICKET_CALL)

            override fun checkClearEditingTicketId() =
                functionsCallsStack.checkCalled(CLEAR_TICKET_CALL)

            private var deleteTicketCalledCount = 0

            override fun deleteTicket() {
                functionsCallsStack.put(DELETE_CALL)
                deleteTicketCalledCount++
            }

            override fun init() = Unit

            override fun checkDeleteCalled() {
                functionsCallsStack.checkCalled(DELETE_CALL)
                assertEquals(1, deleteTicketCalledCount)
            }

            companion object {
                private const val MAKE_CHANGES_CALL = "EditTicketRepository#makeChanges"
                private const val DELETE_CALL = "EditTicketRepository#deleteTicket"
                private const val CLEAR_TICKET_CALL = "EditTicketRepository#clearTicketId"
            }
        }
    }

    private interface FakeStateCommunication : EditTicketStateCommunication {

        fun checkCalledWith(state: EditTicketUiState)

        class Base(private val functionsCallsStack: FunctionsCallsStack) : FakeStateCommunication {

            private val list = mutableListOf<EditTicketUiState>()
            private var index = 0

            override fun map(source: EditTicketUiState) {
                list.add(source)
                functionsCallsStack.put(MAP_CALL)
            }

            override fun checkCalledWith(state: EditTicketUiState) {
                functionsCallsStack.checkCalled(MAP_CALL)
                assertEquals(state, list[index++])
            }

            companion object {
                private const val MAP_CALL = "EditTicketStateCommunication#map"
            }
        }
    }

    private class FakeBoardsMembersCommunication : BoardMembersCommunication.Observe
    private class FakeEditTicketCommunication : EditTicketCommunication.Observe
}