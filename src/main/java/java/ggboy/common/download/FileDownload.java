package java.ggboy.common.download;

import java.ggboy.common.exception.CommonUtilException;
import java.io.InputStream;

public interface FileDownload {
	void addTask(InputStream is) throws CommonUtilException;

	long schedule();

	void destroy();
}