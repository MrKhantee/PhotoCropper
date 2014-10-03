package org.hybridsquad.android.library;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created with Android Studio.
 * User: ryan@xisue.com
 * Date: 10/1/14
 * Time: 11:08 AM
 * Desc: CropHelper
 * Revision:
 * - 10:00 2014/10/03 Basic utils.
 * - 11:30 2014/10/03 Add static methods for generating crop intents.
 */
public class CropHelper {

    public static final String TAG = "CropHelper";

    /**
     * request code of Activities or Fragments
     * You will have to change the values of the request codes below if they conflict with your own.
     */
    public static final int REQUEST_CROP = 127;
    public static final int REQUEST_CAMERA = 128;
    public static final int REQUEST_GALLERY = 129;

    public static final String CROP_CACHE_FILE_NAME = "crop_cache_file.jpg";

    public static Uri buildUri() {
        return Uri
                .fromFile(Environment.getExternalStorageDirectory())
                .buildUpon()
                .appendPath(CROP_CACHE_FILE_NAME)
                .build();
    }

    public static void handleResult(CropHandler handler, int requestCode, int resultCode, Intent data) {
        if (handler == null) return;

        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onCropCancel();
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CROP:
                    Log.d(TAG, "Photo cropped!");
                    handler.onPhotoCropped(buildUri());
                    break;
                case REQUEST_CAMERA:

                    break;
                case REQUEST_GALLERY:

                    break;
            }
        }
    }

    public static boolean clearCachedCropFile(Uri uri) {
        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result)
                Log.i(TAG, "Cached crop file cleared.");
            else
                Log.e(TAG, "Failed to clear cached crop file.");
            return result;
        } else {
            Log.w(TAG, "Trying to clear cached crop file but it does not exist.");
        }
        return false;
    }

    public static Intent buildCropFromUriIntent(CropParams params) {
        return buildCropIntent("com.android.camera.action.CROP", params);
    }

    public static Intent buildCropFromGalleryIntent(CropParams params) {
        return buildCropIntent(Intent.ACTION_GET_CONTENT, params);
    }

    public static Intent buildCaptureIntent(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("crop-custom-data", "ryan hoo");
        return intent;
    }

    public static Intent buildCropIntent(String action, CropParams params) {
        Intent intent = new Intent(action, null);
        intent.setType(params.type);
        intent.putExtra("crop", params.crop);
        intent.putExtra("scale", params.scale);
        intent.putExtra("aspectX", params.aspectX);
        intent.putExtra("aspectY", params.aspectY);
        intent.putExtra("outputX", params.outputX);
        intent.putExtra("outputY", params.outputY);
        intent.putExtra("return-data", params.returnData);
        intent.putExtra("outputFormat", params.outputFormat);
        intent.putExtra("noFaceDetection", params.noFaceDetection);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
        return intent;
    }

}