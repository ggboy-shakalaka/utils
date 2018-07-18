package mustry.common.context.holder;

public interface ContextHolder<T> {
	T getContext();
	void createContext(String className);
}