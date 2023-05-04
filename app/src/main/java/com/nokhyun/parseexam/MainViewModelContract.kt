package com.nokhyun.parseexam

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface STATE
interface EVENT
interface SideEffect

interface MainViewModelContract : BaseContract<MainViewModelContract.TodoState, MainViewModelContract.TodoEvent, MainViewModelContract.TodoSideEffect> {

    data class TodoState(
        val result: Todo? = null,
        val isVisible: Boolean = true
    ) : STATE

    sealed interface TodoEvent : EVENT {
        object FetchTodo : TodoEvent
        object FetchTodoSerialization: TodoEvent
    }

    sealed interface TodoSideEffect : SideEffect {
        object ShowToast : TodoSideEffect
    }
}

interface BaseContract<S : STATE, E : EVENT, SE : SideEffect> {
    val state: StateFlow<S>
    val sideEffect: SharedFlow<SE>
    fun event(event: E)
}

