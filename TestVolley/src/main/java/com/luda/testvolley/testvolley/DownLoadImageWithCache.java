package com.luda.testvolley.testvolley;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * DownLoadImageWithCache: 下载图片，设有sdcard缓存
 * 
 * @author chaos
 * 
 */
public class DownLoadImageWithCache extends AsyncTask<String, Void, Bitmap> {

	private ImageView imageView = null;

	public DownLoadImageWithCache(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		// TODO Auto-generated method stub
		String url = (String) urls[0];
		Bitmap bitmap = null;
		int fileSize = 0;
		int loopTimes = 0;
		String bitmapName = url.substring(url.lastIndexOf("/") + 1);
		bitmapName = OtherUtils.getFileNameNoEx(bitmapName);
		String dirSrc = "/mnt/sdcard/PaixinCache/";
		String bitmapSrc = dirSrc + bitmapName;
		File cacheDir = new File(dirSrc);
		File bitmapFile = new File(bitmapSrc);
		FileOutputStream fileOutputStream = null;

		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}

		while (true) {
			if (bitmapFile.exists()) {
				try {
					fileSize = new FileInputStream(bitmapFile).available();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (fileSize != 0) { // 如果图像文件存在并且大小不为0，跳出循环
					break;
				} else
					// 如果图像文件存在但大小为0（即为空文件），则删除该文件
					bitmapFile.delete();
			}
			// 创建图像文件
			try {
				bitmapFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 从网络获取资源
			try {
				InputStream inputStream = new URL(url).openStream();
				bitmap = null;
				bitmap = BitmapFactory.decodeStream(inputStream);
				if (null == bitmap) {
					return doInBackground("http://www.baidu.com/img/bdlogo.gif");
				}
			} catch (MalformedURLException e) {
				return doInBackground("http://www.baidu.com/img/bdlogo.gif");
			} catch (IOException e) {
				return doInBackground("http://www.baidu.com/img/bdlogo.gif");
			}

			// 保存图像到文件
			try {
				fileOutputStream = new FileOutputStream(bitmapFile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100,
						fileOutputStream);
				fileOutputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			loopTimes++;
		}

		if (loopTimes == 0) {
			Log.i("image", "decodeded: " + bitmapName);
			return BitmapFactory.decodeFile(bitmapSrc);
		} else {
			Log.i("image", "downloaded: " + bitmapName);
			return bitmap;
		}

		// return (bitmap != null) ? bitmap : null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
	}



    public static int CalculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap DecodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = CalculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
