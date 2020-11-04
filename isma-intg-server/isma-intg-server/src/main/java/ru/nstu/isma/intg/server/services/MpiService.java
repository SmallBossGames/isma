package ru.nstu.isma.intg.server.services;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import mpi.MPI;
import mpi.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.server.models.Message;

public class MpiService {

	private static final int MASTER_RANK = 0;

	private final Logger logger = LoggerFactory.getLogger(MpiService.class);
	private ExecutorService executor;
    private int rank;
    private int rankCount;

	public MpiService() {
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setDaemon(true)
				.setNameFormat(Thread.currentThread().getName() + "-mpi-exec")
				.build();
		this.executor = Executors.newSingleThreadExecutor(threadFactory);
	}

	public String[] initMpi(String[] args) {
		return execute(() -> {
            String[] results = MPI.Init(args);
            rank = MPI.COMM_WORLD.Rank();
            rankCount = MPI.COMM_WORLD.Size();
            return results;
        });
	}

	public void finalizeMpi() {
		execute(MPI::Finalize);
	}

	public boolean isMasterNode() {
		return getRank() == MASTER_RANK;
	}

	public int getRank() {
		return rank;
	}

	public int getRankCount() {
		return rankCount;
	}

	public void barrier() {
		execute(MPI.COMM_WORLD::Barrier);
	}

	public void sendMessageToEveryone(Message message) {
		execute(() -> {
			logger.debug("Sending a message {} to all ranks ...", message.getType());
			Message[] messages = new Message[1];
			messages[0] = message;
			int rankCount = MPI.COMM_WORLD.Size();
			for (int rank = 0; rank < rankCount; rank++) {
				MPI.COMM_WORLD.Isend(messages, 0, 1, MPI.OBJECT, rank, 99);
			}
		});
	}

	public void sendMessageToMaster(Message message) {
		sendMessage(message, MASTER_RANK);
	}

	public void sendMessage(Message message, int toRank) {
		execute(() -> {
			logger.debug("Sending a message {} to rank {}...", message.getType(), toRank);
			Message[] messages = new Message[1];
			messages[0] = message;
			MPI.COMM_WORLD.Isend(messages, 0, 1, MPI.OBJECT, toRank, 99);
		});
	}

	public Message receiveMessageFromMaster() {
		return receiveMessageFromRank(MASTER_RANK);
	}

	public Message receiveMessageFromRank(int fromRank) {
		return execute(() -> {
			Status status = MPI.COMM_WORLD.Iprobe(fromRank, 99);
			if (status == null) {
				return null;
			}

			Message[] messages = new Message[1];
			MPI.COMM_WORLD.Recv(messages, 0, 1, MPI.OBJECT, fromRank, 99);
			return messages[0];
		});
	}

	private <T> T execute(Callable<T> task) {
		try {
			return executor.submit(task::call).get();
		} catch (InterruptedException | ExecutionException e) {
			throw Throwables.propagate(e);
		}
	}

	private void execute(Runnable task) {
		try {
			executor.submit(task::run).get();
		} catch (InterruptedException | ExecutionException e) {
			throw Throwables.propagate(e);
		}
	}

}
