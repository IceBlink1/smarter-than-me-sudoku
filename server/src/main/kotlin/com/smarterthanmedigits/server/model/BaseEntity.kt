package com.smarterthanmedigits.server.model

import lombok.Data
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@MappedSuperclass
@Data
open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreatedDate
    @Column(name = "created")
    var created: Date? = null

    @LastModifiedDate
    @Column(name = "updated")
    var updated: Date? = null


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status? = null
}

enum class Status {
    ACTIVE, NOT_ACTIVE, DELETED
}