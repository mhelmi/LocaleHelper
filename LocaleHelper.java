package com.github.mhelmi.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.Locale;

/**
 * This class is used to change your application locale and save this change for the next time
 * that your app is going to be used.
 * You can also force your application to change the locale by using the setLocale method.
 */
public class LocaleHelper {
  /**
   * Language and Country Key
   */
  private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
  private static final String SELECTED_COUNTRY = "Locale.Helper.Selected.Country";

  /**
   * Countries for Locale: Egypt, Saudi Arabia, United Arab and Kuwait
   */
  public static final String EGYPT = "EG";
  public static final String SAUDI_ARABIA = "SA";

  /**
   * Languages for Locale: Arabic, English
   */
  public static final String AR = "ar";
  public static final String EN = "en";

  /**
   * use this method in base activity onCreate method to be called before setContentView
   * use this method in base activity attachBaseContext method
   * and pass the returned context to the supper method
   *
   * @param context current context
   */
  public static Context onAttach(Context context) {
    String language = getLanguage(context);
    String country = getCountry(context);
    return setLocale(context, language, country);
  }

  /**
   * use this if you need to change locale by language only without specify country
   * use this method in you application attachBaseContext method
   * and pass the returned context to the supper method
   *
   * @param context current context
   * @param defaultLanguage default language key in lowercase like ar or en
   */
  public static Context onAttach(Context context, String defaultLanguage) {
    String language = getLanguage(context, defaultLanguage);
    return setLocale(context, language);
  }

  /**
   * use this if you need to change locale by language and country
   * you should use this method in you application attachBaseContext method
   * and pass the returned context to the supper method
   *
   * @param context current context
   * @param defaultLanguage default language key in lowercase like ar or en
   * @param defaultCountry default country key in uppercase like EG or SA
   */
  public static Context onAttach(Context context, String defaultLanguage, String defaultCountry) {
    String language = getLanguage(context, defaultLanguage);
    String country = getCountry(context, defaultCountry);
    return setLocale(context, language, country);
  }

  /**
   * Get saved Language from SharedPreferences
   *
   * @param context current context
   * @param defaultLanguage default language key in lowercase like ar or en
   * @return current language key
   */
  private static String getLanguage(Context context, String defaultLanguage) {
    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return mPreferences.getString(SELECTED_LANGUAGE, defaultLanguage);
  }

  /**
   * Get saved Language from SharedPreferences
   *
   * @param context current context
   * @return current language key by default return ar
   */
  public static String getLanguage(Context context) {
    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return mPreferences.getString(SELECTED_LANGUAGE, AR);
  }

  /**
   * save language key in SharedPreferences
   *
   * @param context current context
   * @param language the language key in lowercase like ar or en
   */
  private static void setLanguage(Context context, String language) {
    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    mPreferences.edit().putString(SELECTED_LANGUAGE, language).apply();
  }

  /**
   * Get saved country from SharedPreferences
   *
   * @param context current context
   * @param defaultCountry default country key in uppercase like EG or SA
   * @return current country key
   */
  private static String getCountry(Context context, String defaultCountry) {
    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return mPreferences.getString(SELECTED_COUNTRY, defaultCountry);
  }

  /**
   * Get saved country from SharedPreferences
   *
   * @param context current context
   * @return current country key by default return EG
   */
  public static String getCountry(Context context) {
    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return mPreferences.getString(SELECTED_COUNTRY, null);
  }

  /**
   * save country key in SharedPreferences
   *
   * @param context current context
   * @param country the new country key in uppercase like EG or SA
   */
  private static void setCountry(Context context, String country) {
    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    mPreferences.edit().putString(SELECTED_COUNTRY, country).apply();
  }

  /**
   * use this method of you need to change locale by language only without specify country
   * this public method change locale and resources on any android version
   * that's what we should call if we need to change locale
   *
   * @param context the base context
   * @param language the language key in lowercase like ar or en
   * @return Context
   */
  public static Context setLocale(Context context, String language) {
    setLanguage(context, language);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      return updateResources(context, language, null);
    }
    return updateResourcesLegacy(context, language, null);
  }

  /**
   * this public method change locale and resources on any android version
   * that's what we should call if we need to change locale
   *
   * @param context the base context
   * @param language the language key in lowercase like ar or en
   * @param country the country key in uppercase like EG or SA
   * @return Context
   */
  public static Context setLocale(Context context, String language, String country) {
    setLanguage(context, language);
    setCountry(context, country);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      return updateResources(context, language, country);
    }
    return updateResourcesLegacy(context, language, country);
  }

  /**
   * this private method change locale and resources for android Oreo Api 26 and above
   *
   * @param context the base context
   * @param language the language key in lowercase like ar or en
   * @param country the country key in uppercase like EG or SA
   * @return Context
   */
  @TargetApi(Build.VERSION_CODES.O)
  private static Context updateResources(Context context, String language, String country) {
    Locale locale = createLocale(language, country);
    Locale.setDefault(locale);
    Configuration configuration = context.getResources().getConfiguration();
    configuration.setLocale(locale);
    configuration.setLayoutDirection(locale);
    return context.createConfigurationContext(configuration);
  }

  /**
   * this private method change locale and resources for pre android Oreo Api 26
   *
   * @param context the base context
   * @param language the language key in lowercase like ar or en
   * @param country the country key in uppercase like EG or SA
   * @return Context
   */
  private static Context updateResourcesLegacy(Context context, String language, String country) {
    Locale locale = createLocale(language, country);
    Locale.setDefault(locale);
    Resources resources = context.getResources();
    Configuration configuration = resources.getConfiguration();
    configuration.locale = locale;
    configuration.setLayoutDirection(locale);
    resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    return context;
  }

  /**
   * create locale if specify country or not
   *
   * @param language the language key in lowercase like ar or en
   * @param country the country key in uppercase like EG or SA
   */
  private static Locale createLocale(@NonNull String language, String country) {
    if (TextUtils.isEmpty(country)) {
      return new Locale(language);
    } else {
      return new Locale(language, country);
    }
  }
}
