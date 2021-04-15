package com.smarterthanmedigits.server.model

import lombok.Data
import javax.persistence.*


@Entity
@Table(name = "users")
@Data
class User : BaseEntity() {
    @Column(name = "username")
    var username: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "password")
    var password: String? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles: List<Role> = arrayListOf()

    @OneToMany(
        mappedBy = "user",
        cascade = arrayOf(CascadeType.ALL),
        orphanRemoval = true
    )
    var sudokus: List<Sudoku> = arrayListOf()

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status? = null

    @Column(name = "reset_password_code")
    var resetCode: String? = null
}