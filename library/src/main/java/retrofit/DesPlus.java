package retrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import utils.SystemUtils;

public class DesPlus {

	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	public static final String bjAY2008 = "bjAY2008";

	/**
	 * DES算法，加密
	 *
	 * @param data
	 *            待加密字符串
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws Exception
	 */
	public static String encode(String key, String data) {
		if (data == null)
			return null;
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = cipher.doFinal(data.getBytes());
			return byte2String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * DES算法，解密
	 *
	 * @param data
	 *            待解密字符串
	 * @param key
	 *            解密私钥，长度不能够小于8位
	 * @return 解密后的字节数组
	 * @throws Exception
	 *             异常
	 */
	public static String decode(String key, String data) {
		if (data == null)
			return null;
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return new String(cipher.doFinal(byte2hex(data.getBytes())));
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * 二行制转字符串
	 *
	 * @param b
	 * @return
	 */
	private static String byte2String(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase(Locale.CHINA);
	}

	/**
	 * 二进制转化成16进制
	 *
	 * @param b
	 * @return
	 */
	private static byte[] byte2hex(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static String  getStringTime(){
		Date mData = new Date();
		long time = mData.getTime();
		String  data = encode(bjAY2008,time+"");
		return data;
	}



	/**
	 * header用户信息
	 * @return
	 */
	public static String getDefaultHeaderValue() {
		String value = "";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("phoneModel", SystemUtils.getMobileModel());//机型
			jsonObject.put("sysVersion", SystemUtils.getMobileSysVersion());//系统版本
			jsonObject.put("w_and_h", SystemUtils.getScreenWidth(null) + "*" + SystemUtils.getScreenHeight(null));//宽高
			jsonObject.put("version", SystemUtils.getVersionName());//app版本
			JSONObject addressJson = new JSONObject();
			addressJson.put("province", HttpSharedUtils.getLocationProvince());//省
			addressJson.put("city", HttpSharedUtils.getLocationCity());//市
			addressJson.put("district", HttpSharedUtils.getLocationDistrict());//县区
			addressJson.put("detailsAddress", HttpSharedUtils.getLocationAddress());//详细地址
			jsonObject.put("userAddress", addressJson);
			jsonObject.put("network", SystemUtils.getNetworkType());//网络制式
			jsonObject.put("comeFrom", "az_SH");//蜗牛来源
			jsonObject.put("userId", HttpSharedUtils.getUserId());

			jsonObject.put("UUID", SystemUtils.getSzImei());
			value = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}






}
