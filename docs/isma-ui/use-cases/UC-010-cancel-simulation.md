# UC-010: Cancel Running Simulation

## Description

This use case describes stopping a simulation that is currently executing. The user initiates cancellation from the Tasks panel, and the request is sent to the simulation server.

## Actors

- **User** — cancels a running simulation
- **Simulation server** — receives and processes the cancellation request

## Precondition

- A simulation is currently running
- The simulation appears in the "In progress" section of the Tasks panel
- The Tasks panel is open (user clicked the "Tasks" button)

## Main Flow

1. **User clicks the Abort button** (close icon) on a running task in the "In progress" section.
2. **A cancellation request is sent** to the simulation server for the running simulation.
3. **The server stops** the simulation execution.
4. **The task is removed** from the "In progress" section of the Tasks panel.
5. **No result is produced** — since the simulation was cancelled, no results are downloaded or cached.

## Alternative Flows

### A1: Simulation already completed

If the simulation has already finished (the task has moved to the "Completed" section), the Abort button is not available. The user sees the task with Show, Export, Remove, and Details buttons instead.

### A2: Server unreachable during cancellation

If the server is not reachable (e.g., it crashed during the simulation), the cancellation request fails. The task may remain in the "In progress" section briefly until the application detects the server disconnection and removes it automatically.

### A3: Task has no active simulation

In the unlikely event that a task entry exists without a corresponding active simulation on the server, the cancellation request has no effect.

## Postcondition

- The running simulation is stopped on the server
- The task is removed from the "In progress" section
- No result is produced or cached for the cancelled simulation

## Related Use Cases

- **UC-006** — Running a simulation (prerequisite)
