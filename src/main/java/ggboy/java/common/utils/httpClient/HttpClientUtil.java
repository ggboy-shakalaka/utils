package ggboy.java.common.utils.httpClient;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.alibaba.fastjson.JSON;

import ggboy.java.common.constant.BaseConstant;
import ggboy.java.common.enums.ErrorCode;
import ggboy.java.common.exception.CommonUtilException;
import ggboy.java.common.utils.bean.BeanUtil;
import ggboy.java.common.utils.string.StringUtil;

public class HttpClientUtil {

	public final static HttpEntity buildEntity(Map<String, String> param, Map<String, File> fileParam) {
		StringEntity entity = new StringEntity(JSON.toJSONString(param), Charset.forName("UTF-8"));
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		return entity;

		// MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		// if (!BeanUtil.isNull(fileParam)) {
		// for (Map.Entry<String, File> item : fileParam.entrySet()) {
		// if (BeanUtil.isEmpty(item.getValue())) {
		// continue;
		// }
		// entityBuilder.addBinaryBody(item.getKey(), item.getValue());
		// }
		// }
		// if (!BeanUtil.isNull(param)) {
		// for (Map.Entry<String, String> item : param.entrySet()) {
		// if (BeanUtil.isEmpty(item.getValue())) {
		// continue;
		// }
		// entityBuilder.addTextBody(item.getKey(), item.getValue());
		// }
		// }
		// return entityBuilder.build();
	}

	public final static Post buildPostReq(String url, HttpEntity entity) {
		if (BeanUtil.isEmpty(url)) {
			return null;
		}
		return new Post(url, entity);
	}

	public final static Get buildGetReq(String url, Map<String, String> param) {
		if (BeanUtil.isEmpty(url)) {
			return null;
		}
		String paramStr = "";
		if (!BeanUtil.isEmpty(param)) {
			StringBuilder sb = new StringBuilder("?");
			for (Entry<String, String> entry : param.entrySet()) {
				if (!StringUtil.isEmpty(entry.getValue()))
					try {
						String value = URLEncoder.encode(entry.getValue(), BaseConstant.default_charset);
						sb.append(entry.getKey()).append("=").append(value).append("&");
					} catch (UnsupportedEncodingException e) {
					}
			}
			paramStr = sb.delete(sb.length() - 1, sb.length()).toString();
		}
		return new Get(url + paramStr);
	}

	public final static HttpResponse execute(HttpClient httpClient, HttpUriRequest req) throws CommonUtilException {
		try {
			return httpClient.execute(req);
		} catch (Exception e) {
			throw new CommonUtilException(ErrorCode.http_send_request_error, e);
		}
	}

	public static HttpClient buildClient(int maxTotal, int maxPerRoute, int connectionRequestTimeout, int connectTimeout, int socketTimeout) {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		connManager.setMaxTotal(maxTotal);
		connManager.setDefaultMaxPerRoute(maxPerRoute);
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
		return HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();
	}

	static class Post extends HttpPost {
		Post(final String url, HttpEntity entity) {
			super(url);
			setEntity(entity);
		}
	}

	static class Get extends HttpGet {
		Get(final String url) {
			super(url);
		}
	}
}
