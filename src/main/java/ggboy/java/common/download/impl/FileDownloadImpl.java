package ggboy.java.common.download.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import ggboy.java.common.download.FileDownload;
import ggboy.java.common.exception.CommonUtilException;
import ggboy.java.common.thread.pool.ThreadPool;
import ggboy.java.common.utils.array.ArrayUtil;
import ggboy.java.common.utils.bytee.ByteUtil;
import ggboy.java.common.utils.io.IoUtil;

public class FileDownloadImpl implements FileDownload {

	private final static int BUFFER_SIZE = 8 * 1024;
	private final static String TEMP_SUFFIX_NAME = ".mustry";
	private final static String PROPERTITY_SUFFIX_NAME = ".propertity";

	private String path;
	private String name;
	private long size;
	private volatile RandomAccessFile fileWriter;
	private volatile RandomAccessFile propertityFileWriter;
	private volatile byte[] taskList; // 0:false 1:true
	private volatile List<Integer> lockList;
	private volatile ThreadPool pool;
	private long schedule = -1;
	private static long start;
	/*
	 * 重新获取任务的基数 当获取任务时，下个任务已经被完成，则通过基数重新获取
	 */
	private final int GET_TASK_CARDINAL_NUMBER = 10;

	public FileDownloadImpl(String path, String name, long fileSize) {
		this.path = path;
		this.name = name;
		this.size = fileSize;
	}

	@Override
	public void addTask(InputStream is) throws CommonUtilException {
		if (init())
			pool.push(new Worker(is));
	}

	@Override
	public long schedule() {
		if (schedule < 0)
			return -1;
		return schedule * BUFFER_SIZE;
	}

