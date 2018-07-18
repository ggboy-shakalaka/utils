package mustry.common.download;

import java.io.InputStream;

import mustry.common.exception.CommonUtilException;

public interface FileDownload {
	void addTask(InputStream is) throws CommonUtilException;

	long schedule();

	void destroy();
}