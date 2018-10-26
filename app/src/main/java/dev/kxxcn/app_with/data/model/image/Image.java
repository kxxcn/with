package dev.kxxcn.app_with.data.model.image;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kxxcn on 2018-10-22.
 */
public class Image implements Parcelable {

	private String fileName;
	private Bitmap bitmap;

	public Image(String fileName, Bitmap bitmap) {
		this.fileName = fileName;
		this.bitmap = bitmap;
	}

	public String getFileName() {
		return fileName;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	protected Image(Parcel in) {
		fileName = in.readString();
		bitmap = in.readParcelable(Bitmap.class.getClassLoader());
	}

	public static final Creator<Image> CREATOR = new Creator<Image>() {
		@Override
		public Image createFromParcel(Parcel source) {
			return new Image(source);
		}

		@Override
		public Image[] newArray(int size) {
			return new Image[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fileName);
		dest.writeValue(bitmap);
	}

}