	@Override
	public void destroy() {
		synchronized (this) {
			if (fileWriter != null)
				try {
					fileWriter.close();
				} catch (IOException e) {
				} finally {
					fileWriter = null;
				}

			if (propertityFileWriter != null)
				try {
					propertityFileWriter.close();
				} catch (IOException e) {
				} finally {
					propertityFileWriter = null;
				}

			taskList = null;
			lockList = null;
			pool = null;
			schedule = -1;
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	private boolean init() throws CommonUtilException {
		// 初始化线程池
		if (pool == null)
			synchronized (this) {
				if (pool == null)
					pool = new ThreadPool(2, 100, 0, 1);
			}

		// 初始化文件和任务列表
		if (fileWriter == null)
			synchronized (this) {
				if (fileWriter == null) {
					System.out.println("init");
					start = System.currentTimeMillis();
					schedule = 0;
					try {
						File file = new File(path + "/" + name);
						if (file.exists()) {
							System.out.println("文件已存在");
							destroy();
							return false;
						}
						file = new File(path + "/" + name + TEMP_SUFFIX_NAME);
						byte[] taskList = null;
						if (file.exists()) {
							// 文件存在则获取临时文件并初始化任务列表
							fileWriter = new RandomAccessFile(file, "rw");
							// 查看配置文件并初始化已完成列表
							taskList = readTaskList(path + "/" + name + PROPERTITY_SUFFIX_NAME);
							// 截取前八位作为schedule计数
							if (taskList != null) {
								schedule = ByteUtil.readLong(taskList);
								taskList = ArrayUtil.subArray(taskList, 8, taskList.length);
							}
						} else {
							File folder = new File(path);
							if (!folder.isDirectory())
								// 创建文件夹
								folder.mkdirs();
							// 创建文件
							createEmptyFile(file);
						}
						// 创建任务列表
						createTaskList(taskList);
					} catch (Exception e) {
						System.out.println(e.getMessage());
						destroy();
						throw new CommonUtilException("初始化异常");
					}
				}
			}
		return true;
	}

	/**
	 * 非线程安全 method
	 */
	private void createEmptyFile(File file) throws CommonUtilException {
		try {
			// 创建一个文件 并赋予"读/写"权限
			fileWriter = new RandomAccessFile(file, "rw");
			// 将文件直接改变成size字节大小
			fileWriter.setLength(size);
		} catch (IOException e) {
			throw new CommonUtilException("创建文件出现问题。");
		}
	}

	/**
	 * 非线程安全 method
	 */
	private void createTaskList(byte[] finishedList) {
		if (finishedList != null)
			// 从临时文件中读取的列表不为空则直接赋值
			taskList = finishedList;
		else {
			// 根据bufferSize切成 index组
			long index = size % BUFFER_SIZE == 0 ? size / BUFFER_SIZE : size / BUFFER_SIZE + 1;
			// 将index组 下载的结果 切成length份 每份8个 装入taskList中 byte中的每一位代表 每一个的下载结果
			int length = (int) (index % 8 == 0 ? index / 8 : index / 8 + 1);
			// 初始化 并全为false
			taskList = new byte[length];
		}
	}

	/**
	 * 非线程安全 method
	 */
	private byte[] readTaskList(String propertityFilePath) {
		File propertityFile = new File(propertityFilePath);
		if (propertityFile.exists()) {
			try {
				return IoUtil.file2bytes(propertityFile);
			} catch (CommonUtilException e) {
				System.out.println("获取配置文件失败");
			}
		}
		return null;
	}

	/**
	 * 非线程安全 method
	 */
	private void writeTaskList(int index) throws IOException {
		if (propertityFileWriter == null) {
			propertityFileWriter = new RandomAccessFile(path + "/" + name + PROPERTITY_SUFFIX_NAME, "rw");
		}
		propertityFileWriter.seek(0);
		propertityFileWriter.write(ByteUtil.long2Bytes(++schedule));
		propertityFileWriter.seek(8 + index);
		propertityFileWriter.write(taskList[index]);
	}

	/**
	 * 非线程安全 method
	 */
	private void finish() {
		System.out.println("finish");
		destroy();
		File file = new File(path + "/" + name + TEMP_SUFFIX_NAME);
		if (file.exists())
			file.renameTo(new File(path + "/" + name));
		File propertityFile = new File(path + "/" + name + PROPERTITY_SUFFIX_NAME);
		if (propertityFile.exists())
			propertityFile.delete();
	}

	private void writeFile(byte[] data, int dataSize, int index, int indexOfIndex)
			throws IOException, CommonUtilException {
		synchronized (this) {
			if (fileWriter == null)
				throw new CommonUtilException("destroy");
			synchronized (fileWriter) {
				synchronized (taskList) {
					if ((taskList[index] & (1 << indexOfIndex)) == 0) {
						fileWriter.seek((long) (8 * index + indexOfIndex) * BUFFER_SIZE);
						fileWriter.write(data, 0, dataSize);
						taskList[index] = (byte) (taskList[index] | (1 << indexOfIndex));
						// 将任务列表写入配置文件中
						writeTaskList(index);
						// 写入文件从每组的最低位开始 当最高位完成时代表全组完成
						if (indexOfIndex == 7)
							unlockIndex(index);
					}
				}
			}
		}
	}

	private int getTask(int currentIndex) {
		synchronized (this) {
			if (taskList == null)
				return -1;
			if (currentIndex < taskList.length - 1) {
				if (!isFinished(taskList[currentIndex + 1]) && lockIndex(currentIndex + 1))
					return currentIndex + 1;

				// 重新分配任务
				int index = 0;
				while (true) {
					if (!isFinished(taskList[index]) && index > currentIndex && lockIndex(index))
						return index;
					if (index == taskList.length - 1)
						break;
					if (index > taskList.length - GET_TASK_CARDINAL_NUMBER)
						index++;
					else
						index = (taskList.length - index) / GET_TASK_CARDINAL_NUMBER + index; // 公式 (a - x) / n + x
				}
			}

			// 无可分配任务 校验 taskList
			boolean flag = true;
			for (int i = 0; i < taskList.length; i++) {
				if (!isFinished(taskList[i]))
					// 当前任务之后有未完成的任务且没有被其他线程占用
					if (i > currentIndex && lockIndex(i))
						return i;
					else
						flag = false;
				// 测试发现复用流无法满足条件 此时当前线程所获取的流无法下载之前的任务
			}
			if (flag)
				finish(); // 任务完成 调用结束
			return -1;
		}
	}

	private boolean lockIndex(int index) {
		if (lockList == null)
			synchronized (this) {
				if (lockList == null)
					lockList = new ArrayList<Integer>();
			}

		synchronized (lockList) {
			if (lockList.contains(index))
				return false;

			lockList.add(index);
			return true;
		}
	}

	private void unlockIndex(int index) {
		synchronized (lockList) {
			lockList.remove(index);
		}
	}

	private final static boolean isFinished(byte data) {
		return (data & 0xff) == 0xff;
	}

	class Worker implements Runnable {
		private InputStream inputStream;
		private int index = -1;
		List<Integer> num = new ArrayList<>();

		Worker(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public void run() {
			try {
				int currentIndex = -1;
				byte[] buffer = null;
				int errorTime = 0;
				stop_while: while (true) {
					if (errorTime > 10)
						break;

					index = getTask(currentIndex);
					if (index == -1)
						break;

					try {
						int skip = index - currentIndex - 1;
						if (skip > 0) {
							inputStream.skip(skip * BUFFER_SIZE * 8);
						}
					} catch (IOException e) {
						System.out.println("跳过流文件异常");
						errorTime++;
						continue;
					}

					for (int i = 0; i < 8; i++) {
						// 多线程环境下 taskList 可能会报空指针 没有什么好的解决办法 但是捕获丢掉也不影响逻辑
						if ((taskList[index] & (1 << i)) == 0) {
							if (buffer == null) {
								buffer = new byte[BUFFER_SIZE];
							}
							int length = 0;
							try {
								length = IoUtil.in2BytesByLength(inputStream, buffer, BUFFER_SIZE);
							} catch (CommonUtilException e) {
								System.out.println("读文件异常");
								break stop_while;
							}

							try {
								writeFile(buffer, length, index, i);
							} catch (IOException e) {
								System.out.println("写文件时出现异常");
								break stop_while;
							} catch (CommonUtilException e) {
								if ("destroy".equals(e.getMessage())) {
									System.out.println("下载已关闭");
									break stop_while;
								}
							}
						}
					}
					currentIndex = index;
				}
				// 多线程环境下 可能会报空指针 没有什么好的解决办法 但是捕获丢掉也不影响逻辑
				unlockIndex(index);
			} catch (NullPointerException e) {
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
}