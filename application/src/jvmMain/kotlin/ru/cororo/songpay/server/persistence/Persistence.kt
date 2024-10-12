package ru.cororo.songpay.server.persistence

import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpsertStatement
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

fun Application.configurePersistence() {
    val host = environment.config.propertyOrNull("exposed.host")?.getString() ?: "localhost"
    val port = environment.config.propertyOrNull("exposed.port")?.getString() ?: 5432
    val database = environment.config.propertyOrNull("exposed.database")?.getString() ?: "songpay"
    val username = environment.config.propertyOrNull("exposed.username")?.getString() ?: "songpay"
    val password = environment.config.propertyOrNull("exposed.password")?.getString() ?: "12345678"
    Database.connect("jdbc:postgresql://$host:$port/$database", user = username, password = password)

    transaction {
        addLogger(Slf4jSqlDebugLogger)

    }
}

suspend fun <R> sqlTransaction(block: suspend () -> R): R = newSuspendedTransaction(Dispatchers.IO) {
    addLogger(Slf4jSqlDebugLogger)
    block()
}

fun <I : Comparable<I>> IdTable<I>.deleteById(id: I): Int = deleteWhere {
    it.run {
        this@deleteById.id eq id
    }
}

fun UUIDTable.deleteById(id: Uuid): Int = deleteById(id.toJavaUuid())

fun <I : Comparable<I>> IdTable<I>.selectById(id: I) = selectAll().where {
    this@selectById.id eq id
}

fun UUIDTable.selectById(id: Uuid): Query = selectById(id.toJavaUuid())

fun <T : Any> InsertStatement<T>.asResultRow() = resultedValues?.firstOrNull()

interface CrudPersistence<R : Any, I : Any> {
    suspend fun save(entity: R): R

    suspend fun delete(entity: R)

    suspend fun deleteById(id: I)

    suspend fun getById(entityId: I): R?

    suspend fun getAll(): List<R>
}
