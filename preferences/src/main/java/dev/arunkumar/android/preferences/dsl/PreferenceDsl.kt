/*
 * Copyright 2021 Arunkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.preferences.dsl

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.preference.*

/**
 * DSL marker for restricting access scope when [PreferencesBuilder.preference] is nested.
 */
@DslMarker
annotation class PreferenceDsl

/**
 * Data structure that exposes methods required for preferences DSL.
 *
 * @param preferenceManager [PreferenceManager] instance received from [PreferenceFragmentCompat]
 * @param preferenceGroup [PreferenceGroup] instance that will be used to add any newly created
 *                        [Preference] instances
 * @see preferences
 */
@PreferenceDsl
open class PreferencesBuilder<T : PreferenceGroup>(
  val preferenceManager: PreferenceManager,
  val preferenceGroup: T
) {
  /**
   * Managed [Context] instance from [preferenceManager] that has current preference theme and
   * styles applied
   */
  val preferenceContext: Context = preferenceManager.context

  /**
   * Creates an [Preference] instance of type [T] and adds to current [PreferenceScreen] in the
   * hierarchy. [builder] can be used to access the created [Preference] and configure it.
   *
   * Note: This method uses reflection to instantiate [T]'s with single arg [Context] constructor
   */
  inline fun <reified T : Preference> preference(key: String, crossinline builder: T.() -> Unit) {
    T::class.java.getConstructor(Context::class.java)
      .newInstance(preferenceContext)
      .also { it.key = key }
      .apply(builder)
      .let(preferenceGroup::addPreference)
  }

  /**
   * Same as [preference] but type defaulted to [Preference]
   */
  inline fun simplePreference(key: String, crossinline builder: Preference.() -> Unit) {
    preference(key, builder)
  }

  /**
   * Adds a [PreferenceCategory] in the current [PreferenceScreen]. [builder] can be used to
   * add additional [Preference] to this group.
   */
  inline fun category(crossinline builder: CategoryPreferencesBuilder.() -> Unit) {
    PreferenceCategory(preferenceContext).let { preferenceCategory ->
      preferenceGroup.addPreference(preferenceCategory)
      CategoryPreferencesBuilder(preferenceManager, preferenceCategory).apply(builder)
    }
  }
}

/**
 * Data structure for constructing [Preference] hierarchy with given [PreferenceCategory] as the
 * category header.
 */
@PreferenceDsl
class CategoryPreferencesBuilder(
  preferenceManager: PreferenceManager,
  preferenceScreen: PreferenceCategory
) : PreferencesBuilder<PreferenceCategory>(preferenceManager, preferenceScreen) {

  fun properties(builder: PreferenceCategory.() -> Unit) {
    preferenceGroup.apply(builder)
  }
}

/**
 * Constructs [PreferenceScreen] hierarchy and adds to this [PreferenceFragmentCompat] by calling
 * [PreferenceFragmentCompat.setPreferenceScreen].
 *
 * Usage:
 * ```
 *      override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
 *          preferences{
 *              simplePreference {
 *                  titleRes(R.string.preference_search)
 *                  keyRes(R.string.preference_key_search)
 *                  summaryRes(R.string.preference_search_summary)
 *                  targetFragment<SearchPreferencesFragment>()
 *                  defaultSummaryProvider()
 *              }
 *          }
 *      }
 * ```
 */
inline fun PreferenceFragmentCompat.preferences(crossinline builder: PreferencesBuilder<PreferenceScreen>.() -> Unit) {
  val context = preferenceManager.context
  preferenceScreen = preferenceManager.createPreferenceScreen(context).also { screen ->
    PreferencesBuilder(preferenceManager, screen).apply(builder)
  }
}

inline fun <reified T : Fragment> Preference.targetFragment() {
  fragment = T::class.java.name
}

inline fun Preference.keyRes(@StringRes keyRes: Int) {
  key = context.getString(keyRes)
}

inline fun Preference.summaryRes(@StringRes summaryRes: Int) {
  summary = context.getText(summaryRes)
}

inline fun Preference.titleRes(@StringRes tittleRes: Int) {
  title = context.getText(tittleRes)
}

inline fun PreferenceCategory.titleRes(@StringRes tittleRes: Int) {
  title = context.getText(tittleRes)
}

inline fun Preference.defaultValueRes(@StringRes defaultValueRes: Int) {
  setDefaultValue(context.getString(defaultValueRes))
}

inline fun <reified T : Preference> Preference.summaryProvider(crossinline summaryProvider: T.() -> CharSequence) {
  setSummaryProvider { (this as T).summaryProvider() }
}

fun Preference.defaultSummaryProvider() {
  when (this) {
    is ListPreference -> setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance())
    is EditTextPreference -> setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance())
  }
}

