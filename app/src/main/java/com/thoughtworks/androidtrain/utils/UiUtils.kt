package com.thoughtworks.androidtrain.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class UiUtils {
    fun replaceFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        fragmentId: Int,
        tag: String?
    ) {
        fragmentManager.beginTransaction().apply {
            replace(fragmentId, fragment, tag)
            addToBackStack(tag)
            commitAllowingStateLoss()
        }

    }
}