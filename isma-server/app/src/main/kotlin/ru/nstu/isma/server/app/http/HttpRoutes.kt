package ru.nstu.isma.server.app.http

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationStatus
import java.io.File

fun Routing.simulationResultRoutes(sessionStore: ISimulationSessionStore) {
    get("/simulation/{id}/download") {
        val simulationId = call.parameters["id"]?.toLongOrNull()
        if (simulationId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid simulation ID")
            return@get
        }

        val session = sessionStore.get(simulationId)
        if (session == null || session.status != SimulationStatus.COMPLETED) {
            val status = if (session == null) "not found" else "not completed (${session.status})"
            call.respond(HttpStatusCode.NotFound, "Simulation $simulationId: $status")
            return@get
        }

        val resultFilePath = session.resultFilePath
        if (resultFilePath == null || !File(resultFilePath).exists()) {
            call.respond(HttpStatusCode.NotFound, "Result file not found")
            return@get
        }

        call.response.headers.append(HttpHeaders.ContentType, "text/csv; charset=utf-8")
        call.response.headers.append(
            HttpHeaders.ContentDisposition,
            "attachment; filename=\"simulation_$simulationId.csv\""
        )
        call.respondFile(File(resultFilePath))
    }
}
