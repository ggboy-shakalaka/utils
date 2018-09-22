package ggboy.java.common.download;

import java.io.InputStream;

import ggboy.java.common.exception.CommonUtilException;

public interface FileDownload {
	void addTask(InputStream is) throws CommonUtilException;

	long schedule();

	void destroy();
}