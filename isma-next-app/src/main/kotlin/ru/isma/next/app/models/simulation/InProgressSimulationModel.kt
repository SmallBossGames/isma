package ru.isma.next.app.models.simulation

import javafx.beans.property.SimpleDoubleProperty
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel
import java.util.concurrent.atomic.AtomicReference

class InProgressSimulationModel(
    val id: Int,
    val model: String,
    val parameters: SimulationParametersModel
) {
    private var actualProgress = 0.0

    val progressProperty = SimpleDoubleProperty(actualProgress)

    private val updateProgressJob = AtomicReference<Job?>()

    fun commitProgress(value: Double) {
        actualProgress = value

        val job = coroutineScope.launch(start = CoroutineStart.LAZY) {
            delay(70)

            withContext(Dispatchers.JavaFx){
                progressProperty.set(actualProgress)
            }

            updateProgressJob.setRelease(null)
        }

        if (updateProgressJob.compareAndSet(null, job)){
            job.start()
        }
    }

    private companion object {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
    }
}

