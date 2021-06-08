package com.dany.barcodescanner.ui.dest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dany.barcodescanner.R
import com.dany.barcodescanner.base.BaseFragment
import com.dany.barcodescanner.databinding.FragmentBarcodeBinding
import com.dany.barcodescanner.util.BarcodeAnalyzer
import java.lang.Exception
import java.util.concurrent.Executors

class BarcodeFragment : BaseFragment<FragmentBarcodeBinding>(
  R.layout.fragment_barcode
) {
  private val cameraPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { grant ->
      if (!grant) return@registerForActivityResult

      startCamera()
    }

  private val permissionGranted: Boolean
    get() = ContextCompat.checkSelfPermission(
      requireContext(),
      Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

  private val cameraExecutor by lazy { Executors.newSingleThreadExecutor() }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    when {
      permissionGranted -> startCamera()
      else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    return super.onCreateView(inflater, container, savedInstanceState)
  }

  private fun startCamera() {
    context ?: return

    // used to bind the lifecycle of cameras to the lifecycle owner
    // this eliminates the task of opening and closing the camera since CameraX is lifecycle-aware
    val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

    cameraProviderFuture.addListener({
      // used to bind the lifecycle of cameras to the lifecycle owner
      // this eliminates the task of opening and closing the camera since CameraX is lifecycle-aware
      val cameraProvider = cameraProviderFuture.get()

      val preview = Preview.Builder()
        .build()
        .also {
          it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        }

      val imageAnalyzer = ImageAnalysis.Builder()
        .build()
        .also {
          it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { format, barcode ->
            barcode ?: return@BarcodeAnalyzer

            cameraExecutor.shutdown()
          })
        }

      val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

      try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
      } catch (e: Exception) {}

    }, ContextCompat.getMainExecutor(requireContext()))
  }

  override fun onDestroy() {
    super.onDestroy()

    cameraExecutor.shutdown()
  }
}