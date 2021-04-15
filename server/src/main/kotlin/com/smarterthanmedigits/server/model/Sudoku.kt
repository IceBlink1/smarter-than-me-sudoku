package com.smarterthanmedigits.server.model

import com.vladmihalcea.hibernate.type.array.ListArrayType
import lombok.Data
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity
@Table(name = "sudoku")
@Data
@TypeDef(
    name = "list-array",
    typeClass = ListArrayType::class
)
class Sudoku : BaseEntity() {
    @Column(name = "original_sudoku", unique = true)
    var originalSudoku: String? = null

    @Column(name = "solution")
    var solution: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var user: User? = null

    @Column(name = "complexity")
    var complexity: Int? = null

    @Column(name = "current_sudoku")
    var currentSudoku: String? = null
}