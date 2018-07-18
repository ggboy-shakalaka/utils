package mustry.common.http;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import mustry.common.exception.CommonUtilException;
import mustry.common.utils.httpClient.HttpClientUtil;

public class SimpleHttpClient {

	private final HttpClient client;
	
	public SimpleHttpClient(int maxTotal, int maxPerRoute, int connectionRequestTimeout,int connectTimeout, int socketTimeout) {
		this.client = HttpClientUtil.buildClient(maxTotal, maxPerRoute, connectionRequestTimeout, connectTimeout, socketTimeout);
	}
	
	public SimpleHttpClient(HttpClient client) {
		this.client = client;
	}
	
	public HttpResponse get(String url) throws CommonUtilException {
		return HttpClientUtil.execute(this.client, HttpClientUtil.buildGetReq(url, null));
	}
	
	public HttpResponse get(String url, Map<String, String> param) throws CommonUtilException {
		return HttpClientUtil.execute(this.client, HttpClientUtil.buildGetReq(url, param));
	}
	
	public HttpResponse post(String url, Map<String, String> param) throws CommonUtilException {
		return post(url, param, null);
	}
	
	public HttpResponse post(String url, Map<String, String> param, Map<String, File> fileMap) throws CommonUtilException {
		return HttpClientUtil.execute(this.client, HttpClientUtil.buildPostReq(url, HttpClientUtil.buildEntity(param, fileMap)));
	}
	
	private static final int maxTotal = 10;
	private static final int maxPerRoute = 2;
	private static final int connectionRequestTimeout = 60000;
	private static final int connectTimeout = 60000;
	private static final int socketTimeout = 60000;
	private static volatile SimpleHttpClient singleSimpleClient;

	public static SimpleHttpClient getClient() {
		if (singleSimpleClient != null) {
			return singleSimpleClient;
		}
		synchronized (SimpleHttpClient.class) {
			if (singleSimpleClient != null) {
				return singleSimpleClient;
			}
			HttpClient client = HttpClientUtil.buildClient(maxTotal, maxPerRoute, connectionRequestTimeout, connectTimeout, socketTimeout);
			return singleSimpleClient = new SimpleHttpClient(client);
		}
	}
}
