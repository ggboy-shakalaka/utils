package mustry.common.context.holder;

import mustry.common.exception.CommonUtilException;
import mustry.common.utils.bean.BeanUtil;
import mustry.common.utils.string.StringUtil;

public class ThreadLocalContextHolder<T> implements ContextHolder<T> {

	private ThreadLocal<T> contextHolder = new ThreadLocal<T>();
	private String className;
	
	public ThreadLocalContextHolder(String className) {
		if (StringUtil.isEmpty(className)) {
			throw new NullPointerException("class name cannot be null");
		}

		this.className = className;
	}
	
	@Override
	public T getContext() {
		T bean = contextHolder.get();

		if (bean == null) {
			bean = this.init();
			contextHolder.set(bean);
		}
		
		return bean;
	}
	
	@Override
	public void createContext(String className) {
		if (StringUtil.isEmpty(className)) {
			throw new NullPointerException("class name cannot be null");
		}
		
		if (className.equals(this.className)) {
			return;
		}
		
		this.className = className;
		this.contextHolder.remove();
	}
	
	private T init() {
		try {
			return BeanUtil.createBean(this.className);
		} catch (CommonUtilException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}
	}
}