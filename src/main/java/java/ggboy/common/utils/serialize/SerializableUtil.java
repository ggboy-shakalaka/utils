package java.ggboy.common.utils.serialize;

import java.ggboy.common.enums.ErrorCode;
import java.ggboy.common.exception.CommonUtilException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.ggboy.common.utils.bean.BeanUtil;

public class SerializableUtil {
	public static byte[] serialize(Object object) throws CommonUtilException {
		if (BeanUtil.isEmpty(object))
			return null;
		ByteArrayOutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
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

	@SuppressWarnings("unchecked")
	public static <T> T reverseSerialize(byte[] data, Class<T> clazz) throws CommonUtilException {
		return (T) reverseSerialize(data);
	}

	public static Object reverseSerialize(byte[] data) throws CommonUtilException {
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
}
