package com.nokhyun.parseexam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val serviceRepository: ServiceRepository = ServiceRepositoryImpl()
) : ViewModel(), MainViewModelContract {

    private val _state: MutableStateFlow<MainViewModelContract.TodoState> = MutableStateFlow(MainViewModelContract.TodoState())
    override val state: StateFlow<MainViewModelContract.TodoState> = _state.asStateFlow()

    private val _sideEffect: MutableSharedFlow<MainViewModelContract.TodoSideEffect> = MutableSharedFlow()
    override val sideEffect: SharedFlow<MainViewModelContract.TodoSideEffect> = _sideEffect.asSharedFlow()

    override fun event(event: MainViewModelContract.TodoEvent) {
        when (event) {
            is MainViewModelContract.TodoEvent.FetchTodo -> fetchTodo()
            is MainViewModelContract.TodoEvent.FetchTodoSerialization -> fetchTodoKotlinx()
        }
    }

    private fun fetchTodo() {
        viewModelScope.launch {
            serviceRepository.fetchTodo().collectLatest {
                _state.value = state.value.copy(result = it)
            }
        }
    }

    private fun fetchTodoKotlinx() {
        viewModelScope.launch {
            serviceRepository.fetchTodoSerialization().collectLatest {
                _state.value = state.value.copy(result = it)
            }
        }
    }
}
