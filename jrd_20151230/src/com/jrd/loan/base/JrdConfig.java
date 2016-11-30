package com.jrd.loan.base;

public final class JrdConfig {
	// 生产环境
	private final static String OFFICAL_BASE_URL = "https://app.junrongdai.com/";
	private final static String OFFICAL_APP_KEY = "AB32966E-708E-49EF-9CE4-859396A41A7F";
	private final static String OFFICAL_RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvXwsaowLfvsVosvcOLo7QhOrPXGz0f04goO9lpAl617nZ4fjbQ2JZ5nN/m66dkTzLHXVUlb1bIf0jBiIePP11WhPSub2rhRdnLbGDWwT3iHVPVoWUuV3BZ+20gbRW/BBto4BFEohR2YusVIxO+ak7hJ1uX6OVNs7a1SGb9PRLgQIDAQAB";

	// 测试环境
	// private final static String TEST_BASE_URL =
	// "http://192.168.1.158:8080/jrapps/";
//	private final static String TEST_BASE_URL = "http://123.57.56.161:30003/";
	private final static String TEST_BASE_URL = "http://59.46.172.178:30009/";
	private final static String TEST_APP_KEY = "7D327727-9A6E-4EBB-9768-289355AA38E6";
	private final static String TEST_RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCg3vW2PAJbU5tRobB9PUxFvwkhpdq2zWyhyT3PDMdSrz2ajSq3r5zwguI6H/tPlu9AyHFuiupYPTgWVPHbAG7SSl9TxEJY16sSSqZrffUISOzWsJq68fvtG16BrxTG/fYW65uRX0l8nuq+Nxd0Pee5Unqt3Kis01vwggNgJRaJfwIDAQAB";

	// 默认生产环境
	private static boolean isDebug = false;

	public static void setDebug(boolean isDebug) {
		JrdConfig.isDebug = isDebug;
	}

	public static boolean isDebug() {
		return isDebug;
	}

	public static String getBaseUrl() {
		if (!isDebug) {
			return OFFICAL_BASE_URL;
		}

		return TEST_BASE_URL;
	}

	public static String getRSAPublicKey() {
		if (!isDebug) {
			return OFFICAL_RSA_PUBLIC_KEY;
		}

		return TEST_RSA_PUBLIC_KEY;
	}

	public static String getAppKey() {
		if (!isDebug) {
			return OFFICAL_APP_KEY;
		}

		return TEST_APP_KEY;
	}
}
