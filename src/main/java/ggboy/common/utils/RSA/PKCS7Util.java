package mustry.common.utils.RSA;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

public class PKCS7Util {
	
	private static String digestAlgorithm = "SHA1";
	private static String signingAlgorithm = "SHA1withRSA";

	public static PKCS7Util getSigner(String keyStorePath, String keyPassword) throws Exception{
		return getSigner(keyStorePath, keyPassword, keyPassword);
	}
	
	public static PKCS7Util getSigner(String keyStorePath, String keyStorePassword, String keyPassword) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(keyStorePath);
			keyStore.load(fileInputStream, keyStorePassword.toCharArray());
		} finally {
			if (fileInputStream != null)
				try { fileInputStream.close(); } catch (IOException e) {}
		}
		
		Enumeration<String> keys = keyStore.aliases();
		String key = null;
		while (keys.hasMoreElements()) {
			String keyElement = keys.nextElement();
			if (keyStore.isKeyEntry(keyElement)) {
				key = keyElement;
				break;
			}
		}
		if (key == null)
			throw new GeneralSecurityException("None certificate for sign in this keystore");
		
		Certificate[] certs = keyStore.getCertificateChain(key);
		X509Certificate[] certificates = new X509Certificate[certs.length];
		for (int i = 0; i < certs.length; i++) {
			if (!(certs[i] instanceof X509Certificate)) {
				throw new GeneralSecurityException("Certificate in chain '" + key + "' is not a X509Certificate.");
			}
			certificates[i] = (X509Certificate) certs[i];
		}
		
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(key, keyPassword.toCharArray());
		if (privateKey == null)
			throw new GeneralSecurityException(key + " could not be accessed");

		return new PKCS7Util(certificates, privateKey);
	}

	private X500Name x500Name;
	private BigInteger serial;
	private AlgorithmId digestAlgorithmId;
	private AlgorithmId algorithmRsaOid;
	private X509Certificate[] certificates;
	private ContentInfo contentInfo;
	private PrivateKey privateKey;

	private PKCS7Util(X509Certificate[] certificates, PrivateKey privateKey) throws Exception {
		X509Certificate x509 = certificates[certificates.length - 1];
		this.certificates = certificates;
		this.serial = x509.getSerialNumber();
		this.x500Name = new X500Name(x509.getIssuerDN().getName());
		this.digestAlgorithmId = AlgorithmId.get(digestAlgorithm);
		this.algorithmRsaOid = new AlgorithmId(AlgorithmId.RSAEncryption_oid);
		this.contentInfo = new ContentInfo(ContentInfo.DATA_OID, null);
		this.privateKey = privateKey;
	}

	public byte[] sign(byte[] data) {
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			Signature signer = Signature.getInstance(signingAlgorithm);
			signer.initSign(privateKey);
			signer.update(data, 0, data.length);
			byte[] signedAttributes = signer.sign();
			SignerInfo signerInfo = new SignerInfo(x500Name, serial, digestAlgorithmId, null, algorithmRsaOid, signedAttributes, null);
			PKCS7 p7 = new PKCS7(new AlgorithmId[] { digestAlgorithmId }, contentInfo, certificates, new SignerInfo[] { signerInfo });

			byteArrayOutputStream = new ByteArrayOutputStream();
			p7.encodeSignedData(byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (SignatureException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
			throw new RuntimeException();
		} finally {
			if (byteArrayOutputStream != null)
				try { byteArrayOutputStream.close(); } catch (IOException e) {}
		}
	}

	public boolean verify(byte[] data, byte[] signData) {
		boolean verifyRet = false;
		try {
			// 新建PKCS#7签名数据处理对象
			CMSSignedData sign = new CMSSignedData(new CMSProcessableByteArray(data), signData);
			// 添加BouncyCastle作为安全提供
			Security.addProvider(new BouncyCastleProvider());
			// 获得证书信息
			CertStore certs = sign.getCertificatesAndCRLs("Collection", "BC");
			// 获得签名者信息
			SignerInformationStore signers = sign.getSignerInfos();
			Iterator<?> it = signers.getSigners().iterator();
			// 当有多个签名者信息时需要全部验证
			while (it.hasNext()) {
				SignerInformation signer = (SignerInformation) it.next();
				// 证书链
				Collection<?> certCollection = certs.getCertificates(signer.getSID());
				X509Certificate cert = (X509Certificate) certCollection.iterator().next();
				// 验证数字签名
				if (signer.verify(cert.getPublicKey(), "BC")) {
					verifyRet = true;
				} else {
					return false;
				}
			}
		} catch (CMSException | NoSuchAlgorithmException | NoSuchProviderException | CertStoreException e) {
			verifyRet = false;
		}
		return verifyRet;
	}
}
