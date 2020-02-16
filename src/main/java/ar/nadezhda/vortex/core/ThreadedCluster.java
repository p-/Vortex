package ar.nadezhda.vortex.core;

import ar.nadezhda.vortex.interfaces.Cluster;
import ar.nadezhda.vortex.interfaces.Kernel;
import ar.nadezhda.vortex.support.Message;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>Un cluster multi-threading.</p>
	*
	* @author Agustín Golmar
	*/

public final class ThreadedCluster implements Cluster {

	private static final Logger log = LoggerFactory.getLogger(ThreadedCluster.class);

	private final int cores;
	private final int fragmentSize;
	private final int height;
	private final ExecutorService pool;
	private final Future<?> [] tasks;
	private final int width;

	private ThreadedCluster(final Builder builder) {
		this.cores = builder.cores;
		this.fragmentSize = builder.width / cores;
		this.height = builder.height;
		this.pool = Executors.newFixedThreadPool(builder.cores);
		this.width = builder.width;
		this.tasks = new Future<?> [this.cores];
	}

	@Override
	public void compute(final Kernel kernel) {
		for (int k = 0; k < cores; ++k) {
			final int lower = k * fragmentSize;
			final int upper = (k == cores - 1)? width : lower + fragmentSize;
			tasks[k] = pool.submit(() -> {
				for (int w = lower; w < upper; ++w) {
					for (int h = 0; h < height; ++h) {
						kernel.at(w, h);
					}
				}
			});
		}
		try {
			for (final Future<?> task : tasks) {
				task.get();
			}
		}
		catch (final ExecutionException | InterruptedException exception) {
			log.error(Message.CLUSTER_INTERRUPTED.toString());
		}
	}

	@Override
	public void release() {
		pool.shutdown();
		try {
			if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
				pool.shutdownNow();
				if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
					log.error(Message.CANNOT_TERMINATE_THREAD_POOL.toString());
				}
			}
		}
		catch (final InterruptedException exception) {
			pool.shutdownNow();
		}
	}

	public static final class Builder {

		private int cores;
		private int height;
		private int width;

		public ThreadedCluster build() {
			return new ThreadedCluster(this);
		}

		public Builder cores(final int cores) {
			this.cores = cores;
			return this;
		}

		public Builder height(final int height) {
			this.height = height;
			return this;
		}

		public Builder width(final int width) {
			this.width = width;
			return this;
		}
	}
}
