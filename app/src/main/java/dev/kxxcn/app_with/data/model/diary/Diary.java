package dev.kxxcn.app_with.data.model.diary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kxxcn on 2018-09-13.
 */
public class Diary implements Parcelable {

	private String device_id;
	private String letter;
	private String letterDate;
	private int fontStyle;
	private int fontColor;
	private float fontSize;
	private int primaryPosition;
	private String galleryName;

	public Diary(String device_id, String letter, String letterDate, int fontStyle, int fontColor, float fontSize, int primaryPosition, String galleryName) {
		this.device_id = device_id;
		this.letter = letter;
		this.letterDate = letterDate;
		this.fontStyle = fontStyle;
		this.fontColor = fontColor;
		this.fontSize = fontSize;
		this.primaryPosition = primaryPosition;
		this.galleryName = galleryName;
	}

	public String getDevice_id() {
		return device_id;
	}

	public String getLetter() {
		return letter;
	}

	public String getLetterDate() {
		return letterDate;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public int getFontColor() {
		return fontColor;
	}

	public float getFontSize() {
		return fontSize;
	}

	public int getPrimaryPosition() {
		return primaryPosition;
	}

	public String getGalleryName() {
		return galleryName;
	}

	public static Creator<Diary> getCREATOR() {
		return CREATOR;
	}

	protected Diary(Parcel in) {
		device_id = in.readString();
		letter = in.readString();
		letterDate = in.readString();
		fontStyle = in.readInt();
		fontColor = in.readInt();
		fontSize = in.readFloat();
		primaryPosition = in.readInt();
		galleryName = in.readString();
	}

	public static final Creator<Diary> CREATOR = new Creator<Diary>() {
		@Override
		public Diary createFromParcel(Parcel source) {
			return new Diary(source);
		}

		@Override
		public Diary[] newArray(int size) {
			return new Diary[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(device_id);
		dest.writeString(letter);
		dest.writeString(letterDate);
		dest.writeInt(fontStyle);
		dest.writeInt(fontColor);
		dest.writeFloat(fontSize);
		dest.writeInt(primaryPosition);
		dest.writeString(galleryName);
	}

}
