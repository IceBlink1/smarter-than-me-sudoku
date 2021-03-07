package com.smarterthanmedigits.server.rest

import com.smarterthanmedigits.server.dto.SudokuDto
import com.smarterthanmedigits.server.mappers.SudokuMapper
import com.smarterthanmedigits.server.model.Sudoku
import com.smarterthanmedigits.server.model.User
import com.smarterthanmedigits.server.repository.SudokuRepository
import com.smarterthanmedigits.server.response.SudokuResponse
import com.smarterthanmedigits.server.security.JwtUser
import com.smarterthanmedigits.server.service.UserService
import io.micrometer.core.ipc.http.HttpSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = arrayOf("/api/v1/content/"))
class ContentRestControllerV1 @Autowired constructor(val userService: UserService,
                                                     val sudokuRepository: SudokuRepository,
                                                     val sudokuMapper: SudokuMapper) {

    @GetMapping("sudokus")
    @ResponseBody
    fun getSudokus(): ResponseEntity<List<SudokuResponse>> {
        val user = getUser()
        val sudokus = user?.sudokus
        return if (sudokus != null) {
            ResponseEntity.ok(sudokus.map { sudokuMapper.mapModelToResponse(it) })
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("sudokus")
    fun postSudokus(@RequestBody sudoku: List<SudokuDto>) {
        val user = getUser()
        sudoku.map {
            Sudoku().apply {
                originalSudoku = it.sudoku
                solution = it.solution
                this.user = user
            }
        }.forEach {
            sudokuRepository.save(it)
        }
    }

    private fun getUser(): User? {
        return (SecurityContextHolder.getContext().authentication.principal as JwtUser).username?.let {
            userService.findByUsername(it)
        }
    }
}