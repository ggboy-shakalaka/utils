package mustry.common.utils.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import mustry.common.constant.IOConstant;
import mustry.common.enums.ErrorCode;
import mustry.common.exception.CommonUtilException;
import mustry.common.utils.array.ArrayUtil;
import mustry.common.utils.bean.BeanUtil;
import mustry.common.utils.bytee.ByteUtil;

public class IoUtil {

	public static byte[] in2Bytes(InputStream inputStream) throws CommonUtilException {
		return in2Bytes(inputStream, 0);
	}
	
	public final static byte[] in2Bytes(InputStream in, int bufferSize) throws CommonUtilException {
		if (BeanUtil.isEmpty(in)) {
			throw new CommonUtilException(ErrorCode.reverse_serialize_error);
		}
		try {
			bufferSize = bufferSize > 0 ? bufferSize : IOConstant.default_buffer_length;
			byte[] dataBuffer = new byte[bufferSize];
			byte[] data = null;
			int dataSize = -1;
			while ((dataSize = in.read(dataBuffer)) != -1) {
				data = ArrayUtil.merge(data, dataBuffer, 0, dataSize);
			}
			return data;
		} catch (IOException e) {
			throw new CommonUtilException(ErrorCode.reverse_serialize_error, e);
		}
	}
	
	public final static byte[] in2BytesByLength(InputStream in, int dataLength) throws CommonUtilException {
		byte[] data = new byte[dataLength];
		in2BytesByLength(in, data, dataLength);
		return data;
	}
	
	public final static int in2BytesByLength(InputStream in, byte[] data, int dataLength) throws CommonUtilException {
		if (BeanUtil.isEmpty(in) || dataLength <= 0) {
			throw new CommonUtilException(ErrorCode.system_error);
		}
		
		try {
			int dataSize = -1;
			int count = 0;
			while (count < dataLength) {
				dataSize = in.read(data, count, dataLength - count);
				if (dataSize == -1) {
					break;
				}
				count += dataSize;
			}
			return count;
		} catch (IOException e) {
			throw new CommonUtilException(ErrorCode.reverse_serialize_error, e);
		}
	}

	public static byte[] in2BytesByHeadWithDataLength(InputStream in) throws CommonUtilException {
		if (BeanUtil.isEmpty(in)) {
			return null;
		}
		byte[] headBuffer = in2BytesByLength(in, IOConstant.default_head_size);
		int dataLength = ByteUtil.byte2Int(headBuffer);
		return in2BytesByLength(in, dataLength);
	}
	
	public final static int in2Out(InputStream in, OutputStream out) throws CommonUtilException {
		return in2Out(in, out, IOConstant.default_buffer_length);
	}

	public final static int in2Out(InputStream in, OutputStream out, int bufferSize) throws CommonUtilException {
		if (BeanUtil.isEmpty(in) || BeanUtil.isEmpty(out)) {
			throw new CommonUtilException(ErrorCode.reverse_serialize_error);
		}
		try {
			bufferSize = bufferSize > 0 ? bufferSize : IOConstant.default_buffer_length;
			byte[] dataBuffer = new byte[bufferSize];
			int dataSize = -1;
			int count = 0;
			while ((dataSize = in.read(dataBuffer)) != -1) {
				out.write(dataBuffer, 0, dataSize);
				count += dataSize;
			}
			return count;
		} catch (IOException e) {
			throw new CommonUtilException(ErrorCode.reverse_serialize_error, e);
		}
	}
	
	public static Object byte2Obj(byte[] data) throws CommonUtilException {
		if (BeanUtil.isEmpty(data))
			return null;
		ByteArrayInputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			inputStream = new ByteArrayInputStream(data);
			objectInputStream = new ObjectInputStream(inputStream);
			return objectInputStream.readObject();
		} catch (Exception e) {
			throw new CommonUtilException(ErrorCode.reverse_serialize_error, e);
		} finally {
			if (inputStream != null)
				try { inputStream.close(); } catch (IOException e) {}
			if (objectInputStream != null)
				try { objectInputStream.close(); } catch (IOException e) {}
		}
	}

	public static byte[] obj2Byte(Object object) throws CommonUtilException {
		if (BeanUtil.isEmpty(object))
			return null;
		ByteArrayOutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			outputStream = new ByteArrayOutputStream(IOConstant.default_buffer_length);
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(object);
			return outputStream.toByteArray();
		} catch (Exception e) {
			throw new CommonUtilException(ErrorCode.serialize_error, e);
		} finally {
			if (objectOutputStream != null)
				try { objectOutputStream.close(); } catch (IOException e) {}
			if (outputStream != null)
				try { outputStream.close(); } catch (IOException e) {}
		}
	}

	public static byte[] file2bytes(File file) throws CommonUtilException {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			return in2Bytes(fileInputStream);
		} catch (CommonUtilException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new CommonUtilException(ErrorCode.serialize_error, e);
		} finally {
			if (fileInputStream != null)
				try { fileInputStream.close(); } catch (IOException e) {}
		}
	}

	// public static void byte2File(byte[] buf, String filePath, String fileName) {
	// BufferedOutputStream bos = null;
	// FileOutputStream fos = null;
	// File file = null;
	// try {
	// File dir = new File(filePath);
	// if (!dir.exists() && dir.isDirectory()) {
	// dir.mkdirs();
	// }
	// file = new File(filePath + File.separator + fileName);
	// fos = new FileOutputStream(file);
	// bos = new BufferedOutputStream(fos);
	// bos.write(buf);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (bos != null) {
	// try {
	// bos.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (fos != null) {
	// try {
	// fos.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
}
