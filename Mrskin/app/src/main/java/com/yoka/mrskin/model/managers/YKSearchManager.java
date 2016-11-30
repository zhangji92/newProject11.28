package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKBrand;
import com.yoka.mrskin.model.data.YKCategory;
import com.yoka.mrskin.model.data.YKEfficacy;
import com.yoka.mrskin.model.data.YKExperience;
import com.yoka.mrskin.model.data.YKImage;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKSearchManager extends YKManager
{
	public enum YKSearchType {
		SEARCHTYPE_PRODUCT, SEARCHTYPE_INFORMATION, SEARCHTYPE_EXPERIENCE,
	};

	private static final String PATH_RECOMMENDATION = getBase() + "cosmetics/base";
	private static final String PATH_SEARCH_SHORTCUT = getBase()
			+ "search/recommend";
	private static final String PATH_INFORMATION = getBase() + "search/information";
	private static final String PATH_COMMENT = getBase() + "search/comment";
	private static final String PATH_EXPERIENCE = getBase() + "search/result";
	private static final String PATH_COSMETICS_LIST = getBase() + "cosmetics/list";

	private static final String PATH_BRAND_DETAIL = getBase() + "brand/zone";
	private static final String PATH_BRAND_PRODUCT = getBase() + "brand/result";

	private static YKSearchManager instance = null;
	public static String CACHE_BRAND = "BrandData";
	public static String CACHE_CATEG = "CategorData";
	public static String CACHE_EffIC = "EfficData";
	public static String CACHE_IMAGE = "IMAGEData";
	private static Object lock = new Object();

	public static YKSearchManager getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new YKSearchManager();
			}
		}
		return instance;
	}
	public YKSearchManager() {

	}

	public ArrayList<YKBrand> getBrandData() {
		return loadDataBrandFile();
	}
	public ArrayList<YKCategory> getCategorData() {
		return loadDataCategoriesFile();
	}
	public ArrayList<YKEfficacy> getEfficData() {
		return loadDataEfficaciesFile();
	}
	public ArrayList<YKImage> getImageData() {
		return loadDataImageFile();
	}

	// 获取妆品库搜索页推荐数据接口
	public YKHttpTask requestRecommendations(
			final RecommendationCallback callback) {
		//        HashMap<String, Object> parameters = new HashMap<String, Object>();
		//
		//        parameters.put("index_page", pageIndex);
		//        parameters.put("count", Integer.valueOf(count));
		return super.postURL(PATH_SEARCH_SHORTCUT, null,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				ArrayList<YKCategory> categories = new ArrayList<YKCategory>();
				ArrayList<YKBrand> brands = new ArrayList<YKBrand>();
				ArrayList<YKEfficacy> efficacies = new ArrayList<YKEfficacy>();
				ArrayList<YKImage> image = new ArrayList<YKImage>();

				if (result.isSucceeded()) {
					// parse brands
					JSONArray tmpArray = object.optJSONArray("brands");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								brands.add(YKBrand.parse(tmpArray
										.getJSONObject(i)));
								if (i == 0) {
									brands.get(i).setmFlag(true);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse categories
					tmpArray = object.optJSONArray("categories");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								categories.add(YKCategory
										.parse(tmpArray
												.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse efficacies
					tmpArray = object.optJSONArray("efficacies");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								efficacies.add(YKEfficacy
										.parse(tmpArray
												.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse image
					tmpArray = object.optJSONArray("entries");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								image.add(YKImage
										.parse(tmpArray
												.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				if(brands != null && brands.size() > 0){
					saveDataBrandFile(brands);
				}
				if(categories != null && categories.size() > 0){
					saveDataCategoriesFile(categories);
				}
				if(efficacies != null && efficacies.size() > 0){
					saveDataEfficaciesFile(efficacies);
				}
				if(image != null && image.size() > 0){
					saveDataImageFile(image);
				}

				// do callback
				if (callback != null) {
					callback.callback(result, brands, categories,efficacies, image);
				}
			}
		});
	}

	public YKHttpTask requestGeneralData(final DataCallback callback) {
		return super.postURL(PATH_RECOMMENDATION, null,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				ArrayList<YKCategory> categories = new ArrayList<YKCategory>();
				ArrayList<YKBrand> brands = new ArrayList<YKBrand>();
				ArrayList<YKEfficacy> efficacies = new ArrayList<YKEfficacy>();
				String version = "";

				if (result.isSucceeded()) {
					// parse brands
					JSONArray tmpArray = object.optJSONArray("brands");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								brands.add(YKBrand.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse categories
					tmpArray = object.optJSONArray("categories");
					HashMap<String, YKCategory> categoryTable = new HashMap<String, YKCategory>();
					if (tmpArray != null) {
						YKCategory category = null;
						YKCategory parentCategory = null;
						String parentID = null;
						String categoryID = null;
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								category = YKCategory.parse(tmpArray
										.getJSONObject(i));
								parentID = category.getParentID();

								if ((!TextUtils.isEmpty(parentID)
										&&parentID.equals("1")) || TextUtils.isEmpty(parentID)) {
									categoryID = category.getID();

									if (!TextUtils.isEmpty(categoryID)) {
										categoryTable.put(categoryID,
												category);
										categories.add(category);
									}

								} else {
									parentCategory = categoryTable
											.get(parentID);
									if (parentCategory == null) {
										categoryID = category.getID();
										if (!TextUtils
												.isEmpty(categoryID)) {
											categoryTable.put(categoryID,
													category);
											categories.add(category);
										}

									} else {
										parentCategory
										.addSubCategory(category);
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse efficacies
					tmpArray = object.optJSONArray("efficacies");
					HashMap<String, YKEfficacy> efficacyTable = new HashMap<String, YKEfficacy>();
					if (tmpArray != null) {
						YKEfficacy efficacy = null;
						YKEfficacy parentEfficacy = null;
						String parentID = null;
						String efficacyID = null;
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								efficacy = YKEfficacy.parse(tmpArray
										.getJSONObject(i));
								parentID = efficacy.getParentID();

								if ((!TextUtils.isEmpty(parentID)
										&& parentID.equals("0"))  || TextUtils.isEmpty(parentID)) {
									efficacyID = efficacy.getID();
									if (!TextUtils
											.isEmpty(efficacyID)) {
										efficacyTable.put(efficacyID,
												efficacy);
										efficacies.add(efficacy);
									}

								} else {
									parentEfficacy = efficacyTable
											.get(parentID);
									if (parentEfficacy == null) {
										efficacyID = efficacy.getID();
										if (!TextUtils
												.isEmpty(efficacyID)) {
											efficacyTable.put(efficacyID,
													efficacy);
											efficacies.add(efficacy);
										}
									} else {
										parentEfficacy
										.addSubEfficacy(efficacy);
									}

								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					version = object.optString("version");
				}
				// do callback
				if (callback != null) {
					callback.callback(result, version, brands,
							categories, efficacies);
				}
			}
		});
	}

	public YKHttpTask requestInformation(String beforeID,
			final TopicCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("before_id", beforeID);

		return super.postURL(PATH_INFORMATION, params,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				ArrayList<YKTopicData> topics = new ArrayList<YKTopicData>();

				if (result.isSucceeded()) {
					// parse brands
					JSONArray tmpArray = object.optJSONArray("topics");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								topics.add(YKTopicData.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				// do callback
				if (callback != null) {
					callback.callback(result, topics);
				}
			}
		});
	}

	public YKHttpTask requestComment(String beforeID,
			final TopicCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("before_id", beforeID);

		return super.postURL(PATH_COMMENT, params,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				ArrayList<YKTopicData> topics = new ArrayList<YKTopicData>();

				if (result.isSucceeded()) {
					// parse brands
					JSONArray tmpArray = object.optJSONArray("topics");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								topics.add(YKTopicData.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}
				// do callback
				if (callback != null) {
					callback.callback(result, topics);
				}
			}
		});
	}

	public YKHttpTask requestExperience(String beforeID,
			final TopicCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("before_id", beforeID);

		return super.postURL(PATH_EXPERIENCE, params,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				ArrayList<YKTopicData> topics = new ArrayList<YKTopicData>();

				if (result.isSucceeded()) {
					// parse brands
					JSONArray tmpArray = object.optJSONArray("topics");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								topics.add(YKTopicData.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}
				// do callback
				if (callback != null) {
					callback.callback(result, topics);
				}
			}
		});
	}

	public YKHttpTask requestSearchData(String keyword, YKSearchType type,
			int pageIndex, final searchCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("page_index", Integer.valueOf(pageIndex));
		params.put("keyword", keyword);
		String strType = "product";
		if (type == YKSearchType.SEARCHTYPE_PRODUCT) {
			strType = "product";
		} else if (type == YKSearchType.SEARCHTYPE_EXPERIENCE) {
			strType = "experience";
		} else if (type == YKSearchType.SEARCHTYPE_INFORMATION) {
			strType = "information";
		}
		params.put("type", strType);
		return super.postURL(PATH_EXPERIENCE, params,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				ArrayList<YKProduct> product = new ArrayList<YKProduct>();
				ArrayList<YKExperience> experience = new ArrayList<YKExperience>();
				ArrayList<YKTopicData> information = new ArrayList<YKTopicData>();

				if (result.isSucceeded()) {
					// parse brands
					JSONArray tmpArray = object
							.optJSONArray("products");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								product.add(YKProduct.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse categories
					tmpArray = object.optJSONArray("experience");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								experience.add(YKExperience
										.parse(tmpArray
												.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse efficacies
					tmpArray = object.optJSONArray("information");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								information.add(YKTopicData
										.parse(tmpArray
												.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				// do callback
				if (callback != null) {
					callback.callback(result, product, experience,
							information);
				}
			}
		});
	}
	public YKHttpTask requestBrandResult(int brandId,String keyword, YKSearchType type,
			int pageIndex, final searchCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", brandId);
		params.put("page_index", Integer.valueOf(pageIndex));
		params.put("keyword", keyword);
		String strType = "product";
		if (type == YKSearchType.SEARCHTYPE_PRODUCT) {
			strType = "product";
		} else if (type == YKSearchType.SEARCHTYPE_EXPERIENCE) {
			strType = "experience";
		} else if (type == YKSearchType.SEARCHTYPE_INFORMATION) {
			strType = "information";
		}
		params.put("type", strType);
		return super.postURL(PATH_BRAND_PRODUCT, params,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				ArrayList<YKProduct> product = new ArrayList<YKProduct>();
				ArrayList<YKExperience> experience = new ArrayList<YKExperience>();
				ArrayList<YKTopicData> information = new ArrayList<YKTopicData>();

				//				Log.d("Product", null == object ? " null" : object.toString());
				if (result.isSucceeded()) {
					//
					// parse brands

					JSONArray tmpArray = object
							.optJSONArray("products");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								product.add(YKProduct.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse categories
					tmpArray = object.optJSONArray("experience");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								experience.add(YKExperience
										.parse(tmpArray
												.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// parse efficacies
					tmpArray = object.optJSONArray("information");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								information.add(YKTopicData
										.parse(tmpArray
												.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				// do callback
				if (callback != null) {
					callback.callback(result, product, experience,
							information);
				}
			}
		});
	}



	public YKHttpTask requestProductCosmetics(int pageIndex,int strType,int count,int id,int searchType, final cosmeticsCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("page_index", Integer.valueOf(pageIndex));
		params.put("type", Integer.valueOf(strType));
		params.put("count", Integer.valueOf(count));
		params.put("id", Integer.valueOf(id));
		params.put("searchtype", searchType);
		return super.postURL(PATH_COSMETICS_LIST, params,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				ArrayList<YKProduct> product = new ArrayList<YKProduct>();

				if (result.isSucceeded()) {
					// parse brands
					JSONArray tmpArray = object
							.optJSONArray("product");
					if (tmpArray != null) {
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								product.add(YKProduct.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				// do callback
				if (callback != null) {
					callback.callback(result, product);
				}
			}
		});
	}

	public interface RecommendationCallback
	{
		public void callback(YKResult result, ArrayList<YKBrand> brands,
				ArrayList<YKCategory> categories,
				ArrayList<YKEfficacy> efficacies,ArrayList<YKImage> mCover);

	}

	public interface DataCallback
	{
		public void callback(YKResult result, String version,
				ArrayList<YKBrand> brands, ArrayList<YKCategory> categories,
				ArrayList<YKEfficacy> efficacies);

	}

	public interface TopicCallback
	{
		public void callback(YKResult result, ArrayList<YKTopicData> topics);

	}

	public interface searchCallback
	{
		public void callback(YKResult result, ArrayList<YKProduct> product,
				ArrayList<YKExperience> experience,
				ArrayList<YKTopicData> information);
	}

	public interface cosmeticsCallback
	{
		public void callback(YKResult result, ArrayList<YKProduct> product);
	}


	//saveYKBrand
	private boolean saveDataBrandFile(ArrayList<YKBrand> brand) {
		JSONObject object;
		String str;
		byte[] data = null;
		try {
			JSONArray imageArray = new JSONArray();
			for (YKBrand bran : brand) {
				imageArray.put(bran.toJson());
			}
			object = new JSONObject();
			object.put("brand", imageArray);
			str = object.toString();
			data = str.getBytes("utf-8");
		} catch (Exception e) {
			return false;
		}
		YKFile.save(CACHE_BRAND, data);
		return true;
	}
	//loadYKBrand
	private ArrayList<YKBrand> loadDataBrandFile() {
		byte[] data = YKFile.read(CACHE_BRAND);
		String str;
		ArrayList<YKBrand> objectData = new ArrayList<YKBrand>();
		try {
			str = new String(data, "utf-8");
			JSONObject object = new JSONObject(str);
			JSONArray array = object.getJSONArray("brand");
			YKBrand brand;
			for (int i = 0; i < array.length(); ++i) {
				brand = YKBrand.parse(array.getJSONObject(i));
				objectData.add(brand);
			};
		} catch (Exception e) {
		}
		return objectData;
	}

	//saveYKCategory
	private boolean saveDataCategoriesFile(ArrayList<YKCategory> categories) {
		JSONObject object;
		String str;
		byte[] data = null;
		try {
			JSONArray imageArray = new JSONArray();
			for (YKCategory categ : categories) {
				imageArray.put(categ.toJson());
			}
			object = new JSONObject();
			object.put("categories", imageArray);
			str = object.toString();
			data = str.getBytes("utf-8");
		} catch (Exception e) {
			return false;
		}
		YKFile.save(CACHE_CATEG, data);
		return true;
	}

	//loadYKCategory
	private ArrayList<YKCategory> loadDataCategoriesFile() {
		byte[] data = YKFile.read(CACHE_CATEG);
		String str;
		ArrayList<YKCategory> objectData = new ArrayList<YKCategory>();
		try {
			str = new String(data, "utf-8");
			JSONObject object = new JSONObject(str);
			JSONArray array = object.getJSONArray("categories");
			YKCategory brand;
			for (int i = 0; i < array.length(); ++i) {
				brand = YKCategory.parse(array.getJSONObject(i));
				objectData.add(brand);
			};
		} catch (Exception e) {
		}
		return objectData;
	}

	//saveYKEfficacy
	private boolean saveDataEfficaciesFile(ArrayList<YKEfficacy> eddicacy) {
		JSONObject object;
		String str;
		byte[] data = null;
		try {
			JSONArray imageArray = new JSONArray();
			for (YKEfficacy cateffic : eddicacy) {
				imageArray.put(cateffic.toJson());
			}
			object = new JSONObject();
			object.put("efficacy", imageArray);
			str = object.toString();
			data = str.getBytes("utf-8");
		} catch (Exception e) {
			return false;
		}
		YKFile.save(CACHE_EffIC, data);
		return true;
	}

	//loadYKEfficacy
	private ArrayList<YKEfficacy> loadDataEfficaciesFile() {
		byte[] data = YKFile.read(CACHE_EffIC);
		String str;
		ArrayList<YKEfficacy> objectData = new ArrayList<YKEfficacy>();
		try {
			str = new String(data, "utf-8");
			JSONObject object = new JSONObject(str);
			JSONArray array = object.getJSONArray("efficacy");
			YKEfficacy efficay;
			for (int i = 0; i < array.length(); ++i) {
				efficay = YKEfficacy.parse(array.getJSONObject(i));
				objectData.add(efficay);
			};
		} catch (Exception e) {
		}
		return objectData;
	}

	//saveYKImage
	private boolean saveDataImageFile(ArrayList<YKImage> image) {
		JSONObject object;
		String str;
		byte[] data = null;
		try {
			JSONArray imageArray = new JSONArray();
			for (YKImage images : image) {
				imageArray.put(images.toJson());
				Log.e("Product", "image-json-"+images.toJson());
			}
			object = new JSONObject();
			object.put("image", imageArray);
			str = object.toString();
			data = str.getBytes("utf-8");
		} catch (Exception e) {
			return false;
		}
		YKFile.save(CACHE_IMAGE, data);
		return true;
	}

	//loadYKImage
	private ArrayList<YKImage> loadDataImageFile() {
		byte[] data = YKFile.read(CACHE_IMAGE);
		String str;
		ArrayList<YKImage> objectData = new ArrayList<YKImage>();
		try {
			str = new String(data, "utf-8");
			JSONObject object = new JSONObject(str);
			JSONArray array = object.getJSONArray("image");
			YKImage images;
			for (int i = 0; i < array.length(); ++i) {
				images = YKImage.parse(array.getJSONObject(i));
				objectData.add(images);
			};
		} catch (Exception e) {
		}
		return objectData;
	}
}
