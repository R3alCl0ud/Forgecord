package io.discloader.discloader.network.rest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.logger.DLLogger;
import io.discloader.discloader.network.util.Methods;

/**
 * @author Perry Berman
 *
 * @param <T>
 *            Completion type
 */
public abstract class RestAction<T> {

	public static final Logger LOG = DLLogger.getLogger(RestAction.class);
	public static final Consumer<?> DEFAULT_SUCCESS = o -> {};
	public static final Consumer<Throwable> DEFAULT_FAILURE = t -> {
		LOG.throwing(t.getStackTrace()[0].getClassName(), t.getStackTrace()[0].getMethodName(), t);
	};

	protected final DiscLoader loader;
	protected final String endpoint;
	protected final RESTOptions options;
	protected final Methods method;
	protected final Gson gson;
	protected final CompletableFuture<T> future = new CompletableFuture<>();
	protected final AtomicBoolean executed = new AtomicBoolean(false);

	public RestAction(DiscLoader loader, String endpoint, Methods method, RESTOptions options) {
		this.loader = loader;
		this.endpoint = endpoint;
		this.method = method;
		this.options = options;
		this.gson = new Gson();
	}

	protected void autoExecute() {
		if (loader.getOptions().autoExecRestActions()) {
			execute();
		}
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	public RestAction<T> complete(T value) {
		future.complete(value);
		return this;
	}

	public RestAction<T> completeExceptionally(Throwable ex) {
		future.completeExceptionally(ex);
		return this;
	}

	public abstract RestAction<T> execute();

	public RestAction<T> execute(Consumer<? super T> success, Consumer<Throwable> failure) {
		return onSuccess(success).onException(failure).execute();
	}

	public T get() throws InterruptedException, ExecutionException {
		return future.get();
	}

	public boolean isCanceled() {
		return future.isCancelled();
	}

	public boolean isDone() {
		return future.isDone();
	}

	public T join() {
		return future.join();
	}

	public RestAction<T> onException(Consumer<Throwable> failure) {
		future.exceptionally(ex -> {
			failure.accept(ex);
			return null;
		});
		return this;
	}

	public RestAction<T> onSuccess(Consumer<? super T> action) {
		future.thenAcceptAsync(action);
		return this;
	}

	protected <U> CompletableFuture<U> sendRequest(Class<U> cls) {
		return loader.rest.request(method, endpoint, options, cls);
	}

}
