package com.nokhyun.parseexam

import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import retrofit2.await

interface ServiceRepository {
    fun fetchTodo(): Flow<Todo>
    fun fetchTodoSerialization(): Flow<Todo>
}

class ServiceRepositoryImpl(
    private val networkCall: Service = NetworkCall.moshiService(Service::class.java),
    private val kotlinxNetworkCall: Service = NetworkCall.serializationService(Service::class.java)
) : ServiceRepository {
    override fun fetchTodo(): Flow<Todo> = flow {
        emit(networkCall.fetchTodos().await())
    }

    override fun fetchTodoSerialization(): Flow<Todo> = flow {
        emit(kotlinxNetworkCall.fetchTodosSerialization().await())
    }
}

@Serializable
@JsonClass(generateAdapter = true)
data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)