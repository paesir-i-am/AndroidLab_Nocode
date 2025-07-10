package com.example.ch17_database

import android.os.Bundle
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class MySettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // XML 리소스(settings.xml)에서 Preference 설정 로드
        setPreferencesFromResource(R.xml.settings, rootKey)

        // "id" 키를 가진 EditTextPreference 객체 가져오기
        val idPreference: EditTextPreference? = findPreference("id")

        // "color" 키를 가진 ListPreference 객체 가져오기
        val colorPreference: ListPreference? = findPreference("color")

        // ListPreference의 요약은 선택된 entry를 자동으로 보여주도록 설정
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        // EditTextPreference의 요약을 직접 설정하는 람다식 정의
        idPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val text = preference.text
            // 비어 있으면 설정 안 된 것으로 안내, 입력 있으면 요약에 표시
            if (TextUtils.isEmpty(text)) {
                "설정이 되지 않았습니다."
            } else {
                "설정한 ID 값은 : $text 입니다."
            }
        }
    }
}