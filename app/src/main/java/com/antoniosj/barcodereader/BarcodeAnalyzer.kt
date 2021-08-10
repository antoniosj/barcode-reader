package com.antoniosj.barcodereader

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class BarcodeAnalyzer(private val listener: (barcode: String) -> Unit) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_EAN_13)
            .build()

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val scanner = BarcodeScanning.getClient(options)
            val result = scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {

                        val rawValue = barcode.rawValue
                        Log.d("ASJ", "barcode  rawValue - $rawValue")

                        val valueType = barcode.valueType
                        Log.d("ASJ", "valueType - $valueType") // 5 = PHONE TYPE O_o

                        listener(rawValue)
                    }
                }
                .addOnFailureListener {
                    Log.d("ASJ", "failed = ${it.message}")
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}