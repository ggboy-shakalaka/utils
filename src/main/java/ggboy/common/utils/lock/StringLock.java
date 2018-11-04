package ggboy.common.utils.lock;

import ggboy.common.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class StringLock {
	private String lockKey;

	private StringLock(String key) {
		this.lockKey = key;
	}

	public void unlock() {
		unlock(this.lockKey);
	}
	
	private final static List<String> keys;
	static {
		keys = new ArrayList<String>();
	}

	public final static StringLock readLock(String key) {
		String readkey = buildReadStr(key);
		String writekey = buildWriteStr(key);
		synchronized (key.intern()) {
			while (keys.contains(writekey)) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
			keys.add(readkey);
		}
		return new StringLock(readkey);
	}

	public final static StringLock writeLock(String key) {
		String readkey = buildReadStr(key);
		String writekey = buildWriteStr(key);
		synchronized (key.intern()) {
			while (keys.contains(readkey) || keys.contains(writekey)) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
			keys.add(writekey);
		}
		return new StringLock(writekey);
	}

	private static void unlock(String key) {
		synchronized (key.intern()) {
			keys.remove(key);
		}
	}

	private static String buildReadStr(String str) {
		return StringUtil.toString(str, "-r").intern();
	}

	private static String buildWriteStr(String str) {
		return StringUtil.toString(str, "-w").intern();
	}
}