package ggboy.common.download;

import ggboy.common.exception.CommonUtilException;

import java.io.InputStream;

public interface FileDownload {
	void addTask(InputStream is) throws CommonUtilException;

	long schedule();

	void destroy();
}