package com.nokhyun.parseexam

import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import retrofit2.await

interface ServiceRepository {
    suspend fun fetchTodo(): Flow<Todo>
    suspend fun fetchTodoSerialization(): Flow<Todo>
}

class ServiceRepositoryImpl(
    private val networkCall: Service = NetworkCall.moshiService(Service::class.java),
    private val kotlinxNetworkCall: Service = NetworkCall.serializationService(Service::class.java)
) : ServiceRepository {
    override suspend fun fetchTodo(): Flow<Todo> = flow {
        emit(networkCall.fetchTodos().await())
    }

    override suspend fun fetchTodoSerialization(): Flow<Todo> = flow {
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