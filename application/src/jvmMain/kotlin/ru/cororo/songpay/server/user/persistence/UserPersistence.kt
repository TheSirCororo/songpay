package ru.cororo.songpay.server.user.persistence

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert
import ru.cororo.songpay.common.user.model.User
import ru.cororo.songpay.server.persistence.CrudPersistence
import ru.cororo.songpay.server.persistence.asResultRow
import ru.cororo.songpay.server.persistence.deleteById
import ru.cororo.songpay.server.persistence.selectById
import ru.cororo.songpay.server.persistence.sqlTransaction
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

object UserPersistence : CrudPersistence<User, Uuid> {
    override suspend fun save(entity: User): User = sqlTransaction {
        UsersTable.upsert {
            entity.id?.toJavaUuid()?.let { id -> it[this.id] = id }
            it[email] = entity.email
            it[username] = entity.username
            it[isVerified] = entity.isVerified
        }.asResultRow()?.asUser() ?: error("User $entity wasn't saved!")
    }

    override suspend fun delete(entity: User): Unit =
        deleteById(entity.id ?: error("User $entity wasn't persisted!"))

    override suspend fun deleteById(id: Uuid): Unit = sqlTransaction {
        UsersTable.deleteById(id)
    }

    override suspend fun getById(entityId: Uuid): User? = sqlTransaction {
        UsersTable.selectById(entityId).firstOrNull()?.asUser()
    }

    override suspend fun getAll(): List<User> = sqlTransaction {
        UsersTable.selectAll().map { it.asUser() }
    }
}

object UsersTable : UUIDTable() {
    val email = varchar("email", 128).uniqueIndex()
    val username = varchar("username", 16).uniqueIndex()
    val isVerified = bool("isVerified").default(false)
}

fun ResultRow.asUser() = User(
    this[UsersTable.id].value.toKotlinUuid(),
    this[UsersTable.email],
    this[UsersTable.username],
    this[UsersTable.isVerified]
)
