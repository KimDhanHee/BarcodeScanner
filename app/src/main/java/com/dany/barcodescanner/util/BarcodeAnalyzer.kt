package com.dany.barcodescanner.util

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val onScanBarcodes: (List<Barcode>) -> Unit): ImageAnalysis.Analyzer {
  @androidx.camera.core.ExperimentalGetImage
  override fun analyze(imageProxy: ImageProxy) {
    imageProxy.image?.let {
      val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
      BarcodeScanning.getClient()
        .process(image)
        .addOnSuccessListener { barcodes ->
          onScanBarcodes(barcodes)
        }
        .addOnCompleteListener {
          imageProxy.close()
        }
    }
  }
}