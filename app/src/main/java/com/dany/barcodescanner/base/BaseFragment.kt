package com.dany.barcodescanner.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

open class BaseFragment<VDB : ViewDataBinding>(
  @LayoutRes
  private val layoutResID: Int
) : Fragment() {
  protected lateinit var binding: VDB

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View = DataBindingUtil.inflate<VDB>(inflater, layoutResID, container, false).run {
    lifecycleOwner = this@BaseFragment

    binding = this

    bindingVM()
    bindingViewData()
    setEventListener()

    root
  }

  protected open fun VDB.bindingVM() {}
  protected open fun VDB.bindingViewData() {}
  protected open fun VDB.setEventListener() {}
}