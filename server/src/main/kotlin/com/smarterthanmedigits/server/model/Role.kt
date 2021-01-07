package com.smarterthanmedigits.server.model

import lombok.Data
import javax.persistence.*

@Entity
@Table(name = "roles")
@Data
class Role : BaseEntity() {

    @Column(name = "name")
    var name: String? = null

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    var users: List<User> = arrayListOf()
}