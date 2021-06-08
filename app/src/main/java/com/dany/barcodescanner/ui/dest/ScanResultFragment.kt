package com.dany.barcodescanner.ui.dest

import androidx.navigation.fragment.navArgs
import com.dany.barcodescanner.R
import com.dany.barcodescanner.base.BaseFragment
import com.dany.barcodescanner.databinding.FragmentScanResultBinding
import com.google.android.libraries.barhopper.Barcode

class ScanResultFragment: BaseFragment<FragmentScanResultBinding>(
  R.layout.fragment_scan_result
) {
  private val args by navArgs<ScanResultFragmentArgs>()

  override fun FragmentScanResultBinding.bindingViewData() {
    val format = args.format
    val code = args.code

    viewCodeFormat.setImageResource(when(format) {
      Barcode.QR_CODE -> R.drawable.ic_qr_code
      else -> R.drawable.ic_barcode
    })
    viewCodeText.text = code
  }
}